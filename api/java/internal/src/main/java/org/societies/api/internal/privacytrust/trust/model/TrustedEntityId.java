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
package org.societies.api.internal.privacytrust.trust.model;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class is used to uniquely identify an {@link ITrustedEntity}. Trusted
 * Entity Identifiers (TEIDs) are formatted as Uniform Name Numbers (URNs). A
 * URN is composed of the Namespace Identifier (NID) and the Namespace Specific
 * String (NSS). More specifically, using EBNF notation, a URN has the form:
 * 
 * <pre>
 * urn = "urn:" , nid , ":" , nss ;
 * </pre>
 * 
 * The structure of a TEID has the form:
 * 
 * <pre> 
 * teid = "urn:" , "teid" , ":" , ? trustor-id ? , ? entity-type ? , ":" , ? trustee-id ? ;     
 * </pre>
 * where <code>"teid"</code> is the NID of all TEID URNs, while the NSS is
 * composed of the <code>trustor-id, the <code>entity-type</code> and the
 * <code>trustee-id</code>:
 * <dl>
 * <dt><code>trustor-id</code></dt>
 * <dd>A unique identifier of the trustor, i.e. the individual who places trust
 * in the referenced entity, available through {@link #getTrustorId()}.</dd>
 * <dt><code>entity-type<code></dt>
 * <dd>Denotes the type of the identified trusted entity and can be retrieved
 * using {@link #getEntityType()}. All available trusted entity types are
 * defined in {@link TrustedEntityType}.</dd>
 * <dt><code>trustee-id</code></dt>
 * <dd>A unique identifier of the trustee, i.e. the referenced trusted entity, 
 * available through {@link #getTrusteeId()}.</dd>
 * </dl>
 * 
 * @author <a href="mailto:nicolas.liampotis@cn.ntua.gr">Nicolas Liampotis</a> (ICCS)
 * @since 0.0.1 
 */
public class TrustedEntityId implements Serializable {

	private static final long serialVersionUID = 7390835311816850816L;
	
    /** The URI encoding scheme to use for representing trusted entity identifiers. */
    public static final String URI_SCHEME = "urn";
    
    /** The URN delimiter. */
    public static final String URN_DELIM = ":";
    
    /** The URN NID. */
    public static final String URN_NID = "teid";

    /** The String representation of the unique identifier of the trustor. */
    private transient String trustorId;
    
    /** The trusted entity type. */
    private transient TrustedEntityType entityType;
    
    /** The String representation of the unique identifier of the trustee. */
    private transient String trusteeId;
	
    /** The URN-formatted representation of this trusted entity identifier. */
	private volatile URI urn; // The only serialisable field

	/**
	 * Constructs a <code>TrustedEntityId</code> with the specified trustor id,
	 * entity type and trustee id.
	 * 
	 * @param trustorId
	 *            the String representation of the unique identifier of the
	 *            trustor
	 * @param entityType
	 *            the trusted entity type
	 * @param trusteeId
	 *            the String representation of the unique identifier of the
	 *            trustee
	 *        
	 * @throws MalformedTrustedEntityIdException 
	 *             if the URN of this identifier cannot be created
	 */
	public TrustedEntityId(final String trustorId, final TrustedEntityType entityType,
			final String trusteeId) throws MalformedTrustedEntityIdException {
		
		this.trustorId = trustorId;
		this.entityType = entityType;
		this.trusteeId = trusteeId;
		try {
			this.urn = URI.create(URI_SCHEME 
					+ URN_DELIM
					+ URN_NID
					+ URN_DELIM 
					+ trustorId
					+ URN_DELIM 
					+ entityType
					+ URN_DELIM
					+ trusteeId);
		} catch (IllegalArgumentException iae) {
			throw new MalformedTrustedEntityIdException(
					"Could not create trusted entity identifier URN", iae);
		}
	}
	
	/**
	 * Constructs a <code>TrustedEntityId</code> from the specified string
	 * representation. 
	 * 
	 * @param str
	 *            the string representation from which to construct the
	 *            TrustedEntityId instance
	 * @throws MalformedTrustedEntityIdException
	 *            if the specified TrustedEntityId string representation is
	 *            malformed.
	 * @since 0.0.5
	 */
	public TrustedEntityId(String str) throws MalformedTrustedEntityIdException {
		
		this.parseString(str);
	}

	/**
	 * Returns the String representation of the unique identifier of the 
	 * trustor.
	 * 
	 * @return the String representation of the unique identifier of the
	 *         trustor.
	 * @since 0.0.7
	 */
	public String getTrustorId() {
		
		return this.trustorId;
	}
	
	/**
	 * Returns the {@link TrustedEntityType} of the entity referenced by this
	 * identifier.
	 * 
	 * @return the {@link TrustedEntityType} of the entity referenced by this
	 *         trusted entity identifier.
	 * @since 0.0.3
	 */
	public TrustedEntityType getEntityType() {
		
		return this.entityType;
	}
	
	/**
	 * Returns the String representation of the unique identifier of the
	 * trustee.
	 * 
	 * @return the String representation of the unique identifier of the
	 *         trustee.
	 */
	public String getTrusteeId() {
		
		return this.trusteeId;
	}

	/**
	 * Returns a URI representation of this identifier.
     *
     * @return a URI representation of this identifier.
     * @since 0.0.3
	 */
	public URI getUri() {
		
		return this.urn;
	}
	
	/**
	 * Returns a String representation of this identifier. The format of the
	 * returned String is as follows: 
     * <pre>
     * urn:teid:trustorId:entityType:trusteeId
     * </pre>
     * 
     * @return a String representation of this identifier.
	 */
	@Override
	public String toString() {
		
		return this.urn.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		
		final int prime = 31;
		
		int result = 1;
		result = prime * result + ((this.urn == null) ? 0 : this.urn.hashCode());
		
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object that) {
		
		if (this == that)
			return true;
		if (that == null)
			return false;
		if (this.getClass() != that.getClass())
			return false;
		
		TrustedEntityId other = (TrustedEntityId) that;
		if (this.urn == null) {
			if (other.urn != null)
				return false;
		} else if (!this.urn.equals(other.urn))
			return false;
		
		return true;
	}
	
	/**
     * Writes the contents of this TrustedEntityId to the given object output
     * stream.
     * <p> 
     * The only serialisable field of a TrustedEntityId instance is its 
     * {@link #urn} field. That field is given a value, if it does not have one
     * already, and then the {@link java.io.ObjectOutputStream#defaultWriteObject()}
     * method of the given object-output stream is invoked.
     *
     * @param os
     *            the object output stream to which this object is to be written
     */
    private void writeObject(ObjectOutputStream os)	throws IOException {
    	
    	os.defaultWriteObject();	// Writes the urn field only
    }

    /**
     * Reconstructs a TrustedEntityId from the given serial stream.
     * <p> 
     * The {@link java.io.ObjectInputStream#defaultReadObject()} method is
     * invoked to read the value of the {@link #urn} field. The result is then
     * parsed in the usual way.
     *
     * @param is
     *            the object input stream from which this object is being read
     */
    private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
	
    	is.defaultReadObject();     // Reads the urn field only
    	try {
    		this.parseUrn(this.urn);
    	} catch (MalformedTrustedEntityIdException mteide) {
    		IOException ioe = new InvalidObjectException("Invalid trusted entity identifier");
    		ioe.initCause(mteide);
    		throw ioe;
    	}
    }
    
    private void parseUrn(URI input) throws MalformedTrustedEntityIdException {
    	
    	final String scheme = input.getScheme();
    	if (scheme == null)
    		throw new MalformedTrustedEntityIdException("'" + input + "'"
    				+ ": Missing URI scheme");
    	if (!URI_SCHEME.equals(scheme))
    		throw new MalformedTrustedEntityIdException("'" + input + "'"
    				+ ": '" + scheme + "': Invalid URI scheme, expected '" + URI_SCHEME + "'");
    	
    	final String ssp = input.getSchemeSpecificPart();
    	
    	// parts = ["teid", trustor-id, entity-type, trustee-id]
    	final String[] parts = ssp.split(URN_DELIM);
    	if (parts.length != 4)
			throw new MalformedTrustedEntityIdException("'" + input + "'"
					+ ": Malformed scheme specific part, expected format 'teid:trustorId:entityType:trusteeId'");
    	
    	if (!URN_NID.equals(parts[0]))
    		throw new MalformedTrustedEntityIdException("'" + input + "'"
    				+ ((parts[0].isEmpty()) 
    						? ": Missing URN namespace identifier, expected '" + URN_NID + "'" 
    						: ": '" + parts[0] + "': Invalid URN namespace identifier, expected '" + URN_NID + "'"));
    	
    	this.trustorId = parts[1];
		if (this.trustorId.isEmpty())
			throw new MalformedTrustedEntityIdException("'" + input + "'" 
					+ ": Trustor identifier cannot be empty");
    	
    	try {
			this.entityType = TrustedEntityType.valueOf(parts[2]);
		} catch (IllegalArgumentException iae) {
			throw new MalformedTrustedEntityIdException("'" + input + "'"
					+ ((parts[1].isEmpty()) 
    						? ": Missing trusted entity type"
    						: ": '" + parts[1] + "': Invalid trusted entity type"), iae);
		}   
    	
		this.trusteeId = parts[3];
		if (this.trusteeId.isEmpty())
			throw new MalformedTrustedEntityIdException("'" + input + "'" 
					+ ": Trustee identifier cannot be empty");
    }
    
    private void parseString(String input) throws MalformedTrustedEntityIdException {
    	
    	try {
			this.urn = new URI(input);
			this.parseUrn(this.urn);
		} catch (URISyntaxException use) {
			throw new MalformedTrustedEntityIdException("'" + input + "'", use);
		}
    }
}
