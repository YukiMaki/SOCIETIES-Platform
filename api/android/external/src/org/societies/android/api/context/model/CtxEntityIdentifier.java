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
package org.societies.android.api.context.model;

/**
 * This class is used to identify context entities. It provides methods that
 * return information about the identified entity including:
 * <ul>
 * <li><tt>OperatorId</tt>: A unique identifier of the CSS or CIS where the 
 * identified context entity is stored.</li>
 * <li><tt>ModelType</tt>: Describes the type of the identified context model
 * object, i.e. {@link CtxModelType#ENTITY ENTITY}.</li>
 * <li><tt>Type</tt>: A semantic tag that characterises the identified context
 * entity. e.g. "person".</li>
 * <li><tt>ObjectNumber</tt>: A unique number within the CSS/CIS where the
 * respective context information was initially sensed/collected and stored.</li>
 * </ul>
 * <p>
 * A context entity identifier can be represented as a URI formatted String as
 * follows:
 * <pre>
 * &lt;OperatorId&gt;/ENTITY/&lt;Type&gt;/&lt;ObjectNumber&gt;
 * </pre>
 * 
 * @see CtxIdentifier
 * @author <a href="mailto:nicolas.liampotis@cn.ntua.gr">Nicolas Liampotis</a> (ICCS)
 * @since 0.0.1
 */
public class CtxEntityIdentifier extends CtxIdentifier {

	private static final long serialVersionUID = 1550923933016203797L;
	
	/**
	 * Creates a context entity identifier by specifying the CSS/CIS ID
	 * where the identified context model object is stored, as well as,
	 * the entity type and the unique numeric model object identifier.
	 * 
	 * @param operatorId
	 *            the identifier of the CSS/CIS where the identified context
	 *            model object is stored
	 * @param type
	 *            the entity type, e.g. "device"
	 * @param objectNumber
	 *            the unique numeric model object identifier
	 */
	public CtxEntityIdentifier(String operatorId, String type, 
			Long objectNumber) {
		
		super(operatorId, CtxModelType.ENTITY, type, objectNumber);
	}
	
	public CtxEntityIdentifier(String str) throws MalformedCtxIdentifierException {
		
		super(str);
	}

	/** 
	 * Formats the string representation of a context entity identifier as follows:
	 * <pre> 
	 * operatorId/ENTITY/type/objectNumber
	 * </pre>
	 * 
	 * @see CtxIdentifier#defineString()
	 */
	@Override
	protected void defineString() {

		if (super.string != null) 
			return;

		final StringBuilder sb = new StringBuilder();

		sb.append(super.operatorId);
		sb.append("/");
		sb.append(CtxModelType.ENTITY);
		sb.append("/");
		sb.append(super.type);
		sb.append("/");
		sb.append(super.objectNumber);

		super.string = sb.toString();
	}

	/**
	 * Parses the string form of a context entity identifier as follows:
	 * <pre> 
	 * operatorId/ENTITY/type/objectNumber
	 * </pre>
	 * 
	 * @see CtxIdentifier#parseString(java.lang.String)
	 */
	@Override
	protected void parseString(String input)
			throws MalformedCtxIdentifierException {
	
		super.string = input;

		final int length = input.length();

		final int objectNumberDelim = input.lastIndexOf("/");
		if (objectNumberDelim == -1)
			throw new MalformedCtxIdentifierException("'" + input + "'");
		final String objectNumberStr = input.substring(objectNumberDelim+1, length);
		try { 
			super.objectNumber = new Long(objectNumberStr);
		} catch (NumberFormatException nfe) {
			throw new MalformedCtxIdentifierException("'" + input 
					+ "': Invalid context entity object number", nfe);
		}

		final int typeDelim = input.lastIndexOf("/", objectNumberDelim-1);
		super.type = input.substring(typeDelim+1, objectNumberDelim);
		if (super.type.isEmpty())
			throw new MalformedCtxIdentifierException("'" + input 
					+ "': Context entity type cannot be empty");

		final int modelTypeDelim = input.lastIndexOf("/", typeDelim-1);
		if (modelTypeDelim == -1)
			throw new MalformedCtxIdentifierException("'" + input + "'");
		final String modelTypeStr = input.substring(modelTypeDelim+1, typeDelim);
		try {
			super.modelType = CtxModelType.valueOf(modelTypeStr);
		} catch (IllegalArgumentException iae) {
			throw new MalformedCtxIdentifierException("'" + input 
					+ "': Malformed context model type", iae);
		}
		if (!CtxModelType.ENTITY.equals(super.modelType))
			throw new MalformedCtxIdentifierException("'" + input 
					+ "': Expected 'ENTITY' but found '"
					+ super.modelType + "'");

		operatorId = input.substring(0, modelTypeDelim);
		if (operatorId.isEmpty())
			throw new MalformedCtxIdentifierException("'" + input 
					+ "': Operator ID cannot be empty");
	}
}