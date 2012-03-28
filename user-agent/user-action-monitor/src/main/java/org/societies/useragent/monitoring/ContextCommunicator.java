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

package org.societies.useragent.monitoring;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.societies.api.context.CtxException;
import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxAttributeIdentifier;
import org.societies.api.context.model.CtxEntityIdentifier;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxModelType;
import org.societies.api.identity.IIdentity;
import org.societies.api.internal.context.broker.ICtxBroker;
import org.societies.api.personalisation.model.IAction;
import org.societies.api.servicelifecycle.model.IServiceResourceIdentifier;

public class ContextCommunicator {

	ICtxBroker ctxBroker;

	public ContextCommunicator(ICtxBroker ctxBroker){
		this.ctxBroker = ctxBroker;
	}

	public void updateHistory(IIdentity owner, IAction action){
		try {
			//Get Entity with serviceId
			Future<List<CtxIdentifier>> futureEntityIDs = ctxBroker.lookup(CtxModelType.ENTITY, action.getServiceID().getIdentifier().toString());
			List<CtxIdentifier> entityIds = futureEntityIDs.get();
			if(entityIds.size() > 0){
				//Get Attribute from Entity with parameter name
				CtxEntityIdentifier entityId = (CtxEntityIdentifier)entityIds.get(0);
				Future<List<CtxIdentifier>> futureAttrIDs = ctxBroker.lookup(CtxModelType.ATTRIBUTE, action.getparameterName());
				List<CtxIdentifier> ids = futureAttrIDs.get();
				if(ids.size() > 0){
					Future<List<CtxAttribute>> futureAttrs = ctxBroker.retrieveFuture((CtxAttributeIdentifier)ids.get(0), null);
					List<CtxAttribute> attrs = futureAttrs.get();
				}else{ //no history for this parameter name
					
				}
			}else{ //no histories for this services' parameters

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (CtxException e) {
			e.printStackTrace();
		}
	}

	public void updateServiceModel(IIdentity owner, IAction action){
		try {
			Future<List<CtxIdentifier>> futureIDs = ctxBroker.lookup(CtxModelType.ENTITY, action.getServiceID().getIdentifier().toString());			
		} catch (CtxException e) {
			e.printStackTrace();
		}
	}
}
