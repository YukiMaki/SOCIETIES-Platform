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
package org.societies.privacytrust.privacyprotection.api.model.privacypreference;

import java.io.Serializable;

import javax.swing.tree.DefaultTreeModel;

import org.societies.api.comm.xmpp.datatypes.Identity;
import org.societies.api.context.model.CtxAttributeIdentifier;
import org.societies.api.servicelifecycle.model.IServiceResourceIdentifier;
import org.societies.privacytrust.privacyprotection.api.model.privacypreference.constants.PrivacyPreferenceTypeConstants;

/**
 * This class represents a tree model for Privacy Policy Negotiation Preferences and encapsulates a tree of IPrivacyPreference objects.
 * @author Elizabeth
 *
 */
public class PPNPrivacyPreferenceTreeModel extends DefaultTreeModel implements IPrivacyPreferenceTreeModel, Serializable {

	
	private CtxAttributeIdentifier affectedCtxId;
	private String myContextType;
	private Identity providerDPI;
	private IServiceResourceIdentifier serviceID;
	private PrivacyPreferenceTypeConstants myPrivacyType;
	private IPrivacyPreference pref;
	
	public PPNPrivacyPreferenceTreeModel(String myCtxType, IPrivacyPreference preference){
		super(preference);
		this.myContextType = myCtxType;
		this.myPrivacyType = PrivacyPreferenceTypeConstants.PPNP;
		this.pref = preference;
	}
	
	public CtxAttributeIdentifier getAffectedContextIdentifier() {
		return this.getAffectedCtxId();
	}

	
	public String getContextType() {
		return this.myContextType;
	}


	@Override
	public PrivacyPreferenceTypeConstants getPrivacyType() {
		return this.myPrivacyType;
	}


	@Override
	public IPrivacyPreference getRootPreference() {
		return this.pref;
	}

	public void setAffectedCtxId(CtxAttributeIdentifier affectedCtxId) {
		this.affectedCtxId = affectedCtxId;
	}

	public CtxAttributeIdentifier getAffectedCtxId() {
		return affectedCtxId;
	}

	public void setProviderDPI(Identity providerDPI) {
		this.providerDPI = providerDPI;
	}

	public Identity getProviderDPI() {
		return providerDPI;
	}

	public void setServiceID(IServiceResourceIdentifier serviceID) {
		this.serviceID = serviceID;
	}

	public IServiceResourceIdentifier getServiceID() {
		return serviceID;
	}

}