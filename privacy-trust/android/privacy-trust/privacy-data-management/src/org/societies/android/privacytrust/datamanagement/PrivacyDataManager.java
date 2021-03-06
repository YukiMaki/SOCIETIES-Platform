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
package org.societies.android.privacytrust.datamanagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.societies.android.api.internal.privacytrust.IPrivacyDataManager;
import org.societies.android.api.internal.privacytrust.model.PrivacyException;
import org.societies.android.api.internal.privacytrust.model.dataobfuscation.LocationCoordinates;
import org.societies.android.api.internal.privacytrust.model.dataobfuscation.Name;
import org.societies.android.api.internal.privacytrust.model.dataobfuscation.PostalLocation;
import org.societies.android.api.internal.privacytrust.model.dataobfuscation.Status;
import org.societies.android.api.internal.privacytrust.model.dataobfuscation.Temperature;
import org.societies.android.api.internal.privacytrust.model.dataobfuscation.obfuscator.IDataObfuscator;
import org.societies.android.api.internal.privacytrust.model.dataobfuscation.wrapper.IDataWrapper;
import org.societies.android.api.internal.privacytrust.privacyprotection.model.privacypolicy.AAction;
import org.societies.android.privacytrust.api.IPrivacyDataManagerInternal;
import org.societies.android.privacytrust.dataobfuscation.obfuscator.LocationCoordinatesObfuscator;
import org.societies.android.privacytrust.dataobfuscation.obfuscator.NameObfuscator;
import org.societies.android.privacytrust.dataobfuscation.obfuscator.PostalLocationObfuscator;
import org.societies.android.privacytrust.dataobfuscation.obfuscator.StatusObfuscator;
import org.societies.android.privacytrust.dataobfuscation.obfuscator.TemperatureObfuscator;
import org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Action;
import org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.ActionConstants;
import org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Condition;
import org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Decision;
import org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.RequestItem;
import org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.Resource;
import org.societies.api.internal.schema.privacytrust.privacyprotection.model.privacypolicy.ResponseItem;
import org.societies.api.schema.identity.DataIdentifier;
import org.societies.api.schema.identity.RequestorBean;
import org.societies.api.schema.identity.RequestorServiceBean;

import android.content.Context;
import android.util.Log;


/**
 * @author Olivier Maridat (Trialog)
 */
public class PrivacyDataManager implements IPrivacyDataManager {
	private final static String TAG = PrivacyDataManager.class.getSimpleName();

	private IPrivacyDataManagerInternal privacyDataManagerInternal;
	private IPrivacyDataManager privacyDataManagerRemote;


	public PrivacyDataManager(Context context)  {
		privacyDataManagerInternal = new PrivacyDataManagerInternal();
		privacyDataManagerRemote = new PrivacyDataManagerRemote(context);
	}


	public ResponseItem checkPermission(RequestorBean requestor, DataIdentifier dataId, AAction[] actions) throws PrivacyException {
		// -- Verify parameters
		if (null == requestor) {
			Log.e(TAG, "verifyParemeters: Not enought information: requestor is missing");
			throw new NullPointerException("Not enought information: requestor is missing");
		}
		if (null == dataId) {
			Log.e(TAG, "verifyParemeters: Not enought information: data id is missing");
			throw new NullPointerException("Not enought information: data id is missing");
		}
		ResponseItem permission = null;


		// -- Create Useful Values for NULL Result
		// RequestItem
		Resource resource = new Resource();
		resource.setDataIdUri(dataId.getUri());
		List<Condition> conditions = new ArrayList<Condition>();
		RequestItem requestItemNull = new RequestItem();
		requestItemNull.setResource(resource);
		requestItemNull.setOptional(false);

		// -- Retrieve a stored permission
		List actionsList = Arrays.asList(actions);
		permission = privacyDataManagerInternal.getPermission(requestor, dataId, actionsList);

		// -- Permission not available: remote call
		if (null == permission) {
			Log.e(TAG, "No Permission retrieved: remote call");
			try {
				permission = privacyDataManagerRemote.checkPermission(requestor, dataId, actions);
			} catch (Exception e) {
				Log.e(TAG, "Error when retrieving permission from PrivacyDataManagerRemote", e);
			}

			// Permission still not available: deny access
			if (null == permission) {
				permission = new ResponseItem();
				permission.setRequestItem(requestItemNull);
				permission.setDecision(Decision.DENY);
			}
			// Store new permission retrieved from PrivacyPreferenceManager
			privacyDataManagerInternal.updatePermission(requestor, permission);
		}
		return permission;
	}

	public IDataWrapper obfuscateData(RequestorBean requestor, IDataWrapper dataWrapper) throws PrivacyException {
		// -- Verify parameters
		if (null == requestor) {
			Log.e(TAG, "verifyParemeters: Not enought information: requestor is missing");
			throw new NullPointerException("Not enought information: requestor is missing");
		}
		if (null == dataWrapper) {
			Log.e(TAG, "verifyParemeters: Not enought information: data wrapper is missing");
			throw new NullPointerException("Not enought information: data wrapper is missing");
		}
		if (null == dataWrapper.getData()) {
			Log.e(TAG, "verifyParemeters: Not enought information: data is missing");
			throw new NullPointerException("Not enought information: data is missing");
		}
		if (null == dataWrapper.getDataId()) {
			Log.e(TAG, "verifyParemeters: Not enought information: data id is missing");
			throw new NullPointerException("Not enought information: data id is missing");
		}

		//		// -- Retrieve the obfuscation level
		//		DObfOutcome dataObfuscationPreferences = privacyPreferenceManager.evaluateDObfPreference(requestor, ownerId, dataWrapper.getDataId());
		//		double obfuscationLevel = dataObfuscationPreferences.getObfuscationLevel();
		double obfuscationLevel = 1;
		// If no obfuscation is required: return directly the wrapped data
		if (obfuscationLevel >= 1) {
			return dataWrapper;
		}

		// -- Verify params
		// Wrapper ready for obfuscation
		if (!dataWrapper.isReadyForObfuscation()) {
			throw new PrivacyException("This data wrapper is not ready for obfuscation. Data are needed.");
		}
		// Obfuscation level in [0, 1]
		if (obfuscationLevel > 1) {
			obfuscationLevel = 1;
		}
		if (obfuscationLevel < 0) {
			obfuscationLevel = 0.001;
		}
		// Return directly if obfuscation level is 1
		if (1 == obfuscationLevel) {
			return dataWrapper;
		}

		// -- Mapping: retrieve the relevant obfuscator
		IDataObfuscator obfuscator = getDataObfuscator(dataWrapper);

		// -- Obfuscate
		IDataWrapper obfuscatedDataWrapper = null;
		try {
			// - Obfuscation
			// Local obfuscation
			if (obfuscator.isAvailable()) {
				Log.d(TAG, "Local obfuscation");
				obfuscatedDataWrapper = obfuscator.obfuscateData(obfuscationLevel);
			}
			// Remote obfuscation needed
			else {
				Log.d(TAG, "Remote obfuscation needed");
				// TODO: remote call
			}

			// - Persistence
			//			if (dataWrapper.isPersistenceEnabled()) {
			// TODO: persiste the obfuscated data using a data broker
			//				System.out.println("Persist the data "+dataWrapper.getDataId());
			//			}
		}
		catch(Exception e) {
			throw new PrivacyException("Obfuscation aborted", e);
		}
		return obfuscatedDataWrapper;
	}

	public DataIdentifier hasObfuscatedVersion(RequestorBean requestor, IDataWrapper dataWrapper) throws PrivacyException {
		// -- Verify parameters
//		if (null == requestor) {
//			Log.e(TAG, "verifyParemeters: Not enought information: requestor is missing");
//			throw new NullPointerException("Not enought information: requestor is missing");
//		}
//		if (null == dataWrapper) {
//			Log.e(TAG, "verifyParemeters: Not enought information: data wrapper is missing");
//			throw new NullPointerException("Not enought information: data wrapper is missing");
//		}
//		if (null == dataWrapper.getDataId()) {
//			Log.e(TAG, "verifyParemeters: Not enought information: data id is missing");
//			throw new NullPointerException("Not enought information: data id is missing");
//		}

		return dataWrapper.getDataId();
	}


	// -- Private methods
	private boolean containsAction(List<Action> actions, Action action) {
		if (null == actions || actions.size() <= 0 || null == action) {
			return false;
		}
		for(Action actionTmp : actions) {
			if (actionTmp.equals(action)) {
				return true;
			}
		}
		return false;
	}

	private IDataObfuscator getDataObfuscator(IDataWrapper dataWrapper) throws PrivacyException {
		IDataObfuscator obfuscator = null;
		if (dataWrapper.getData() instanceof LocationCoordinates) {
			obfuscator = new LocationCoordinatesObfuscator((IDataWrapper<LocationCoordinates>) dataWrapper);
		}
		else if (dataWrapper.getData() instanceof Name) {
			obfuscator = new NameObfuscator((IDataWrapper<Name>) dataWrapper);
		}
		else if (dataWrapper.getData() instanceof Temperature) {
			obfuscator = new TemperatureObfuscator((IDataWrapper<Temperature>) dataWrapper);
		}
		else if (dataWrapper.getData() instanceof Status) {
			obfuscator = new StatusObfuscator((IDataWrapper<Status>) dataWrapper);
		}
		else if (dataWrapper.getData() instanceof PostalLocation) {
			obfuscator = new PostalLocationObfuscator((IDataWrapper<PostalLocation>) dataWrapper);
		}
		else {
			throw new PrivacyException("Obfuscation aborted: no known obfuscator for this type of data");
		}
		return obfuscator;
	}
}
