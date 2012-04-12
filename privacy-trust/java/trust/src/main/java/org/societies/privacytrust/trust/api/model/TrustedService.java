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

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This class represents trusted services. A TrustedService object is referenced
 * by its TrustedEntityId, while the associated Trust value objects express the
 * trustworthiness of this service, i.e. direct, indirect and user-perceived. Each
 * trusted service is also associated with a TrustedCSS which represents its
 * provider.
 * 
 * @author <a href="mailto:nicolas.liampotis@cn.ntua.gr">Nicolas Liampotis</a> (ICCS)
 * @since 0.0.1
 */
@Entity
@Table(name="t_services")
public class TrustedService extends TrustedEntity {

	private static final long serialVersionUID = 8253551733059925542L;
	
	/** The CSS providing this service. */
	private final TrustedCss provider;
	
	/* The communities sharing this service. */
	//private final Set<TrustedCis> communities = new CopyOnWriteArraySet<TrustedCis>();
	
	/** The type of this service. */
	private final String type;
	
	/** The developer of this service. */
	private TrustedDeveloper developer;

	public TrustedService(TrustedEntityId trustor, TrustedEntityId teid, String type, TrustedCss provider) {
		
		super(trustor, teid);
		this.type = type;
		this.provider = provider;
	}

	/**
	 * Returns the type of this service.
	 * 
	 * @return the type of this service.
	 */
	public String getType() {
		
		return this.type;
	}
	
	/**
	 * Returns the CSS providing this service.
	 * 
	 * @return the CSS providing this service.
	 */
	public TrustedCss getProvider() {
		
		return this.provider;
	}
	
	/** 
	 * Returns the developer of this service.
	 * 
	 * @return the developer of this service.
	 */
	public TrustedDeveloper getDeveloper() {
		
		return this.developer;
	}
	
	/**
	 * Sets the developer of this service.
	 * 
	 * @param developer the service developer to set. 
	 */
	public void setDeveloper(TrustedDeveloper developer) {
		
		this.developer = developer;
	}
}