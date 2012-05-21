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
package org.societies.personalisation.UserPreferenceManagement.impl.management;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.context.CtxException;
import org.societies.api.context.model.CtxAssociation;
import org.societies.api.context.model.CtxAssociationIdentifier;
import org.societies.api.context.model.CtxAssociationTypes;
import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxEntity;
import org.societies.api.context.model.CtxEntityIdentifier;
import org.societies.api.context.model.CtxEntityTypes;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxModelObject;
import org.societies.api.context.model.CtxModelType;
import org.societies.api.context.model.IndividualCtxEntity;
import org.societies.api.context.model.util.SerialisationHelper;
import org.societies.api.identity.IIdentity;
import org.societies.api.internal.context.broker.ICtxBroker;
import org.societies.personalisation.preference.api.model.IPreferenceTreeModel;



/**
 * @author Elizabeth
 *
 */
public class PreferenceStorer {

	private Logger logging = LoggerFactory.getLogger(this.getClass());
	private final ICtxBroker broker;


	public PreferenceStorer(ICtxBroker broker){
		this.broker = broker;	
	}


	public void deletePreference(IIdentity userId, CtxIdentifier id){
		CtxAttribute attrPreference;
		try {
			attrPreference = (CtxAttribute) broker.retrieve(id);
			if (attrPreference == null){
				this.logging.debug("Cannot delete preference. Doesn't exist");
			}else{
				broker.remove(id);
			}
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	public boolean storeExisting(IIdentity userId, CtxIdentifier id, IPreferenceTreeModel p){
		try {
			p.setLastModifiedDate(new Date());
			CtxAttribute attrPreference = (CtxAttribute) broker.retrieve(id).get();
			if (attrPreference==null){
				return false;
			}

			attrPreference.setBinaryValue(SerialisationHelper.serialise(p));
			broker.update(attrPreference);
			return true;
			
		} catch (CtxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	
	private byte[] toByteArray(Object obj){
		try {
			return SerialisationHelper.serialise((Serializable) obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush(); 
			oos.close(); 
			bos.close();
			this.logging.debug("Trying to store preference of size: "+bos.size());
			return bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		*/
		return null;
	}


	public CtxIdentifier storeNewPreference(IIdentity userId, IPreferenceTreeModel iptm, String key){
		try{
			iptm.setLastModifiedDate(new Date());
			Future<List<CtxIdentifier>> futureCtxIDs = broker.lookup(/*userId,*/ CtxModelType.ENTITY, CtxEntityTypes.PREFERENCE); 
			List<CtxIdentifier> ctxIDs = futureCtxIDs.get();
			if (ctxIDs.size()==0){
				this.logging.debug("Preference Entity doesn't exist");
				Future<IndividualCtxEntity> futurePerson = broker.retrieveCssOperator();
				CtxEntity person = (CtxEntity) futurePerson.get();
				if (person==null){
					this.logging.debug("CtxEntity for operator with userId: "+userId.getIdentifier()+" does not exist. aborting storing and exiting");
					return null;
				}
				
				Set<CtxAssociationIdentifier> assocIDs = person.getAssociations(CtxModelTypes.HAS_PREFERENCES);
				CtxAssociation assoc = null;
				if (assocIDs.size()==0){
					Future<CtxAssociation> futureAssoc = broker.createAssociation(/*userId, */CtxModelTypes.HAS_PREFERENCES);
					assoc = futureAssoc.get();
					assoc.setParentEntity(person.getId());
					broker.update(assoc);
				}else{
					assoc = (CtxAssociation) broker.retrieve(assocIDs.iterator().next()).get();
				}
				CtxEntity preferenceEntity = (broker.createEntity(CtxEntityTypes.PREFERENCE)).get();
				assoc.addChildEntity(preferenceEntity.getId());
				broker.update(assoc);
				this.logging.debug("Created Preference Entity");
				CtxAttribute attr = (broker.createAttribute(preferenceEntity.getId(), key)).get();
				attr.setBinaryValue(SerialisationHelper.serialise(iptm));
				broker.update(attr);
				this.logging.debug("Created attribute: "+attr.getType());
				return attr.getId();
				
			}else{
				if (ctxIDs.size()>1){
					this.logging.debug("There's more than one entity of type Preference for userId: "+userId.getIdentifier()+"\nStoring preference under the first in the list");
				}
				CtxIdentifier preferenceEntityID = ctxIDs.get(0);
				CtxAttribute attr = (broker.createAttribute((CtxEntityIdentifier) preferenceEntityID, key)).get();
				attr.setBinaryValue(SerialisationHelper.serialise(iptm));
				broker.update(attr);
				this.logging.debug("Created attribute: "+attr.getType());
				return attr.getId();
			}
		}catch(CtxException ctxE){
			ctxE.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
/*	public CtxIdentifier storeNewPreference(IIdentity userId, IPreferenceTreeModel iptm, String key){
		iptm.setLastModifiedDate(new Date());


		try {
			//the original code has the lookup based on an IIdentity. this has to be updated accordingly if the contextAPI is to change and 
			//methods will include the extra IIdentity parameter for the user (data owner)
			Future<List<CtxIdentifier>> futureCtxIDs = broker.lookup(userId, CtxModelType.ENTITY, CtxEntityTypes.PREFERENCE); 
			List<CtxIdentifier> ctxIDs = futureCtxIDs.get();
			if (ctxIDs.size()==0){
				this.logging.debug("Entity Preference doesn't exist in DB. ");
				//Preference Entity doesn't exist for this dpi so we're going to check if an association exists of type hasPreferences

				Future<List<CtxIdentifier>> futureAssocCtxIDs =broker.lookup(userId, CtxModelType.ASSOCIATION, CtxModelTypes.HAS_PREFERENCES); 
				List<CtxIdentifier> assocCtxIDs = futureAssocCtxIDs.get();

				CtxAssociation assoc = null;
				if (assocCtxIDs.size()==0){
					this.logging.debug(CtxModelTypes.HAS_PREFERENCES+" association doesn't exist in DB.");
					//Has_Preferences association doesn't exist for this dpi, so we're going to check if the Person Entity exists and create the association
					CtxEntity person = broker.retrieveOperator(userId);
					Future<List<CtxIdentifier>> futurePersonCtxIDs = broker.lookup(CtxModelType.ENTITY, "PERSON");
					List<CtxIdentifier> personCtxIDs = futurePersonCtxIDs.get();
					
					if (personCtxIDs.size()==0){
						this.logging.debug("CtxEntity for operator with userId: "+userId.getIdentifier()+" does not exist. aborting storing and exiting");
						return null;
					}
					
					Future<CtxModelObject> futurePerson = broker.retrieve(personCtxIDs.get(0));
					Future<IndividualCtxEntity> futurePerson = broker.retrieveCssOperator();
					CtxEntity person = (CtxEntity) futurePerson.get();
					if (person==null){
						this.logging.debug("CtxEntity for operator with userId: "+userId.getIdentifier()+" does not exist. aborting storing and exiting");
						return null;
					}

					Future<CtxAssociation> futureAssoc = broker.createAssociation(userId, CtxModelTypes.HAS_PREFERENCES);
					assoc = futureAssoc.get();
					assoc.setParentEntity(person.getId());
					broker.update(assoc);

				}else{
					if (assocCtxIDs.size()>1){
						this.logging.debug("There's more than one association of type hasPreferences for userId:"+userId.getIdentifier()+"\nStoring Preference under the first in the list");
					}
					assoc = (CtxAssociation) broker.retrieve(assocCtxIDs.get(0));
				}

				CtxEntity preferenceEntity = (broker.createEntity(userId, CtxEntityTypes.PREFERENCE)).get();
				assoc.addChildEntity(preferenceEntity.getId());
				broker.update(assoc);
				CtxAttribute attr = (broker.createAttribute(preferenceEntity.getId(), key)).get();
				attr.setBinaryValue(this.toByteArray(iptm));
				this.logging.debug("Created attribute: "+attr.getType());
				return attr.getId();
			}else{
				if (ctxIDs.size()>1){
					this.logging.debug("There's more than one entity of type Preference for userId: "+userId.getIdentifier()+"\nStoring preference under the first in the list");
				}
				CtxIdentifier preferenceEntityID = ctxIDs.get(0);
				CtxAttribute attr = (broker.createAttribute((CtxEntityIdentifier) preferenceEntityID, key)).get();
				attr.setBinaryValue(this.toByteArray(iptm));
				this.logging.debug("Created attribute: "+attr.getType());
				return attr.getId();
			}
		} catch (CtxException e) {
			this.logging.debug("Unable to store preference: "+key);
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}*/
	
	public void storeRegistry(IIdentity userId, Registry registry){
		try {
			List<CtxIdentifier> attrList = (broker.lookup(/*userId, */CtxModelType.ATTRIBUTE, CtxModelTypes.PREFERENCE_REGISTRY)).get();
			
				if (attrList.size()>0){
					CtxIdentifier identifier = attrList.get(0);
					CtxAttribute attr =  (CtxAttribute) (broker.retrieve(identifier)).get();
					attr.setBinaryValue(SerialisationHelper.serialise(registry));
					
					broker.update(attr);					
					this.logging.debug("Successfully updated preference registry for userId: "+userId.getIdentifier());
				}else{
					this.logging.debug("PreferenceRegistry not found in DB for userId:. Creating new registry");
					
					Future<IndividualCtxEntity> futurePerson = broker.retrieveCssOperator();
					CtxEntity person = (CtxEntity) futurePerson.get();
					CtxAttribute attr = (broker.createAttribute(person.getId(), CtxModelTypes.PREFERENCE_REGISTRY)).get();
					
					
					attr.setBinaryValue(SerialisationHelper.serialise(registry));
					broker.update(attr);
					this.logging.debug("Successfully stored new preference registry");
				}
			
		} catch (CtxException e) {
			this.logging.debug("Exception while storing PreferenceRegistry to DB for userId:"+userId.getIdentifier());
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}

