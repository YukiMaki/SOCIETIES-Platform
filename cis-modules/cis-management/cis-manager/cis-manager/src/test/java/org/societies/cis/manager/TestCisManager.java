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

package org.societies.cis.manager;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.activity.ActivityFeed;
import org.societies.api.cis.management.ICisOwned;
import org.societies.api.cis.management.ICisParticipant;
import org.societies.api.cis.management.ICis;
import org.societies.api.comm.xmpp.exceptions.CommunicationException;
import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.societies.api.comm.xmpp.interfaces.IFeatureServer;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.IIdentityManager;
import org.societies.api.identity.INetworkNode;
import org.societies.api.identity.InvalidFormatException;
import org.societies.api.internal.comm.ICISCommunicationMgrFactory;
import org.societies.identity.NetworkNodeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

/**
 * Junit and Mockito Test for CIS
 *
 * @author Thomas Vilarinho (Sintef)
 *
 */
//@RunWith(PowerMockRunner.class)
  
@PrepareForTest( { ActivityFeed.class })
@ContextConfiguration(locations = { "../../../../CisManagerTest-context.xml" })
public class TestCisManager extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger LOG = LoggerFactory
			.getLogger(TestCisManager.class);
	//@Autowired
	private CisManager cisManagerUnderTest;
	@Autowired
	private SessionFactory sessionFactory;
	private ICISCommunicationMgrFactory mockCcmFactory;
	private ICommManager mockCSSendpoint;
	private ICommManager mockCISendpoint1;
	private ICommManager mockCISendpoint2;
	private ICommManager mockCISendpoint3;
	
	public static final String TEST_GOOD_JID = "testXcmanager.societies.local";
	public static final String TEST_CSSID = "juca@societies.local";
	public static final String TEST_CSS_PWD = "password";
	public static final String TEST_CIS_NAME_1 = "Flamengo Futebol Clube";
	public static final String TEST_CIS_TYPW = "futebol";
	public static final int TEST_CIS_MODE = 0;
	
	public static final String TEST_CISID_1 = "flamengo.societies.local";
	public static final String TEST_CISID_2 = "santos.societies.local";
	public static final String TEST_CIS_NAME_2 = "Santos Futebol Clube";
	public static final String TEST_CISID_3 = "palmeiras.societies.local";
	public static final String TEST_CIS_NAME_3 = "Palmeiras Futebol Clube";
	
	public static final String MEMBER_JID_1 = "zico@flamengo.com";
	public static final String MEMBER_ROLE_1 = "participant";

	public static final String MEMBER_JID_2 = "romario@vasco.com";
	public static final String MEMBER_ROLE_2 = "participant";

	public static final String MEMBER_JID_3 = "pele@santos.com";
	public static final String MEMBER_ROLE_3 = "admin";

	
	public static final String INVALID_USER_JID = "invalid";
	public static final String INVALID_ROLE = "invalid";
	
	
	IIdentityManager mockIICisManagerId;
	INetworkNode testCisManagerId;
	INetworkNode testCisId_1;
	INetworkNode testCisId_2;
	INetworkNode testCisId_3;
	IIdentityManager mockIICisId_1;
	IIdentityManager mockIICisId_2;
	IIdentityManager mockIICisId_3;
	Session session = null;
	
	void setUpFactory() throws Exception {
		System.out.println("in setupFactory!");
		mockCcmFactory = mock(ICISCommunicationMgrFactory.class);
		
		// mocking the IcomManagers
		mockCISendpoint1 = mock (ICommManager.class);
		mockCISendpoint2 = mock (ICommManager.class);
		mockCISendpoint3 = mock (ICommManager.class);

		// mocking their Identity Manager
		mockIICisId_1 = mock (IIdentityManager.class);
		mockIICisId_2 = mock (IIdentityManager.class);
		mockIICisId_3 = mock (IIdentityManager.class);

		
		// creating a NetworkNordImpl for each Identity Manager		
		testCisId_1 = new NetworkNodeImpl(TEST_CISID_1);
		testCisId_2 = new NetworkNodeImpl(TEST_CISID_2);
		testCisId_3 = new NetworkNodeImpl(TEST_CISID_3);
		when(mockCISendpoint1.getIdManager()).thenReturn(mockIICisId_1);
		when(mockCISendpoint2.getIdManager()).thenReturn(mockIICisId_2);
		when(mockCISendpoint3.getIdManager()).thenReturn(mockIICisId_3);
		
		when(mockIICisId_1.getThisNetworkNode()).thenReturn(testCisId_1);
		when(mockIICisId_2.getThisNetworkNode()).thenReturn(testCisId_2);
		when(mockIICisId_3.getThisNetworkNode()).thenReturn(testCisId_3);
		
		
		when(mockCISendpoint1.UnRegisterCommManager()).thenReturn(true);
		when(mockCISendpoint2.UnRegisterCommManager()).thenReturn(true);
		when(mockCISendpoint3.UnRegisterCommManager()).thenReturn(true);
		
		
		
		when(mockCcmFactory.getNewCommManager()).thenReturn(mockCISendpoint1,mockCISendpoint2,mockCISendpoint3);
		
		
		
	}
	
	@Before
	public void setUp() throws Exception {
		// create mocked class
		System.out.println("in setup!");
		mockCSSendpoint = mock (ICommManager.class);

		mockIICisManagerId = mock (IIdentityManager.class);
		
		testCisManagerId = new NetworkNodeImpl(TEST_GOOD_JID);
		
		// mocking the CISManager
		when(mockCSSendpoint.getIdManager()).thenReturn(mockIICisManagerId);
		when(mockIICisManagerId.getThisNetworkNode()).thenReturn(testCisManagerId);
		doNothing().when(mockCSSendpoint).register(any(org.societies.api.comm.xmpp.interfaces.IFeatureServer.class));
		
		// mocking the activity feed static methods
		PowerMockito.mockStatic(ActivityFeed.class);
		//this.session = sessionFactory.openSession();
		System.out.println("in setup! cisManagerUnderTest.getSessionFactory(): "+sessionFactory);
		ActivityFeed.setStaticSessionFactory(sessionFactory);
		//cisManagerUnderTest.setSessionFactory(sessionFactory);
		//cisManagerUnderTest.setSessionFactory(sessionFactory);
		//Mockito.when(ActivityFeed.startUp(anyString())).thenReturn(new ActivityFeed());
		setUpFactory();
		
	}

	@After
	public void tearDown() throws Exception {
		mockCcmFactory = null;
		mockCSSendpoint = null;
		testCisManagerId = null;
		
		//sessionFactory.getCurrentSession().close();
		//if(sessionFactory.getCurrentSession()!=null)
		//	sessionFactory.getCurrentSession().disconnect();

	}
	//@Ignore
	@Test
	public void testConstructor() {

		cisManagerUnderTest = new CisManager();
		cisManagerUnderTest.setICommMgr(mockCSSendpoint); cisManagerUnderTest.setCcmFactory(mockCcmFactory); cisManagerUnderTest.setSessionFactory(sessionFactory);
		cisManagerUnderTest.init();
		
		assertEquals(TEST_GOOD_JID, cisManagerUnderTest.cisManagerId.getJid());
	}
	//@Ignore
	@Test
	public void testCreateCIS() {
		
		cisManagerUnderTest = new CisManager();
		cisManagerUnderTest.setICommMgr(mockCSSendpoint); cisManagerUnderTest.setCcmFactory(mockCcmFactory); cisManagerUnderTest.setSessionFactory(sessionFactory);
		cisManagerUnderTest.init();
		
		Future<ICisOwned> testCIS = cisManagerUnderTest.createCis(TEST_CSSID, TEST_CSS_PWD,
				TEST_CIS_NAME_1, TEST_CIS_TYPW , TEST_CIS_MODE);
		try {
			assertNotNull(testCIS.get());
			assertNotNull(testCIS.get().getCisId());
			assertEquals(testCIS.get().getName(), TEST_CIS_NAME_1);
			assertEquals(testCIS.get().getCisType(), TEST_CIS_TYPW);
			assertEquals(testCIS.get().getMembershipCriteria(), TEST_CIS_MODE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	//@Ignore
	@Test
	public void testListCIS() throws InterruptedException, ExecutionException {

		cisManagerUnderTest = new CisManager();
		cisManagerUnderTest.setICommMgr(mockCSSendpoint); cisManagerUnderTest.setCcmFactory(mockCcmFactory);cisManagerUnderTest.setSessionFactory(sessionFactory);
		cisManagerUnderTest.init();
		
		ICisOwned[] ciss = new ICisOwned [3]; 
		int[] cissCheck = {0,0,0};
		
		ciss[0] =  (cisManagerUnderTest.createCis(TEST_CSSID, TEST_CSS_PWD,
				TEST_CIS_NAME_1+"aa", TEST_CIS_TYPW , TEST_CIS_MODE)).get();
		ciss[1] = (cisManagerUnderTest.createCis(TEST_CSSID, TEST_CSS_PWD,
				TEST_CIS_NAME_2, TEST_CIS_TYPW , TEST_CIS_MODE)).get();
		ciss[2] = (cisManagerUnderTest.createCis(TEST_CSSID, TEST_CSS_PWD,
				TEST_CIS_NAME_3, TEST_CIS_TYPW , TEST_CIS_MODE)).get();

		List<ICisOwned> l = cisManagerUnderTest.getListOfOwnedCis();
		Iterator<ICisOwned> it = l.iterator();
		 
		while(it.hasNext()){
			ICisOwned element = it.next();
			 assertEquals(element.getOwnerId(),TEST_CSSID);
			 for(int i=0;i<ciss.length;i++){
				 if(element.getName().equals(ciss[i].getName()) 
				&& 	element.getCisId().equals(ciss[i].getCisId())
				&& 	element.getCisType().equals(ciss[i].getCisType())
				&& 	(element.getMembershipCriteria() == ciss[i].getMembershipCriteria())		 
						 )
					 cissCheck[i] = 1; // found a matching CIS
					 
			 }
			 
			 //LOG.info("CIS with id " + element.getCisRecord().getCisId());
	     }
		
		// check if it found all matching CISs
		 for(int i=0;i<ciss.length;i++){
			 assertEquals(cissCheck[i], 1);
		 }
	
	}
	//TODO: this tests fails on line 802 in Cis.java: 				IIdentity targetCssIdentity = this.CISendpoint.getIdManager().fromJid(element.getMembersJid());//new IdentityImpl(element.getMembersJid());
	//TODO: needs more mocking thomas?
	@Ignore
	@Test
	public void testdeleteCIS() throws InterruptedException, ExecutionException {

		cisManagerUnderTest = new CisManager();
		LOG.info("testdeleteCIS, sessionFactory: "+sessionFactory.hashCode());
		cisManagerUnderTest.setICommMgr(mockCSSendpoint); cisManagerUnderTest.setCcmFactory(mockCcmFactory); cisManagerUnderTest.setSessionFactory(sessionFactory);
		cisManagerUnderTest.init();
		LOG.info("testdeleteCIS, sessionFactory: "+sessionFactory.hashCode());
		
		ICisOwned[] ciss = new ICisOwned [2]; 
		String jidTobeDeleted = "";
		
		ciss[0] =  (cisManagerUnderTest.createCis(TEST_CSSID, TEST_CSS_PWD,
				TEST_CIS_NAME_1, TEST_CIS_TYPW , TEST_CIS_MODE)).get();
		ciss[1] = (cisManagerUnderTest.createCis(TEST_CSSID, TEST_CSS_PWD,
				TEST_CIS_NAME_2, TEST_CIS_TYPW , TEST_CIS_MODE)).get();
		
		LOG.info("cis 1 sessionfactory:"+((Cis)ciss[0]).getSessionFactory().hashCode());
		List<ICis> l = cisManagerUnderTest.getCisList();
		LOG.info("cis 1 sessionfactory:"+((Cis)l.get(0)).getSessionFactory());
		Iterator<ICis> it = l.iterator();
		ICis element = it.next(); 
		jidTobeDeleted = element.getCisId();
		
		boolean presence = false;
		
		presence = cisManagerUnderTest.deleteCis("", "", jidTobeDeleted);
		assertEquals(true,presence);
		
		presence = false;
		// refresh list and get a new iterator
		l = cisManagerUnderTest.getCisList();
		it = l.iterator();
		
		int interactions = 0;
		while(it.hasNext()){
			 element = it.next();
			 interactions++;
			 if(element.getCisId().equals(jidTobeDeleted))		 
						presence = true; // found a matching CIS
	     }
		
		assertEquals(false,presence);
		assertEquals(1,interactions);
		
	
	}
	
	//@Ignore
	//@Rollback
	@Test
	public void testAddMemberToOwnedCIS() throws InterruptedException, ExecutionException {

		cisManagerUnderTest = new CisManager();
		cisManagerUnderTest.setICommMgr(mockCSSendpoint); cisManagerUnderTest.setCcmFactory(mockCcmFactory); cisManagerUnderTest.setSessionFactory(sessionFactory);
		cisManagerUnderTest.init();
		
		ICisOwned Iciss =  (cisManagerUnderTest.createCis(TEST_CSSID, TEST_CSS_PWD,
				TEST_CIS_NAME_1, TEST_CIS_TYPW , TEST_CIS_MODE)).get();

		try {
			assertEquals(true,Iciss.addMember(MEMBER_JID_1, MEMBER_ROLE_1).get());
			assertEquals(true,Iciss.addMember(MEMBER_JID_2, MEMBER_ROLE_2).get());
			assertEquals(false,Iciss.addMember(MEMBER_JID_3, INVALID_ROLE).get());
			// assertEquals(false,Iciss.addMember(INVALID_USER_JID, MEMBER_ROLE_3).get());  NOT USE OF TESTING THAT AS IDENTITY MANAGER HAS BEEN MOCKED
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
	
	}
	@Ignore
	@Test
	public void listdMembersOnOwnedCIS() throws InterruptedException, ExecutionException {

		cisManagerUnderTest = new CisManager();
		cisManagerUnderTest.setICommMgr(mockCSSendpoint); cisManagerUnderTest.setCcmFactory(mockCcmFactory); cisManagerUnderTest.setSessionFactory(sessionFactory);
		cisManagerUnderTest.init();
		ICisOwned Iciss =  (cisManagerUnderTest.createCis(TEST_CSSID, TEST_CSS_PWD,
				TEST_CIS_NAME_1, TEST_CIS_TYPW , TEST_CIS_MODE)).get();
				
		try {
			Iciss.addMember(MEMBER_JID_1, MEMBER_ROLE_1).get();
			Iciss.addMember(MEMBER_JID_2, MEMBER_ROLE_2).get();
			Iciss.addMember(MEMBER_JID_3, MEMBER_ROLE_3).get();
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		int[] memberCheck = {0,0,0};
		
		Set<ICisParticipant> l = (Iciss.getMemberList()).get();
		Iterator<ICisParticipant> it = l.iterator();
		 
		while(it.hasNext()){
			ICisParticipant element = it.next();
			if(element.getMembersJid().equals(MEMBER_JID_1) && element.getMembershipType().equals(MEMBER_ROLE_1))
				memberCheck[0] = 1;
			if(element.getMembersJid().equals(MEMBER_JID_2) && element.getMembershipType().equals(MEMBER_ROLE_2))
				memberCheck[1] = 1;	
			if(element.getMembersJid().equals(MEMBER_JID_3) && element.getMembershipType().equals(MEMBER_ROLE_3))
				memberCheck[2] = 1;	

	     }
		
		// check if it found all matching CISs
		 for(int i=0;i<memberCheck.length;i++){
			 assertEquals(memberCheck[i], 1);
		 }	
	
	}
	
}
