package org.mydb.projects.jdbcexamples;

/**
 * Main class to test the DBAO.
 *
 */
public class App {
	
	private String configFile;
	
	public App(String configFile) {
		this.configFile = configFile;
	}
	
	private void executeStatement() {
		DBAO dbao = new DBAO(this.configFile);
		dbao.printEmployees();
	}
	
	private void executePreparedStatement() {
		int[] eids = {142519864, 11564812, 74454898, 156489494};
		PreparedDBAO dbao = new PreparedDBAO(this.configFile);
		dbao.printEmployeeData(eids);
	}
	
	private void executeUpdateStatement() {
		UpdateDBAO dbao = new UpdateDBAO(this.configFile);
		//sum of all salaries = 2437368.00 (8 tuples with salary > 60,000)
		dbao.updateEmployeeSalaries(1000.0f, 60000.0f);
	}
	
	private void executeStatementWithMetaData() {
		MetaDataDBAO dbao = new MetaDataDBAO(this.configFile);
		dbao.printTableAsJSON("emp", 5);
	}
	
    public static void main( String[] args ) {
        App app = new App(args[0]);
        //app.executeStatement();
        //app.executePreparedStatement();
        //app.executeUpdateStatement();
        app.executeStatementWithMetaData();
    }
    
}


