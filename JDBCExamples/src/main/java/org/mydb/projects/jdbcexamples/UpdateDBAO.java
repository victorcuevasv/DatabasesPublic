package org.mydb.projects.jdbcexamples;

import java.sql.*;

public class UpdateDBAO extends DBAO {
	
	public UpdateDBAO(String configFile) {
		super(configFile);
	}
	
	public void updateEmployeeSalaries(float raise, float baseSalary) {
		try {
			PreparedStatement ps;
			ps = conn.prepareStatement("UPDATE emp SET salary = salary + ? WHERE salary > ?");
			ps.setFloat(1,  raise);
			ps.setFloat(2,  baseSalary);
			int modified = ps.executeUpdate();
			System.out.println(modified + " rows modified.");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
