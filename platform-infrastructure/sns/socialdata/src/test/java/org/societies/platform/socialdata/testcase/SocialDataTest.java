package org.societies.platform.socialdata.testcase;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.shindig.social.opensocial.model.ActivityEntry;
import org.apache.shindig.social.opensocial.model.Group;
import org.apache.shindig.social.opensocial.model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.societies.api.internal.sns.ISocialConnector;
import org.societies.api.internal.sns.ISocialData;
import org.societies.platform.socialdata.SocialData;


public class SocialDataTest {

	
    private static String access_token = "AAAFs43XOj3IBAGbtrA2I7cibWs8YD1ODGr7JiqXl0ZCJ4DBkeXKeSsth9r2EbRGj6jh1eBIhUAkIZBNs1nKOJU1Ys81xKxUqZAC13DwBAZDZD";
	private static ISocialConnector fbConnector=null;
	private static ISocialData		socialData =null;
	private static final Logger logger   = Logger.getLogger(SocialDataTest.class.getSimpleName());
	
	private static ISocialConnector mockedSocialConnector; 
	
	@BeforeClass
	public static void setUp() throws Exception {
		
		
		 
		
		  mockedSocialConnector = mock(ISocialConnector.class);
		  stub(mockedSocialConnector.getConnectorName()).toReturn("facebook");
		  stub(mockedSocialConnector.getID()).toReturn("facebook_0001");
		  stub(mockedSocialConnector.getUserFriends()).toReturn(readFileAsString("mocks/friends.txt"));
		  stub(mockedSocialConnector.getUserActivities()).toReturn(readFileAsString("mocks/activities.txt"));
		  stub(mockedSocialConnector.getUserGroups()).toReturn(readFileAsString("mocks/groups.txt"));
		  stub(mockedSocialConnector.getUserProfile()).toReturn(readFileAsString("mocks/profile.txt"));

		  
		 // fbConnector = new FacebookConnectorImpl(access_token, null);
		  socialData  = new SocialData();
		  
		  assertNotNull("Social Data is null", socialData);
		  assertNotNull("Moked FB Connector is null", mockedSocialConnector);
		 
		  
		  
		  logger.info("Created new FB Connector with id:" + mockedSocialConnector.getID());
		  logger.info("Created SocialData Container"+socialData.getLastUpdate());
		  
		  try {
			socialData.addSocialConnector(mockedSocialConnector);
			logger.info("Add Social Connector");
		} catch (Exception e) {
			
			e.printStackTrace();
			fail(e.getMessage());
		}
		  
		
	}
	
	private static String readFileAsString(String filePath)
		    throws java.io.IOException{
		        StringBuffer fileData = new StringBuffer(1000);
		        BufferedReader reader = new BufferedReader(
		                new FileReader(filePath));
		        char[] buf = new char[1024];
		        int numRead=0;
		        while((numRead=reader.read(buf)) != -1){
		            String readData = String.valueOf(buf, 0, numRead);
		            fileData.append(readData);
		            buf = new char[1024];
		        }
		        reader.close();
		        return fileData.toString();
		    }

	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		mockedSocialConnector = null;
	
	}

	
	@Before
	public void updateSocialDataTest(){
		try{
			socialData.updateSocialData();
			logger.info("SocialData ready in memory");
		}
		catch(Exception ex){
			fail(ex.getMessage());
		}
		
	}
	
	@Test
	public void printProfiles(){
		List<?> profiles = null;
		profiles = socialData.getSocialProfiles();
		assertNotNull("Profiles are null", profiles);
	    Iterator it = profiles.iterator();
	    int index =1;
	    while(it.hasNext()){
	    	Person p = (Person) it.next();
	    	logger.info("--- Profile "+index +" ID:" +p.getId() + " -->"+p.getName().getFormatted() );
	    	index++;
	    }
		
	}
	
	@Test
	public void printFriends(){
		List<?> friends = null;
		friends = socialData.getSocialPeople();
		assertNotNull("Friends list is null", friends);
	    Iterator it = friends.iterator();
	    int index =1;
	    while(it.hasNext()){
	    	Person p = (Person) it.next();
	    	logger.info(index +" FriendsID:" +p.getId() + " -->"+p.getName().getFormatted() );
	    	index++;
	    }
		
	}
	
	@Test
	public void printGroups(){
		List<?> groups = null;
		groups = socialData.getSocialGroups();
		assertNotNull("Social Group list is null", groups);
	    Iterator it = groups.iterator();
	    int index =1;
	    while(it.hasNext()){
	    	Group g = (Group) it.next();
	    	logger.info(index +" GroupID:" +g.getId() + " Title: "+g.getTitle() +  " descr:"+g.getDescription());
	    	index++;
	    }
		
	}
	
	@Test
	public void printActivities(){
		List<?> activities = null;
		activities = socialData.getSocialActivity();
		assertNotNull("Social Activity list is null", activities);
	    Iterator it = activities.iterator();
	    int index =1;
	    while(it.hasNext()){
	    	ActivityEntry a = (ActivityEntry) it.next();
	    	logger.info(index +"] " +a.getActor().getDisplayName() + " made "+a.getVerb() + " TO "+a.getContent());
	    	index++;
	    }
		
	}
	
	
	@Test
	public void printConnectors(){
		List<ISocialConnector> connectors = null;
		connectors = socialData.getSocialConnectors();
		assertNotNull("Connector list is null", connectors);
	    Iterator<ISocialConnector> it = connectors.iterator();
	    int index =1;
	    while(it.hasNext()){
	    	ISocialConnector conn = it.next();
	    	logger.info(index +"] Connector for " +conn.getConnectorName() + " ID: "+conn.getID());
	    	index++;
	    }
		
	}
	
	
	

}
