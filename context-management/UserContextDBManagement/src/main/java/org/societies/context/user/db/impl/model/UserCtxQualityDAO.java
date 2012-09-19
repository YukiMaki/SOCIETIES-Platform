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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.societies.api.context.model.CtxIdentifier;

/**
 * Describe your class here...
 *
 * @author Pavlos Kosmides
 *
 */
@Entity
@Table(name = "qoc")
public class UserCtxQualityDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	private CtxIdentifier attributeId;
	private Date timestamp;
	private String origin;
	private Double precision;
	private Double updateFrequency;
	
	private UserCtxAttributeDAO attribute;

	/**
	 * @param timestamp
	 * @param origin
	 * @param precision
	 * @param updateFrequency
	 */
	public UserCtxQualityDAO(Date timestamp, String origin, Double precision, Double updateFrequency) {
		super();

		this.timestamp = timestamp;
		this.origin = origin;
		this.precision = precision;
		this.updateFrequency = updateFrequency;
	}

	/**
	 * 
	 */
	public UserCtxQualityDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@OneToOne(mappedBy="quality", optional=false, fetch=FetchType.LAZY)
	   public UserCtxAttributeDAO getAttribute() {
		return this.attribute;
	}

	public void setAttribute(UserCtxAttributeDAO attribute) {
		this.attribute = attribute;
	}

	@Id
	@Type(type="org.societies.context.user.db.impl.model.hibernate.CtxAttributeIdentifierType")
	@GeneratedValue(generator="foreign")
	@GenericGenerator(name="foreign", strategy="foreign", parameters={
			@Parameter(name="property", value="attribute")
	})
	public CtxIdentifier getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(CtxIdentifier attributeId) {
		this.attributeId = attributeId;
	}

	@Column(name = "timestamp")
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Column(name = "origin")
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Column(name = "precision_")
	public Double getPrecision() {
		return precision;
	}
	public void setPrecision(Double precision) {
		this.precision = precision;
	}

	@Column(name = "update_frequency")
	public Double getUpdateFrequency() {
		return updateFrequency;
	}
	public void setUpdateFrequency(Double updateFrequency) {
		this.updateFrequency = updateFrequency;
	}

}