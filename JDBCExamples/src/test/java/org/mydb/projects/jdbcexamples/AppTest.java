package org.mydb.projects.jdbcexamples;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.sql.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    
    public void testAttributeRetrieval() {
    	boolean match = false;
    	try {
    		DBAO dbao = new DBAO("dbconn.properties");
    		Statement stmt = dbao.conn.createStatement();
    		ResultSet rs = stmt.executeQuery("SELECT * FROM emp");
    		match = true;
    		while(rs.next()) {
    			int eid1 = rs.getInt("eid");
    			int eid2 = rs.getInt(1);
    			if( eid1 != eid2 )
    				match = false;
    			String ename1 = rs.getString("ename");
    			String ename2 = rs.getString(2);
    			if( ! ename1.equals(ename2) )
    				match = false;
    			int age1 = rs.getInt("age");
    			int age2 = rs.getInt(3);
    			if( age1 != age2 )
    				match = false;
    			float salary1 = rs.getFloat("salary");
    			float salary2 = rs.getFloat(4);
    			if( salary1 != salary2 )
    				match = false;
    		}
    	}
    	catch(Exception e) {
    		match = false;
    		e.printStackTrace();
    	}
    	assertTrue( match );
    }
    
}


