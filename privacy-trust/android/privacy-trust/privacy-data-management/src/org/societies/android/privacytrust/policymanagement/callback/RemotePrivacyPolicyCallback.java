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
package org.societies.android.privacytrust.policymanagement.callback;

import java.util.List;

import org.societies.api.comm.xmpp.datatypes.Stanza;
import org.societies.api.comm.xmpp.datatypes.XMPPInfo;
import org.societies.api.comm.xmpp.exceptions.XMPPError;
import org.societies.api.comm.xmpp.interfaces.ICommCallback;
import org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.RequestPolicy;
import org.societies.api.internal.schema.privacytrust.privacyprotection.privacypolicymanagement.MethodType;
import org.societies.api.internal.schema.privacytrust.privacyprotection.privacypolicymanagement.PrivacyPolicyManagerBeanResult;

import android.util.Log;

/**
 * @author Olivier Maridat (Trialog)
 *
 */
public class RemotePrivacyPolicyCallback implements ICommCallback {
	private final static String TAG = RemotePrivacyPolicyCallback.class.getSimpleName();

	private List<String> ELEMENT_NAMES;
	private List<String> NAME_SPACES;
	private List<String> PACKAGES;

	private RequestPolicy privacyPolicy;
	private boolean ack;
	private String ackMessage;


	public RemotePrivacyPolicyCallback(List<String> eLEMENT_NAMES,
			List<String> nAME_SPACES, List<String> pACKAGES) {
		super();
		ELEMENT_NAMES = eLEMENT_NAMES;
		NAME_SPACES = nAME_SPACES;
		PACKAGES = pACKAGES;
	}


	public void receiveResult(Stanza stanza, Object payload) {
		Log.d(TAG, "receiveResult");
		Log.d(TAG, "Payload class of type: " + payload.getClass().getName());
		if (payload instanceof PrivacyPolicyManagerBeanResult) {
			PrivacyPolicyManagerBeanResult resultBean = (PrivacyPolicyManagerBeanResult) payload;
			MethodType methodType = resultBean.getMethod();
			ack = resultBean.isAck();
			ackMessage = resultBean.getAckMessage();
			Log.d(TAG, "Type of response: " +methodType+" "+(ack ? "success" : "faillure"));
			if (ack && (MethodType.GET_PRIVACY_POLICY.equals(methodType) || MethodType.UPDATE_PRIVACY_POLICY.equals(methodType))) {
				privacyPolicy = resultBean.getPrivacyPolicy();
			}
		}
		debugStanza(stanza);
		// Tell upper layer that received is finished
		notifyAll();
	}

	public void receiveError(Stanza stanza, XMPPError error) {
		Log.d(TAG, "receiveError");
	}

	public void receiveInfo(Stanza stanza, String node, XMPPInfo info) {
		Log.d(TAG, "receiveInfo");
	}

	public void receiveMessage(Stanza stanza, Object payload) {
		Log.d(TAG, "receiveMessage");
		debugStanza(stanza);
	}

	public void receiveItems(Stanza stanza, String node, List<String> items) {
		Log.d(TAG, "receiveItems");
		debugStanza(stanza);
		Log.d(TAG, "node: "+node);
		Log.d(TAG, "items:");
		for(String  item:items)
			Log.d(TAG, item);
	}

	private void debugStanza(Stanza stanza) {
		Log.d(TAG, "id="+stanza.getId());
		Log.d(TAG, "from="+stanza.getFrom());
		Log.d(TAG, "to="+stanza.getTo());
	}

	public List<String> getXMLNamespaces() {
		return NAME_SPACES;
	}
	public List<String> getJavaPackages() {
		return PACKAGES;
	}

	/**
	 * @return the privacyPolicy
	 */
	public RequestPolicy getPrivacyPolicy() {
		return privacyPolicy;
	}
	/**
	 * @param privacyPolicy the privacyPolicy to set
	 */
	public void setPrivacyPolicy(RequestPolicy privacyPolicy) {
		this.privacyPolicy = privacyPolicy;
	}


	/**
	 * @return the ack
	 */
	public boolean isAck() {
		return ack;
	}
	/**
	 * @param ack the ack to set
	 */
	public void setAck(boolean ack) {
		this.ack = ack;
	}

	/**
	 * @return the ackMessage
	 */
	public String getAckMessage() {
		return ackMessage;
	}
	/**
	 * @param ackMessage the ackMessage to set
	 */
	public void setAckMessage(String ackMessage) {
		this.ackMessage = ackMessage;
	}
}
