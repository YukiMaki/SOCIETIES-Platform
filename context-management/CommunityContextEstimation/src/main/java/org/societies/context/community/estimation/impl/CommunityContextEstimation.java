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
package org.societies.context.community.estimation.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.societies.api.context.CtxException;
import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxEntity;
import org.societies.api.context.model.CtxEntityIdentifier;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxModelType;
import org.societies.api.mock.EntityIdentifier;
import org.societies.context.api.community.estimation.EstimationModels;
import org.societies.context.api.community.estimation.ICommunityCtxEstimationMgr;
import org.societies.context.broker.impl.InternalCtxBroker;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yboul 07-Dec-2011 4:15:14 PM
 * @param <communityMembers>
 */
/**
 * @author yboul
 *
 * @param <communityMembers>
 */
public class CommunityContextEstimation<communityMembers> implements ICommunityCtxEstimationMgr{
	
	
	//Constructor
	
	public CommunityContextEstimation() {
		// TODO Auto-generated constructor stub
			
	}
	
	@Autowired
	//private InternalCtxBroker b;
	private InternalCtxBroker b;
	private CtxEntityIdentifier comId;
	private String entityType;
	private String attributeType;
	

	//@Override
	public void estimateContext(EstimationModels estimationModel, List<CtxAttribute> list) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void retrieveCurrentCisContext(boolean Current, EntityIdentifier communityID, List<CtxAttribute> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveHistoryCisContext(boolean Current, EntityIdentifier communityID, List<CtxAttribute> list) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void updateContextModelObject(CtxEntity estimatedContext) {
		// TODO Auto-generated method stub
		
	}

	
	//******************************************NEW***************************************************************************
	//
	//
	//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	
	
	/**
	 * @param modelType
	 * @param ctxEntityType
	 * @param ctxAttr
	 * @return
	 * @throws CtxException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public Double estimateMeanValueOfIntegers(CtxModelType modelType, String ctxEntityType, String ctxAttr) throws CtxException, InterruptedException, ExecutionException{
				
		int total = 0;
		Double mo = 0.0;
		
		CommunityContextEstimation<communityMembers> cce = new CommunityContextEstimation<communityMembers>();
		List<CtxAttribute> listOfCtxAttributes = cce.retrieveCertainAttributes(modelType, ctxEntityType, ctxAttr);
			
		//Mean value estimation		
		for (CtxAttribute cA : listOfCtxAttributes) {
				 total = cA.getIntegerValue();
			}
		
		int noOfCtxEntities = cce.retrieveListOfCtxEntities(modelType, ctxEntityType).size();	
			 
		mo = (double) (total/noOfCtxEntities);
		
		return mo;		
		
	}
	

	/**
	 * @param modelType
	 * @param ctxEntityType
	 * @param ctxAttr
	 * @return
	 * @throws CtxException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<CtxAttribute> retrieveCertainAttributes(CtxModelType modelType, String ctxEntityType, String ctxAttr) throws CtxException, InterruptedException, ExecutionException {
		
		//Retrieve a list of ctxEntitiesIdentifiers, of certain modelType (e.g. entity) and certain ctxEntityType (e.g. "Person")
		CommunityContextEstimation<communityMembers> cce = new CommunityContextEstimation<communityMembers>();
		List<CtxEntity> listOfCtxEntities = cce.retrieveListOfCtxEntities(modelType, ctxEntityType);
		
		//We iterate on the previous list of ctxEntities in order to retrieve the attributes of each ctxEntity, of the given value (ctxAttr)
		List<CtxAttribute> listOfCtxAttributes = new ArrayList<CtxAttribute>();		
		for (CtxEntity cE : listOfCtxEntities){
			
			Set<CtxAttribute> setOfEntityCtxAttributes = cE.getAttributes();
			
			for (CtxAttribute cA : setOfEntityCtxAttributes) {
				if (cA.getStringValue() == ctxAttr) {
					listOfCtxAttributes.add(cA);	
				}
			}		
		}
		return listOfCtxAttributes;
	}
	
	/**
	 * @param modelType
	 * @param ctxEntityType
	 * @return
	 * @throws CtxException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<CtxEntity> retrieveListOfCtxEntities(CtxModelType modelType, String ctxEntityType) throws CtxException, InterruptedException, ExecutionException {
		
		Future<List<CtxIdentifier>> ctxEntitiesIdentifiersFutureList = b.lookup(modelType, ctxEntityType);
		List<CtxIdentifier> ctxEntitiesIdentifiersList = ctxEntitiesIdentifiersFutureList.get();
		List<CtxEntity> listOfCtxEntities= new ArrayList<CtxEntity>();
		
		for (CtxIdentifier id : ctxEntitiesIdentifiersList){
			CtxEntity ctxEntity = (CtxEntity) b.retrieve(id);
			listOfCtxEntities.add(ctxEntity);	
		}		
		return listOfCtxEntities;
	}
		
		
// Setters and Getters for the private fields ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^//^

	public InternalCtxBroker getB() {                                                                                                  
		return b;																												 
	}																															  

	public void setB(InternalCtxBroker b) {																								
		this.b = b;																												
	}																															


	public CtxEntityIdentifier getComId() {																						
		return comId;																											
	}																															


	public void setComId(CtxEntityIdentifier comId) {
		this.comId = comId;
	}


	public String getEntityType() {
		return entityType;
	}


	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}


	public String getAttributeType() {
		return attributeType;
	}


	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

//	
	
	
	
//	private void returnListOfDesiredAttributes(List<CtxAttributeIdentifier> listOfMembersOfGivenType) {
//	// TODO Auto-generated method stub
//	// I want to receive the attributes value through the contextIdentifier
//	
//	ArrayList<CtxAttribute> listOfAttributes = new ArrayList<CtxAttribute>();
//	Iterator<CtxAttributeIdentifier> membIterator = listOfMembersOfGivenType.iterator();
//	while (membIterator.hasNext()){
//		CtxAttributeIdentifier cEI = membIterator.next();
//		CtxAttribute ctxAtt;
//		CtxModelObject ctxModObj;
//		//if 
//		//(cEI.getModelType().ATTRIBUTE != null)
//			//ctxModObj.
//			//listOfAttributes.add(e);
//		
//	}
//}

	
	
//	private List<CtxAttributeIdentifier> returnEntitiesWithGivenEntiryType(List<CtxEntityIdentifier> allMembersList) {
//	// TODO Auto-generated method stub
//		//If the modelType is Entity then put in the listCtxEntityIdentifier this ctxEntityIdentifier.
//		//So at the end I will have a list with Entity ctxEntityIdentifiers of the community under discussion
//		
//	 List<CtxAttributeIdentifier> listCtxEntityIdentifier = new ArrayList<CtxAttributeIdentifier>();
//		
//		Iterator<CtxEntityIdentifier> membIterator = allMembersList.iterator();
//		while (membIterator.hasNext()){
//			CtxEntityIdentifier cEI = membIterator.next();
//			CtxAttributeIdentifier a = new CtxAttributeIdentifier(cEI, cEI.getType(),cEI.getObjectNumber());
//			{
//				if
//				(cEI.getModelType().ENTITY != null && cEI.getType().equals(entityType)) 
//					listCtxEntityIdentifier.add(a);
//				else
//					System.out.println(cEI.getType());
//			}
//				
//		}
//		return listCtxEntityIdentifier;
//}	
	
	
	
//	public void estimateContext_John(EntityIdentifier communityID, List<CtxAttribute> list, Boolean currentDB) throws CtxException{
//	// TODO Auto-generated method stub
//	
////	CtxAttribute a = new CtxAttribute(null);
////	a.getId().getType();
////	a.getIntegerValue();
//	
//	ArrayList<CtxAttribute> allAttributes = new ArrayList<CtxAttribute>();
//	
//	ArrayList<CtxEntity> m = retrieveCisMembersWitPredefinedAttr_John(communityID, list);
//	// elegxos gia null h oxi (ta members)
//	for (CtxEntity e:m){
//		allAttributes.addAll((retrieveMembersAttribute_John(e, list)));
//	}
//	
//	CalculateAlgorithm(allAttributes);
//	CtxEntityIdentifier community = null;
//	Identity requester = null;
//	b.retrieveCommunityMembers(requester, community);
//	
//}
//
//private ArrayList<CtxEntity> retrieveCisMembersWitPredefinedAttr_John(EntityIdentifier communityID, List<CtxAttribute> hasTheseAttributes) throws CtxException {
//	// TODO Auto-generated method stub
//	//b
//	//return (ArrayList<CtxEntity>) b.retrieveAdministratingCSS(null, null); na vro tin kanoniki methodo tou broker...
//	return null;
//}
//
//
//private ArrayList<CtxAttribute> retrieveMembersAttribute_John(CtxEntity member, List<CtxAttribute> hasTheseAttributes) {
//	// TODO Auto-generated method stub
//	//b
//	//return (ArrayList<CtxEntity>) b.retrieveAdministratingCSS(null, null); na vro tin kanoniki methodo tou broker...
//	return null;
//}

}
