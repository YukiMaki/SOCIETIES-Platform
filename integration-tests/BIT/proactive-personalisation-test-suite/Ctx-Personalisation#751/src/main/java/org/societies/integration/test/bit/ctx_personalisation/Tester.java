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

package org.societies.integration.test.bit.ctx_personalisation;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import junit.framework.Assert;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.context.CtxException;
import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxAttributeTypes;
import org.societies.api.context.model.CtxEntity;
import org.societies.api.context.model.CtxEntityTypes;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxModelType;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.IIdentityManager;
import org.societies.api.identity.InvalidFormatException;
import org.societies.api.internal.context.broker.ICtxBroker;
import org.societies.api.useragent.monitoring.IUserActionMonitor;



/**
 * Utility class that creates mock actions
 *
 * @author Eliza
 *
 */
public class Tester {
	private IUserActionMonitor uam;
	private IIdentityManager idm;
	private IHelloWorld helloWorldService;
	private IIdentity userId;
	private ICtxBroker ctxBroker;
	private CtxEntity person;
	private CtxAttribute symLocAttribute;
	private CtxAttribute statusAttribute;
	private Logger logging = LoggerFactory.getLogger(this.getClass());

	public Tester(){

	}
	
	@Before
	public void setUp(){
		try{
		this.uam = Test751.getUam();
		this.idm = Test751.getCommsMgr().getIdManager();
		this.helloWorldService = Test751.getHelloWorld();
		this.ctxBroker = Test751.getCtxBroker();
		userId = idm.getThisNetworkNode();
		setupContext();
		
		logging.debug("751SETUPCOMPLETE");
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
	
	
	@org.junit.Test
	public void Test(){
	
		changeContext("home", "free");
		
		for (int i=0; i<5; i++){
			log("Step: "+i);
			
			this.helloWorldService.setBackgroundColour(userId, "red");
			
			changeContext("work", "busy");
			
			this.helloWorldService.setBackgroundColour(userId, "black");
			
			changeContext("home", "free");	
		}
					
			Assert.assertEquals("red", this.helloWorldService.getBackgroundColour(userId));
			
			changeContext("work", "busy");
			
			Assert.assertEquals("black", this.helloWorldService.getBackgroundColour(userId));
		
		
		
	}
	
	
	
	private void changeContext(String symLocValue, String statusValue){
		try {
			this.symLocAttribute.setStringValue(symLocValue);
			this.symLocAttribute = (CtxAttribute) this.ctxBroker.update(symLocAttribute).get();
			
			this.statusAttribute.setStringValue(statusValue);
			this.statusAttribute = (CtxAttribute) this.ctxBroker.update(statusAttribute).get();
			logging.debug("changeContext("+symLocValue+", "+statusValue+");");

		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * PreTest setup:
	 */
	
	
	
	
	private void setupContext() {
		this.getPersonEntity();
		this.getSymLocAttribute();
		this.getStatusAttribute();
	}

	private void getPersonEntity(){
		try {
			Future<List<CtxIdentifier>> futurePersons = this.ctxBroker.lookup(CtxModelType.ENTITY, CtxEntityTypes.PERSON);
			List<CtxIdentifier> persons = futurePersons.get();
			if (persons.size() == 0){
				person = this.ctxBroker.createEntity(CtxEntityTypes.PERSON).get();
				
			}else{
				person = (CtxEntity) this.ctxBroker.retrieve(persons.get(0)).get();
			}
			
			if (person==null){
				log("Person CtxEntity is null");
			}else{
				log("Got Person CtxEntity - NOT NULL");
			}
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getSymLocAttribute(){
		try {
			Future<List<CtxIdentifier>> futureAttrs = this.ctxBroker.lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.LOCATION_SYMBOLIC);
			List<CtxIdentifier> attrs = futureAttrs.get();
			if (attrs.size() == 0){
				symLocAttribute = this.ctxBroker.createAttribute(person.getId(), CtxAttributeTypes.LOCATION_SYMBOLIC).get();
			}else{
				symLocAttribute = (CtxAttribute) this.ctxBroker.retrieve(attrs.get(0)).get();
			}
			if (symLocAttribute==null){
				log(CtxAttributeTypes.LOCATION_SYMBOLIC+" CtxAttribute is null");
			}else{
				log(CtxAttributeTypes.LOCATION_SYMBOLIC+" CtxAttribute - NOT NULL");
			}
			
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
			this.log("EXCEPTION!");
		}
			
	}
	
	
	private void getStatusAttribute(){
		try {
			Future<List<CtxIdentifier>> futureAttrs = this.ctxBroker.lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.STATUS);
			List<CtxIdentifier> attrs = futureAttrs.get();
			if (attrs.size() == 0){
				statusAttribute = this.ctxBroker.createAttribute(person.getId(), CtxAttributeTypes.STATUS).get();
			}else{
				statusAttribute = (CtxAttribute) this.ctxBroker.retrieve(attrs.get(0)).get();
			}
			
			if (statusAttribute==null){
				log(CtxAttributeTypes.STATUS+" CtxAttribute is null");
			}else{
				log(CtxAttributeTypes.STATUS+" CtxAttribute - NOT NULL");
			}
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	private void log(String msg){
		logging.debug(this.getClass().getName()+": "+msg);
	}
	/*
	private List<IIdentity> identities = new ArrayList<IIdentity>();
	private List<IAction> actions = new ArrayList<IAction>();
	private List<ServiceResourceIdentifier> services = new ArrayList<ServiceResourceIdentifier>();
	private List<String> names;
	private List<String> values = new ArrayList<String>();
	private String parameterName = "volume";
	
	
	
	
	
	
	
	
	public ActionSender(IUserActionMonitor uam, IIdentityManager idm){
		this.uam = uam;
		this.idm = idm;
		this.createIIdentities();
		this.createServiceIDs();
		this.createValues();
		this.createActions();
		
	}
	
	private void createValues() {
		values.add("0");
		values.add("50");
		values.add("100");
		
	}

	public void createActions(){

		for (ServiceResourceIdentifier serviceID : services){
			for (String value : values){
				actions.add(new Action(serviceID, "aServiceType", parameterName, value));
			}
		}
	}
	
	public void createServiceIDs(){
		
		for (String name : names){
			ServiceResourceIdentifier serviceID = new ServiceResourceIdentifier();
			serviceID.setServiceInstanceIdentifier("css://"+name+"/service");
		}
		
	}
	
	public static void main(String[] args){
		try {
			URI uri = new URI("css://eliza@societies.org");
			logging.debug(uri.toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void createIIdentities(){
		
		names = new ArrayList<String>();
		names.add("Eliza@societies.org");
		names.add("Sarah@societies.org");
		names.add("Nicolas@societies.org");
		
		for (String name : names){
			try {
				identities.add(this.idm.fromJid(name));
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	*/
	
}
