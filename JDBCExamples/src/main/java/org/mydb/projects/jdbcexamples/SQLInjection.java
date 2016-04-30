package org.mydb.projects.jdbcexamples;

import java.io.Console;
import java.sql.*;

public class SQLInjection {
	
	private DBAO dbao;
	
	public SQLInjection(String configFile) {
		this.dbao = new DBAO(configFile);
	}
	
	private void execute() {
		//90873519; CREATE TABLE Foo(a integer);
		try {
			Console console = System.console();
			String eidStr = console.readLine("Enter the employee id: ");
			Statement stmt = dbao.conn.createStatement();
			int modified = stmt.executeUpdate("UPDATE emp SET salary=salary+1000 WHERE eid=" + eidStr);
			System.out.println("Salary updated.");
		}
		catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		SQLInjection app = new SQLInjection(args[0]);
		app.execute();
	}

}

