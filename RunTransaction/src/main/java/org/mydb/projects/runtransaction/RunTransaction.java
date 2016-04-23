package org.mydb.projects.runtransaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunTransaction {
	
	private static final String DBCONFIG_FILENAME = "dbconn.properties";
	
	private static final String[] isolationLevels= {"READ UNCOMMITTED", "READ COMMITTED", 
			                                        "REPEATABLE READ", "SERIALIZABLE"};
	private DBAO dbao;
	
	public RunTransaction() {
		/* prepare the database connection stuff */
		dbao = new DBAO(DBCONFIG_FILENAME);
		dbao.openConnection();
	}

	public static void usage() {
		/* prints the choices for commands and parameters */
		System.out.println();
		System.out.println(" *** Please enter one of the following commands *** ");
		System.out.println("> begin transaction");
		System.out.println("> execute sql statement");
		System.out.println("> commit transaction");
		System.out.println("> rollback transaction");
		System.out.println("> quit");
	}

	
	public static String[] tokenize(String command) {
		String regex = "\"([^\"]*)\"|(\\S+)";
		Matcher m = Pattern.compile(regex).matcher(command);
		List<String> tokens = new ArrayList<String>();
		while (m.find()) {
			/* group(num) returns the text matched by the numth set of capturing parentheses,
			 * or null if that set didn't participate in the match. A num of zero indicates
			 * the entire match, so group(0) is the same as group()
			 */
			if (m.group(1) != null) {
				tokens.add(m.group(1));
			} else {
				tokens.add(m.group(2));
			}
		}
		return tokens.toArray(new String[0]);
	}

	
	public void menu() {
		String command = null;
		try {
			while (true) {
				usage();
				BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("> ");
				command = r.readLine();
				String[] tokens = tokenize(command.trim());
				if (tokens.length == 0) {
					System.out.println("Please enter a command");
					continue; // back to top of loop
				}
				if (tokens[0].equals("begin")) {
					int retVal = beginTransactionOption(r);
					if (retVal == -1)
						continue;
				}
				else if (tokens[0].equals("execute")) {
					System.out.println();
					System.out.println(" *** Please enter the SQL statement *** ");
					String sqlStmtStr = r.readLine();
					if (sqlStmtStr.trim().toUpperCase().startsWith("SELECT")) {
						dbao.executeSelectStatement(sqlStmtStr);
					}
					else {
						dbao.executeUpdateStatement(sqlStmtStr);
					}
				}
				else if (tokens[0].equals("commit")) {
					dbao.commitTransaction();
				}
				else if (tokens[0].equals("rollback")) {
					dbao.rollbackTransaction();
				}
				else if (tokens[0].equals("quit")) {
					System.exit(0);
				}
				else {
					System.out.println("Error: unrecognized command '" + tokens[0] + "'");
				}
			} //end while
		} //end try
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	
	private int beginTransactionOption(BufferedReader r) {
		int retVal = -1;
		System.out.println();
		System.out.println(" *** Please select a transaction isolation level *** ");
		System.out.println("> [1] READ UNCOMMITTED");
		System.out.println("> [2] READ COMMITTED");
		System.out.println("> [3] REPEATABLE READ");
		System.out.println("> [4] SERIALIZABLE");
		try {
			String optionStr = r.readLine();
			int option = Integer.parseInt(optionStr);
			if (option >= 1 && option <= 4) {
				dbao.beginTransaction(isolationLevels[option - 1]);
				retVal = 1;
			}
			else {
				System.out.println("Error: invalid option");
				retVal = -1;
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			retVal = -1;
		}
		catch (NumberFormatException e) {
			System.out.println("Error: invalid option");
			retVal = -1;
		}
		return retVal;
	}
	
	
	public void closeConnection() {
		dbao.closeConnection();
	}
	
	
	public static void main(String[] args) {
		RunTransaction app = new RunTransaction();
		app.menu();
		app.closeConnection();
	}

	
}

