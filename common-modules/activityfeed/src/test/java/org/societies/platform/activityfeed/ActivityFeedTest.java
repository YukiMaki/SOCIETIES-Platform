/**
 * Copyright (c) 2011, SOCIETIES Consortium (WATERFORD INSTITUTE OF TECHNOLOGY (TSSG), HERIOT-WATT UNIVERSITY (HWU), SOLUTA.NET 
 * (SN), GERMAN AEROSPACE CENTRE (Deutsches Zentrum fuer Luft- und Raumfahrt e.V.) (DLR), Zavod za varnostne tehnologije
 * informacijske družbe in elektronsko poslovanje (SETCCE), INSTITUTE OF COMMUNICATION AND COMPUTER SYSTEMS (ICCS), LAKE
 * COMMUNICATIONS (LAKE), INTEL PERFORMANCE LEARNING SOLUTIONS LTD (INTEL), PORTUGAL TELECOM INOVAÇÃO, SA (PTIN), IBM Corp., 
 * INSTITUT TELECOM (ITSUD), AMITEC DIACHYTI EFYIA PLIROFORIKI KAI EPIKINONIES ETERIA PERIORISMENIS EFTHINIS (AMITEC), TELECOM 
 * ITALIA S.p.a.(TI),  TRIALOG (TRIALOG), Stiftelsen SINTEF (SINTEF), NEC EUROPE LTD (NEC))
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.societies.platform.activityfeed;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.activity.ActivityFeed;
import org.societies.activity.model.Activity;
import org.societies.api.activity.IActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * 
 * 
 * @author bjornmagnus adopted from solutanet
 * 
 */
@ContextConfiguration(locations = { "../../../../META-INF/ActivityFeedTest-context.xml" })
public class ActivityFeedTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	private static Logger LOG = LoggerFactory
			.getLogger(ActivityFeedTest.class);
	@Autowired
	private ActivityFeed actFeed;
	
	static {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
    }

	
	@Test
	@Rollback(false)
	public void testGetActivities() {

		try {
			
			actFeed.getActivities("","");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	@Rollback(false)
	public void testGetActivities2() {

		try {
			
			actFeed.getActivities("", "", "");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Rollback(false)
	public void testAddCisActivity() {

		try {
			
			actFeed.getActivities("", "", "");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Rollback(false)
	public void testCleanupFeed() {
		try{
			actFeed.cleanupFeed("");
		}catch(Exception e){
			
		}
		
	}
	@Test
	@Rollback(false)
	public void testFilter(){
		ActivityFeed actfeed = new ActivityFeed();
		Activity act1 = new Activity(); act1.setActor("user1"); act1.setPublished(Long.toString(System.currentTimeMillis()-100));
		String timeSeries = Long.toString(System.currentTimeMillis()-1000)+" "+Long.toString(System.currentTimeMillis());
		actfeed.addCisActivity(act1);
		JSONObject searchQuery = new JSONObject();
		try {
			searchQuery.append("filterBy", "actor");
			searchQuery.append("filterOp", "equals");
			searchQuery.append("filterValue", "user1");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		LOG.info("sending timeSeries: "+timeSeries+ " act published: "+act1.getPublished());
		List<IActivity> results = actfeed.getActivities(searchQuery.toString(), timeSeries);
		LOG.info("testing filtering filter result: "+results.size());
		assert(results.size() > 1);
	}
	
	@Test
	@Rollback(false)
	public void testBootStrap(){
		SessionFactory ses = ActivityFeed.getStaticSessionFactory();
		Session s = ses.openSession();
		Transaction t = s.beginTransaction();
		t.begin();
		for(int i=0;i<10;i++){
			ActivityFeed feed = new ActivityFeed(Integer.toString(i));
			
			s.save(feed);
		}
		t.commit();
		ActivityFeed queryFeed = ActivityFeed.startUp("0");
		assert(queryFeed != null);
	}

}
