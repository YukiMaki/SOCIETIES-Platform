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
package org.societies.android.platform.servicemonitor;


import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.societies.android.api.internal.servicelifecycle.IServiceControl;
import org.societies.android.api.internal.servicelifecycle.IServiceDiscovery;
import org.societies.android.api.servicelifecycle.AService;
import org.societies.android.api.servicelifecycle.AServiceResourceIdentifier;
import org.societies.android.api.servicelifecycle.IServiceUtilities;
import org.societies.api.comm.xmpp.datatypes.Stanza;
import org.societies.api.comm.xmpp.datatypes.XMPPInfo;
import org.societies.api.comm.xmpp.exceptions.XMPPError;
import org.societies.api.comm.xmpp.interfaces.ICommCallback;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.InvalidFormatException;
import org.societies.api.schema.servicelifecycle.servicecontrol.ServiceControlMsgBean;
import org.societies.api.schema.servicelifecycle.servicecontrol.ServiceControlResult;
import org.societies.api.schema.servicelifecycle.servicecontrol.ServiceControlResultBean;
import org.societies.api.schema.servicelifecycle.servicediscovery.MethodName;
import org.societies.api.schema.servicelifecycle.servicediscovery.ServiceDiscoveryMsgBean;
import org.societies.api.schema.servicelifecycle.servicediscovery.ServiceDiscoveryResultBean;
import org.societies.comm.xmpp.client.impl.ClientCommunicationMgr;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

/**
 * Describe your class here...
 *
 * @author aleckey
 *
 */
public class ServiceManagement extends Service implements IServiceDiscovery, IServiceUtilities {// , IServiceControl {

	//COMMS REQUIRED VARIABLES
	private static final List<String> ELEMENT_NAMES = Arrays.asList("serviceDiscoveryMsgBean", "serviceDiscoveryResultBean", "serviceControlMsgBean", "ServiceControlResultBean");
    private static final List<String> NAME_SPACES = Arrays.asList("http://societies.org/api/schema/servicelifecycle/servicediscovery",
															  	  "http://societies.org/api/schema/servicelifecycle/servicecontrol",
															  	  "http://societies.org/api/schema/servicelifecycle/model");
    private static final List<String> PACKAGES = Arrays.asList("org.societies.api.schema.servicelifecycle.servicediscovery", 
															   "org.societies.api.schema.servicelifecycle.servicecontrol",
															   "org.societies.api.schema.servicelifecycle.model");
    private ClientCommunicationMgr commMgr;
    
    //SERVICE LIFECYCLE INTENTS
	public static final String INTENT_RETURN_VALUE = "org.societies.android.platform.servicediscovery.ReturnValue";
	public static final String GET_SERVICE     = "org.societies.android.platform.servicediscovery.GET_SERVICE";
	public static final String GET_SERVICES    = "org.societies.android.platform.servicediscovery.GET_SERVICES";
	public static final String GET_MY_SERVICES     = "org.societies.android.platform.servicediscovery.GET_MY_SERVICES";
	public static final String SEARCH_SERVICES = "org.societies.android.platform.servicediscovery.SEARCH_SERVICES";
	public static final String GET_MY_SERVICE_ID = "org.societies.android.platform.servicemanagement.GET_MY_SERVICE_ID";
	//SERVICE CONTROL INTENTS
	public static final String START_VIRGO_SERVICE = "org.societies.android.platform.servicediscovery.START_VIRGO_SERVICE";
	public static final String STOP_VIRGO_SERVICE = "org.societies.android.platform.servicediscovery.STOP_VIRGO_SERVICE";
		
    private static final String LOG_TAG = ServiceManagement.class.getName();
    private IBinder binder = null;
    
    @Override
	public void onCreate () {
		this.binder = new LocalBinder();
		Log.d(LOG_TAG, "ServiceManagement service starting");
		try {
			//INSTANTIATE COMMS MANAGER
			commMgr = new ClientCommunicationMgr(this);
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
        }    
	}

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "ServiceDiscovery service terminating");
	}

	/**Create Binder object for local service invocation */
	public class LocalBinder extends Binder {
		public ServiceManagement getService() {
			return ServiceManagement.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return this.binder;
	}
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> IServiceDiscovery >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	/* @see org.societies.android.api.internal.servicelifecycle.IServiceDiscovery#getServices(java.lang.String, org.societies.api.identity.IIdentity)*/
	public AService[] getMyServices(String client) {
		Log.d(LOG_TAG, "getMyServices called by client: " + client);
		
		//MESSAGE BEAN
		ServiceDiscoveryMsgBean messageBean = new ServiceDiscoveryMsgBean();
		messageBean.setMethod(MethodName.GET_LOCAL_SERVICES);

		//COMMS STUFF
		ICommCallback discoCallback = new ServiceLifecycleCallback(client, GET_MY_SERVICES); 
		IIdentity toID = commMgr.getIdManager().getCloudNode();
		Log.e(LOG_TAG, ">>>>>>>>>>>>>>Cloud Node: " + toID.getJid());
		Stanza stanza = new Stanza(toID);
        try {
        	commMgr.register(ELEMENT_NAMES, discoCallback);
        	commMgr.sendIQ(stanza, IQ.Type.GET, messageBean, discoCallback);
			Log.d(LOG_TAG, "Sending stanza");
		} catch (Exception e) {
			Log.e(LOG_TAG, "ERROR sending message: " + e.getMessage());
        }
        return null;
	}

	
	/* @see org.societies.android.api.internal.servicelifecycle.IServiceDiscovery#getServices(java.lang.String, org.societies.api.identity.IIdentity)*/
	public AService[] getServices(String client, String identity) {
		Log.d(LOG_TAG, "getServices called by client: " + client);
		
		//MESSAGE BEAN
		ServiceDiscoveryMsgBean messageBean = new ServiceDiscoveryMsgBean();
		messageBean.setMethod(MethodName.GET_LOCAL_SERVICES);

		//COMMS STUFF
		ICommCallback discoCallback = new ServiceLifecycleCallback(client, GET_SERVICES); 
		IIdentity toID;
		try {
			toID = commMgr.getIdManager().fromJid(identity);
		} catch (InvalidFormatException e1) {
			toID = commMgr.getIdManager().getCloudNode();
			e1.printStackTrace();
		}
		Stanza stanza = new Stanza(toID);
        try {
        	//commMgr.register(ELEMENT_NAMES, discoCallback);
        	commMgr.sendIQ(stanza, IQ.Type.GET, messageBean, discoCallback);
			Log.d(LOG_TAG, "Sending stanza");
		} catch (Exception e) {
			Log.e(this.getClass().getName(), "ERROR sending message: " + e.getMessage());
        }
        return null;
	}

	/* @see org.societies.android.api.internal.servicelifecycle.IServiceDiscovery#getService(java.lang.String, org.societies.api.schema.servicelifecycle.model.ServiceResourceIdentifier, org.societies.api.identity.IIdentity)*/
	public AService getService(String client, AServiceResourceIdentifier sri, String identity) {
		return null;
	}

	/* @see org.societies.android.api.internal.servicelifecycle.IServiceDiscovery#searchService(java.lang.String, org.societies.api.schema.servicelifecycle.model.Service, org.societies.api.identity.IIdentity) */
	public AService[] searchService(String client, AService filter, String identity) {
		return null;
	}

	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> IServiceUtilities >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	/* @see org.societies.android.api.servicelifecycle.IServiceUtilities#getMyServiceId(java.lang.String) */
	public AServiceResourceIdentifier getMyServiceId(String client) {
		Log.d(LOG_TAG, "Calling getMyServiceId from client: " + client);
		
		AServiceResourceIdentifier sri = new AServiceResourceIdentifier();
		String appName = getCallingAppName(client);
		String uri = "http://" + commMgr.getIdManager().getThisNetworkNode().getJid() + "/" + appName;
		try {
			sri.setIdentifier(new URI(uri));
		} catch (URISyntaxException e) {
			Log.d(LOG_TAG, "Exception parsing URI: " + uri);
		}
		sri.setServiceInstanceIdentifier(client);

		//SETUP RETURN INTENT STUFF
		if (client != null) {
			Intent intent = new Intent(GET_MY_SERVICE_ID);
			intent.putExtra(INTENT_RETURN_VALUE, (Parcelable)sri);
			intent.setPackage(client);
			this.sendBroadcast(intent);
		}
		return null;
	}

	private String getCallingAppName(String client) {
		List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
		String appName = client; //DEFAULT BACK TO PACKAGE
		for(int i=0;i<packs.size();i++) {
			PackageInfo p = packs.get(i);
			if (p.versionName == null) { //A SYSTEM PACKAGE - IGNORE
				continue ;
			}
			//FILTER PACKAGES
			String pack = p.packageName;
			if (pack.contains(client))
				appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
		}
		return appName;
	}
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> IServiceControl >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public ServiceControlResult installService(String client, URL arg1, String identity) {
		return null;
	}

	public ServiceControlResult shareService(String client, org.societies.api.schema.servicelifecycle.model.Service service, String identity) {
		return null;
	}

	public ServiceControlResult startService(String client, AServiceResourceIdentifier sri, String identity) {
		Log.d(LOG_TAG, "getMyServices called by client: " + client);
		
		//MESSAGE BEAN
		ServiceControlMsgBean messageBean = new ServiceControlMsgBean();
		messageBean.setMethod(org.societies.api.schema.servicelifecycle.servicecontrol.MethodType.START_SERVICE);
		messageBean.setServiceId(sri);

		//COMMS STUFF
		ICommCallback discoCallback = new ServiceLifecycleCallback(client, START_VIRGO_SERVICE); 
		IIdentity toID = commMgr.getIdManager().getCloudNode();
		Log.e(LOG_TAG, ">>>>>>>>>>>>>>Cloud Node: " + toID.getJid());
		Stanza stanza = new Stanza(toID);
        try {
        	commMgr.register(ELEMENT_NAMES, discoCallback);
        	commMgr.sendIQ(stanza, IQ.Type.GET, messageBean, discoCallback);
			Log.d(LOG_TAG, "Sending stanza");
		} catch (Exception e) {
			Log.e(LOG_TAG, "ERROR sending message: " + e.getMessage());
        }
        return null;
	}

	public ServiceControlResult stopService(String client, AServiceResourceIdentifier sri, String identity) {
		Log.d(LOG_TAG, "getMyServices called by client: " + client);
		
		//MESSAGE BEAN
		ServiceControlMsgBean messageBean = new ServiceControlMsgBean();
		messageBean.setMethod(org.societies.api.schema.servicelifecycle.servicecontrol.MethodType.STOP_SERVICE);
		messageBean.setServiceId(sri);

		//COMMS STUFF
		ICommCallback discoCallback = new ServiceLifecycleCallback(client, STOP_VIRGO_SERVICE); 
		IIdentity toID = commMgr.getIdManager().getCloudNode();
		Log.e(LOG_TAG, ">>>>>>>>>>>>>>Cloud Node: " + toID.getJid());
		Stanza stanza = new Stanza(toID);
        try {
        	commMgr.register(ELEMENT_NAMES, discoCallback);
        	commMgr.sendIQ(stanza, IQ.Type.GET, messageBean, discoCallback);
			Log.d(LOG_TAG, "Sending stanza");
		} catch (Exception e) {
			Log.e(LOG_TAG, "ERROR sending message: " + e.getMessage());
        }
        return null;
	}

	public ServiceControlResult unshareService(String client, org.societies.api.schema.servicelifecycle.model.Service arg1, String identity) {
		return null;
	}
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> COMMS CALLBACK >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	/**
	 * Callback required for Android Comms Manager
	 */
	private class ServiceLifecycleCallback implements ICommCallback {
		private String returnIntent;
		private String client;

		/**Constructor sets the calling client and Intent to be returned
		 * @param client
		 * @param returnIntent
		 */
		public ServiceLifecycleCallback(String client, String returnIntent) {
			this.client = client;
			this.returnIntent = returnIntent;
		}

		public List<String> getXMLNamespaces() {
			return NAME_SPACES;
		}

		public List<String> getJavaPackages() {
			return PACKAGES;
		}

		public void receiveError(Stanza arg0, XMPPError err) {
			Log.d(LOG_TAG, "Callback receiveError:" + err.getMessage());			
		}

		public void receiveInfo(Stanza arg0, String arg1, XMPPInfo arg2) {
			Log.d(LOG_TAG, "Callback receiveInfo");
		}

		public void receiveItems(Stanza arg0, String arg1, List<String> arg2) {
			Log.d(LOG_TAG, "Callback receiveItems");
		}

		public void receiveMessage(Stanza arg0, Object arg1) {
			Log.d(LOG_TAG, "Callback receiveMessage");	
		}

		public void receiveResult(Stanza returnStanza, Object msgBean) {
			Log.d(LOG_TAG, "Callback receiveResult");
			
			if (client != null) {
				Intent intent = new Intent(returnIntent);
				
				Log.d(LOG_TAG, ">>>>>Return Stanza: " + returnStanza.toString());
				if (msgBean==null) Log.d(LOG_TAG, ">>>>msgBean is null");
				// --------- Service Discovery Bean ---------
				if (msgBean instanceof ServiceDiscoveryResultBean) {
					Log.d(LOG_TAG, "ServiceDiscoveryBeanResult!");
					ServiceDiscoveryResultBean discoResult = (ServiceDiscoveryResultBean) msgBean;
					List<org.societies.api.schema.servicelifecycle.model.Service> serviceList = discoResult.getServices();
					//CONVERT TO PARCEL BEANS
					int i=0;
					//AService serviceArray[] = AService.CREATOR.newArray(serviceList.size());
					Parcelable serviceArray[] = new Parcelable[serviceList.size()];
					for(org.societies.api.schema.servicelifecycle.model.Service tmpService: serviceList) {
						serviceArray[i] = AService.convertService(tmpService);
						i++;
					}
					//NOTIFY CALLING CLIENT
					intent.putExtra(INTENT_RETURN_VALUE, serviceArray); 
					intent.setPackage(client);
				} 
				// --------- Service Control Bean ---------
				if(msgBean instanceof ServiceControlResultBean) {
					Log.d(LOG_TAG, "ServiceControlBeanResult!");
					ServiceControlResultBean controlResult = (ServiceControlResultBean)msgBean;
					ServiceControlResult resultObj = controlResult.getControlResult();
					Log.d(LOG_TAG, "ServiceControlBeanResult: " + resultObj.getMessage());
					
					//NOTIFY CALLING CLIENT
					intent.putExtra(INTENT_RETURN_VALUE, (Parcelable) resultObj);
					intent.setPackage(client);
				}
				ServiceManagement.this.sendBroadcast(intent);
				ServiceManagement.this.commMgr.unregister(ELEMENT_NAMES, this);
			}
		}
	}//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> END COMMS CALLBACK >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
