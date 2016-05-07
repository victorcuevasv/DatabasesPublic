package org.mydb.projects.dbwebservice;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBAO {

	private String configFilename;
	
	protected Connection conn;
	
	public DBAO(String configFilename) {
		this.configFilename = configFilename;
		this.openConnection();
	}
	
	private void openConnection() {
		try {
			Properties configProps = new Properties();
			configProps.load(new FileInputStream(this.configFilename));
			String driverStr = configProps.getProperty("jdbc_driver");
			String urlStr = configProps.getProperty("db_url");
			Class.forName(driverStr);
			this.conn = DriverManager.getConnection(urlStr);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	
}


