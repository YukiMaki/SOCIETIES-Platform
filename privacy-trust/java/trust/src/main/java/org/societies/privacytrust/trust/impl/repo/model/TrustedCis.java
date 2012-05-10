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
package org.societies.privacytrust.trust.impl.repo.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.societies.api.internal.privacytrust.trust.model.TrustedEntityId;
import org.societies.privacytrust.trust.api.model.ITrustedCis;
import org.societies.privacytrust.trust.api.model.ITrustedCss;

/**
 * This class represents trusted CISs. A <code>TrustedCis</code> object is
 * referenced by its {@link TrustedEntityId}, while the associated {@link Trust}
 * value objects express the trustworthiness of this community, i.e. direct, 
 * indirect and user-perceived. Each trusted CIS is assigned a set of 
 * {@link TrustedCss} objects, which represent its members.
 * 
 * @author <a href="mailto:nicolas.liampotis@cn.ntua.gr">Nicolas Liampotis</a> (ICCS)
 * @since 0.0.1
 */
@Entity
@Table(name = TableName.TRUSTED_CIS, uniqueConstraints={@UniqueConstraint(columnNames={"trustorId", "trusteeId"})})
public class TrustedCis extends TrustedEntity implements ITrustedCis {

	private static final long serialVersionUID = -438368876927927076L;
	
	/** The members of this trusted CIS. */
	@ManyToMany(
	        mappedBy = "communities",
	        targetEntity = TrustedCss.class,
	        fetch = FetchType.EAGER
	)
	//@Fetch(FetchMode.JOIN)
	private Set<ITrustedCss> members = new HashSet<ITrustedCss>();
	
	/* TODO The services shared by this trusted CIS. */
	// private Set<TrustedService> services = new CopyOnWriteArraySet<TrustedService>();

	/* Empty constructor required by Hibernate */
	private TrustedCis() {
		
		super(null);
	}
	
	/**
	 * @param teid
	 */
	public TrustedCis(final TrustedEntityId teid) {
		
		super(teid);
	}

	/*
	 * @see org.societies.privacytrust.trust.api.model.ITrustedCis#getMembers()
	 */
	@Override
	public Set<ITrustedCss> getMembers() {
		
		return this.members;
	}
	
	/*
	 * @see org.societies.privacytrust.trust.api.model.ITrustedCis#addMember(org.societies.privacytrust.trust.api.model.TrustedCss)
	 */
	@Override
	public void addMember(final ITrustedCss member) {
		
		if (!this.members.contains(member))
			this.members.add(member);
		
		if (!member.getCommunities().contains(this))
			member.getCommunities().add(this);
	}
	
	/*
	 * @see org.societies.privacytrust.trust.api.model.ITrustedCis#removeMember(org.societies.privacytrust.trust.api.model.ITrustedCss)
	 */
	@Override
	public void removeMember(final ITrustedCss member) {
		
		if (this.members.contains(member))
			this.members.remove(member);
		
		if (member.getCommunities().contains(this))
			member.getCommunities().remove(this);
	}
     
	/*
	 * TODO			
	 *
	public Set<TrustedService> getServices() {
		return this.services;
	}

	/**
	 * 
	 * @param s
	 *
	public Set<TrustedService> getServices(String serviceType) {
		return null;
	}*/
}