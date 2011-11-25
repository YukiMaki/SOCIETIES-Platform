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

package org.societies.context.user.inference.api.platform;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.societies.context.mock.spm.identity.EntityIdentifier;
import org.societies.context.model.api.CtxAttribute;
import org.societies.context.model.api.CtxAttributeIdentifier;
import org.societies.context.model.api.CtxAttributeValueType;
import org.societies.context.model.api.CtxModelObject;
import org.societies.context.user.prediction.api.platform.PredictionMethod;
//import org.societies.context.user.prediction.api.platform.*;

/**
 * @author nikosk
 * @version 1.0
 * @created 12-Nov-2011 7:15:15 PM
 */
public interface IUserCtxInferenceMgr {

	/**
	 * 
	 * @param object
	 */
	public void checkQuality(CtxModelObject object);

	/**
	 * 
	 * @param ctxID
	 * @param ctxID2
	 */
	public Double evaluateSimilarity(CtxAttributeIdentifier ctxID, CtxAttributeIdentifier ctxID2);

	/**
	 * 
	 * @param listCtxID
	 * @param listCtxID2
	 */
	public Map<CtxAttributeIdentifier,Double> evaluateSimilarity(List<CtxAttributeIdentifier> listCtxID, List<CtxAttributeIdentifier> listCtxID2);

	/**
	 * 
	 * @param predictionMethod
	 */
	public PredictionMethod getDefaultPredictionMethod(PredictionMethod predictionMethod);

	/**
	 * 
	 * @param predictionMethodl
	 */
	public PredictionMethod getPredictionMethod(PredictionMethod predictionMethod);

	/**
	 * 
	 * @param ctxAttrId
	 * @param type
	 * @param cisid
	 */
	public void inheritContext(CtxAttributeIdentifier ctxAttrId, CtxAttributeValueType type, EntityIdentifier cisid);

	/**
	 * 
	 * @param ctxAttrID
	 * @param predictionMethod
	 * @param date
	 */
	public CtxAttribute predictContext(CtxAttributeIdentifier ctxAttrID, PredictionMethod predictionMethod, Date date);

	/**
	 * 
	 * @param ctxAttrID
	 * @param predictionMethodl
	 * @param int
	 */
	public CtxAttribute predictContext(CtxAttributeIdentifier ctxAttrID, PredictionMethod predictionMethodl, int index );

	/**
	 * 
	 * @param ctxAttrID
	 * @param date
	 */
	public CtxAttribute predictContext(CtxAttributeIdentifier ctxAttrID, Date date);

	/**
	 * 
	 * @param ctxAttrID
	 * @param index
	 */
	public CtxAttribute predictContext(CtxAttributeIdentifier ctxAttrID, int index);

	/**
	 * 
	 * @param ctxAttrId
	 */
	public void refineContext(CtxAttributeIdentifier ctxAttrId);

	/**
	 * 
	 * @param predictionMethod
	 */
	public void removePredictionMethod(PredictionMethod predictionMethod);

	/**
	 * 
	 * @param predMethod
	 */
	public void setDefaultPredictionMethod(PredictionMethod predMethod);

	/**
	 * 
	 * @param predMethod
	 */
	public void setPredictionMethod(PredictionMethod predMethod);

}