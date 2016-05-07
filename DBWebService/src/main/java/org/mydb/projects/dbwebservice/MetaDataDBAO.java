package org.mydb.projects.dbwebservice;

import java.io.InputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.net.*;

import org.json.*;

public class MetaDataDBAO {

	
	private MetaDataDBAO dbao;
	
	private String configFile;
	
	private static final MetaDataDBAO instance = new MetaDataDBAO();
	
	private Connection conn;
	
	
	public MetaDataDBAO() {

	}
	
	
	public static MetaDataDBAO getInstance() {
    	return instance;
    }
	
	
	public synchronized void init(String configFile) {
		if( this.dbao == null || ( this.configFile != null && ! this.configFile.equals(configFile) ) ) {
			if( this.dbao != null )
				this.dbao.shutdown();
			this.configFile = configFile;
			this.openConnection();
		}
	}
	
	
	private void openConnection() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			URL resourceURL = classLoader.getResource(this.configFile);
			InputStream inputStream = resourceURL.openStream();
			Properties configProps = new Properties();
			configProps.load(inputStream);
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
	
	
	public synchronized void shutdown() {
		try {
			this.conn.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		this.dbao = null;
	}
	
	
	public synchronized JSONArray getTableAsJSON(String tableName, int limit) {
		JSONArray jsonArray = new JSONArray();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT " + limit);
			ResultSetMetaData rsmd = rs.getMetaData();
			int ncols = rsmd.getColumnCount();
			while(rs.next()) {
				JSONObject jsonObj = new JSONObject();
				for(int i = 1; i <= ncols; i++) {
					String colName = rsmd.getColumnName(i);
					String val = rs.getString(i);
					jsonObj.put(colName, val);
				}
				jsonArray.put(jsonObj);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return jsonArray;
	}
	
	
}


