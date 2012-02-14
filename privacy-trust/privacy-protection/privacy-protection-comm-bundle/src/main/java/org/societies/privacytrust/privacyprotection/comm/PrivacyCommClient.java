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
package org.societies.privacytrust.privacyprotection.comm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.comm.xmpp.datatypes.IdentityImpl;
import org.societies.privacytrust.privacyprotection.comm.api.IPrivacyPolicyManagerRemote;
import org.societies.privacytrust.privacyprotection.comm.api.TestCallback;
import org.societies.privacytrust.privacyprotection.schema.IPrivacyPolicyManagerBean;
import org.societies.privacytrust.privacyprotection.schema.IPrivacyPolicyManagerBean.methodType;
import org.societies.api.comm.xmpp.datatypes.Identity;
import org.societies.api.comm.xmpp.datatypes.IdentityType;
import org.societies.api.comm.xmpp.datatypes.Stanza;
import org.societies.api.comm.xmpp.datatypes.XMPPInfo;
import org.societies.api.comm.xmpp.datatypes.XMPPNode;
import org.societies.api.comm.xmpp.exceptions.CommunicationException;
import org.societies.api.comm.xmpp.exceptions.XMPPError;
import org.societies.api.comm.xmpp.interfaces.ICommCallback;
import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.societies.api.internal.privacytrust.privacyprotection.IPrivacyPolicyManager;
import org.springframework.scheduling.annotation.Async;

/**
 * Comms Client that initiates the remote communication
 *
 * @author aleckey
 *
 */
public class PrivacyCommClient implements IPrivacyPolicyManagerRemote, ICommCallback {
	private static final List<String> NAMESPACES = Collections.unmodifiableList(
			  Arrays.asList("http://societies.org/example/privacyprotectionservice/schema"));
	private static final List<String> PACKAGES = Collections.unmodifiableList(
			Arrays.asList("org.societies.privacytrust.privacyprotection.schema"));

	//PRIVATE VARIABLES
	private ICommManager commManager;
	private static Logger LOG = LoggerFactory.getLogger(PrivacyCommClient.class);
	
	//PROPERTIES
	public ICommManager getCommManager() {
		return commManager;
	}

	public void setCommManager(ICommManager commManager) {
		this.commManager = commManager;
	}

	public PrivacyCommClient() {}

	public void InitService() {
		//REGISTER OUR ServiceManager WITH THE XMPP Communication Manager
		try {
			getCommManager().register(this); 
		} catch (CommunicationException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.societies.comm.examples.commsmanager.ICalcRemote#AddAsync(int, int)
	 */
	@Override
	@Async
	public void deletePrivacyPolicy(String id, TestCallback listener) {
		Identity toIdentity = new Identity(IdentityType.CSS, "XCManager", "societies.local") {
			@Override
			public String getJid() {
				return getIdentifier() + "." + getDomainIdentifier();
			}
		};
		Stanza stanza = new Stanza(toIdentity);

		//SETUP CALC CLIENT RETURN STUFF
		PrivacyCommClientCallback callback = new PrivacyCommClientCallback(stanza.getId(), listener);

		//CREATE MESSAGE BEAN
		IPrivacyPolicyManagerBean bean = new IPrivacyPolicyManagerBean();
		bean.setId(id); 
		bean.setMethod(methodType.deletePrivacyPolicy);
		try {
			//SEND INFORMATION QUERY - RESPONSE WILL BE IN "callback.RecieveMessage()"
			commManager.sendIQGet(stanza, bean, callback);
		} catch (CommunicationException e) {
			LOG.warn(e.getMessage());
		};
	}
	

	/* (non-Javadoc)
	 * @see org.societies.api.comm.xmpp.interfaces.ICommCallback#getJavaPackages() */
	@Override
	public List<String> getJavaPackages() {
		return PACKAGES;
	}

	/* (non-Javadoc)
	 * @see org.societies.api.comm.xmpp.interfaces.ICommCallback#getXMLNamespaces() */
	@Override
	public List<String> getXMLNamespaces() {
		return NAMESPACES;
	}

	@Override
	public void receiveError(Stanza arg0, XMPPError arg1) { }

	@Override
	public void receiveInfo(Stanza arg0, String arg1, XMPPInfo arg2) { }

	@Override
	public void receiveMessage(Stanza arg0, Object arg1) { }

	@Override
	public void receiveResult(Stanza arg0, Object arg1) { }

	/* (non-Javadoc)
	 * @see org.societies.api.comm.xmpp.interfaces.ICommCallback#receiveItems(org.societies.api.comm.xmpp.datatypes.Stanza, java.lang.String, java.util.List)
	 */
	@Override
	public void receiveItems(Stanza arg0, String arg1, List<String> arg2) {
		// TODO Auto-generated method stub
	}
}
