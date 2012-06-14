package org.societies.domainauthority.webapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/myresource")
public class MyResource {
    
	private static Logger LOG = LoggerFactory.getLogger(MyResource.class);

	/**
	 * URL parameter
	 */
	public static final String KEY = "key";
	
	/**
     * Method processing HTTP GET requests, producing "application/java-archive" MIME media type.
     * 
     * @return String that will be send back as a response of type "text/plain".
     */
	@Path("{name}.jar")
    @GET 
    @Produces("application/java-archive")
    public byte[] getIt(@PathParam("name") String name, @QueryParam(KEY) String key) {

		LOG.debug("HTTP GET: name = {}, key = {}", name, key);
		
		byte[] result = new byte[] {'a', 'h', 'o', 'j'};

		return result;
    }
}
