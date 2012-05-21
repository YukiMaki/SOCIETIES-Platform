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
package org.societies.privacytrust.trust.impl.repo;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.internal.privacytrust.trust.model.TrustedEntityId;
import org.societies.api.internal.privacytrust.trust.model.TrustedEntityType;
import org.societies.privacytrust.trust.api.model.ITrustedEntity;
import org.societies.privacytrust.trust.api.repo.ITrustRepository;
import org.societies.privacytrust.trust.api.repo.TrustRepositoryException;
import org.societies.privacytrust.trust.impl.repo.model.TrustedCis;
import org.societies.privacytrust.trust.impl.repo.model.TrustedCss;
import org.societies.privacytrust.trust.impl.repo.model.TrustedEntity;
import org.societies.privacytrust.trust.impl.repo.model.TrustedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the {@link ITrustRepository} interface.
 *
 * @author <a href="mailto:nicolas.liampotis@cn.ntua.gr">Nicolas Liampotis</a> (ICCS)
 * @since 0.0.5
 */
@Repository
@Lazy(value = false)
public class TrustRepository implements ITrustRepository {

	/** The logging facility. */
	private static final Logger LOG = LoggerFactory.getLogger(TrustRepository.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	TrustRepository() {
		
		LOG.info(this.getClass() + " instantiated");
	}
	
	/* (non-Javadoc)
	 * @see org.societies.privacytrust.trust.api.repo.ITrustRepository#addEntity(org.societies.privacytrust.trust.api.model.TrustedEntity)
	 */
	@Override
	public boolean addEntity(ITrustedEntity entity)
			throws TrustRepositoryException {
		
		if (entity == null)
			throw new NullPointerException("entity can't be null");
		
		boolean result = false;

		// check if the entity is already present
		if (this.retrieveEntity(entity.getTeid()) != null)
			return false;
		
		final Session session = sessionFactory.openSession();
		final Transaction transaction = session.beginTransaction();
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Adding trusted entity " + entity + " to the Trust Repository...");
	
			session.save(entity);
			session.flush();
			transaction.commit();
			result = true;
		} catch (ConstraintViolationException cve) {
			LOG.warn("Rolling back transaction for entity " + entity);
			transaction.rollback();
		} catch (Exception e) {
			LOG.warn("Rolling back transaction for entity " + entity);
			transaction.rollback();
			throw new TrustRepositoryException("Could not add entity " + entity, e);
		} finally {
			if (session != null)
				session.close();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.societies.privacytrust.trust.api.repo.ITrustRepository#retrieveEntity(java.lang.String, org.societies.privacytrust.trust.api.model.TrustedEntityId)
	 */
	@Override
	public ITrustedEntity retrieveEntity(TrustedEntityId teid)
			throws TrustRepositoryException {
		
		Class<? extends TrustedEntity> entityClass = TrustedEntity.class;
		if (TrustedEntityType.CSS.equals(teid.getEntityType()))
			entityClass = TrustedCss.class;
		else if (TrustedEntityType.CIS.equals(teid.getEntityType()))
			entityClass = TrustedCis.class;
		else if (TrustedEntityType.SVC.equals(teid.getEntityType()))
			entityClass = TrustedService.class;
		// TODO TrustedEntityType.LGC
		
		final Session session = sessionFactory.openSession();
		final Criteria criteria = session.createCriteria(entityClass)
			.add(Restrictions.eq("teid", teid));
		
		//if (TrustedCss.class.equals(entityClass))
		//	criteria.setFetchMode("communities", FetchMode.SELECT);
		
		//if (TrustedCis.class.equals(entityClass))
		//	criteria.setFetchMode("members", FetchMode.SELECT);
		
		final ITrustedEntity result = (ITrustedEntity) criteria.uniqueResult();
		
		if (session != null)
			session.close();
			
		return result;
	}

	/* (non-Javadoc)
	 * @see org.societies.privacytrust.trust.api.repo.ITrustRepository#updateEntity(org.societies.privacytrust.trust.api.model.TrustedEntity)
	 */
	@Override
	public ITrustedEntity updateEntity(ITrustedEntity entity)
			throws TrustRepositoryException {
		
		ITrustedEntity result = null;
		final Session session = sessionFactory.openSession();
		final Transaction transaction = session.beginTransaction();
		try {
			result = (ITrustedEntity) session.merge(entity);
			transaction.commit();
		} catch (Exception e) {
			LOG.warn("Rolling back transaction for entity " + entity);
			transaction.rollback();
			throw new TrustRepositoryException("Could not add entity " + entity
					+ ": " + e.getLocalizedMessage(), e);
		} finally {
			if (session != null)
				session.close();
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.societies.privacytrust.trust.api.repo.ITrustRepository#removeEntity(org.societies.api.internal.privacytrust.trust.model.TrustedEntityId)
	 */
	@Override
	public void removeEntity(final TrustedEntityId teid)
			throws TrustRepositoryException {

		if (teid == null)
			throw new NullPointerException("teid can't be null");
		
		final ITrustedEntity entity = this.retrieveEntity(teid);
		if (entity == null)
			return;
		
		final Session session = sessionFactory.openSession();
		final Transaction transaction = session.beginTransaction();
		try {
			session.delete(entity);
			transaction.commit();
		} catch (Exception e) {
			LOG.warn("Rolling back transaction for entity " + entity);
			transaction.rollback();
			throw new TrustRepositoryException("Could not remove entity " + entity
					+ ": " + e.getLocalizedMessage(), e);
		} finally {
			if (session != null)
				session.close();
		}
	}
}