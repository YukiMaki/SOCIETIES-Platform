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

package org.societies.context.comms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.comm.xmpp.datatypes.Stanza;
import org.societies.api.comm.xmpp.datatypes.XMPPInfo;
import org.societies.api.comm.xmpp.exceptions.CommunicationException;
import org.societies.api.comm.xmpp.exceptions.XMPPError;
import org.societies.api.comm.xmpp.interfaces.ICommCallback;
import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.IIdentityManager;
import org.societies.api.identity.InvalidFormatException;
import org.societies.api.internal.useragent.remote.IUserAgentRemoteMgr;
//import org.societies.api.internal.context.remote.IContextRemoteMgr;
import org.societies.api.schema.servicelifecycle.model.ServiceResourceIdentifier;
import org.societies.api.schema.useragent.monitoring.MethodType;
import org.societies.api.schema.useragent.monitoring.UserActionMonitorBean;
//import org.societies.api.schema.context.contextmanagement.CtxBrokerBean;

public class CMCommsClient implements IUserAgentRemoteMgr, ICommCallback{	
	private static final List<String> NAMESPACES = Collections.unmodifiableList(
			Arrays.asList("http://societies.org/api/schema/context/contextmanagement"));
	private static final List<String> PACKAGES = Collections.unmodifiableList(
			Arrays.asList("org.societies.api.schema.context.contextmanagement"));

	//PRIVATE VARIABLES
	private ICommManager commsMgr;
	private IIdentityManager idManager;
	private Logger LOG = LoggerFactory.getLogger(CMCommsClient.class);

	//PROPERTIES
	public ICommManager getCommsMgr() {
		return commsMgr;
	}

	public void setCommsMgr(ICommManager commsMgr) {
		this.commsMgr = commsMgr;
	}

	public CMCommsClient() {	
	}

	public void initService() {
		//REGISTER OUR ServiceManager WITH THE XMPP Communication Manager
		LOG.info("Registering CMCommsClient with XMPP Communication Manager");
		try {
			getCommsMgr().register(this); 
		} catch (CommunicationException e) {
			e.printStackTrace();
		}
		idManager = commsMgr.getIdManager();
	}

	@Override
	public void monitor(IIdentity owner, ServiceResourceIdentifier serviceId, String serviceType,
			String parameterName, String value) {
		LOG.info("monitor method called in CMCommsClient");
		IIdentity toIdentity = null;
		try {
			toIdentity = idManager.fromJid("XCManager.societies.local");
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}
		Stanza stanza = new Stanza(toIdentity);

		//CREATE MESSAGE BEAN
		LOG.info("Creating message to send to CMCommsServer");
/*		CtxBrokerBean cbBean = new CtxBrokerBean();
		cbBean.setCreate(null);
		cbBean.setCreateAssoc(null);
		cbBean.setCreateAttr(null);
		cbBean.setLookup(null);
		cbBean.setRemove(null);
		cbBean.setRetrieve(null);
		cbBean.setUpdate(null);
		cbBean.setUpdateAttr(null);
*/
		UserActionMonitorBean uaBean = new UserActionMonitorBean();
		uaBean.setIdentity(owner.getJid());
		uaBean.setServiceResourceIdentifier(serviceId);
		uaBean.setServiceType(serviceType);
		uaBean.setParameterName(parameterName);
		uaBean.setValue(value);
		uaBean.setMethod(MethodType.MONITOR);
		try {
			LOG.info("Sending message to CMCommsServer");
			//SEND INFORMATION QUERY - RESPONSE WILL BE IN "callback.RecieveMessage()"
			commsMgr.sendMessage(stanza, uaBean);
		} catch (CommunicationException e) {
			e.printStackTrace();
		};
	}

	/*@Override
	public void registerForActionUpdates(IUserActionListener listener) {
		IIdentity toIdentity = null;
		try {
			toIdentity = idManager.fromJid("XCManager.societies.local");
		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		}
		Stanza stanza = new Stanza(toIdentity);

		//CREATE MESSAGE BEAN
		UserActionMonitorBean uaBean = new UserActionMonitorBean();
		//uaBean.setListener(listener);
		//uaBean.setMethod(MethodType.REGISTER);
		try {
			//SEND INFORMATION QUERY - RESPONSE WILL BE IN "callback.RecieveMessage()"
			commManager.sendMessage(stanza, uaBean);
		} catch (CommunicationException e) {
			e.printStackTrace();
		};
	}*/

	
	
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
