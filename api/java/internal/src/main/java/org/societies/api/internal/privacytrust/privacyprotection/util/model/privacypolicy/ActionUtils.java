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
package org.societies.api.internal.privacytrust.privacyprotection.util.model.privacypolicy;

import java.util.ArrayList;
import java.util.List;

import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.Action;
import org.societies.api.internal.privacytrust.privacyprotection.model.privacypolicy.constants.ActionConstants;

/**
 * Tool class to manage conversion between Java type and Bean XMLschema generated type
 * @author Olivier Maridat (Trialog)
 */
public class ActionUtils {
	public static Action toAction(org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Action actionBean)
	{
		if (null == actionBean) {
			return null;
		}
		ActionConstants actionConstant = ActionConstants.valueOf(actionBean.getActionConstant().value());
		return new Action(actionConstant, actionBean.isOptional());
	}
	public static List<Action> toActions(List<org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Action> actionBeans)
	{
		if (null == actionBeans) {
			return null;
		}
		List<Action> actions = new ArrayList<Action>();
		for(org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Action actionBean : actionBeans) {
			actions.add(ActionUtils.toAction(actionBean));
		}
		return actions;
	}
	
	public static org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Action toActionBean(Action action)
	{
		if (null == action) {
			return null;
		}
		org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Action actionBean = new org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Action();
		org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.ActionConstants actionConstant = org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.ActionConstants.fromValue(action.getActionType().name());
		actionBean.setActionConstant(actionConstant);
		actionBean.setOptional(action.isOptional());
		return actionBean;
	}
	public static List<org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Action> toActionBeans(List<Action> actions)
	{
		if (null == actions) {
			return null;
		}
		List<org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Action> actionBeans = new ArrayList<org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Action>();
		for(Action action : actions) {
			actionBeans.add(ActionUtils.toActionBean(action));
		}
		return actionBeans;
	}
	
	public static boolean contains(Action actionToCheck, List<Action> actions) {
		if (null == actions || actions.size() <= 0 || null == actionToCheck) {
			return false;
		}
		for(Action action : actions) {
			if (action.getActionType().equals(actionToCheck.getActionType())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean contains(List<Action> actionsToCheck, List<Action> actions) {
		return contains(actionsToCheck, actions, null);
	}
	/**
	 * 
	 * @param actionsToCheck
	 * @param actions
	 * @param intersection Will be filled with the intersection of the two list. It will works only if actions contains actionsToCheck
	 * @return
	 */
	public static boolean contains(List<Action> actionsToCheck, List<Action> actions, List<Action> intersection) {
		if (null == actions || actions.size() <= 0 || null == actionsToCheck || actionsToCheck.size() <= 0 || actions.size() < actionsToCheck.size()) {
			return false;
		}
		for(Action actionToCheck : actionsToCheck) {
			if (!contains(actionToCheck, actions)) {
				return false;
			}
			if (null != intersection) {
				intersection.add(actionToCheck);
			}
		}
		return true;
	}
	
	public static boolean containsOr(List<Action> actionsToCheck, List<Action> actions) {
		if (null == actions || actions.size() <= 0 || null == actionsToCheck || actionsToCheck.size() <= 0 || actions.size() < actionsToCheck.size()) {
			return false;
		}
		for(Action actionToCheck : actionsToCheck) {
			if (contains(actionToCheck, actions)) {
				return true;
			}
		}
		return false;
	}
	
	public static List<Action> intersect(List<Action> actionsToCheck, List<Action> actions) {
		if (null == actions || actions.size() <= 0 || null == actionsToCheck || actionsToCheck.size() <= 0 || actions.size() < actionsToCheck.size()) {
			return null;
		}
		List<Action> result = new ArrayList<Action>();
		for(Action actionToCheck : actionsToCheck) {
			if (contains(actionToCheck, actions)) {
				result.add(actionToCheck);
			}
		}
		return result;
	}
}
