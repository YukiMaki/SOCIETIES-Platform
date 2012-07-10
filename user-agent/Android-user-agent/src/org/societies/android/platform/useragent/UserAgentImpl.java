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

package org.societies.android.platform.useragent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.jivesoftware.smack.packet.IQ;
import org.societies.android.api.context.broker.ICtxClientBroker;
import org.societies.android.api.useragent.IAndroidUserAgent;
import org.societies.api.comm.xmpp.datatypes.Stanza;
import org.societies.api.comm.xmpp.datatypes.XMPPInfo;
import org.societies.api.comm.xmpp.exceptions.XMPPError;
import org.societies.api.comm.xmpp.interfaces.ICommCallback;
import org.societies.api.context.CtxException;
import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxModelType;
import org.societies.api.identity.IIdentity;
import org.societies.api.internal.context.model.CtxAttributeTypes;
import org.societies.api.internal.useragent.model.ExpProposalContent;
import org.societies.api.internal.useragent.model.ImpProposalContent;
import org.societies.api.personalisation.model.IAction;
import org.societies.api.schema.useragent.monitoring.MethodType;
import org.societies.api.schema.useragent.monitoring.UserActionMonitorBean;
import org.societies.comm.xmpp.client.impl.ClientCommunicationMgr;

import android.util.Log;

public class UserAgentImpl implements IAndroidUserAgent{

	private static final String LOG_TAG = UserAgentImpl.class.getName();
	private static final List<String> ELEMENT_NAMES = Arrays.asList(
			"userActionMonitorBean", 
			"userFeedbackBean", 
			"expFeedbackResultBean", 
			"impFeedbackResultBean");
	private static final List<String> NAME_SPACES = Arrays.asList(
			"http://societies.org/api/schema/useragent/monitoring");
	private static final List<String> PACKAGES = Arrays.asList(
			"org.societies.api.schema.useragent.monitoring");

	private ClientCommunicationMgr ccm;
	private IIdentity destination;
	private ICtxClientBroker acb;
	public boolean connectedToCtxService = false;


	public UserAgentImpl(ClientCommunicationMgr ccm, IIdentity destination){
		this.ccm = ccm;
		this.destination = destination;	
	}


	/**
	 * UAM methods
	 */
	public void monitor(IIdentity identity, IAction action){
		Log.d(LOG_TAG, "User Agent received monitored user action from identity " +identity.getJid()+ 
				": "+action.getparameterName()+" = "+action.getvalue());

		//SET UID
		/*Log.d(LOG_TAG, "Setting UID to this device ID using local ctxBrokerService");
		try {
			if(connectedToCtxService){
				List<CtxIdentifier> attrIds = ctxBrokerService.lookup(CtxModelType.ATTRIBUTE, CtxAttributeTypes.UID);
				if(attrIds.size() > 0){  //UID attribute found
					CtxAttribute uidAttr = (CtxAttribute)ctxBrokerService.retrieve(attrIds.get(0));
					//SET TO THIS DEVICE ID
				}
			}else{
				Log.d(LOG_TAG, "Cannot set UID - not connected to ctxBrokerService");
			}
		} catch (CtxException e1) {
			e1.printStackTrace();
		}*/

		//CREATE MESSAGE BEAN
		UserActionMonitorBean uamBean = new UserActionMonitorBean();
		Log.d(LOG_TAG, "Creating message to send to virgo user agent");
		uamBean.setIdentity(identity.getJid());
		uamBean.setServiceResourceIdentifier(action.getServiceID());
		uamBean.setServiceType(action.getServiceType());
		uamBean.setParameterName(action.getparameterName());
		uamBean.setValue(action.getvalue());
		uamBean.setMethod(MethodType.MONITOR);

		Stanza stanza = new Stanza(destination);

		ICommCallback callback = new UserAgentCallback();

		try {
			Log.d(LOG_TAG, "registering info with comms FW:");
			List<String> nameSpaces = callback.getXMLNamespaces();
			List<String> jPackages = callback.getJavaPackages();
			for(String nextNameSpace: nameSpaces){
				Log.d(LOG_TAG, nextNameSpace);
			}
			for(String nextPackage: jPackages){
				Log.d(LOG_TAG, nextPackage);
			}
			ccm.register(ELEMENT_NAMES, callback);
			ccm.sendIQ(stanza, IQ.Type.SET, uamBean, callback);
			Log.d(LOG_TAG, "Stanza sent!");
		} catch (Exception e) {
			Log.e(LOG_TAG, Log.getStackTraceString(e));
		} 
	}


	/**
	 * User Feedback methods
	 */
	public Future<List<String>> getExplicitFB(int type, ExpProposalContent content) {
		// TODO Auto-generated method stub
		return null;
	}


	public Future<Boolean> getImplicitFB(int type, ImpProposalContent content) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * non-interface methods
	 */
	public void connectCtxClientBroker(ICtxClientBroker acb){
		this.acb = acb;
		connectedToCtxService = true;
	}
	
	public void disconnectCtxClientBroker(){
		this.acb = null;
		connectedToCtxService = false;
	}

	/*
	 * Callback - not needed
	 */
	private class UserAgentCallback implements ICommCallback{

		public List<String> getJavaPackages() {
			return PACKAGES;
		}

		public List<String> getXMLNamespaces() {
			return NAME_SPACES;
		}

		public void receiveError(Stanza arg0, XMPPError arg1) {
			// TODO Auto-generated method stub

		}

		public void receiveInfo(Stanza arg0, String arg1, XMPPInfo arg2) {
			// TODO Auto-generated method stub

		}

		public void receiveItems(Stanza arg0, String arg1, List<String> arg2) {
			// TODO Auto-generated method stub

		}

		public void receiveMessage(Stanza arg0, Object arg1) {
			// TODO Auto-generated method stub

		}

		public void receiveResult(Stanza arg0, Object arg1) {
			// TODO Auto-generated method stub

		}

	}

}
