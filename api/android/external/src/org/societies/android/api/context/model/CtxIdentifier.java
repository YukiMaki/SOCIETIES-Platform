package org.societies.android.api.context.model;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class CtxIdentifier implements Serializable {

	private static final long serialVersionUID = 3552976823045895472L;
	
	/** The unique identifier of the CSS or CIS where the identified context model object is stored.*/
	protected transient String operatorId;
	
	/** The type of the identified context model object. */
	protected transient CtxModelType modelType;
	
	/** The semantic tag that characterises the identified context model object. */
	protected transient String type;
	
	/** The unique number within the CSS/CIS where the identified context model object was initially sensed/collected and stored. */
	protected transient Long objectNumber;
	
	/** The string form of this context identifier. */
	protected volatile String string;    // The only serialisable field
	
	/**
	 * Constructs a context model object identifier by specifying the CSS/CIS ID
	 * where the identified object is stored, as well as, the {@link CtxModelType}
	 * and the unique numeric model object identifier.
	 * 
	 * @param operatorId
	 *            the identifier of the CSS/CIS where the identified context
	 *            model object is stored
	 * @param type
	 *            the semantic tag that characterises the identified context
     *            model object. e.g. "person"
	 * @param objectNumber
	 *            the unique numeric model object identifier
	 */
	CtxIdentifier(String operatorId, CtxModelType modelType, String type, Long objectNumber) {
		
		this.operatorId = operatorId;
		this.modelType = modelType;
		this.type = type;
		this.objectNumber = objectNumber;
	}
	
	/**
	 * Constructs a context model object identifier by parsing the given string. 
	 * 
	 * @throws MalformedCtxIdentifierException
	 *             if the given string cannot be parsed
	 */
	CtxIdentifier(String str) throws MalformedCtxIdentifierException {
		
		this.parseString(str);
	}
	
	/**
	 * Returns a unique identifier of the CSS or CIS where the identified
	 * context model object is stored
     * 
	 * @return a unique identifier of the CSS or CIS where the identified 
	 * context model object is stored
	 */
	public String getOperatorId() {
		
		return this.operatorId;
	}
	
	/**
	 * Returns the type of the identified context model object
	 * 
	 * @return the type of the identified context model object
	 * @see CtxModelType
	 */
	public CtxModelType getModelType() {
		
		return this.modelType;
	}

	/**
	 * Returns the semantic tag (e.g. "person") that characterises the
	 * identified context model object
     * 
     * @return the semantic tag of the identified context model object 
	 */
	public String getType() {
		
		return this.type;
	}
	
	/**
	 * Returns the numeric part of this context model object identifier
     * 
     * @return the numeric part of this context model object identifier
	 */
	public Long getObjectNumber() {
		
		return this.objectNumber;
	}
	
	/**
	 * Returns a URI formatted String representation of this context model
	 * object identifier
	 * 
	 * @return a URI formatted String representation of this context model
	 * object identifier
	 */
	public String toUriString() {
		
		return this.toString();
	}
	
	/**
	 * Returns a String representation of this context model object identifier
	 * 
	 * @return a String representation of this context model object identifier
	 */
	@Override
	public String toString() {
		
		this.defineString();
		
		return this.string;
	}
	
	/**
     * @see java.lang.Object#hashCode()
     * @since 0.0.2
     */
    @Override
    public int hashCode() {
    	
        final int prime = 31;
        int result = 1;
        
        result = prime * result
                + ((this.operatorId == null) ? 0 : this.operatorId.hashCode());
        result = prime * result
                + ((this.modelType == null) ? 0 : this.modelType.hashCode());
        result = prime * result
                + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result
                + ((this.objectNumber == null) ? 0 : this.objectNumber.hashCode());
        
        return result;
    }
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @since 0.0.2
	 */
	@Override
    public boolean equals(Object that) {
		
        if (this == that)
            return true;
        if (that == null)
            return false;
        if (this.getClass() != that.getClass())
            return false;
        
        CtxIdentifier other = (CtxIdentifier) that;
        if (this.operatorId == null) {
            if (other.operatorId != null)
                return false;
        } else if (!this.operatorId.equals(other.operatorId))
            return false;
        if (this.modelType == null) {
            if (other.modelType != null)
                return false;
        } else if (!this.modelType.equals(other.modelType))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        } else if (!this.type.equals(other.type))
            return false;
        if (this.objectNumber == null) {
            if (other.objectNumber != null)
                return false;
        } else if (!this.objectNumber.equals(other.objectNumber))
            return false;
        
        return true;
    }
	
	/**
     * Formats the string representation of this context identifier.
     */
    protected abstract void defineString();
    
    /**
     * Parses the information contained in the given context identifier string.
     * 
     * @param input
     *            the context identifier string to parse
     * @throws MalformedCtxIdentifierException
     *             if the given string cannot be parsed.             
     */
	protected abstract void parseString(String input) throws MalformedCtxIdentifierException;
	
	/**
     * Writes the contents of this CtxIdentifier to the given object output stream.
     * <p> 
     * The only serialisable field of a CtxIdentifier instance is its 
     * {@link #string} field. That field is given a value, if it does not have
     * one already, and then the {@link java.io.ObjectOutputStream#defaultWriteObject()}
     * method of the given object-output stream is invoked.
     *
     * @param os
     *            the object output stream to which this object is to be written
     */
    private void writeObject(ObjectOutputStream os)	throws IOException {
    	
    	this.defineString();        // Initialise the string field
    	os.defaultWriteObject();	// Write the string field only
    }

    /**
     * Reconstructs a CtxIdentifier from the given serial stream.
     * <p> 
     * The {@link java.io.ObjectInputStream#defaultReadObject()} method is
     * invoked to read the value of the <tt>string</tt> field. The result is
     * then parsed in the usual way.
     *
     * @param is
     *            the object input stream from which this object is being read
     */
    private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
	
    	is.defaultReadObject();     // Read the string field only
    	try {
    		this.parseString(this.string);
    	} catch (MalformedCtxIdentifierException mcie) {
    		IOException ioe = new InvalidObjectException("Invalid context identifier");
    		ioe.initCause(mcie);
    		throw ioe;
    	}
    }
}