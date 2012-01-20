/**
 * Copyright (c) 2011, SOCIETIES Consortium (WATERFORD INSTITUTE OF TECHNOLOGY (TSSG), HERIOT-WATT UNIVERSITY (HWU), SOLUTA.NET 
 * (SN), GERMAN AEROSPACE CENTRE (Deutsches Zentrum fuer Luft- und Raumfahrt e.V.) (DLR), Zavod za varnostne tehnologije
 * informacijske družbe in elektronsko poslovanje (SETCCE), INSTITUTE OF COMMUNICATION AND COMPUTER SYSTEMS (ICCS), LAKE
 * COMMUNICATIONS (LAKE), INTEL PERFORMANCE LEARNING SOLUTIONS LTD (INTEL), PORTUGAL TELECOM INOVA��O, SA (PTIN), IBM ISRAEL
 * SCIENCE AND TECHNOLOGY LTD (IBM), INSTITUT TELECOM (ITSUD), AMITEC DIACHYTI EFYIA PLIROFORIKI KAI EPIKINONIES ETERIA
 * PERIORISMENIS EFTHINIS (AMITEC), TELECOM ITALIA S.p.a.(TI),  TRIALOG (TRIALOG), Stiftelsen SINTEF (SINTEF), NEC EUROPE LTD
 * (NEC))
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

package org.societies.comm.xmpp.datatypes;

/**
 * @author Joao M. Goncalves (PTIN)
 * 
 * TODO
 * 
 */

public class Identity {
	protected IdentityType type;
	protected String identifier;
	protected String domainIdentifier;
	
	public static Identity fromJid(String jid) {
		String[] parts = jid.split("@|/");
		switch (parts.length) {
			case 1:
				int firstDot = jid.indexOf(".");
				return new Identity(IdentityType.CIS, jid.substring(0,firstDot), jid.substring(firstDot+1));
			case 2:
				return new Identity(IdentityType.CSS, parts[0], parts[1]);
			case 3:
				return new Endpoint(IdentityType.CSS, parts[0], parts[1], parts[2]);
			default:
				return null;
		}
	}
	
	public Identity(IdentityType type, String identifier, String domainIdentifier) {
		this.type = type;
		this.identifier = identifier;
		this.domainIdentifier = domainIdentifier;
	}


	@Override
	public String toString() {
		return getJid();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((domainIdentifier == null) ? 0 : domainIdentifier.hashCode());
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Identity))
			return false;
		Identity other = (Identity) obj;
		if (domainIdentifier == null) {
			if (other.domainIdentifier != null)
				return false;
		} else if (!domainIdentifier.equals(other.domainIdentifier))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	public enum IdentityType {
		CSS,
		CIS;
	}
	
	public IdentityType getType() {
		return type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getDomainIdentifier() {
		return domainIdentifier;
	}
	
	public String getJid(){
		if (type.equals(IdentityType.CSS))
			return identifier+"@"+domainIdentifier;
		else
			return identifier+"."+domainIdentifier;
	}
	
}