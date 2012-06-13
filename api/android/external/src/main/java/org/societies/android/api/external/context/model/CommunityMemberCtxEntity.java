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
package org.societies.android.api.external.context.model;

import java.util.HashSet;
import java.util.Set;

/**
 * This abstract class is used in order to represent members of a
 * {@link CommunityCtxEntity} (CIS). A <code>CommunityMemberCtxEntity</code>
 * can be an individual or a sub-community, hence, there are two concrete
 * implementations of this class, namely {@link IndividualCtxEntity} and
 * {@link CommunityCtxEntity}. A CommunityMemberCtxEntity may belong to
 * multiple communities, simultaneously. This class provides methods for
 * accessing and modifying these communities.
 * 
 * @see CtxEntityIdentifier
 * @author <a href="mailto:nicolas.liampotis@cn.ntua.gr">Nicolas Liampotis</a> (ICCS)
 * @since 0.0.1
 */
public abstract class CommunityMemberCtxEntity extends CtxEntity {
	
	private static final long serialVersionUID = 3614587369237968591L;
	
	/** The communities this entity is member of. */
	private Set<CommunityCtxEntity> communities = new HashSet<CommunityCtxEntity>();
	
	CommunityMemberCtxEntity(CtxEntityIdentifier id) {
		
		super(id);
	}

	/**
	 * Returns a set with the community members.
	 * 
	 * @return set CommunityCtxEntity
	 */
	public Set<CommunityCtxEntity> getCommunities() {
		
		return new HashSet<CommunityCtxEntity>(this.communities);
	}
	
	/**
	 * Add a CommunityCtxEntity to the community
	 * 
	 * @param community
	 */
	public void addCommunity(CommunityCtxEntity community) {
		
		this.communities.add(community);
	}
	
	/**
	 * Remove a CommunityCtxEntity from the community.
	 * 
	 * @param community
	 */
	public void removeCommunity(CommunityCtxEntity community) {
		
		this.communities.remove(community);
	}
}