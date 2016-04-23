package org.mydb.projects.runtransaction;

import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Runs queries against a back-end database
 */
public class DBAO {

	private String configFilename;
	private Properties configProps = new Properties();

	private String jSQLDriver;
	private String jSQLUrl;

	// DB Connection
	private Connection conn;

	// transactions
	private PreparedStatement beginTransactionStatement;
	private PreparedStatement commitTransactionStatement;
	private PreparedStatement rollbackTransactionStatement;
	
	public DBAO(String configFilename) {
		this.configFilename = configFilename;
	}

	/**********************************************************/
	/* Connection code to PostgreSQL. */
	public void openConnection() {
		try {
			configProps.load(new FileInputStream(configFilename));

			jSQLDriver = configProps.getProperty("flightservice.jdbc_driver");
			jSQLUrl = configProps.getProperty("flightservice.url");

			/* load jdbc drivers */
			Class.forName(jSQLDriver).newInstance();

			/* open connections to the flights database */
			conn = DriverManager.getConnection(jSQLUrl);

			conn.setAutoCommit(true); // by default automatically commit after
										// each statement
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		catch (IllegalAccessException iae) {
			iae.printStackTrace();
		}
		catch (InstantiationException inste) {
			inste.printStackTrace();
		}
		catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}

	}

	
	public void closeConnection() {
		try {
			conn.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	
	public void beginTransaction(String isolationLevel) {
		try {
			conn.setAutoCommit(false);
			beginTransactionStatement = conn.prepareStatement("BEGIN ISOLATION LEVEL " 
															+ isolationLevel + ";");
			beginTransactionStatement.executeUpdate();
			System.out.println("begin successful");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	
	public void commitTransaction() {
		try {
			commitTransactionStatement = conn.prepareStatement("COMMIT;");
			commitTransactionStatement.executeUpdate();
			conn.setAutoCommit(true);
			System.out.println("commit successful");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	
	public void rollbackTransaction() {
		try {
			rollbackTransactionStatement = conn.prepareStatement("ROLLBACK;");
			rollbackTransactionStatement.executeUpdate();
			conn.setAutoCommit(true);
			System.out.println("rollback successful");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void executeSelectStatement(String query) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.println("");

			int numberOfColumns = rsmd.getColumnCount();

			for (int i = 1; i <= numberOfColumns; i++) {
				if (i > 1)
					System.out.print(",  ");
				String columnName = rsmd.getColumnName(i);
				System.out.print(columnName);
			}
			System.out.println("");

			while (rs.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					if (i > 1)
						System.out.print(",  ");
					String columnValue = rs.getString(i);
					System.out.print(columnValue);
				}
				System.out.println("");
			}
			stmt.close();
		}
		catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
		}
	}
	
	
	public void executeUpdateStatement(String sqlStmtStr) {
		try {
			Statement stmt = conn.createStatement();
			int modified = stmt.executeUpdate(sqlStmtStr);
			System.out.println(modified + " rows modified");
		}
		catch(SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
		}
	}

}


