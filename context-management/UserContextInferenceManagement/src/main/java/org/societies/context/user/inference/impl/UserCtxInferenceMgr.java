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
package org.societies.context.user.inference.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxAttributeIdentifier;
import org.societies.api.context.model.CtxAttributeValueType;
import org.societies.api.context.model.CtxModelObject;
import org.societies.api.identity.IIdentity;
import org.societies.context.api.user.inference.IUserCtxInferenceMgr;

public class UserCtxInferenceMgr implements IUserCtxInferenceMgr {

	@Override
	public void checkQuality(CtxModelObject arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Double evaluateSimilarity(CtxAttributeIdentifier arg0,
			CtxAttributeIdentifier arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<CtxAttributeIdentifier, Double> evaluateSimilarity(
			List<CtxAttributeIdentifier> arg0, List<CtxAttributeIdentifier> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void inheritContext(CtxAttributeIdentifier arg0,
			CtxAttributeValueType arg1, IIdentity arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CtxAttribute predictContext(CtxAttributeIdentifier arg0, Date arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CtxAttribute predictContext(CtxAttributeIdentifier arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refineContext(CtxAttributeIdentifier arg0) {
		// TODO Auto-generated method stub
		
	}

}