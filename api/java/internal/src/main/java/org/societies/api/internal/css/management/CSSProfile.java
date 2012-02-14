/**
Copyright (c) 2011, SOCIETIES Consortium (WATERFORD INSTITUTE OF TECHNOLOGY (TSSG), HERIOT-WATT UNIVERSITY (HWU), SOLUTA.NET 

(SN), GERMAN AEROSPACE CENTRE (Deutsches Zentrum fuer Luft- und Raumfahrt e.V.) (DLR), Zavod za varnostne tehnologije
informacijske družbe in elektronsko poslovanje (SETCCE), INSTITUTE OF COMMUNICATION AND COMPUTER SYSTEMS (ICCS), LAKE
COMMUNICATIONS (LAKE), INTEL PERFORMANCE LEARNING SOLUTIONS LTD (INTEL), PORTUGAL TELECOM INOVAÇÃO, SA (PTIN), IBM Corp (IBM),
INSTITUT TELECOM (ITSUD), AMITEC DIACHYTI EFYIA PLIROFORIKI KAI EPIKINONIES ETERIA PERIORISMENIS EFTHINIS (AMITEC), TELECOM 
ITALIA S.p.a.(TI), TRIALOG (TRIALOG), Stiftelsen SINTEF (SINTEF), NEC EUROPE LTD (NEC))
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.societies.api.internal.css.management;

/**
 * Defines a CSS profile
 * 
 * In order to allow for Android compatibility, the natural Enum types 
 * are int types. 
 */
public class CSSProfile {
	/**
	 * Enum for CSS status
	 */
	public enum cssStatus {
		Active("Active"), 
		Inactive("Inactive");
	
	    private String name;

	    cssStatus(String name) {
	        this.name = name;
	    }

	    public String getName() {
	        return this.name;
	    }

	};
	/**
	 * Enum for entity types
	 */
	public enum entityType {
		Person("Personal CSS"), 
		Organisation("Organisational CSS");
		
		
	    private String name;

	    entityType(String name) {
	        this.name = name;
	    }

	    public String getName() {
	        return this.name;
	    }

	};
	/**
	 * Enum for gender types
	 */
	public enum genderType {
		Male("Male"), 
		Female("Female"), 
		Unspecified("Unspecified");
		
	    private String name;

	    genderType(String name) {
	        this.name = name;
	    }

	    public String getName() {
	        return this.name;
	    }
	
	};
	/**
	 * Enum for presence types
	 */
	public enum presenceType {
		Available("Available"), 
		DoNotDisturb("Do not disturb"), 
		Offline("Offline"), 
		Away("Away"), 
		ExtendedAway("Extended Away");
		
	    private String name;

	    presenceType(String name) {
	        this.name = name;
	    }

	    public String getName() {
	        return this.name;
	    }
	
	};
	
	
	/**
	 * is the CSS a person or organisation ?
	 */
	int entity = 0;
	/**
	 * used for personal CSS
	 */
	String foreName = null;
	/**
	 * used for personal surname or organisation's name
	 */
	String name = null;
	/**
	 * CSS name
	 * Will be required to be unique
	 */
	String identityName = null;
	/**
	 * password 
	 * Will be required to one way encrypted e.g. SHA-1
	 */
	String password = null;
	/**
	 * e-mail account
	 */
	String emailID = null;
	/**
	 * Instant messaging ID
	 */
	String imID = null;
	/**
	 * Social Network URI
	 */
	String socialURI = null;
	/**
	 * Gender of person
	 */
	int sex = 0;
	/**
	 * Home or default location
	 */
	String homeLocation = null;
	
	/**
	 * CSS UID 
	 */
	String cssIdentity = null;
	/**
	 * Current list of node IDs that constitute a CSS
	 */
	CSSNode cssNodes[] = null;
	/**
	 * Status of CSS
	 */
	int status = 0;
	/**
	 * Date of CSS registration
	 */
	String cssRegistration = null;
	/**
	 * Date of CSS inactivation
	 */
	String cssInactivation = null;
	/**
	 * Number of minutes that the CSS has been logged in
	 */
	int cssUpTime = 0;
	
//	/**
//	 * List of CIS that the CSS has participated in
//	 * TODO Requires CIS generic data type
//	 */
//	List encounteredCIS = null;
	/**
	 * Array of nodes that have participated in the CSS
	 */
	CSSNode archiveCSSNodes[] = null;
	/**
	 * Presence status user
	 */
	int presence = 0;
	
	/**
	 * Default Contructor
	 */
	public CSSProfile() {
		
	}

	public int getEntity() {
		return entity;
	}

	public void setEntity(int entity) {
		this.entity = entity;
	}

	public String getForeName() {
		return foreName;
	}

	public void setForeName(String foreName) {
		this.foreName = foreName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentityName() {
		return identityName;
	}

	public void setIdentityName(String identityName) {
		this.identityName = identityName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getImID() {
		return imID;
	}

	public void setImID(String imID) {
		this.imID = imID;
	}

	public String getSocialURI() {
		return socialURI;
	}

	public void setSocialURI(String socialURI) {
		this.socialURI = socialURI;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getHomeLocation() {
		return homeLocation;
	}

	public void setHomeLocation(String homeLocation) {
		this.homeLocation = homeLocation;
	}

	public String getCssIdentity() {
		return cssIdentity;
	}

	public void setCssIdentity(String cssIdentity) {
		this.cssIdentity = cssIdentity;
	}

	public CSSNode[] getCssNodes() {
		return cssNodes;
	}

	public void setCssNodes(CSSNode[] cssNodes) {
		this.cssNodes = cssNodes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCssRegistration() {
		return cssRegistration;
	}

	public void setCssRegistration(String cssRegistration) {
		this.cssRegistration = cssRegistration;
	}

	public String getCssInactivation() {
		return cssInactivation;
	}

	public void setCssInactivation(String cssInactivation) {
		this.cssInactivation = cssInactivation;
	}

	public int getCssUpTime() {
		return cssUpTime;
	}

	public void setCssUpTime(int cssUpTime) {
		this.cssUpTime = cssUpTime;
	}

	public CSSNode[] getArchiveCSSNodes() {
		return archiveCSSNodes;
	}

	public void setArchiveCSSNodes(CSSNode[] archiveCSSNodes) {
		this.archiveCSSNodes = archiveCSSNodes;
	}

	public int getPresence() {
		return presence;
	}

	public void setPresence(int presence) {
		this.presence = presence;
	}



}