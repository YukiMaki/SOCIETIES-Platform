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

/**
 * Describe your class here...
 *
 * @author aleckey
 *
 */
package org.societies.api.internal.privacytrust.privacyprotection.remote;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.comm.xmpp.datatypes.Stanza;
import org.societies.api.comm.xmpp.exceptions.CommunicationException;
import org.societies.api.comm.xmpp.exceptions.XMPPError;
import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.societies.api.comm.xmpp.interfaces.IFeatureServer;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxIdentifierFactory;
import org.societies.api.context.model.MalformedCtxIdentifierException;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.IIdentityManager;
import org.societies.api.identity.InvalidFormatException;
import org.societies.api.identity.Requestor;
import org.societies.api.identity.RequestorCis;
import org.societies.api.identity.RequestorService;
import org.societies.api.internal.privacytrust.privacyprotection.INegotiationAgent;
import org.societies.api.internal.privacytrust.privacyprotection.IPrivacyDataManager;
import org.societies.api.internal.privacytrust.privacyprotection.model.PrivacyException;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.Action;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.AgreementEnvelope;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.IAgreementEnvelope;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.RequestPolicy;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.ResponseItem;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.ResponsePolicy;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.constants.ActionConstants;
import org.societies.api.internal.privacytrust.privacyprotection.model.util.ActionUtils;
import org.societies.api.internal.privacytrust.privacyprotection.model.util.ResponseItemUtils;
import org.societies.api.internal.schema.privacytrust.privacyprotection.negotiation.NegotiationAgentBean;
import org.societies.api.internal.schema.privacytrust.privacyprotection.privacydatamanagement.PrivacyDataManagerBean;
import org.societies.api.internal.schema.privacytrust.privacyprotection.privacydatamanagement.PrivacyDataManagerBeanResult;
import org.societies.api.schema.identity.RequestorBean;
import org.societies.api.schema.identity.RequestorCisBean;
import org.societies.api.schema.identity.RequestorServiceBean;


public class CommsServer implements IFeatureServer {

	private static final List<String> NAMESPACES = Collections.unmodifiableList(
			Arrays.asList("http://societies.org/api/internal/schema/privacytrust/privacyprotection/negotiation", 
					"http://societies.org/api/internal/schema/privacytrust/privacyprotection/privacydatamanagement", 
					"http://societies.org/api/schema/servicelifecycle/model"));
	private static final List<String> PACKAGES = Collections.unmodifiableList(
			Arrays.asList("org.societies.api.internal.schema.privacytrust.privacyprotection.negotiation",
					"org.societies.api.internal.schema.privacytrust.privacyprotection.privacydatamanagement",
					"org.societies.api.schema.servicelifecycle.model"));


	//PRIVATE VARIABLES
	private ICommManager commManager;

	private INegotiationAgent negAgent;

	private IPrivacyDataManager privacyDataManager;

	private static Logger LOG = LoggerFactory.getLogger(CommsServer.class);

	//PROPERTIES
	public ICommManager getCommManager() {
		return commManager;
	}
	public void setCommManager(ICommManager commManager) {
		this.commManager = commManager;
	}

	public INegotiationAgent getNegAgent() {
		return negAgent;
	}
	public void setNegAgent(INegotiationAgent negAgent) {
		this.negAgent = negAgent;
	}

	public IPrivacyDataManager getPrivacyDataManager() {
		return privacyDataManager;
	}
	public void setPrivacyDataManager(IPrivacyDataManager privacyDataManager) {
		this.privacyDataManager = privacyDataManager;
	}


	//METHODS
	public CommsServer() {
	}

	public void InitService() {
		//REGISTER OUR ServiceManager WITH THE XMPP Communication Manager
		try {
			getCommManager().register(this); 
		} catch (CommunicationException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.societies.comm.xmpp.interfaces.FeatureServer#getJavaPackages()
	 */
	@Override
	public List<String> getJavaPackages() {
		return PACKAGES;
	}

	/* (non-Javadoc)
	 * @see org.societies.comm.xmpp.interfaces.FeatureServer#getXMLNamespaces()
	 */
	@Override
	public List<String> getXMLNamespaces() {
		return NAMESPACES;
	}

	/* Put your functionality here if there is NO return object, ie, VOID 
	 */
	@Override
	public void receiveMessage(Stanza stanza, Object payload) {
		//CHECK WHICH END BUNDLE TO BE CALLED THAT I MANAGE

	}

	/* Put your functionality here if there IS a return object
	 */
	@Override
	public Object getQuery(Stanza stanza, Object payload) throws XMPPError {
		//CHECK WHICH END BUNDLE TO BE CALLED THAT I MANAGE
		System.out.println("Generic query handler, doing nothing");

		if (payload instanceof NegotiationAgentBean){
			return this.getQuery(stanza, (NegotiationAgentBean) payload);
		}
		else if (payload instanceof PrivacyDataManagerBean){
			return this.getQuery(stanza, (PrivacyDataManagerBean) payload);
		}

		return null;
	}

	public Object getQuery(Stanza stanza, NegotiationAgentBean bean){
		if (bean.getMethod().equals("acknowledgeAgreement")){
			byte[] agreementEnvelopeArray = bean.getAgreementEnvelope();
			Object obj = Util.convertToObject(agreementEnvelopeArray, this.getClass());
			if (obj!=null){
				if (obj instanceof AgreementEnvelope){
					Boolean b;
					try {
						b = this.negAgent.acknowledgeAgreement((IAgreementEnvelope) obj).get();
						return b;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return false;
				}
			}
		}else if (bean.getMethod().equals("getPolicy")){
			try{

				RequestPolicy policy =  this.negAgent.getPolicy(this.getRequestorFromBean(bean.getRequestor())).get();
				if (policy!=null){
					return Util.toByteArray(policy);
				}
			} catch (InterruptedException e){

			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}else if (bean.getMethod().equals("getProviderIdentity")){
			try {
				return this.getNegAgent().getProviderIdentity().get().getJid();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (bean.getMethod().equals("negotiate")){
			try{
				byte[] responseArray = bean.getResponsePolicy();
				Object obj = Util.convertToObject(responseArray,this.getClass());
				if (obj!=null){
					if (obj instanceof ResponsePolicy){
						ResponsePolicy policy = (this.negAgent.negotiate(this.getRequestorFromBean(bean.getRequestor()), (ResponsePolicy) obj)).get();
						if (policy!=null){
							return Util.toByteArray(policy);
						}
					}
				}
			} catch (InterruptedException e){

			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public Object getQuery(Stanza stanza, PrivacyDataManagerBean bean){
		PrivacyDataManagerBeanResult beanResult = new PrivacyDataManagerBeanResult();
		boolean ack = true;

		// -- Check Permission
		if (bean.getMethod().equals("checkPermission")) {
			try {
				Requestor requestor = getRequestorFromBean(bean.getRequestor());
				IIdentity ownerId = commManager.getIdManager().fromJid(bean.getOwnerId());
				Action action = ActionUtils.toAction(bean.getAction());
				CtxIdentifier dataId = CtxIdentifierFactory.getInstance().fromString(bean.getDataId());
				ResponseItem permission = privacyDataManager.checkPermission(requestor, ownerId, dataId, action);
				beanResult.setPermission(ResponseItemUtils.toResponseItemBean(permission));
			} catch (MalformedCtxIdentifierException e) {
				ack = false;
				beanResult.setAckMessage("Error MalformedCtxIdentifierException: "+e.getMessage());
			}
			catch (PrivacyException e) {
				ack = false;
				beanResult.setAckMessage("Error PrivacyException: "+e.getMessage());
			} catch (InvalidFormatException e) {
				ack = false;
				beanResult.setAckMessage("Error InvalidFormatException: "+e.getMessage());
			}
		}
		// -- Obfuscate Data
		else if (bean.getMethod().equals("obfuscateData")) {
			ack = false;
		}
		beanResult.setAck(ack);
		return beanResult;
	}


	/* (non-Javadoc)
	 * @see org.societies.comm.xmpp.interfaces.FeatureServer#setQuery(org.societies.comm.xmpp.datatypes.Stanza, java.lang.Object)
	 */
	@Override
	public Object setQuery(Stanza arg0, Object arg1) throws XMPPError {
		// TODO Auto-generated method stub
		return null;
	}

	private Requestor getRequestorFromBean(RequestorBean bean){
		IIdentityManager idm = this.commManager.getIdManager();
		try {
			if (bean instanceof RequestorCisBean){
				RequestorCis requestor = new RequestorCis(idm.fromJid(bean.getRequestorId()), idm.fromJid(((RequestorCisBean) bean).getCisRequestorId()));
				return requestor;

			}else if (bean instanceof RequestorServiceBean){
				RequestorService requestor = new RequestorService(idm.fromJid(bean.getRequestorId()), ((RequestorServiceBean) bean).getRequestorServiceId());
				return requestor;
			}else{
				return new Requestor(idm.fromJid(bean.getRequestorId()));
			}
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


}
