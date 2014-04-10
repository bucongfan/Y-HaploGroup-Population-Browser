/*
 * Project: HaploGroupBrowser
 * File: "au.id.fc.haplogroup/DbUtils.java
 * Developer: Felix Jeyareuben <i@fc.id.au>
 * Website: www.y-str.org
 */
package au.id.fc.haplogroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import au.id.fc.haplogroup.objects.HaploGroupRecord;
import au.id.fc.haplogroup.objects.RecordValue;

public class DbUtils
{
    private static Connection _con = null;
    private static String DATABASE = "haplogroups.db";
    private static HashMap<String, Object[]> maxList=null;
    
    public static Connection getConnection() throws ClassNotFoundException, SQLException
    {
	if (_con == null)
	{
	    Class.forName("org.sqlite.JDBC");
	   // _con = DriverManager.getConnection("jdbc:sqlite:" + DATABASE);
	    _con = DriverManager.getConnection("jdbc:sqlite::resource:" + DbUtils.class.getResource("/au/id/fc/haplogroup/"+DATABASE));
	}
	return _con;
    }

    public static void init() throws ClassNotFoundException, SQLException
    {
	
	Thread thread = new Thread()
	{
	    
	    @Override
	    public void run()
	    {
		 try
		{
		    getMaxHaploRecords();
		} catch (ClassNotFoundException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (SQLException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	};
	thread.start();
    }
    
    public static Object[] getAllPopulations() throws ClassNotFoundException, SQLException
    {
	Connection connection = null;
	ArrayList<String> list = new ArrayList<String>();
	connection = DbUtils.getConnection();
	Statement statement = connection.createStatement();
	statement.setQueryTimeout(30);
	ResultSet rs = statement.executeQuery("select distinct Population from haplogroups order by Population");
	while (rs.next())
	{
	    list.add(rs.getString(1));
	}
	rs.close();
	return list.toArray();
    }

    public static Object[] getHaplogroups() throws ClassNotFoundException, SQLException
    {
	Connection connection = null;
	ArrayList<String> list = new ArrayList<String>();
	connection = DbUtils.getConnection();
	Statement statement = connection.createStatement();
	statement.setQueryTimeout(30);
	ResultSet rs = statement.executeQuery("select distinct Haplogroup from haplogroups order by Haplogroup");
	while (rs.next())
	{
	    list.add(rs.getString(1));
	}
	rs.close();
	statement.close();
	return list.toArray();
    }
    
    public static HashMap<String, Object[]> getMaxHaploRecords() throws ClassNotFoundException, SQLException
    {
	if(maxList==null)
	{
        	Connection connection = DbUtils.getConnection();
        	PreparedStatement ps = connection.prepareStatement("select Population,Haplogroup,PercentAvailable from haplogroups A WHERE PercentAvailable!=0 AND PercentAvailable = (select MAX(PercentAvailable) from haplogroups WHERE Haplogroup=A.Haplogroup  GROUP BY Haplogroup) GROUP BY Haplogroup");
        	ps.setQueryTimeout(30);
        	ResultSet rs = ps.executeQuery();
        	maxList = new HashMap<String, Object[]>();
        	Object[] o = null;
        	while (rs.next())
        	{
        	    o = new Object[2];
        	    o[0]=rs.getString("Population");
        	    o[1]=rs.getDouble("PercentAvailable");
        	    maxList.put(rs.getString("Haplogroup"), o);
        	}
        	rs.close();
        	ps.close();
	}	
	return maxList;
    }
    
    public static ArrayList<RecordValue> getHaplogroups(String population) throws ClassNotFoundException, SQLException
    {
	HaploGroupRecord hgr = getHaploRecord(population);
	return hgr.getHaploGroup();
    }
    
    public static HaploGroupRecord getHaploRecord(String population) throws ClassNotFoundException, SQLException
    {
	Connection connection = null;
	HaploGroupRecord hg_rec = new HaploGroupRecord();
	connection = DbUtils.getConnection();
	PreparedStatement ps = connection.prepareStatement("select Id,Population,Language,SampleN,Haplogroup,PercentAvailable,Reference from haplogroups where Population=? ORDER BY PercentAvailable DESC");
	ps.setString(1, population);
	ps.setQueryTimeout(30);
	ResultSet rs = ps.executeQuery();
	boolean flag;
	flag = false;
	ArrayList<RecordValue> recVal_arr = new ArrayList<RecordValue>();
	while (rs.next())
	{
	    if (!flag)
	    {
		hg_rec.setId(rs.getInt("Id"));
		hg_rec.setPopulation(rs.getString("Population"));
		hg_rec.setLanguage(rs.getString("Language"));
		hg_rec.setSampleN(rs.getString("SampleN"));
		hg_rec.setReference(rs.getString("Reference"));
		flag = true;
	    }
	    RecordValue recVal = new RecordValue();
	    recVal.setHaploGroup(rs.getString("Haplogroup"));
	    recVal.setPercentAvailable(rs.getDouble("PercentAvailable"));
	    recVal_arr.add(recVal);
	    hg_rec.setHaploGroup(recVal_arr);
	}
	rs.close();
	ps.close();
	return hg_rec;
    }

    /*
     
     */

    public static ArrayList<HaploGroupRecord> getCloseRelatives(String population,int factor) throws ClassNotFoundException, SQLException
    {
	Connection connection = null;
	connection = DbUtils.getConnection();
	PreparedStatement ps = connection.prepareStatement("SELECT A.Population P,count(*) C FROM haplogroups A, haplogroups B " + " WHERE B.Population = ? AND A.Haplogroup = B.Haplogroup AND A.PercentAvailable > B.PercentAvailable-B.PercentAvailable*"+Integer.toString(factor)+"/100 AND " + " A.PercentAvailable < B.PercentAvailable+B.PercentAvailable*"+Integer.toString(factor)+"/100 AND A.PercentAvailable!=0 AND B.PercentAvailable!=0 " + " GROUP BY A.Population HAVING C>2 ORDER  BY C DESC");
	ps.setString(1, population);
	ps.setQueryTimeout(30);
	ResultSet rs = ps.executeQuery();
	ArrayList<HaploGroupRecord> hgr_array = new ArrayList<HaploGroupRecord>();
	while (rs.next())
	{
	    HaploGroupRecord hgr = getHaploRecord(rs.getString("P"));
	    hgr_array.add(hgr);
	}
	rs.close();
	ps.close();
	return hgr_array;
    }

    public static void Shutdown() throws SQLException
    {
	_con.close();
    }
}
