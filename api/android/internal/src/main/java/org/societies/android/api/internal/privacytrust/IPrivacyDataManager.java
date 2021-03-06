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
package org.societies.android.api.internal.privacytrust;

import java.util.List;

import org.societies.android.api.internal.privacytrust.model.PrivacyException;
import org.societies.android.api.internal.privacytrust.model.dataobfuscation.wrapper.IDataWrapper;
import org.societies.android.api.internal.privacytrust.privacyprotection.model.privacypolicy.AAction;
import org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Action;
import org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.ResponseItem;
import org.societies.api.schema.identity.RequestorBean;
import org.societies.api.schema.identity.DataIdentifier;;

/**
 * Interface exposed to Societies components in order to manage access control over resources
 * @author Olivier Maridat (Trialog)
 * @created 09-nov.-2011 16:45:26
 */
public interface IPrivacyDataManager {
	public static final String CHECK_PERMISSION = "org.societies.android.api.internal.privacytrust.checkPermission";
	public static final String CHECK_PERMISSION_RESULT = "org.societies.android.api.internal.privacytrust.checkPermissionResult";
	public static final String OBFUSCATE_DATA = "org.societies.android.api.internal.privacytrust.obfuscateData";
	public static final String OBFUSCATE_DATA_RESULT = "org.societies.android.api.internal.privacytrust.obfuscateDataResult";
	public static final String HAS_OBFUSCATED_VERSION = "org.societies.android.api.internal.privacytrust.hasObfuscatedVersion";
	public static final String HAS_OBFUSCATED_VERSION_RESULT = "org.societies.android.api.internal.privacytrust.hasObfuscatedVersionResult";

	/**
	 * Check permission to access/use/disclose a data

	 * @param requestor Requestor of the obfuscation. It may be a CSS, or a CSS requesting a data through a 3P service, or a CIS.
	 * @param dataId ID of the requested data.
	 * @param action Action requested over this data.
	 * @return A ResponseItem with permission information in it
	 * @throws PrivacyException
	 */
	public ResponseItem checkPermission(RequestorBean requestor, DataIdentifier dataId, AAction[] actions) throws PrivacyException;

	/**
	 * Protect a data following the user preferences by obfuscating it to a correct
	 * obfuscation level. The data information are wrapped into a relevant data
	 * wrapper in order to execute the relevant obfuscation operation into relevant
	 * information.
	 * @param requestor Requestor of the ofuscation. It may be a CSS, or a CSS requesting a data through a 3P service, or a CIS.
	 * @param dataWrapper Data wrapped in a relevant data wrapper. Use DataWrapperFactory to select the relevant DataWrapper
	 * @return Obfuscated data wrapped in a DataWrapper (of the same type that the one used to instantiate the obfuscator)
	 * @throws PrivacyException
	 */
	public IDataWrapper obfuscateData(RequestorBean requestor, IDataWrapper dataWrapper) throws PrivacyException;

	/**
	 * Check if there is an obfuscated version of the data and return its ID.
	 * @param requestor Requestor of the ofuscation. It may be a CSS, or a CSS requesting a data through a 3P service, or a CIS.
	 * @param dataWrapper Data ID wrapped in the relevant DataWrapper. Only the ID information is mandatory to retrieve an obfuscated version. Use DataWrapperFactory to select the relevant DataWrapper
	 * @return ID of the obfuscated version of the data if the persistence is enabled and if the obfuscated data exists
	 * @return otherwise ID of the non-obfuscated data
	 * @throws PrivacyException
	 */
	public DataIdentifier hasObfuscatedVersion(RequestorBean requestor, IDataWrapper dataWrapper) throws PrivacyException;
}