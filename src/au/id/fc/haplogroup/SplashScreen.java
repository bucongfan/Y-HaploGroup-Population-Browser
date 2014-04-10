/*
 * Project: HaploGroupBrowser
 * File: "au.id.fc.haplogroup/SplashScreen.java
 * Developer: Felix Jeyareuben <i@fc.id.au>
 * Website: www.y-str.org
 */
package au.id.fc.haplogroup;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class SplashScreen extends JDialog
{

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
	EventQueue.invokeLater(new Runnable()
	{
	    public void run()
	    {
		try
		{
		    SplashScreen dialog = new SplashScreen();
		    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		    dialog.setVisible(true);
		} catch (Exception e)
		{
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the dialog.
     */
    public SplashScreen()
    {
    	setUndecorated(true);
    	setResizable(false);
    	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    	setAlwaysOnTop(true);
	setBounds(100, 100, 512, 363);
	getContentPane().setLayout(null);
	
	JLabel lblNewLabel = new JLabel("");
	lblNewLabel.setIcon(new ImageIcon(SplashScreen.class.getResource("/au/id/fc/haplogroup/ico/icon256.png")));
	lblNewLabel.setBounds(40, 69, 256, 256);
	getContentPane().add(lblNewLabel);
	
	JLabel lblHaplogroupBrowser = new JLabel("Y-DNA HaploGroup Browser");
	lblHaplogroupBrowser.setFont(new Font("Tahoma", Font.PLAIN, 26));
	lblHaplogroupBrowser.setHorizontalAlignment(SwingConstants.RIGHT);
	lblHaplogroupBrowser.setBounds(10, 11, 492, 47);
	getContentPane().add(lblHaplogroupBrowser);
	
	JLabel lblNewLabel_1 = new JLabel("© 2012 Felix Jeyareuben");
	lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
	lblNewLabel_1.setBounds(332, 338, 170, 14);
	getContentPane().add(lblNewLabel_1);
	
	JLabel lblHgfcidau = new JLabel("www.y-str.org");
	lblHgfcidau.setHorizontalAlignment(SwingConstants.RIGHT);
	lblHgfcidau.setBounds(384, 55, 118, 14);
	getContentPane().add(lblHgfcidau);

    }
}
