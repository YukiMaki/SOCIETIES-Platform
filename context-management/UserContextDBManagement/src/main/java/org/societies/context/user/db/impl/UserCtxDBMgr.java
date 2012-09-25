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
import org.societies.context.user.db.impl.model.UserIndividualCtxEntityDAO;


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

		//with maps
		final CtxAssociation association = new  CtxAssociation(identifier);
		this.modelObjects.put(association.getId(), association);		

		//with hibernate
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try{
			UserCtxModelObjectNumberDAO objectNumber = new UserCtxModelObjectNumberDAO();
			objectNumber.setNextValue(modelObjectNumber);
			session.save(objectNumber);

			UserCtxAssociationDAO associationDB = new UserCtxAssociationDAO();

			associationDB.setAssociationId(association.getId());
//			associationDB.setLastModified(association.getLastModified());

			if (association.getParentEntity() != null) {
				associationDB.setParentEntityId(association.getParentEntity());
			}
			//TODO specify dynamic
//			associationDB.setDynamic(association.get)

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
//			e.printStackTrace();
			LOG.error("Could not create association: " + e.getLocalizedMessage(),e);
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

		if (scope == null)
			throw new NullPointerException("scope can't be null");
		if (type == null)
			throw new NullPointerException("type can't be null");

		final CtxEntity entity = (CtxEntity) modelObjects.get(scope);
		
		if (entity == null)	
			throw new UserCtxDBMgrException("Scope not found: " + scope);

		long modelObjectNumber = CtxModelObjectNumberGenerator.getNextValue();

		//with maps
		CtxAttributeIdentifier attrIdentifier = new CtxAttributeIdentifier(scope, type, modelObjectNumber);
		final CtxAttribute attribute = new CtxAttribute(attrIdentifier);

		this.modelObjects.put(attribute.getId(), attribute);
		LOG.info("Attribute created with maps - " + attribute.getId());
		
		//with hibernate
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try{
			UserCtxModelObjectNumberDAO objectNumber = new UserCtxModelObjectNumberDAO();
			objectNumber.setNextValue(modelObjectNumber);
			session.save(objectNumber);
				
			UserCtxEntityDAO entityDB = new UserCtxEntityDAO();
			entityDB = (UserCtxEntityDAO) session.get(UserCtxEntityDAO.class, scope);
				
			LOG.info("Attribute to BE created - " + attribute.getId());
			LOG.info("ModelObjectNumber " + modelObjectNumber + " and " + attribute.getObjectNumber());
			UserCtxAttributeDAO attributeDB = new UserCtxAttributeDAO();
			attributeDB.setAttributeId(attribute.getId());
			attributeDB.setValueStr(attribute.getStringValue());
			attributeDB.setValueInt(attribute.getIntegerValue());
			attributeDB.setValueDbl(attribute.getDoubleValue());
			attributeDB.setValueBlob(attribute.getBinaryValue());
			attributeDB.setHistory(attribute.isHistoryRecorded());
			attributeDB.setSourceId(attribute.getSourceId());
			//TODO doesn't get value Type!!!
			attributeDB.setValType(attribute.getValueType().toString());
			attributeDB.setValueMetric(attribute.getValueMetric());
				
			//setting identifier
			UserCtxAttributeIdentifierDAO attrIdentDB = new UserCtxAttributeIdentifierDAO();
			attrIdentDB.setType(attribute.getType());
			attrIdentDB.setObjectNumber(attribute.getObjectNumber());
			attrIdentDB.setScope(entityDB);
			attributeDB.setCtxIdentifier(attrIdentDB);
			entityDB.getAttrScope().add(attributeDB);
			LOG.info("AttributeCtxIdentifier created with hibernate - " + attrIdentDB);
				
			session.save(attributeDB);
			t.commit();				
			LOG.info("Attribute created with hibernate from - " + attribute.getId());
		}
		catch (Exception e) {
			LOG.error("Could not create attribute: " + e.getLocalizedMessage(),e);
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

		//using maps
		final CtxEntity entity = new  CtxEntity(identifier);
		this.modelObjects.put(entity.getId(), entity);		

		//using hibernate
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try{

			UserCtxModelObjectNumberDAO objectNumber = new UserCtxModelObjectNumberDAO();
			objectNumber.setNextValue(modelObjectNumber);
			session.save(objectNumber);
	
			//Prepare CtxEntityDAO
			UserCtxEntityDAO entityDB = new UserCtxEntityDAO();
			entityDB.setEntityId(entity.getId());
			
			LOG.info("Entity to BE created - " + entity.getId() + " and ownerId " + entity.getOwnerId());
			LOG.info("ModelObjectNumber " + modelObjectNumber + " and " + entity.getObjectNumber());
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
			LOG.error("Could not create entity: " + e.getLocalizedMessage(),e);
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

		//using maps 
		IndividualCtxEntity entity = new IndividualCtxEntity(identifier);
		this.modelObjects.put(entity.getId(), entity);

		//using hibernate
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try{
			UserCtxModelObjectNumberDAO objectNumber = new UserCtxModelObjectNumberDAO();
			objectNumber.setNextValue(modelObjectNumber);
			session.save(objectNumber);
			
			UserIndividualCtxEntityDAO indEntityDB = new UserIndividualCtxEntityDAO();
			indEntityDB.setEntityId(entity.getId());
			
			LOG.info("Entity to BE created - " + entity.getId() + " and ownerId " + entity.getOwnerId());
			LOG.info("ModelObjectNumber " + modelObjectNumber + " and " + entity.getObjectNumber());
			//setting identifier
			UserCtxEntityIdentifierDAO identDB = new UserCtxEntityIdentifierDAO();
			identDB.setOperatorId(entity.getOwnerId());
			identDB.setType(entity.getType());
			identDB.setObjectNumber(entity.getObjectNumber());

			//testing individual creation
			indEntityDB.setCtxIdentifier(identDB);
			session.save(indEntityDB);
			
			t.commit();
		}
		catch (Exception e) {
			LOG.error("Could not create individual entity: " + e.getLocalizedMessage(),e);
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
		
		if (modelType == null) {
			throw new NullPointerException("modelType can't be null");
		}
		if (type == null) {
			throw new NullPointerException("type can't be null");
		}
		
		//final List<CtxIdentifier> foundList = new ArrayList<CtxIdentifier>();
		List<CtxIdentifier> foundList = new ArrayList<CtxIdentifier>();
///		List<? extends CtxIdentifier> foundList = new ArrayList<CtxIdentifier>();
		
//        final boolean isWildcardType = type.contains("%");

		for (CtxIdentifier identifier : modelObjects.keySet()) {
			if (identifier.getModelType().equals(modelType) && identifier.getType().equals(type)) {
				foundList.add(identifier);
			}		
		}
		System.out.println("the list is - " + foundList);
		
/*		LOG.info("problem here before sessionFactory!!!!");
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		LOG.info("problem after sessionFactory!!!");

        try {
            // Start a unit of work
            if (modelType.equals(CtxModelType.ENTITY)) {

            	Query query = session.getNamedQuery("getCtxEntityIdsByType");
            	query.setParameter("type", type, Hibernate.STRING);
            	foundList = (List<CtxIdentifier>) query.list();
            	LOG.info("test lookup (an entity) - " + foundList.get(0));
            	System.out.println("test - " + foundList.get(0));
            	CtxIdentifier testIdent = foundList.get(0);
            	System.out.println("the list is - " + foundList);
            	LOG.info("test lookup (an entity) - the list is: " + foundList);
            	
            } else if (modelType.equals(CtxModelType.ATTRIBUTE)) {

            	Query query = session.getNamedQuery("getCtxAttributeIdsByType");
            	query.setParameter("type", type, Hibernate.STRING);
            	foundList = query.list();
            	System.out.println("the list is - " + foundList);
            	LOG.info("test lookup (an attribute) the list is - " + foundList);
            	
            } else if (modelType.equals(CtxModelType.ASSOCIATION)) {

            	Query query = session.getNamedQuery("getCtxAssociationIdsByType");
            	query.setParameter("type", type, Hibernate.STRING);
            	foundList = query.list();
				System.out.println("the list is - " + foundList);
				LOG.info("test lookup (an association) the list is - " + foundList);
				
            } else {
 
                throw new IllegalArgumentException(
                        "Unsupported context model type: " + modelType);
            }

            // End the unit of work
            t.commit();
        } catch (Exception e) {
//			e.printStackTrace();
			LOG.error("Could not lookup: " + e.getLocalizedMessage(),e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return (List<CtxIdentifier>) foundList;*/
		return foundList;
		
/*		final List<CtxIdentifier> foundList = new ArrayList<CtxIdentifier>();
		
		for (CtxIdentifier identifier : modelObjects.keySet()) {
			if (identifier.getModelType().equals(modelType) && identifier.getType().equals(type)) {
				foundList.add(identifier);
			}		
		}
		return foundList;*/
	}

	@Override
	public List<CtxEntityIdentifier> lookupEntities(String entityType,
			String attribType, Serializable minAttribValue,
			Serializable maxAttribValue) throws CtxException {
		List<? extends CtxEntityIdentifier> foundList = new ArrayList<CtxEntityIdentifier>();

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		
		try {
			
			if (minAttribValue instanceof String && maxAttribValue instanceof String) {
				Query query = session.getNamedQuery("getCtxEntityIdsByAttrStringValue");
				query.setParameter("entType", entityType, Hibernate.STRING);
				query.setParameter("attrType", attribType, Hibernate.STRING);
				query.setParameter("minAttribValue", (String) minAttribValue, Hibernate.STRING);
				query.setParameter("maxAttribValue", (String) maxAttribValue, Hibernate.STRING);
              
				foundList = (List<CtxEntityIdentifier>) query.list();
				System.out.println("the list is - " + foundList);
				LOG.info("test lookupEntities (string) minAttribValue: " + minAttribValue + " maxAttribValue: " + maxAttribValue);
				LOG.info("test lookupEntities (String) the list is - " + foundList);
            } else if (minAttribValue instanceof Integer && maxAttribValue instanceof Integer) {
            	Query query = session.getNamedQuery("getCtxEntityIdsByAttrIntegerValue");
            	query.setParameter("entType", entityType, Hibernate.STRING);
            	query.setParameter("attrType", attribType, Hibernate.STRING);
            	query.setParameter("minAttribValue", (Integer) minAttribValue, Hibernate.INTEGER);
            	query.setParameter("maxAttribValue", (Integer) maxAttribValue, Hibernate.INTEGER);
               
            	foundList = (List<CtxEntityIdentifier>) query.list();
            	System.out.println("the list is - " + foundList);
				LOG.info("test lookupEntities (attribute) minAttribValue: " + minAttribValue + " maxAttribValue: " + maxAttribValue);
            	LOG.info("test lookupEntities (Integer) the list is - " + foundList);

           } else if (minAttribValue instanceof Double && maxAttribValue instanceof Double) {
             	Query query = session.getNamedQuery("getCtxEntityIdsByAttrDoubleValue");
            	query.setParameter("entType", entityType, Hibernate.STRING);
            	query.setParameter("attrType", attribType, Hibernate.STRING);
            	query.setParameter("minAttribValue", (Double) minAttribValue, Hibernate.DOUBLE);
            	query.setParameter("maxAttribValue", (Double) maxAttribValue, Hibernate.DOUBLE);
                
            	foundList = (List<CtxEntityIdentifier>) query.list();
            	System.out.println("the list is - " + foundList);
				LOG.info("test lookupEntities (double) minAttribValue: " + minAttribValue + " maxAttribValue: " + maxAttribValue);
            	LOG.info("test lookupEntities (Double) the list is - " + foundList);

           } else { // if (attribValue instanceof Serializable)
          	 
        	   byte[] minValueBytes;
        	   byte[] maxValueBytes;
        	   try {
        		   minValueBytes = SerialisationHelper.serialise(minAttribValue);
        		   maxValueBytes = SerialisationHelper.serialise(maxAttribValue);
        		   if (Arrays.equals(minValueBytes, maxValueBytes)) {
        			   Query query = session.getNamedQuery("getCtxEntityIdsByAttrBlobValue"); 
                       query.setParameter("entType", entityType, Hibernate.STRING);
                       query.setParameter("attrType", attribType, Hibernate.STRING);
                       query.setParameter("minAttribValue", (byte[]) minAttribValue, Hibernate.BINARY);
//                  	 query.setParameter("maxAttribValue", (byte[]) maxAttribValue, Hibernate.BINARY);
                  	 
                       foundList = (List<CtxEntityIdentifier>) query.list();
                       System.out.println("the list is - " + foundList);
                       LOG.info("test lookupEntities (blobs) minAttribValue: " + minAttribValue);
                       LOG.info("test lookupEntities (Blobs) the list is - " + foundList);
                   }
        	   } catch (IOException e) {
          			 // TODO Auto-generated catch block
          			 //e.printStackTrace();
        		   LOG.error("Could not lookupEntities (Blobs): " + e.getLocalizedMessage(),e);
        	   }
          	 
          }

/*        	Query query = session.getNamedQuery("getCtxEntityByAttrType");
      	query.setParameter("entType", entityType, Hibernate.STRING);
          query.setParameter("attrType", attribType, Hibernate.STRING);
          foundList = (List<CtxEntityIdentifier>) query.list();
         
          System.out.println("test - " + foundList.get(0));
          CtxEntityIdentifier testIdent = foundList.get(0);
          System.out.println("the list is - " + foundList);
*/          	
          t.commit();
      } catch (Exception e) {
//			e.printStackTrace();
			LOG.error("Could not lookupEntities: " + e.getLocalizedMessage(),e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

/*        for (CtxIdentifier identifier : modelObjects.keySet()) {
          if (identifier.getModelType().equals(CtxModelType.ATTRIBUTE)
                  && identifier.getType().equals(attribType)) {
              final CtxAttribute attribute = (CtxAttribute) modelObjects
              .get(identifier);
//              if (attribute.getScope().getType().equals(entityType) && attribute.getValue().equals(minAttribValue)) {
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
      }*/
		return (List<CtxEntityIdentifier>) foundList;
//      return foundList;
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

		CtxModelObject retrieved = null;
		CtxModelObject retrieved2 = null;

		//using maps
    	retrieved = this.modelObjects.get(id);

    	//using hibernate
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

        try {
            // Start a unit of work
            if (id.getModelType().equals(CtxModelType.ENTITY)) {            	

            	if (session.get(UserCtxEntityDAO.class, id) != null) {
            		LOG.info("trying from CtxEntity!");
            		retrieved2 = (CtxModelObject) session.get(UserCtxEntityDAO.class, id);
            	}
            	else {
            		LOG.info("trying from IndividualCtxEntity!");
            		retrieved2 = (CtxModelObject) session.get(UserIndividualCtxEntityDAO.class, id);       		
            	}
            	
            	System.out.println("test - " + retrieved);
            	LOG.info("test retrieve (entity) - " + retrieved + " and from db - " + retrieved2);
            } else if (id.getModelType().equals(CtxModelType.ATTRIBUTE)) {

           		LOG.info("trying from CtxAttribute!");
           		retrieved2 = (CtxModelObject) session.get(UserCtxAttributeDAO.class, id);
//            	if (retrieved2 == null) {
//            		LOG.info("trying from IndividualCtxAttribute!");
//            		retrieved2 = (CtxModelObject) session.get(UserIndCtxAttributeDAO.class, id);
//            	}
            	
            	System.out.println("test - " + retrieved);
            	LOG.info("test retrieve (attribute)" + retrieved + " and from db - " + retrieved2);
            } else if (id.getModelType().equals(CtxModelType.ASSOCIATION)) {

            	retrieved2 = (CtxModelObject) session.get(UserCtxAssociationDAO.class,id);
            	retrieved = this.modelObjects.get(id);

            	System.out.println("test - " + retrieved);
            	LOG.info("test retrieve (association)" + retrieved + " and from db - " + retrieved2);
            }             // End the unit of work
            t.commit();
        } catch (Exception e) {
			LOG.error("Could not retrieve: " + e.getLocalizedMessage(),e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

//		return this.modelObjects.get(id);
//		this.modelObjects.put(id, retrModObj);
		return retrieved;
	}
	
	@Override
	public CtxModelObject retrieveIndividualEntity(IIdentity cssId)
			throws CtxException {

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		
		CtxModelObject retrievedEntity = null;

        try {
	
        	String id = cssId.toString();
        	LOG.info("the id I give is - " + id);
			Query query = session.getNamedQuery("getIndividualCtxEntityByCssId").setString("id", id); 

			LOG.info("directly I get - " + query.list());

	        retrievedEntity = (CtxModelObject) query.uniqueResult();
	        LOG.info("from retrieveIndividualEntity I get - " + retrievedEntity);
        }catch (Exception e) {
				LOG.error("Could not retrieve: " + e.getLocalizedMessage(),e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return retrievedEntity;
	}

	@Override
	public CtxModelObject update(CtxModelObject modelObject) throws CtxException {

		if (modelObject == null) 
			throw new NullPointerException("modelObject can't be null");

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();

		try{

			if (modelObject.getModelType().equals(CtxModelType.ENTITY)) {
				
				if (session.get(UserIndividualCtxEntityDAO.class, modelObject.getId()) != null) {
					UserIndividualCtxEntityDAO entityDB = new UserIndividualCtxEntityDAO();
					CtxEntity ctxEntCopy = (CtxEntity) this.retrieve(modelObject.getId());
					entityDB = (UserIndividualCtxEntityDAO) session.get(UserIndividualCtxEntityDAO.class, modelObject.getId());				
					
					entityDB.setEntityId(ctxEntCopy.getId());
					
					//setting identifier
					UserCtxEntityIdentifierDAO entIdentDB = new UserCtxEntityIdentifierDAO();
					entIdentDB.setOperatorId(ctxEntCopy.getOwnerId());
					entIdentDB.setType(ctxEntCopy.getType());
					entIdentDB.setObjectNumber(ctxEntCopy.getObjectNumber());
	
					entityDB.setCtxIdentifier(entIdentDB);
	
					session.update(entityDB);
					t.commit();
				}
				else{
					UserCtxEntityDAO entityDB = new UserCtxEntityDAO();
					CtxEntity ctxEntCopy = (CtxEntity) this.retrieve(modelObject.getId());
					entityDB = (UserCtxEntityDAO) session.get(UserCtxEntityDAO.class, modelObject.getId());				
					
					entityDB.setEntityId(ctxEntCopy.getId());
					
					//setting identifier
					UserCtxEntityIdentifierDAO entIdentDB = new UserCtxEntityIdentifierDAO();
					entIdentDB.setOperatorId(ctxEntCopy.getOwnerId());
					entIdentDB.setType(ctxEntCopy.getType());
					entIdentDB.setObjectNumber(ctxEntCopy.getObjectNumber());
	
					entityDB.setCtxIdentifier(entIdentDB);
	
					session.update(entityDB);
					t.commit();					
				}		
			}
			else if (modelObject.getModelType().equals(CtxModelType.ATTRIBUTE)) {
				CtxAttribute ctxAttrCopy = (CtxAttribute) this.retrieve(modelObject.getId());
				
				UserCtxAttributeDAO attributeDB = new UserCtxAttributeDAO();
				attributeDB = (UserCtxAttributeDAO) session.get(UserCtxAttributeDAO.class, modelObject.getId());
	
				UserCtxEntityDAO entityDB = new UserCtxEntityDAO();
				entityDB = (UserCtxEntityDAO) session.get(UserCtxEntityDAO.class, ctxAttrCopy.getScope());
	
				attributeDB.setAttributeId(ctxAttrCopy.getId());

				attributeDB.setValueStr(ctxAttrCopy.getStringValue());
				attributeDB.setValueInt(ctxAttrCopy.getIntegerValue());
				attributeDB.setValueDbl(ctxAttrCopy.getDoubleValue());
				attributeDB.setValueBlob(ctxAttrCopy.getBinaryValue());
				attributeDB.setHistory(ctxAttrCopy.isHistoryRecorded());
				attributeDB.setSourceId(ctxAttrCopy.getSourceId());
				attributeDB.setValType(ctxAttrCopy.getValueType().toString());
				attributeDB.setValueMetric(ctxAttrCopy.getValueMetric());
		
				//setting identifier
				UserCtxAttributeIdentifierDAO attrIdentDB = new UserCtxAttributeIdentifierDAO();
				attrIdentDB.setType(ctxAttrCopy.getType());
				attrIdentDB.setObjectNumber(ctxAttrCopy.getObjectNumber());
				attrIdentDB.setScope(entityDB);
				attributeDB.setCtxIdentifier(attrIdentDB);
				entityDB.getAttrScope().add(attributeDB);

				session.update(attributeDB);
				t.commit();
			}
			else if (modelObject.getModelType().equals(CtxModelType.ASSOCIATION)) {
				UserCtxAssociationDAO associationDB = new UserCtxAssociationDAO();
				CtxAssociation ctxAssocCopy = (CtxAssociation) this.retrieve(modelObject.getId());
				associationDB = (UserCtxAssociationDAO) session.get(UserCtxAssociationDAO.class, modelObject.getId());
//				CtxAssociationIdentifier identifier = (CtxAssociationIdentifier) session.get(UserCtxAssociationIdentifierDAO.class, modelObject.getId());
				
				associationDB.setAssociationId(ctxAssocCopy.getId());
//				associationDB.setLastModified(ctxAssocCopy.getLastModified());

				if (ctxAssocCopy.getParentEntity() != null) {
					associationDB.setParentEntityId(ctxAssocCopy.getParentEntity());
				}
				//TODO specify dynamic
//				associationDB.setDynamic(association.get)
				if (!ctxAssocCopy.childEntities.isEmpty())
					associationDB.setMap(ctxAssocCopy.childEntities);
				
				//setting identifier
				UserCtxAssociationIdentifierDAO associationIdentDB = new UserCtxAssociationIdentifierDAO();
				associationIdentDB.setOperatorId(ctxAssocCopy.getOwnerId());
//				associationIdentDB.setOperatorId(identifier.getOperatorId());
				associationIdentDB.setOwnerId(ctxAssocCopy.getOwnerId());
				associationIdentDB.setObjectNumber(ctxAssocCopy.getObjectNumber());
				associationIdentDB.setType(ctxAssocCopy.getType());
				associationDB.setCtxIdentifier(associationIdentDB);			

				session.update(associationDB);
				
				t.commit();
			}
			else {
				throw new IllegalArgumentException (
						"Unsupported context model type: " 
							+ modelObject.getModelType());
			}
		}
		catch (Exception e) {
//			e.printStackTrace();
			LOG.error("Could not update: " + e.getLocalizedMessage(),e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

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