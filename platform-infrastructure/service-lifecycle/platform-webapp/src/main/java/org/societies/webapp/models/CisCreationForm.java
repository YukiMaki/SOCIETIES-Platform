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
package org.societies.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.societies.api.identity.IIdentityManager;
import org.societies.api.identity.InvalidFormatException;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.Action;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.Condition;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.RequestItem;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.RequestPolicy;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.Resource;
import org.springframework.util.AutoPopulatingList;


/**
 * Describe the complete form for the CIS creation
 * Ok, this is dirty, but we can't do better with a such architecture
 *
 * @author Olivier Maridat (Trialog)
 *
 */
public class CisCreationForm extends CisManagerForm {
	// - CIS member ship criteria
	private String attribute;
	private String operator;
	private String value;
	
	// -- Privacy Policy
	private AutoPopulatingList<PrivacyPolicyResourceForm> resources;
	
	
	public CisCreationForm() {
		resources = new AutoPopulatingList<PrivacyPolicyResourceForm>(PrivacyPolicyResourceForm.class);
	}
	
	/**
	 * @param cisForm
	 */
	public CisCreationForm(CisManagerForm cisForm) {
		setCisName(cisForm.getCisName());
		setCisType(cisForm.getCisType());
		setAttribute(cisForm.getAttribute());
		setOperator(cisForm.getOperator());
		setValue(cisForm.getValue());
	}

	public void fillCisConfiguration(String cisName, String cisType, String attribute, String operator, String value) {
		setCisName(cisName);
		setCisType(cisType);
		setAttribute(attribute);
		setOperator(operator);
		setValue(value);
	}
	
	public void createEmptyPrivacyPolicyFrom() {
		resources = new AutoPopulatingList<PrivacyPolicyResourceForm>(PrivacyPolicyResourceForm.class);
		
		PrivacyPolicyResourceForm resource = new PrivacyPolicyResourceForm();
		resource.setOptional(true);
		
		AutoPopulatingList<PrivacyActionForm> actions = new AutoPopulatingList<PrivacyActionForm>(PrivacyActionForm.class);
		actions.add(new PrivacyActionForm());
		resource.setActions(actions);
		
		resources.add(resource);
	}
	
	
	/**
	 * @return the resources
	 */
	public AutoPopulatingList<PrivacyPolicyResourceForm> getResources() {
		return resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(AutoPopulatingList<PrivacyPolicyResourceForm> resources) {
		this.resources = resources;
	}
	public void addResource(PrivacyPolicyResourceForm resource) {
		if (null == resources) {
			resources = new AutoPopulatingList<PrivacyPolicyResourceForm>(PrivacyPolicyResourceForm.class);
		}
		this.resources.add(resource);
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("PrivacyPolicyForm [");
		int i = 0;
		if (null != resources && resources.size() > 0) {
			str.append(", resources=\n");
			for(PrivacyPolicyResourceForm resource : resources) {
				str.append("* Resource "+(i++)+": "+resource.toString()+"\n");
			}
		}
		str.append("]");
		return str.toString();
	}

	/**
	 * @return
	 * @throws InvalidFormatException 
	 */
	public RequestPolicy toRequestPolicy(IIdentityManager idManager) throws InvalidFormatException {
		List<RequestItem> requestItems = new ArrayList<RequestItem>();
		for(PrivacyPolicyResourceForm resourceForm : resources) {
			String type = resourceForm.getResourceType();
			if (null == resourceForm.getResourceType() || "".equals(resourceForm)) {
				type = resourceForm.getResourceTypeCustom();
			}
			Resource resource = new Resource(type);
			List<Action> actions = new ArrayList<Action>();
			for(PrivacyActionForm actionForm : resourceForm.getActions()) {
				actions.add((Action) actionForm);
			}
			List<Condition> conditions = new ArrayList<Condition>();
			for(PrivacyConditionForm conditionForm : resourceForm.getConditions()) {
				conditions.add((Condition) conditionForm);
			}
			requestItems.add(new RequestItem(resource, actions, conditions));
		}
		RequestPolicy privacyPolicy = new RequestPolicy(requestItems);
		return privacyPolicy;
	}
}
