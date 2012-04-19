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

package org.societies.context.broker.impl.comm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.societies.api.comm.xmpp.datatypes.Stanza;
import org.societies.api.comm.xmpp.datatypes.XMPPInfo;
import org.societies.api.comm.xmpp.exceptions.CommunicationException;
import org.societies.api.comm.xmpp.exceptions.XMPPError;
import org.societies.api.comm.xmpp.interfaces.ICommCallback;
import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.societies.api.context.model.CtxEntity;
import org.societies.api.context.model.CtxAssociation;
import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxModelObject;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxModelType;
import org.societies.api.context.model.CtxEntityIdentifier;
import org.societies.api.context.model.CtxAttributeIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.IIdentityManager;
import org.societies.api.identity.InvalidFormatException;
import org.societies.api.schema.context.contextmanagement.CtxBrokerBean;
import org.societies.api.schema.context.contextmanagement.CtxBrokerCreateEntityBean;
import org.societies.context.broker.api.ICtxCallback;



public class CtxBrokerClient implements ICommCallback {


	private final static List<String> NAMESPACES = Collections
			.singletonList("http://jabber.org/protocol/pubsub");
	private static final List<String> PACKAGES = Collections
			.unmodifiableList(Arrays.asList("jabber.x.data",
					"org.jabber.protocol.pubsub",
					"org.jabber.protocol.pubsub.errors",
					"org.jabber.protocol.pubsub.owner",
					"org.jabber.protocol.pubsub.event"));


	private ICommManager commManager;
	private static Logger LOG = LoggerFactory.getLogger(CtxBrokerClient.class);
	private IIdentityManager idMgr;


	public CtxBrokerClient(){

	}


	//PROPERTIES
	public ICommManager getCommManager() {
		return commManager;
	}

	public void setCommManager(ICommManager commManager) {
		this.commManager = commManager;
	}

	public void InitService() {
		//REGISTER OUR ServiceManager WITH THE XMPP Communication Manager
		try {
			getCommManager().register(this);
		} catch (CommunicationException e) {
			e.printStackTrace();
		}
		idMgr = commManager.getIdManager();
	}

	@Autowired
	CtxBrokerClient(ICommManager commManager){
		this.commManager = commManager;
		try {
			this.commManager.register(this);
		} catch (CommunicationException e) {

		}

	}

	public void createRemoteEntity(IIdentity identity, String type, ICtxCallback callback){

		final CtxEntity entity = null;

		// creating the identity of the CtxBroker that will be contacted
		IIdentity toIdentity = null;
		try {
			toIdentity = idMgr.fromJid("XCManager.societies.local");
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}

		//create the message to be sent
		Stanza stanza = new Stanza(toIdentity);
		CtxBrokerBean cbPacket = new CtxBrokerBean();
		// use the create entity method : createCtxEntity(String type)
		//org.societies.api.schema.context.contextschema.CtxBrokerCreateEntityBean ctxBrokerCreateEntityBean = new org.societies.api.schema.context.contextschema.CtxBrokerCreateEntityBean();
		CtxBrokerCreateEntityBean ctxBrokerCreateEntityBean = new CtxBrokerCreateEntityBean();
		// add the signatures of the method
		ctxBrokerCreateEntityBean.setType(type);
		ctxBrokerCreateEntityBean.setRequester("FOO");
		cbPacket.setCreate(ctxBrokerCreateEntityBean);

		CtxBrokerCommCallback commCallback = new CtxBrokerCommCallback(stanza.getId(), callback);
		//send the message
		try {
			this.commManager.sendIQGet(stanza, ctxBrokerCreateEntityBean, this);
			//this.commManager.sendMessage(stanza, ctxBrokerCreateEntityBean);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

/*


	public Future<CtxAttribute> createRemoteAttribute(IIdentity identity, CtxEntityIdentifier scope, String type){

		final CtxAttribute attribute = null;

		// creating the identity of the CtxBroker that will be contacted
		IIdentity toIdentity = null;
		try {
			toIdentity = idMgr.fromJid("XCManager.societies.local");
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}

		//create the message to be sent
		Stanza stanza = new Stanza(toIdentity);
		org.societies.api.schema.context.contextschema.CtxBroker cbPacket = new org.societies.api.schema.context.contextschema.CtxBroker();
		// use the method : createAttribute(CtxEntityIdentifier scope, String type)
		org.societies.api.schema.context.contextschema.CtxBrokerCreateAttributeBean ctxBrokerCreateAttributeBean = new org.societies.api.schema.context.contextschema.CtxBrokerCreateAttributeBean();
		// add the signatures of the method
		ctxBrokerCreateAttributeBean.setOperatorIdjid(identity.getJid());
		ctxBrokerCreateAttributeBean.setType(type);
		cbPacket.setCreateAttr(ctxBrokerCreateAttributeBean);


		//send the message
		try {
			this.commManager.sendIQGet(stanza, ctxBrokerCreateAttributeBean, this);
			//this.commManager.sendMessage(stanza, ctxBrokerCreateAttributeBean);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new AsyncResult<CtxAttribute>(attribute);
	}

	public Future<CtxAssociation> createRemoteAssociation(IIdentity identity, String type){

		final CtxAssociation association = null;

		// creating the identity of the CtxBroker that will be contacted
		IIdentity toIdentity = null;
		try {
			toIdentity = idMgr.fromJid("XCManager.societies.local");
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}

		//create the message to be sent
		Stanza stanza = new Stanza(toIdentity);
		org.societies.api.schema.context.contextschema.CtxBroker cbPacket = new org.societies.api.schema.context.contextschema.CtxBroker();
		// use the method : createAssociation(String type)
		org.societies.api.schema.context.contextschema.CtxBrokerCreateAssociationBean ctxBrokerCreateAssociationBean = new org.societies.api.schema.context.contextschema.CtxBrokerCreateAssociationBean();
		// add the signatures of the method
		ctxBrokerCreateAssociationBean.setOperatorIdjid(identity.getJid());
		ctxBrokerCreateAssociationBean.setType(type);
		cbPacket.setCreateAssoc(ctxBrokerCreateAssociationBean);


		//send the message
		try {
			this.commManager.sendIQGet(stanza, ctxBrokerCreateAssociationBean, this);
			//this.commManager.getIdManager().getThisNetworkNode()sendMessage(stanza, ctxBrokerCreateAssociationBean);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new AsyncResult<CtxAssociation>(association);
	}

	public Future<CtxModelObject> removeRemote(IIdentity requester, CtxIdentifier identifier){
		//remove(Identity requester, CtxIdentifier identifier)
		final CtxModelObject model = null;

		// creating the identity of the CtxBroker that will be contacted
		IIdentity toIdentity = null;
		try {
			toIdentity = idMgr.fromJid("XCManager.societies.local");
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}

		//create the message to be sent
		Stanza stanza = new Stanza(toIdentity);
		org.societies.api.schema.context.contextschema.CtxBroker cbPacket = new org.societies.api.schema.context.contextschema.CtxBroker();
		// use the method : remove(CtxIdentifier identifier)
		org.societies.api.schema.context.contextschema.CtxBrokerRemoveBean ctxBrokerRemoveBean = new org.societies.api.schema.context.contextschema.CtxBrokerRemoveBean();
		// add the signatures of the method
		ctxBrokerRemoveBean.setOperatorIdjid(requester.getJid());
		ctxBrokerRemoveBean.setType(identifier.getType());
		cbPacket.setRemove(ctxBrokerRemoveBean);


		//send the message
		try {
			//this.commManager.sendIQGet(stanza, ctxBrokerRemoveBean, this);
			this.commManager.sendMessage(stanza, ctxBrokerRemoveBean);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new AsyncResult<CtxModelObject>(model);
	}

	public Future<CtxModelObject> updateRemote(IIdentity identity, CtxModelObject object){
		//update(Identity requester, CtxModelObject object)

		final CtxModelObject model = null;

		// creating the identity of the CtxBroker that will be contacted
		IIdentity toIdentity = null;
		try {
			toIdentity = idMgr.fromJid("XCManager.societies.local");
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}

		//create the message to be sent
		Stanza stanza = new Stanza(toIdentity);
		org.societies.api.schema.context.contextschema.CtxBroker cbPacket = new org.societies.api.schema.context.contextschema.CtxBroker();
		// use the method : update
		org.societies.api.schema.context.contextschema.CtxBrokerUpdateBean ctxBrokerUpdateBean = new org.societies.api.schema.context.contextschema.CtxBrokerUpdateBean();
		// add the signatures of the method
		ctxBrokerUpdateBean.setOperatorIdjid(identity.getJid());
		ctxBrokerUpdateBean.setModelTypeName(object.getModelType().name());
		ctxBrokerUpdateBean.setType(object.getType());
		cbPacket.setUpdate(ctxBrokerUpdateBean);


		//send the message
		try {
			//this.commManager.sendIQGet(stanza, ctxBrokerUpdateBean, this);
			this.commManager.sendMessage(stanza, ctxBrokerUpdateBean);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new AsyncResult<CtxModelObject>(model);
	}

	public Future<CtxModelObject> updateRemoteAttribute(IIdentity identity, CtxAttributeIdentifier attributeId, Serializable value){
		//updateAttribute(CtxAttributeIdentifier attributeId, Serializable value)

		final CtxModelObject model = null;

		// creating the identity of the CtxBroker that will be contacted
		IIdentity toIdentity = null;
		try {
			toIdentity = idMgr.fromJid("XCManager.societies.local");
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}

		//create the message to be sent
		Stanza stanza = new Stanza(toIdentity);
		org.societies.api.schema.context.contextschema.CtxBroker cbPacket = new org.societies.api.schema.context.contextschema.CtxBroker();
		// use the method : remove(CtxIdentifier identifier)
		org.societies.api.schema.context.contextschema.CtxBrokerUpdateAttributeBean ctxBrokerUpdateAttributeBean = new org.societies.api.schema.context.contextschema.CtxBrokerUpdateAttributeBean();
		// add the signatures of the method
		ctxBrokerUpdateAttributeBean.setOperatorIdjid(identity.getJid());
		ctxBrokerUpdateAttributeBean.setType(attributeId.getType());
		ctxBrokerUpdateAttributeBean.setValue((byte[]) value);
		cbPacket.setUpdateAttr(ctxBrokerUpdateAttributeBean);


		//send the message
		try {
			//this.commManager.sendIQGet(stanza, ctxBrokerUpdateAttributeBean, this);
			this.commManager.sendMessage(stanza, ctxBrokerUpdateAttributeBean);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new AsyncResult<CtxModelObject>(model);
	}



	public Future<CtxModelObject> retrieveRemote(IIdentity identity, CtxIdentifier identifier){
		//retrieve(CtxIdentifier identifier)

		final CtxModelObject model = null;

		// creating the identity of the CtxBroker that will be contacted
		IIdentity toIdentity = null;
		try {
			toIdentity = idMgr.fromJid("XCManager.societies.local");
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}

		//create the message to be sent
		Stanza stanza = new Stanza(toIdentity);
		org.societies.api.schema.context.contextschema.CtxBroker cbPacket = new org.societies.api.schema.context.contextschema.CtxBroker();
		// use the method : retrieve
		org.societies.api.schema.context.contextschema.CtxBrokerRetrieveBean ctxBrokerRetrieveBean = new org.societies.api.schema.context.contextschema.CtxBrokerRetrieveBean();
		// add the signatures of the method
		ctxBrokerRetrieveBean.setOperatorIdjid(identity.getJid());
		ctxBrokerRetrieveBean.setType(identifier.getType());
		cbPacket.setRetrieve(ctxBrokerRetrieveBean);


		//send the message
		try {
			//this.commManager.sendIQGet(stanza, ctxBrokerRetrieveBean, this);
			this.commManager.sendMessage(stanza, ctxBrokerRetrieveBean);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new AsyncResult<CtxModelObject>(model);
	}

	public Future<List<CtxIdentifier>> lookupRemote(IIdentity identity, CtxModelType modelType, String type){
		//Future<List<CtxIdentifier>> lookup(CtxModelType modelType, String type)

		final List<CtxIdentifier> listOfIdentifiers= new ArrayList<CtxIdentifier>();

		// creating the identity of the CtxBroker that will be contacted
		IIdentity toIdentity = null;
		try {
			toIdentity = idMgr.fromJid("XCManager.societies.local");
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}

		//create the message to be sent
		Stanza stanza = new Stanza(toIdentity);
		org.societies.api.schema.context.contextschema.CtxBroker cbPacket = new org.societies.api.schema.context.contextschema.CtxBroker();
		// use the method : lookup
		org.societies.api.schema.context.contextschema.CtxBrokerLookupBean ctxBrokerLookupBean = new org.societies.api.schema.context.contextschema.CtxBrokerLookupBean();
		// add the signatures of the method
		ctxBrokerLookupBean.setOperatorIdjid(identity.getJid());
		ctxBrokerLookupBean.setType(type);
		ctxBrokerLookupBean.setModelTypeName(modelType.name());
		cbPacket.setLookup(ctxBrokerLookupBean);


		//send the message
		try {
			//this.commManager.sendIQGet(stanza, ctxBrokerLookupBean, this);
			this.commManager.sendMessage(stanza, ctxBrokerLookupBean);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new AsyncResult<List<CtxIdentifier>>(listOfIdentifiers);
	}

*/
	@Override
	public List<String> getJavaPackages() {
		return PACKAGES;
	}

	@Override
	public List<String> getXMLNamespaces() {
		return NAMESPACES;
	}

	@Override
	public void receiveError(Stanza arg0, XMPPError arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveInfo(Stanza arg0, String arg1, XMPPInfo arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveItems(Stanza arg0, String arg1, List<String> arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveMessage(Stanza arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveResult(Stanza arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}