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
package org.societies.privacytrust.trust.api.model;

import java.io.Serializable;

/**
 * This abstract class is used to represent an entity trusted by the trustor,
 * i.e. the owner of a CSS. Each trusted entity is referenced by its
 * {@link TrustedEntityId}, while the associated {@link Trust} objects express
 * the trustworthiness of that entity, i.e. direct, indirect and user-perceived
 * trust.
 * 
 * @author <a href="mailto:nicolas.liampotis@cn.ntua.gr">Nicolas Liampotis</a> (ICCS)
 * @since 0.0.1 
 */
public abstract class TrustedEntity implements Serializable {
	
	private static final long serialVersionUID = -495088232194787430L;

	/** The identifier of the trustor. */
	private final TrustedEntityId trustor;
	
	/** The identifier of this trusted entity. */
	private final TrustedEntityId teid;
	
	private DirectTrust directTrust ;
	private IndirectTrust indirectTrust ;
	private UserPerceivedTrust userPerceivedTrust;

	TrustedEntity(TrustedEntityId trustor, TrustedEntityId teid) {
		
		this.trustor = trustor;
		this.teid = teid;
	}
	
	/**
	 * Returns the identifier of the trustor.
	 * 
	 * @return the identifier of the trustor.
	 */
	public TrustedEntityId getTrustor() {
		
		return this.trustor;
	}
	
	/**
	 * Returns the identifier of this trusted entity.
	 * 
	 * @return the identifier of this trusted entity.
	 */
	public TrustedEntityId getId() {
		
		return this.teid;
	}
	
	/**
	 * 
	 * @return
	 * @since 0.0.3
	 */
	public DirectTrust getDirectTrust() {
		
		return this.directTrust;
	}
	
	/**
	 * 
	 * @param directTrust
	 * @since 0.0.3
	 */
	public void setDirectTrust(DirectTrust directTrust) {
		
		this.directTrust = directTrust;
	}
	
	/**
	 * 
	 * @return
	 * @since 0.0.3
	 */
	public IndirectTrust getIndirectTrust() {
		
		return this.indirectTrust;
	}
	
	/**
	 * 
	 * @param indirectTrust
	 * @since 0.0.3
	 */
	public void setIndirectTrust(IndirectTrust indirectTrust) {
		
		this.indirectTrust = indirectTrust;
	}
	
	/**
	 * 
	 * @return
	 * @since 0.0.3
	 */
	public UserPerceivedTrust getUserPerceivedTrust() {
		
		return this.userPerceivedTrust;
	}
	
	/**
	 * 
	 * @param userPerceivedTrust
	 * @since 0.0.3
	 */
	public void setUserPerceivedTrust(UserPerceivedTrust userPerceivedTrust) {
		
		this.userPerceivedTrust = userPerceivedTrust;
	}
}