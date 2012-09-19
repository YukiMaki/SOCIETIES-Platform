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
package org.societies.context.user.db.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.util.SerializationHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.context.CtxException;
import org.societies.api.context.event.CtxChangeEvent;
import org.societies.api.context.model.CtxAssociation;
import org.societies.api.context.model.CtxAssociationIdentifier;
import org.societies.api.context.model.CtxAttribute;
import org.societies.api.context.model.CtxAttributeIdentifier;
import org.societies.api.context.model.CtxAttributeValueType;
import org.societies.api.context.model.CtxEntityIdentifier;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.api.context.model.CtxModelObject;
import org.societies.api.context.model.CtxModelType;
import org.societies.api.context.model.CtxEntity;
import org.societies.api.context.model.IndividualCtxEntity;
import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.IIdentityManager;
import org.societies.api.context.model.util.SerialisationHelper;

import org.societies.context.api.event.CtxChangeEventTopic;
import org.societies.context.api.event.CtxEventScope;
import org.societies.context.api.event.ICtxEventMgr;
import org.societies.context.api.user.db.IUserCtxDBMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.societies.context.user.db.impl.model.UserCtxAssociationDAO;
//import org.societies.context.user.db.impl.model.UserCtxAssociationEntitiesDAO;
import org.societies.context.user.db.impl.model.UserCtxAssociationIdentifierDAO;
import org.societies.context.user.db.impl.model.UserCtxAttributeDAO;
import org.societies.context.user.db.impl.model.UserCtxAttributeIdentifierDAO;
import org.societies.context.user.db.impl.model.UserCtxEntityDAO;
import org.societies.context.user.db.impl.model.UserCtxEntityIdentifierDAO;
import org.societies.context.user.db.impl.model.UserCtxModelObjectNumberDAO;


/**
 * Implementation of the {@link IUserCtxDBMgr} interface.
 * 
 * @author 
 * @since 0.0.1
 */
@Service("userCtxDBMgr")
public class UserCtxDBMgr implements IUserCtxDBMgr {
	
	private SessionFactory sessionFactory;
	/** The logging facility. */
	private static final Logger LOG = LoggerFactory.getLogger(UserCtxDBMgr.class);
	
	/** The Context Event Mgmt service reference. */
	@Autowired(required=true)
	private ICtxEventMgr ctxEventMgr;

	private final ConcurrentMap<CtxIdentifier, CtxModelObject> modelObjects;
//	private final Map<CtxIdentifier, CtxModelObject> modelObjects;

	private final IIdentityManager idMgr;
	
	private final IIdentity privateId;
	
	// TODO Remove and instantiate privateId properly so that privateId.toString() can be used instead
	private final String privateIdtoString = "myFooIIdentity@societies.local";

	@Autowired(required=true)
	UserCtxDBMgr (ICommManager commMgr) {

		LOG.info(this.getClass() + " instantiated");
		this.modelObjects =  new ConcurrentHashMap<CtxIdentifier, CtxModelObject>();
		
		this.idMgr = commMgr.getIdManager();
		privateId = idMgr.getThisNetworkNode();
		
	}

	/*
	 * Used for JUnit testing only
	 */
	public UserCtxDBMgr() {
		
		LOG.info(this.getClass() + " instantiated - fooId");
		this.modelObjects =  new ConcurrentHashMap<CtxIdentifier, CtxModelObject>();
		
		// TODO !!!!!! Identity should be instantiated properly
		this.privateId = null;
		this.idMgr = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.societies.context.api.user.db.IUserCtxDBMgr#createAssociation(java.lang.String)
	 */
	@Override
	public CtxAssociation createAssociation(String type) throws CtxException {

		if (type == null)
			throw new NullPointerException("type can't be null");

		final CtxAssociationIdentifier identifier;

		long modelObjectNumber = CtxModelObjectNumberGenerator.getNextValue();

		if (this.idMgr != null) {
			identifier = new CtxAssociationIdentifier(this.privateId.getBareJid(), 
					type, modelObjectNumber);
		}
		else {
			identifier = new CtxAssociationIdentifier(this.privateIdtoString, 
				type, modelObjectNumber);
		}
		
		final CtxAssociation association = new  CtxAssociation(identifier);
		this.modelObjects.put(association.getId(), association);		

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try{
//			UserCtxModelObjectNumberDAO objectNumber = new UserCtxModelObjectNumberDAO();
//			objectNumber.setNextValue(modelObjectNumber);
//			session.save(objectNumber);
			Date date = new Date();
			UserCtxAssociationDAO associationDB = new UserCtxAssociationDAO();

			associationDB.setAssociationId(association.getId());
			associationDB.setTimestamp(association.getLastModified());

			if (association.getParentEntity() != null) {
				associationDB.setParentEntityId(association.getParentEntity());
			}
			//TODO specify dynamic
//			associationDB.setDynamic(association.get)
//			session.save(tmpEntity);
			if (!association.childEntities.isEmpty())
				associationDB.setMap(association.childEntities);
			
			//setting identifier
			UserCtxAssociationIdentifierDAO associationIdentDB = new UserCtxAssociationIdentifierDAO();
			associationIdentDB.setOperatorId(identifier.getOperatorId());
			associationIdentDB.setOwnerId(identifier.getOwnerId());
			associationIdentDB.setObjectNumber(identifier.getObjectNumber());
			associationIdentDB.setType(identifier.getType());
			associationDB.setCtxIdentifier(associationIdentDB);			

			session.save(associationDB);
			
			t.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		if (this.ctxEventMgr != null) {
			this.ctxEventMgr.post(new CtxChangeEvent(association.getId()), 
					new String[] { CtxChangeEventTopic.CREATED }, CtxEventScope.BROADCAST);
		} else {
			LOG.warn("Could not send context change event to topics '" 
					+ CtxChangeEventTopic.CREATED 
					+ "' with scope '" + CtxEventScope.BROADCAST + "': "
					+ "ICtxEventMgr service is not available");
		}
		
		return association;
	}
	
	/*
	 * @see org.societies.context.api.user.db.IUserCtxDBMgr#createAttribute(org.societies.api.context.model.CtxEntityIdentifier, java.lang.String)
	 */
	@Override
	public CtxAttribute createAttribute(final CtxEntityIdentifier scope,
			final String type) throws CtxException {

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		if (scope == null)
			throw new NullPointerException("scope can't be null");
		if (type == null)
			throw new NullPointerException("type can't be null");

		final CtxEntity entity = (CtxEntity) modelObjects.get(scope);
		
		if (entity == null)	
			throw new UserCtxDBMgrException("Scope not found: " + scope);

		long modelObjectNumber = CtxModelObjectNumberGenerator.getNextValue();
		
		CtxAttributeIdentifier attrIdentifier = new CtxAttributeIdentifier(scope, type, modelObjectNumber);
		final CtxAttribute attribute = new CtxAttribute(attrIdentifier);

		this.modelObjects.put(attribute.getId(), attribute);
		entity.addAttribute(attribute);
		
		try{
//			UserCtxModelObjectNumberDAO objectNumber = new UserCtxModelObjectNumberDAO();
//			objectNumber.setNextValue(modelObjectNumber);
//			session.save(objectNumber);
			
			UserCtxEntityDAO entityDB = new UserCtxEntityDAO();
			entityDB = (UserCtxEntityDAO) session.get(UserCtxEntityDAO.class, scope);
			
			Date date = new Date();
			UserCtxAttributeDAO attributeDB = new UserCtxAttributeDAO();
			attributeDB.setAttributeId(attribute.getId());
			//TODO change to timestamp
//			attributeDB.setTimestamp(attribute.getLastModified());
			attributeDB.setTimestamp(date);
			attributeDB.setValueStr(attribute.getStringValue());
			attributeDB.setValueInt(attribute.getIntegerValue());
			attributeDB.setValueDbl(attribute.getDoubleValue());
			attributeDB.setValueBlob(attribute.getBinaryValue());
			attributeDB.setHistory(attribute.isHistoryRecorded());
			attributeDB.setSourceId(attribute.getSourceId());
			attributeDB.setValueType(attribute.getValueType().toString());
			attributeDB.setValueMetric(attribute.getValueMetric());
			
			//setting identifier
			UserCtxAttributeIdentifierDAO attrIdentDB = new UserCtxAttributeIdentifierDAO();
			attrIdentDB.setType(attribute.getType());
			attrIdentDB.setObjectNumber(attrIdentifier.getObjectNumber());
			attrIdentDB.setScope(entityDB);
			attributeDB.setCtxIdentifier(attrIdentDB);
			entityDB.getAttrScope().add(attributeDB);

			session.save(attributeDB);
			t.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		if (this.ctxEventMgr != null) {
			this.ctxEventMgr.post(new CtxChangeEvent(attribute.getId()), 
					new String[] { CtxChangeEventTopic.CREATED }, CtxEventScope.BROADCAST);
		} else {
			LOG.warn("Could not send context change event to topics '" 
					+ CtxChangeEventTopic.CREATED 
					+ "' with scope '" + CtxEventScope.BROADCAST + "': "
					+ "ICtxEventMgr service is not available");
		}

		return attribute;
	}

	/*
	 * (non-Javadoc)
	 * @see org.societies.context.api.user.db.IUserCtxDBMgr#createEntity(java.lang.String)
	 */
	@Override
	public CtxEntity createEntity(String type) throws CtxException {

		final CtxEntityIdentifier identifier;
		
		long modelObjectNumber = CtxModelObjectNumberGenerator.getNextValue();
		
		if (this.idMgr != null) {
			identifier = new CtxEntityIdentifier(this.privateId.getBareJid(), 
					type, modelObjectNumber);
		}
		else {
			identifier = new CtxEntityIdentifier(this.privateIdtoString, 
					type, modelObjectNumber);
		}

		final CtxEntity entity = new  CtxEntity(identifier);
		this.modelObjects.put(entity.getId(), entity);		

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try{

//			UserCtxModelObjectNumberDAO objectNumber = new UserCtxModelObjectNumberDAO();
//			objectNumber.setNextValue(modelObjectNumber);
//			session.save(objectNumber);
	
			Date date = new Date();

			//Prepare CtxEntityDAO
			UserCtxEntityDAO entityDB = new UserCtxEntityDAO();
			entityDB.setEntityId(entity.getId());
			//TODO change to timestamp
//			entityDB.setTimestamp(entity.getLastModified());
			entityDB.setTimestamp(date);
			
			//setting identifier
			UserCtxEntityIdentifierDAO entIdentDB = new UserCtxEntityIdentifierDAO();
			entIdentDB.setOperatorId(entity.getOwnerId());
			entIdentDB.setType(entity.getType());
			entIdentDB.setObjectNumber(entity.getObjectNumber());

			entityDB.setCtxIdentifier(entIdentDB);

			session.save(entityDB);
			t.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		if (this.ctxEventMgr != null) {
			this.ctxEventMgr.post(new CtxChangeEvent(entity.getId()), 
					new String[] { CtxChangeEventTopic.CREATED }, CtxEventScope.BROADCAST);
		} else {
			LOG.warn("Could not send context change event to topics '" 
					+ CtxChangeEventTopic.CREATED 
					+ "' with scope '" + CtxEventScope.BROADCAST + "': "
					+ "ICtxEventMgr service is not available");
		}
		
		return entity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.societies.context.api.user.db.IUserCtxDBMgr#createIndividualCtxEntity(java.lang.String)
	 */
	@Override
	public IndividualCtxEntity createIndividualCtxEntity(String type) throws CtxException {

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		CtxEntityIdentifier identifier;

		long modelObjectNumber = CtxModelObjectNumberGenerator.getNextValue();
		
		if (this.idMgr != null) {
			identifier = new CtxEntityIdentifier(this.privateId.getBareJid(),
					type, modelObjectNumber);	
		}
		else {
			identifier = new CtxEntityIdentifier(this.privateIdtoString,
					type, modelObjectNumber);			
		}
		
		IndividualCtxEntity entity = new IndividualCtxEntity(identifier);
		this.modelObjects.put(entity.getId(), entity);

		try{
//			UserCtxModelObjectNumberDAO objectNumber = new UserCtxModelObjectNumberDAO();
//			objectNumber.setNextValue(modelObjectNumber);
//			session.save(objectNumber);
			
			Date date = new Date();

			//Prepare CtxEntityDAO
			UserCtxEntityDAO entityDB = new UserCtxEntityDAO();
			entityDB.setEntityId(entity.getId());
			//TODO change to timestamp
//			entityDB.setTimestamp(entity.getLastModified());
			entityDB.setTimestamp(date);
			
			//setting identifier
			UserCtxEntityIdentifierDAO entIdentDB = new UserCtxEntityIdentifierDAO();
			entIdentDB.setOperatorId(entity.getOwnerId());
			entIdentDB.setType(entity.getType());
			entIdentDB.setObjectNumber(entity.getObjectNumber());

			entityDB.setCtxIdentifier(entIdentDB);

			session.save(entityDB);
			t.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		if (this.ctxEventMgr != null) {
			this.ctxEventMgr.post(new CtxChangeEvent(entity.getId()), 
					new String[] { CtxChangeEventTopic.CREATED }, CtxEventScope.BROADCAST);
		} else {
			LOG.warn("Could not send context change event to topics '" 
					+ CtxChangeEventTopic.CREATED 
					+ "' with scope '" + CtxEventScope.BROADCAST + "': "
					+ "ICtxEventMgr service is not available");
		}

		return entity;
	}

	@Override
	public List<CtxIdentifier> lookup(CtxModelType modelType, String type) throws CtxException {
		// TODO Auto-generated method stub
		
		final List<CtxIdentifier> foundList = new ArrayList<CtxIdentifier>();
		
		for (CtxIdentifier identifier : modelObjects.keySet()) {
			if (identifier.getModelType().equals(modelType) && identifier.getType().equals(type)) {
				foundList.add(identifier);
			}		
		}
		return foundList;
	}

	@Override
	public List<CtxEntityIdentifier> lookupEntities(String entityType,
			String attribType, Serializable minAttribValue,
			Serializable maxAttribValue) throws CtxException {
				
        final List<CtxEntityIdentifier> foundList = new ArrayList<CtxEntityIdentifier>();
        for (CtxIdentifier identifier : modelObjects.keySet()) {
            if (identifier.getModelType().equals(CtxModelType.ATTRIBUTE)
                    && identifier.getType().equals(attribType)) {
                final CtxAttribute attribute = (CtxAttribute) modelObjects
                .get(identifier);
//                if (attribute.getScope().getType().equals(entityType) && attribute.getValue().equals(minAttribValue)) {
                if (attribute.getScope().getType().equals(entityType)) {
                	if (minAttribValue instanceof String && maxAttribValue instanceof String) {
                		if (attribute.getStringValue()!=null) {
		                	String valueStr = attribute.getStringValue();
		                		if(valueStr.compareTo(minAttribValue.toString()) >=0 && valueStr.compareTo(maxAttribValue.toString()) <=0)
		               				foundList.add(attribute.getScope());                			
        				}
                	} else if (minAttribValue instanceof Integer && maxAttribValue instanceof Integer) {
                		if(attribute.getIntegerValue()!=null) {
		               		Integer valueInt = attribute.getIntegerValue();
		          			if(valueInt.compareTo((Integer) minAttribValue) >=0 && valueInt.compareTo((Integer) maxAttribValue) <=0)
		               			foundList.add(attribute.getScope());
                		}
                	} else if (minAttribValue instanceof Double && maxAttribValue instanceof Double) {
                		if(attribute.getDoubleValue()!=null) {
		               		Double valueDouble = attribute.getDoubleValue();
		           			if(valueDouble.compareTo((Double) minAttribValue) >= 0 && valueDouble.compareTo((Double) maxAttribValue) <= 0)
		               			foundList.add(attribute.getScope());                			
                		}
                	} else {
                		byte[] valueBytes;
                		byte[] minValueBytes;
                		byte[] maxValueBytes;
						try {
							minValueBytes = SerialisationHelper.serialise(minAttribValue);
							maxValueBytes = SerialisationHelper.serialise(maxAttribValue);
							valueBytes = SerialisationHelper.serialise(attribute.getBinaryValue());
							if (Arrays.equals(minValueBytes, maxValueBytes))
								if (Arrays.equals(valueBytes, minValueBytes))
									foundList.add(attribute.getScope());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}                		
                	}
                	
                }
            }
        }
        return foundList;
	}

	@Override
	public CtxModelObject remove(CtxIdentifier arg0) throws CtxException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.societies.context.api.user.db.IUserCtxDBMgr#retrieve(org.societies.api.context.model.CtxIdentifier)
	 */
	@Override
	public CtxModelObject retrieve(CtxIdentifier id) throws CtxException {

		return this.modelObjects.get(id);
	}

	@Override
	public CtxModelObject update(CtxModelObject modelObject) throws CtxException {

		if (this.modelObjects.keySet().contains(modelObject.getId())) {
			this.modelObjects.put(modelObject.getId(), modelObject);
			
			// TODO CtxChangeEventTopic.MODIFIED should only be used if the model object is actually modified
			final String[] topics = new String[] { CtxChangeEventTopic.UPDATED, CtxChangeEventTopic.MODIFIED };
			if (this.ctxEventMgr != null) {
				this.ctxEventMgr.post(new CtxChangeEvent(modelObject.getId()), 
						topics, CtxEventScope.BROADCAST);
			} else {
				LOG.warn("Could not send context change event to topics '" 
						+ Arrays.toString(topics) 
						+ "' with scope '" + CtxEventScope.BROADCAST 
						+ "': ICtxEventMgr service is not available");
			}
		}
		
		 if (modelObject instanceof CtxAssociation) {

			   CtxEntity ent = null;
			   CtxEntityIdentifier entId;

			   // Add association to parent entity
			   entId = ((CtxAssociation) modelObject).getParentEntity();
			   if (entId != null)
			     ent = (CtxEntity) this.retrieve(entId);
			     if (ent != null)
			       ent.addAssociation(((CtxAssociation) modelObject).getId());

			    // Add association to child entities
			    Set<CtxEntityIdentifier> entIds = ((CtxAssociation) modelObject).getChildEntities();
			    for (CtxEntityIdentifier entIdent : entIds) {
			    	//entIdent = ((CtxAssociation) modelObject).getParentEntity();
			    	ent = (CtxEntity) this.retrieve(entIdent);
			    	if (ent != null)
			    		ent.addAssociation(((CtxAssociation) modelObject).getId());
			    }
		}
			      
		return modelObject;
	}	
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public ICtxEventMgr getCtxEventMgr() {
		return ctxEventMgr;
	}

	public void setCtxEventMgr(ICtxEventMgr ctxEventMgr) {
		this.ctxEventMgr = ctxEventMgr;
	}
}