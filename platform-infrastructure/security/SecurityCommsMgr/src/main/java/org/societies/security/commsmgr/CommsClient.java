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
package org.societies.security.commsmgr;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.IIdentityManager;
import org.societies.api.identity.InvalidFormatException;
import org.societies.api.internal.security.policynegotiator.INegotiationProviderCallback;
import org.societies.api.internal.security.policynegotiator.INegotiationProviderRemote;
import org.societies.api.schema.security.policynegotiator.MethodType;
import org.societies.api.schema.security.policynegotiator.ProviderBean;
import org.societies.api.comm.xmpp.datatypes.Stanza;
import org.societies.api.comm.xmpp.datatypes.XMPPInfo;
import org.societies.api.comm.xmpp.exceptions.CommunicationException;
import org.societies.api.comm.xmpp.exceptions.XMPPError;
import org.societies.api.comm.xmpp.interfaces.ICommCallback;
import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Comms Client that initiates the remote communication
 * 
 * @author Mitja Vardjan
 * 
 */
@Component
public class CommsClient implements INegotiationProviderRemote, ICommCallback {
	private static final List<String> NAMESPACES = Collections
			.unmodifiableList(Arrays.asList(
					"http://societies.org/api/schema/security/policynegotiator"));
	private static final List<String> PACKAGES = Collections
			.unmodifiableList(Arrays.asList(
					"org.societies.api.schema.security.policynegotiator"));

	// PRIVATE VARIABLES
	private ICommManager commManager;
	private static Logger LOG = LoggerFactory.getLogger(CommsClient.class);
	private IIdentityManager idMgr;

	@Autowired
	public CommsClient(ICommManager commManager) {
		
		this.commManager = commManager;
		
		LOG.debug("CommsServer({})", commManager);
	}

	@PostConstruct
	public void init() {
		// REGISTER OUR ServiceManager WITH THE XMPP Communication Manager
		try {
			commManager.register(this);
			LOG.debug("init(): commManager registered");
		} catch (CommunicationException e) {
			LOG.error("init(): ", e);
		}
		idMgr = commManager.getIdManager();
	}

//	@Override
//	@Async
//	public void Add(int valA, int valB, IExamplesCallback calcCallback) {
//		IIdentity toIdentity = null;
//		try {
//			toIdentity = idMgr.fromJid("XCManager.societies.local");
//		} catch (InvalidFormatException e1) {
//			e1.printStackTrace();
//		}
//		Stanza stanza = new Stanza(toIdentity);
//
//		// SETUP CALC CLIENT RETURN STUFF
//		CommsClientCallback callback = new CommsClientCallback(stanza.getId(),
//				calcCallback);
//
//		// CREATE MESSAGE BEAN
//		CalcBean calc = new CalcBean();
//		calc.setA(valA);
//		calc.setB(valB);
//		calc.setMethod(MethodType.ADD);
//		try {
//			// SEND INFORMATION QUERY - RESPONSE WILL BE IN
//			// "callback.RecieveMessage()"
//			commManager.sendIQGet(stanza, calc, callback);
//		} catch (CommunicationException e) {
//			LOG.warn(e.getMessage());
//		}
//		;
//	}

//	@Override
//	public void Subtract(int valA, int valB, IExamplesCallback calcCallback) {
//		IIdentity toIdentity = null;
//		try {
//			toIdentity = idMgr.fromJid("XCManager.societies.local");
//		} catch (InvalidFormatException e1) {
//			e1.printStackTrace();
//		}
//		Stanza stanza = new Stanza(toIdentity);
//
//		// SETUP CALC CLIENT RETURN STUFF
//		CommsClientCallback callback = new CommsClientCallback(stanza.getId(),
//				calcCallback);
//
//		// CREATE MESSAGE BEAN
//		CalcBean calc = new CalcBean();
//		calc.setA(valA);
//		calc.setB(valB);
//		calc.setMethod(MethodType.SUBTRACT);
//		try {
//			// SEND INFORMATION QUERY - RESPONSE WILL BE IN
//			// "callback.RecieveMessage()"
//			commManager.sendIQGet(stanza, calc, callback);
//		} catch (CommunicationException e) {
//			LOG.warn(e.getMessage());
//		}
//		;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.societies.api.comm.xmpp.interfaces.ICommCallback#getJavaPackages()
	 */
	@Override
	public List<String> getJavaPackages() {
		return PACKAGES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.societies.api.comm.xmpp.interfaces.ICommCallback#getXMLNamespaces()
	 */
	@Override
	public List<String> getXMLNamespaces() {
		return NAMESPACES;
	}

	@Override
	public void receiveError(Stanza arg0, XMPPError arg1) {
	}

	@Override
	public void receiveInfo(Stanza arg0, String arg1, XMPPInfo arg2) {
	}

	@Override
	public void receiveMessage(Stanza arg0, Object arg1) {
	}

	@Override
	public void receiveResult(Stanza arg0, Object arg1) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.api.comm.xmpp.interfaces.ICommCallback#receiveItems(
	 * org.societies.api.comm.xmpp.datatypes.Stanza, java.lang.String,
	 * java.util.List)
	 */
	@Override
	public void receiveItems(Stanza arg0, String arg1, List<String> arg2) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.api.internal.security.policynegotiator.
	 * INegotiationProviderRemote# acceptPolicyAndGetSla(int, java.lang.String,
	 * boolean, org.societies.api.internal.security.policynegotiator.
	 * INegotiationProviderCallback)
	 */
	@Override
	@Async
	public void acceptPolicyAndGetSla(int sessionId, String signedPolicyOption,
			boolean modified, INegotiationProviderCallback callback) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.api.internal.security.policynegotiator.
	 * INegotiationProviderRemote#
	 * getPolicyOptions(org.societies.api.internal.security
	 * .policynegotiator.INegotiationProviderCallback)
	 */
	@Override
	@Async
	public void getPolicyOptions(INegotiationProviderCallback callback) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.societies.api.internal.security.policynegotiator.
	 * INegotiationProviderRemote#reject(int)
	 */
	@Override
	@Async
	public void reject(int sessionId) {
		
		IIdentity toIdentity = null;
		try {
			toIdentity = idMgr.fromJid("XCManager.societies.local");
		} catch (InvalidFormatException e) {
			LOG.error("reject({}): ", sessionId, e);
		}
		Stanza stanza = new Stanza(toIdentity);

		// CREATE MESSAGE BEAN
		ProviderBean provider = new ProviderBean();
		provider.setSessionId(sessionId);
		provider.setMethod(MethodType.REJECT);
		try {
			// SEND INFORMATION QUERY - RESPONSE WILL BE IN
			// "callback.RecieveMessage()"
			commManager.sendMessage(stanza, provider);
		} catch (CommunicationException e) {
			LOG.warn(e.getMessage());
		}
		;
	}
}