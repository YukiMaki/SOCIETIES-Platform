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

package org.societies.android.platform.useragent;

import org.societies.android.api.context.broker.ICtxClientBroker;
import org.societies.android.api.context.broker.ICtxLocalBinder;
import org.societies.android.api.useragent.IAndroidUserAgent;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.InvalidFormatException;
import org.societies.comm.xmpp.client.impl.ClientCommunicationMgr;
import org.societies.identity.IdentityManagerImpl;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class UserAgent extends Service{

	private static final String LOG_TAG = UserAgent.class.getName();

	//currently hard coded but should be injected
	private static final String DESTINATION = "xcmanager.societies.local";

	private UserAgentImpl aua;

	//private IBinder binder = null;

	@Override
	public void onCreate () {

		try {
			//set up comms variables
			ClientCommunicationMgr ccm = new ClientCommunicationMgr(this);
			IIdentity toXCManager = IdentityManagerImpl.staticfromJid(DESTINATION);

			//create implementation class
			aua = new UserAgentImpl(ccm, toXCManager);
		} catch (InvalidFormatException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			throw new RuntimeException(e);
		}  

		//bind to local context service
		//this.bindToCtxClientBroker();

		Log.d(LOG_TAG, "User Agent service starting");
	}

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "User Agent service terminating");
	}

	public IAndroidUserAgent getInterface(){
		return this.aua;
	}

	/*private void bindToCtxClientBroker(){
		Intent bindIntent = new Intent(this, ContextManagement.class);
		bindService(bindIntent, cbConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection cbConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.d(LOG_TAG, "Binding to ctxBrokerService");
			//get Service then retrieve interface
			ContextManagement ctxBrokerService = ((LocalBinder<ContextManagement>)service).getService();
			ICtxClientBroker acb = ctxBrokerService.getInterface();
			//pass interfaces to impl class
			aua.connectCtxClientBroker(acb);
			Log.d(LOG_TAG, "User Agent connected to Ctx Broker service");
		}

		public void onServiceDisconnected(ComponentName className) {
			// As our service is in the same process, this should never be called
			aua.disconnectCtxClientBroker();
			Log.d(LOG_TAG, "User Agent disconnected from Ctx Broker service");
		}
	};*/

	/**
	 * Create Binder object for local service invocation
	 *//*
	public class UALocalBinder extends Binder implements IUALocalBinder {
		public IAndroidUserAgent getService() {
			return aua;
		}
	}*/

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(LOG_TAG, "onBind called - returning LocalBinder object for this service");
		return new LocalBinder<UserAgent>(this);
	}

}
