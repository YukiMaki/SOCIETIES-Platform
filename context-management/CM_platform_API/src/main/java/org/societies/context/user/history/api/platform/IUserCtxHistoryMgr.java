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

package org.societies.context.user.history.api.platform;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.societies.context.model.api.CtxAttribute;
import org.societies.context.model.api.CtxAttributeIdentifier;
import org.societies.context.model.api.CtxHistoryAttribute;

/**
 * @author nikosk
 * @version 1.0
 * @created 12-Nov-2011 7:15:15 PM
 */
public interface IUserCtxHistoryMgr {

	public void disableCtxRecording();

	public void enableCtxRecording();

	/**
	 * 
	 * @param primaryAttrIdentifier
	 */
	public List<List <CtxAttributeIdentifier>> getHistoryTuplesID(CtxAttributeIdentifier primaryAttrIdentifier);

	/**
	 * 
	 * @param primaryAttrIdentifier
	 * @param listOfEscortingAttributeIds
	 */
	public void registerHistoryTuples(CtxAttributeIdentifier primaryAttrIdentifier, List<CtxAttributeIdentifier> listOfEscortingAttributeIds);

	/**
	 * 
	 * @param primaryAttrIdentifier
	 * @param listOfEscortingAttributeTypes
	 */
	public void registerHistoryTuples(CtxAttributeIdentifier primaryAttrIdentifier, CtxAttributeIdentifier listOfEscortingAttributeTypes);

	/**
	 * 
	 * @param ctxAttribute
	 * @param startDate
	 * @param endDate
	 */
	public int removeHistory(CtxAttribute ctxAttribute, Date startDate, Date endDate);

	/**
	 * 
	 * @param type
	 * @param startDate
	 * @param endDate
	 */
	public int removeHistory(String type, Date startDate, Date endDate);

	/**
	 * 
	 * @param ctxAttribute
	 */
	public List<CtxHistoryAttribute> retrieveHistory(CtxAttribute ctxAttribute);

	/**
	 * 
	 * @param ctxAttribute
	 * @param startDate
	 * @param endDate
	 */
	public List<CtxHistoryAttribute> retrieveHistory(CtxAttribute ctxAttribute, Date startDate, Date endDate);

	/**
	 * 
	 * @param primaryAttrID
	 * @param listOfEscortingAttributeIds
	 * @param startDate
	 * @param endDate
	 */
	public Map<CtxAttribute, List<CtxAttribute>> retrieveHistoryTuples(CtxAttributeIdentifier primaryAttrID, List<CtxAttributeIdentifier> listOfEscortingAttributeIds, Date startDate, Date endDate);

}