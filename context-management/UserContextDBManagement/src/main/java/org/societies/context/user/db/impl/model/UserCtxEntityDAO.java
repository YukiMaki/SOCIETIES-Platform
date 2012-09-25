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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.MapKey;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.societies.api.context.model.CtxEntity;
import org.societies.api.context.model.CtxEntityIdentifier;
import org.societies.api.context.model.CtxIdentifier;

/**
 * Describe your class here...
 *
 * @author Pavlos Kosmides
 *
 */
@NamedQueries({
	@NamedQuery(
	name = "getCtxEntityIdsByType",
	query = "select entity.id from UserCtxEntityDAO as entity where entity.ctxIdentifier.type = :type"
	),
	@NamedQuery(
	name = "getCtxEntityIdsByAttrType",
	query = "select distinct entity.ctxIdentifier from UserCtxEntityDAO as entity inner join entity.attrScope as attribute " +
			"where entity.ctxIdentifier.type = :entType " +
			"and attribute.ctxIdentifier.type = :attrType"
	),
	@NamedQuery(
	name = "getCtxEntityIdsByAttrStringValue",
	query = "select distinct entity.id from UserCtxEntityDAO as entity inner join entity.attrScope as attribute " +
			"where entity.ctxIdentifier.type = :entType " +
			"and attribute.ctxIdentifier.type = :attrType " +
			"and attribute.valueStr between :minAttribValue and :maxAttribValue"
	),
	@NamedQuery(
	name = "getCtxEntityIdsByAttrIntegerValue",
	query = "select distinct entity.id from UserCtxEntityDAO as entity inner join entity.attrScope as attribute " +
			"where entity.ctxIdentifier.type = :entType " +
			"and attribute.ctxIdentifier.type = :attrType " +
			"and attribute.valueInt between :minAttribValue and :maxAttribValue"
	),
	@NamedQuery(
	name = "getCtxEntityIdsByAttrBlobValue",
	query = "select distinct entity.id from UserCtxEntityDAO as entity inner join entity.attrScope as attribute " +
			"where entity.ctxIdentifier.type = :entType " +
			"and attribute.ctxIdentifier.type = :attrType " +
			"and attribute.valueBlob = :minAttribValue"
	),
	@NamedQuery(
	name = "getCtxEntityIdsByAttrDoubleValue",
	query = "select distinct entity.id from UserCtxEntityDAO as entity inner join entity.attrScope as attribute " +
			"where entity.ctxIdentifier.type = :entType " +
			"and attribute.ctxIdentifier.type = :attrType " +
			"and attribute.valueDbl between :minAttribValue and :maxAttribValue"
	)
})
@Entity
@Table(name = "entities")
@MappedSuperclass
public class UserCtxEntityDAO extends CtxEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private static CtxIdentifier entityId;

	private Date lastModified;
	private Set<UserCtxAttributeDAO> attrScope = new HashSet<UserCtxAttributeDAO>(0);
	private Set<String> map;
	private UserCtxEntityIdentifierDAO ctxIdentifier;

	/** 
	 * @param entityId
	 * @param timestamp
	 */
	public UserCtxEntityDAO(CtxIdentifier entityId, UserCtxEntityIdentifierDAO ctxIdentifier, Date lastModified) {

		super((CtxEntityIdentifier) entityId);
		
		this.entityId = entityId;
		this.ctxIdentifier = ctxIdentifier;
		this.lastModified = lastModified;
	}
	
	public UserCtxEntityDAO(CtxIdentifier entityId, Set<UserCtxAttributeDAO> attrScope) {

		super((CtxEntityIdentifier) entityId);
		
		this.entityId = entityId;
		this.attrScope = attrScope;
	}

	/**
	 * 
	 */
	public UserCtxEntityDAO() {
		super((CtxEntityIdentifier) entityId);
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name="entity_id", unique = true, nullable = false)
	@Type(type="org.societies.context.user.db.impl.model.hibernate.CtxEntityIdentifierType")
	public CtxIdentifier getEntityId() {
		return entityId;
	}

	public void setEntityId(CtxIdentifier entityId) {
		this.entityId = entityId;
	}

	@Column(name = "lastModified")
//	@Temporal(value=TemporalType.TIMESTAMP)
//	@org.hibernate.annotations.Generated(value=GenerationTime.ALWAYS)
	@Version
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ctxIdentifier.scope")
	public Set<UserCtxAttributeDAO> getAttrScope() {
		return this.attrScope;
	}
 
	public void setAttrScope(Set<UserCtxAttributeDAO> attrScope) {
		this.attrScope = attrScope;
	}
		
	@CollectionOfElements
	@JoinTable(name="assoc_entities",
	  joinColumns = @JoinColumn(name="association_id"))
	@MapKey(columns={@Column(name="entity_id")})
	@Column(name="entity_id")
	public Set<String> getMap() {
	  return this.map;
	}
	public void setMap(Set<String> map) {
		  this.map = map;
	}
	
	@Embedded
	public UserCtxEntityIdentifierDAO getCtxIdentifier() {
		return ctxIdentifier;
	}
	public void setCtxIdentifier (UserCtxEntityIdentifierDAO ctxIdentifier) {
		this.ctxIdentifier = ctxIdentifier;
	}
}