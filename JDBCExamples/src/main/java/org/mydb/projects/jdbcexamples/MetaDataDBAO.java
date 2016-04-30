package org.mydb.projects.jdbcexamples;

import java.sql.*;
import org.json.*;

public class MetaDataDBAO extends DBAO {

	public MetaDataDBAO(String configFile) {
		super(configFile);
	}
	
	public void printTableAsJSON(String tableName, int limit) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT " + limit);
			ResultSetMetaData rsmd = rs.getMetaData();
			int ncols = rsmd.getColumnCount();
			JSONArray jsonArray = new JSONArray();
			while(rs.next()) {
				JSONObject jsonObj = new JSONObject();
				for(int i = 1; i <= ncols; i++) {
					String colName = rsmd.getColumnName(i);
					String val = rs.getString(i);
					jsonObj.put(colName, val);
				}
				jsonArray.put(jsonObj);
			}
			System.out.println(jsonArray.toString());
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
}
