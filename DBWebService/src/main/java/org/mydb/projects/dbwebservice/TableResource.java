package org.mydb.projects.dbwebservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/tableresource")
public class TableResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt(@QueryParam("tablename") String tablename, @QueryParam("limit") String limitStr) {
    	MetaDataDBAO dbao = MetaDataDBAO.getInstance();
    	dbao.init("dbconn.properties");
    	String retVal = null;
    	try {
    		int limit = Integer.parseInt(limitStr);
    		retVal = dbao.getTableAsJSON(tablename, limit).toString();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	return retVal;
    }
    
}


