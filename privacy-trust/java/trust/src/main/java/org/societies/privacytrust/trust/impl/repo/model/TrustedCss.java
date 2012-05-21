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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.societies.api.internal.privacytrust.trust.model.TrustedEntityId;
import org.societies.privacytrust.trust.api.model.ITrustedCis;
import org.societies.privacytrust.trust.api.model.ITrustedCss;
import org.societies.privacytrust.trust.api.model.ITrustedService;

/**
 * This class represents trusted CSSs. A <code>TrustedCss</code> object is
 * referenced by its {@link TrustedEntityId}, while the associated 
 * {@link Trust} value objects express the trustworthiness of this CSS, i.e.
 * direct, indirect and user-perceived. Each trusted CSS is assigned a set of
 * {@link TrustedCis} objects representing the communities this CSS is member
 * of. In addition, the services provided by a TrustedCss are modelled as
 * {@link TrustedService} objects.
 * 
 * @author <a href="mailto:nicolas.liampotis@cn.ntua.gr">Nicolas Liampotis</a> (ICCS)
 * @since 0.0.1
 */
@Entity
@Table(
		name = TableName.TRUSTED_CSS, 
		uniqueConstraints = { @UniqueConstraint(columnNames = { "trustor_id", "trustee_id" }) }
)
public class TrustedCss extends TrustedEntity implements ITrustedCss {
	
	private static final long serialVersionUID = 6564159563124215460L;
	
	/** The communities this CSS is member of. */
	@ManyToMany(
			cascade = CascadeType.MERGE,
			targetEntity = TrustedCis.class,
			fetch = FetchType.EAGER
	)
	@JoinTable(
			name = TableName.TRUSTED_CSS_CIS, 
			joinColumns = { @JoinColumn(name = TableName.TRUSTED_CSS + "_id") }, 
			inverseJoinColumns = { @JoinColumn(name = TableName.TRUSTED_CIS + "_id") }
	)
	private final Set<ITrustedCis> communities = new HashSet<ITrustedCis>();
	
	/** The services provided by this CSS. */
	@OneToMany(
			cascade = CascadeType.REMOVE,
			mappedBy = "provider",
			targetEntity = TrustedService.class,
			fetch = FetchType.EAGER
	)
	private final Set<ITrustedService> services = new HashSet<ITrustedService>();

	/* Empty constructor required by Hibernate */
	private TrustedCss() {
		
		super(null);
	}
	
	/**
	 * Constructs a <code>TrustedCss</code> with the specified identifier.
	 * 
	 * @param teid
	 *            the identifier of the new <code>TrustedCss</code>
	 */
	public TrustedCss(final TrustedEntityId teid) {
		
		super(teid);
	}

	/*
	 * @see org.societies.privacytrust.trust.api.model.ITrustedCss#getCommunities()
	 */
	@Override
	public Set<ITrustedCis> getCommunities(){
		
		return this.communities;
	}
	
	/*
	 * @see org.societies.privacytrust.trust.api.model.ITrustedCss#addCommunity(org.societies.privacytrust.trust.api.model.ITrustedCis)
	 */
	@Override
	public void addCommunity(final ITrustedCis community) {
		
		if (!this.communities.contains(community))
			this.communities.add(community);
	
		if (!community.getMembers().contains(this))
			community.getMembers().add(this);
	}
	
	/*
	 * @see org.societies.privacytrust.trust.api.model.ITrustedCss#removeCommunity(org.societies.privacytrust.trust.api.model.ITrustedCis)
	 */
	@Override
	public void removeCommunity(final ITrustedCis community) {
		
		if (this.communities.contains(community))
			this.communities.remove(community);
		
		if (community.getMembers().contains(this))
			community.getMembers().remove(this);
	}

	/*
	 * @see org.societies.privacytrust.trust.api.model.ITrustedCss#getServices()
	 */
	@Override
	public Set<ITrustedService> getServices() {
		
		return this.services;
	}
	
	/*
	 * TODO 
	 * @param serviceType
	 *
	public Set<TrustedService> getServices(String serviceType) {
		return null;
	}*/
}