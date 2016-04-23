package org.mydb.projects.flightbooking;

import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.FileInputStream;

/**
 * Runs queries against a back-end database
 */
public class Query {

	private String configFilename;
	private Properties configProps = new Properties();

	private String jSQLDriver;
	private String jSQLUrl;

	// DB Connection
	private Connection conn;

	// Logged In User
	private String username;
	private int cid; // Unique customer ID

	// Canned queries

	// search (one hop) -- This query ignores the month and year entirely. You
	// can change it to fix the month and year
	// to July 2015 or you can add month and year as extra, optional, arguments
	private static final String SEARCH_ONE_HOP_SQL = "SELECT year, month_id, day_of_month, carrier_id, flight_num, origin_city, actual_time "
			+ "FROM Flights " + "WHERE origin_city = ? AND dest_city = ? AND day_of_month = ? "
			+ "ORDER BY actual_time ASC LIMIT ? ";
	private PreparedStatement searchOneHopStatement;

	// TODO: Add more queries here

	// transactions
	private static final String BEGIN_TRANSACTION_SQL = "BEGIN ISOLATION LEVEL SERIALIZABLE;";
	private PreparedStatement beginTransactionStatement;

	private static final String COMMIT_SQL = "COMMIT;";
	private PreparedStatement commitTransactionStatement;

	private static final String ROLLBACK_SQL = "ROLLBACK;";
	private PreparedStatement rollbackTransactionStatement;

	public Query(String configFilename) {
		this.configFilename = configFilename;
	}

	/**********************************************************/
	/* Connection code to PostgreSQL. */
	public void openConnection() throws Exception {
		configProps.load(new FileInputStream(configFilename));

		jSQLDriver = configProps.getProperty("flightservice.jdbc_driver");
		jSQLUrl = configProps.getProperty("flightservice.url");

		/* load jdbc drivers */
		Class.forName(jSQLDriver).newInstance();

		/* open connections to the flights database */
		conn = DriverManager.getConnection(jSQLUrl);

		conn.setAutoCommit(true); // by default automatically commit after each
									// statement

		/*
		 * You will also want to appropriately set the transaction's isolation
		 * level through: conn.setTransactionIsolation(...)
		 */

	}

	public void closeConnection() throws Exception {
		conn.close();
	}

	/**********************************************************/
	/*
	 * prepare all the SQL statements in this method. "preparing" a statement is
	 * almost like compiling it. Note that the parameters (with ?) are still not
	 * filled in
	 */

	public void prepareStatements() throws Exception {
		searchOneHopStatement = conn.prepareStatement(SEARCH_ONE_HOP_SQL);
		beginTransactionStatement = conn.prepareStatement(BEGIN_TRANSACTION_SQL);
		commitTransactionStatement = conn.prepareStatement(COMMIT_SQL);
		rollbackTransactionStatement = conn.prepareStatement(ROLLBACK_SQL);

		/*
		 * add here more prepare statements for all the other queries you need
		 */
		/* . . . . . . */
	}

	public void transaction_login(String username, String password) throws Exception {
		// Add code here
	}

	/**
	 * Searches for flights from the given origin city to the given destination
	 * city, on the given day of the month. If "directFlight" is true, it only
	 * searches for direct flights, otherwise is searches for direct flights and
	 * flights with two "hops". Only searches for up to the number of
	 * itineraries given. Prints the results found by the search.
	 */
	public void transaction_search_safe(String originCity, String destinationCity, boolean directFlight, int dayOfMonth,
			int numberOfItineraries) throws Exception {

		// one hop itineraries
		searchOneHopStatement.clearParameters();
		searchOneHopStatement.setString(1, originCity);
		searchOneHopStatement.setString(2, destinationCity);
		searchOneHopStatement.setInt(3, dayOfMonth);
		searchOneHopStatement.setInt(4, numberOfItineraries);
		ResultSet oneHopResults = searchOneHopStatement.executeQuery();
		while (oneHopResults.next()) {
			int result_year = oneHopResults.getInt("year");
			int result_monthId = oneHopResults.getInt("month_id");
			int result_dayOfMonth = oneHopResults.getInt("day_of_month");
			String result_carrierId = oneHopResults.getString("carrier_id");
			String result_flightNum = oneHopResults.getString("flight_num");
			String result_originCity = oneHopResults.getString("origin_city");
			int result_time = oneHopResults.getInt("actual_time");
			System.out.println("Flight: " + result_year + "," + result_monthId + "," + result_dayOfMonth + ","
					+ result_carrierId + "," + result_flightNum + "," + result_originCity + "," + result_time);
		}
		oneHopResults.close();

		// Add code here

	}

	public void transaction_search_unsafe(String originCity, String destinationCity, boolean directFlight,
			int dayOfMonth, int numberOfItineraries) throws Exception {

		// one hop itineraries
		String unsafeSearchSQL = "SELECT year, month_id, day_of_month, carrier_id, flight_num, origin_city, actual_time "
				+ "FROM Flights "
				+ "WHERE origin_city = \'" + originCity + "\' AND dest_city = \'" + destinationCity
				+ "\' AND day_of_month =  " + dayOfMonth + " " + "ORDER BY actual_time ASC LIMIT " + numberOfItineraries;

		System.out.println("Submitting query: " + unsafeSearchSQL);
		Statement searchStatement = conn.createStatement();
		ResultSet oneHopResults = searchStatement.executeQuery(unsafeSearchSQL);

		while (oneHopResults.next()) {
			int result_year = oneHopResults.getInt("year");
			int result_monthId = oneHopResults.getInt("month_id");
			int result_dayOfMonth = oneHopResults.getInt("day_of_month");
			String result_carrierId = oneHopResults.getString("carrier_id");
			String result_flightNum = oneHopResults.getString("flight_num");
			String result_originCity = oneHopResults.getString("origin_city");
			int result_time = oneHopResults.getInt("actual_time");
			System.out.println("Flight: " + result_year + "," + result_monthId + "," + result_dayOfMonth + ","
					+ result_carrierId + "," + result_flightNum + "," + result_originCity + "," + result_time);
		}
		oneHopResults.close();
	}

	public void transaction_book(int itineraryId) throws Exception {
		// Add code here
	}

	public void transaction_reservations() throws Exception {
		// Add code here
	}

	public void transaction_cancel(int reservationId) throws Exception {
		// Add code here
	}

	public void beginTransaction() throws Exception {
		conn.setAutoCommit(false);
		beginTransactionStatement.executeUpdate();
	}

	public void commitTransaction() throws Exception {
		commitTransactionStatement.executeUpdate();
		conn.setAutoCommit(true);
	}

	public void rollbackTransaction() throws Exception {
		rollbackTransactionStatement.executeUpdate();
		conn.setAutoCommit(true);
	}

}


