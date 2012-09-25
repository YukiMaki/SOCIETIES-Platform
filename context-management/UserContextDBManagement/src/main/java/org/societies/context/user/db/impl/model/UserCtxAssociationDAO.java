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
package org.societies.context.user.db.impl.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.MapKey;
import org.hibernate.annotations.Type;
import org.societies.api.context.model.CtxAssociation;
import org.societies.api.context.model.CtxAssociationIdentifier;
import org.societies.api.context.model.CtxEntityIdentifier;
import org.societies.api.context.model.CtxIdentifier;
import org.societies.context.user.db.impl.model.hibernate.CtxAssociationIdentifierType;
import org.societies.context.user.db.impl.model.hibernate.CtxEntityIdentifierType;

/**
 * Describe your class here...
 *
 * @author Pavlos Kosmides
 *
 */
@NamedQueries({
	@NamedQuery(
	name = "getCtxAssociationIdsByType",
	query = "select association.associationId from UserCtxAssociationDAO as association where association.ctxIdentifier.type = :type"
	)
})

@Entity
@Table(name = "associations")
public class UserCtxAssociationDAO extends CtxAssociation implements Serializable {

	private static final long serialVersionUID = 1L;

	private CtxIdentifier associationId;
	private Date lastModified;
	private CtxIdentifier parentEntityId;
	private boolean dynamic;
	private UserCtxAssociationIdentifierDAO ctxIdentifier;
	private Set<CtxEntityIdentifier> map;
	
	/**
	 * @param associationId
	 * @param timestamp 
	 * @param parentEntityId 
	 * @param dynamic 
	 * @param ctxIdentifier
	 *
	 */
	public UserCtxAssociationDAO(CtxIdentifier associationId, Date lastModified, CtxIdentifier parentEntityId, boolean dynamic, UserCtxAssociationIdentifierDAO ctxIdentifier) {

		super((CtxAssociationIdentifier) associationId);

		this.associationId = associationId;
		this.lastModified = lastModified;
		this.parentEntityId = parentEntityId;
		this.dynamic = dynamic;
		this.ctxIdentifier = ctxIdentifier;

	}

	/**
	 * 
	 */
	public UserCtxAssociationDAO() {
		super(null);
		// TODO Auto-generated constructor stub
	}

	@Column(name = "lastModified")
	@Version
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	@Column(name = "parent_entity_id")
	@Type(type="org.societies.context.user.db.impl.model.hibernate.CtxEntityIdentifierType")
	public CtxIdentifier getParentEntityId() {
		return parentEntityId;
	}
	public void setParentEntityId(CtxIdentifier parentEntityId) {
		this.parentEntityId = parentEntityId;
	}

	@Column(name = "dynamic")
	public boolean getDynamic() {
		return dynamic;
	}
	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	@Id
	@Column(name="association_id")
	@Type(type="org.societies.context.user.db.impl.model.hibernate.CtxAssociationIdentifierType")
	public CtxIdentifier getAssociationId() {
		return associationId;
	}

	public void setAssociationId(CtxIdentifier associationId) {
		this.associationId = associationId;
	}

	@Embedded
	public UserCtxAssociationIdentifierDAO getCtxIdentifier() {
		return this.ctxIdentifier;
	}
	public void setCtxIdentifier(UserCtxAssociationIdentifierDAO ctxIdentifier) {
		this.ctxIdentifier = ctxIdentifier;
	}

	@CollectionOfElements (targetElement = CtxEntityIdentifierType.class)
	@JoinTable(name="assoc_entities",
	  joinColumns = @JoinColumn(name="entity_id"))
	@MapKey(columns={@Column(name="association_id")}, targetElement = CtxAssociationIdentifierType.class)
	@Column(name="association_id")
	public Set<CtxEntityIdentifier> getMap() {
	  return this.map;
	}
	public void setMap(Set<CtxEntityIdentifier> map) {
		  this.map = map;
	}
}
