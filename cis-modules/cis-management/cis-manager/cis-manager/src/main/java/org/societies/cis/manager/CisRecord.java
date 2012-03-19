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


package org.societies.cis.manager;

/**
 * Stores meta data relevant for a CIS.
 * 
 * @author Babak Farshchian
 * @version 0
 */


import java.util.Set;

import org.societies.api.cis.collaboration.IServiceSharingRecord;



public class CisRecord {
	public CisActivityFeed feed;
	public String ownerCss;
	public int membershipCriteria;
	public String cisName;
	public String cisJID;
	public String cisType;


	/**
	 * permaLink is a permanent URL to this CIS. A type of CIS homepage.
	 */
	public String permaLink;
	public Set<CisParticipant> membersCss;
	private String password = "none";
	private String host = "none";
	public Set<IServiceSharingRecord> sharedServices;
	

	
	public CisRecord(CisActivityFeed feed, String ownerCss,
			int membershipCriteria, String cisId, String permaLink,
			Set<CisParticipant> membersCss, String password, String host,
			Set<IServiceSharingRecord> sharedServices) {
		super();
		this.feed = feed;
		this.ownerCss = ownerCss;
		this.membershipCriteria = membershipCriteria;
		this.cisName = cisId;
		this.permaLink = permaLink;
		this.membersCss = membersCss;
		this.password = password;
		this.host = host;
		this.sharedServices = sharedServices;
		
		this.cisJID = cisId + "." + host;
		
		this.cisType = "default";
	}
	

	
	public CisRecord(CisActivityFeed feed, String ownerCss,
			int membershipCriteria, String cisJid, String permaLink,
			Set<CisParticipant> membersCss, String password,
			Set<IServiceSharingRecord> sharedServices, String cisType, String cisName) {
		super();
		this.feed = feed;
		this.ownerCss = ownerCss;
		this.membershipCriteria = membershipCriteria;
		this.cisName = cisName;
		this.permaLink = permaLink;
		this.membersCss = membersCss;
		this.password = password;
		this.host = host;
		this.sharedServices = sharedServices;
		
		this.cisJID = cisJid;
		
		this.cisType = cisType;
	}
	

	 // hash code and equals using cisName and host

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cisName == null) ? 0 : cisName.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CisRecord other = (CisRecord) obj;
		if (cisName == null) {
			if (other.cisName != null)
				return false;
		} else if (!cisName.equals(other.cisName))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		return true;
	}


	public String getOwnerCss() {
		return ownerCss;
	}


	public void setOwnerCss(String ownerCss) {
		this.ownerCss = ownerCss;
	}


	public String getCisName() {
		return cisName;
	}


	public void setCisName(String cisId) {
		// TODO: double check that this is consistent with the fulljid
		this.cisName = cisId;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}



	public String getCisJID() {
		return cisJID;
	}

	public void setCisJID(String fullJid) {
		this.cisJID = fullJid;
	}

	

	

}