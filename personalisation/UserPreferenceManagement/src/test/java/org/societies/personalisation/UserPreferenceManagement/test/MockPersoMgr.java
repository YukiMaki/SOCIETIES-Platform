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
package org.societies.personalisation.UserPreferenceManagement.test;

import org.societies.api.comm.xmpp.datatypes.Identity;
import org.societies.api.context.model.CtxAttributeIdentifier;
import org.societies.api.internal.personalisation.model.IFeedbackEvent;
import org.societies.api.personalisation.mgmt.IPersonalisationCallback;
import org.societies.api.servicelifecycle.model.IServiceResourceIdentifier;
import org.societies.personalisation.common.api.management.IInternalPersonalisationManager;
import org.societies.personalisation.common.api.model.PersonalisationTypes;


/**
 * Describe your class here...
 *
 * @author Eliza
 *
 */
public class MockPersoMgr implements IInternalPersonalisationManager{

	@Override
	public void getIntentAction(Identity arg0, Identity arg1,
			IServiceResourceIdentifier arg2, String arg3,
			IPersonalisationCallback arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPreference(Identity arg0, Identity arg1, String arg2,
			IServiceResourceIdentifier arg3, String arg4,
			IPersonalisationCallback arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getIntentAction(Identity arg0, IServiceResourceIdentifier arg1,
			String arg2, IPersonalisationCallback arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPreference(Identity arg0, String arg1,
			IServiceResourceIdentifier arg2, String arg3,
			IPersonalisationCallback arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerForContextUpdate(Identity arg0,
			PersonalisationTypes arg1, CtxAttributeIdentifier arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnFeedback(IFeedbackEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}