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

package org.societies.useragent.feedback;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.societies.api.context.CtxException;
import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxAttributeIdentifier;
import org.societies.api.context.model.CtxEntityIdentifier;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxModelObject;
import org.societies.api.context.model.CtxModelType;
import org.societies.api.internal.context.broker.ICtxBroker;
import org.societies.api.internal.context.model.CtxAttributeTypes;
import org.societies.api.internal.useragent.model.ExpProposalContent;
import org.societies.api.internal.useragent.model.ExpProposalType;
import org.societies.api.internal.useragent.model.ImpProposalContent;
import org.societies.api.internal.useragent.model.ImpProposalType;
import org.springframework.scheduling.annotation.AsyncResult;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestUserFeedback extends TestCase{

	ICtxBroker mockCtxBroker;
	ICommManager mockCommsMgr;
	CtxEntityIdentifier mockEntityId;
	CtxAttributeIdentifier mockUIDId;
	CtxAttribute mockUID;
	List<CtxIdentifier> mockUIDIds;
	String mockMyId;
	String mockMyDeviceID;
	
	UserFeedback userFeedback;
	List<String> expFeedback;
	Boolean impFeedback;

	public void setUp() throws Exception{
		mockCtxBroker = mock(ICtxBroker.class);
		mockCommsMgr = mock(ICommManager.class);
		
		userFeedback = new UserFeedback();
		userFeedback.setCtxBroker(mockCtxBroker);
		userFeedback.setCommsMgr(mockCommsMgr);
		
		mockMyId = "sarah.societies.local";
		mockMyDeviceID = "sarah.societies.local/laptop";
		userFeedback.myDeviceID = mockMyDeviceID;
		
		mockEntityId = new CtxEntityIdentifier(mockMyId, "testEntity", new Long(12345));
		mockUIDId = new CtxAttributeIdentifier(mockEntityId, "uid", new Long(12345));
		mockUID = new CtxAttribute(mockUIDId);
		mockUID.setStringValue(mockMyDeviceID);
		mockUIDIds = new ArrayList<CtxIdentifier>();
		mockUIDIds.add(mockUIDId);
		
		expFeedback = null;
		impFeedback = null;
	}

	public void tearDown() throws Exception{
		//null
	}
	
	public void testTmp(){
		Assert.assertTrue(true);
	}

	public void testAckNackGUI() {
		try {
			when(mockCtxBroker.lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.UID)).thenReturn(new AsyncResult<List<CtxIdentifier>>(mockUIDIds));
			when(mockCtxBroker.retrieve(mockUIDId)).thenReturn(new AsyncResult<CtxModelObject>(mockUID));
			
			String proposalText = "Press: YES";
			String[] options = {"YES", "NO"};	
			expFeedback = userFeedback.getExplicitFB(ExpProposalType.ACKNACK, new ExpProposalContent(proposalText, options)).get();
			while(expFeedback == null){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			verify(mockCtxBroker).lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.UID);
			verify(mockCtxBroker).retrieve(mockUIDId);
			
			//analyse results
			System.out.println("Got here");
			Assert.assertNotNull(expFeedback);
			Assert.assertTrue(expFeedback.size() == 1);
			String results = expFeedback.get(0);
			Assert.assertEquals("YES", results);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		} catch (CtxException e) {
			e.printStackTrace();
		}
	}



	public void testCheckBoxGUI(){
		try {
			when(mockCtxBroker.lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.UID)).thenReturn(new AsyncResult<List<CtxIdentifier>>(mockUIDIds));
			when(mockCtxBroker.retrieve(mockUIDId)).thenReturn(new AsyncResult<CtxModelObject>(mockUID));
			
			String proposalText = "Select: RED, GREEN and BLUE";
			String[] options = {"RED", "WHITE", "GREEN", "BLUE", "BLACK", "YELLOW"};
			expFeedback = userFeedback.getExplicitFB(ExpProposalType.CHECKBOXLIST, new ExpProposalContent(proposalText, options)).get();
			while(expFeedback == null){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			verify(mockCtxBroker).lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.UID);
			verify(mockCtxBroker).retrieve(mockUIDId);
			
			//analyse results
			Assert.assertNotNull(expFeedback);
			Assert.assertTrue(expFeedback.size() == 3);
			Assert.assertTrue(expFeedback.contains("RED"));
			Assert.assertTrue(expFeedback.contains("GREEN"));
			Assert.assertTrue(expFeedback.contains("BLUE"));
			Assert.assertTrue(!expFeedback.contains("WHITE"));
			Assert.assertTrue(!expFeedback.contains("BLACK"));
			Assert.assertTrue(!expFeedback.contains("YELLOW"));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		} catch (CtxException e) {
			e.printStackTrace();
		}
	}



	public void testRadioGUI(){
		try {
			when(mockCtxBroker.lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.UID)).thenReturn(new AsyncResult<List<CtxIdentifier>>(mockUIDIds));
			when(mockCtxBroker.retrieve(mockUIDId)).thenReturn(new AsyncResult<CtxModelObject>(mockUID));
			
			String proposalText = "Select: WHITE";
			String[] options = {"RED", "WHITE", "GREEN", "BLUE", "BLACK", "YELLOW"};
			expFeedback = userFeedback.getExplicitFB(ExpProposalType.RADIOLIST, new ExpProposalContent(proposalText, options)).get();
			while(expFeedback == null){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			verify(mockCtxBroker).lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.UID);
			verify(mockCtxBroker).retrieve(mockUIDId);
			
			//analyse results
			Assert.assertNotNull(expFeedback);
			Assert.assertTrue(expFeedback.size() == 1);
			String results = expFeedback.get(0);
			Assert.assertEquals("WHITE", results);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		} catch (CtxException e) {
			e.printStackTrace();
		}
	}

	
	
	public void testTimedGUI_abort(){
		try {
			when(mockCtxBroker.lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.UID)).thenReturn(new AsyncResult<List<CtxIdentifier>>(mockUIDIds));
			when(mockCtxBroker.retrieve(mockUIDId)).thenReturn(new AsyncResult<CtxModelObject>(mockUID));
			
			String proposalText = "Press: ABORT";
			impFeedback = userFeedback.getImplicitFB(ImpProposalType.TIMED_ABORT, new ImpProposalContent(proposalText, 10000)).get();
			while(impFeedback == null){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			verify(mockCtxBroker).lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.UID);
			verify(mockCtxBroker).retrieve(mockUIDId);
			
			//analyse results
			Assert.assertNotNull(impFeedback);
			Assert.assertEquals(false, impFeedback.booleanValue());
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		} catch (CtxException e) {
			e.printStackTrace();
		}
	}

	
	
	public void testTimedGUI_timeout(){
		
		try {
			when(mockCtxBroker.lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.UID)).thenReturn(new AsyncResult<List<CtxIdentifier>>(mockUIDIds));
			when(mockCtxBroker.retrieve(mockUIDId)).thenReturn(new AsyncResult<CtxModelObject>(mockUID));
			
			String proposalText = "DO NOT press any button";
			impFeedback = userFeedback.getImplicitFB(ImpProposalType.TIMED_ABORT, new ImpProposalContent(proposalText, 5000)).get();
			while(impFeedback == null){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			verify(mockCtxBroker).lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.UID);
			verify(mockCtxBroker).retrieve(mockUIDId);
			
			//analyse results
			Assert.assertNotNull(impFeedback);
			Assert.assertEquals(true, impFeedback.booleanValue());
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		} catch (CtxException e) {
			e.printStackTrace();
		}
	}
}
