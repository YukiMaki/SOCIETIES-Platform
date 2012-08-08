package org.societies.platform.activityfeed;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.activity.ActivityFeed;
import org.societies.activity.PersistedActivityFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = { "../../../../META-INF/ActivityFeedTest-context.xml" })
public class SpeedTests extends
AbstractTransactionalJUnit4SpringContextTests {
	
	private static Logger LOG = LoggerFactory.getLogger(SpeedTests.class);
	
	int messages = 10000;
	@Autowired
	private ActivityFeed actFeed;
	PersistedActivityFeed pFeed;
	
	private SessionFactory sessionFactory=null;
	private Session session=null;
	
	static {
		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
	}
	int feedid=0;
	@Before
	public void setupBefore() throws Exception {
		if(sessionFactory==null){
			sessionFactory = actFeed.getSessionFactory();
		}
		if(session==null){
			session = sessionFactory.openSession();
			actFeed.setSession(session);
		}
		if(!session.isOpen())
			session = sessionFactory.openSession();
		LOG.info("i startup ");
		
	}
	@After
	public void tearDownAfter() throws Exception {
		
		actFeed.clear();
		session.close();
		actFeed = null;
	}
	
	@Test
	void testOrig(){
		actFeed = new ActivityFeed();
		actFeed.startUp(session, "1");
		long start = System.currentTimeMillis();
	}
	@Test
	void testPersisted(){
		pFeed.startUp(session, "2");
		long start = System.currentTimeMillis();
	}
}
