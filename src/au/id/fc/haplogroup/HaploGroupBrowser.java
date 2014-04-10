/*
 * Project: HaploGroupBrowser
 * File: "au.id.fc.haplogroup/HaploGroupBrowser.java
 * Developer: Felix Jeyareuben <i@fc.id.au>
 * Website: www.y-str.org
 */
package au.id.fc.haplogroup;

import java.sql.SQLException;

public class HaploGroupBrowser
{

    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException
    {	
	SplashScreen splash = new SplashScreen();
	splash.setLocationRelativeTo(null);
	splash.setVisible(true);		
	DbUtils.init();	
	Thread.sleep(2000);
	splash.dispose();
	HaploGroupFrame dlg = new HaploGroupFrame();
	dlg.setLocationRelativeTo(null);
	dlg.setVisible(true);	
	dlg.autoSelectList();
    }
}
