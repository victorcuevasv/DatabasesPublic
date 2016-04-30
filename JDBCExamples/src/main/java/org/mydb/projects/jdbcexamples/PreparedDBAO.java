package org.mydb.projects.jdbcexamples;

import java.sql.*;

public class PreparedDBAO extends DBAO {
	
	public PreparedDBAO(String configFile) {
		super(configFile);
	}
	
	public void printEmployeeData(int[] eids) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM emp WHERE eid=?");
			for(int eid: eids) {
				ps.setInt(1, eid);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					String name = rs.getString("ename");
					int age = rs.getInt("age");
					float sal = rs.getFloat("salary");
					System.out.println("eid:" + eid + " ename:" + name + " age:" + age + " salary:" + sal);
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
