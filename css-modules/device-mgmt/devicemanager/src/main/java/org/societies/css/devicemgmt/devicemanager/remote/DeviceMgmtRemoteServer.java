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
package org.societies.css.devicemgmt.devicemanager.remote;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.societies.api.comm.xmpp.datatypes.Stanza;
import org.societies.api.comm.xmpp.exceptions.XMPPError;
import org.societies.api.comm.xmpp.interfaces.IFeatureServer;
import org.societies.api.css.devicemgmt.DriverService;
import org.societies.api.css.devicemgmt.IDevice;
import org.societies.api.css.devicemgmt.IDriverService;
import org.societies.api.schema.css.devicemgmt.devicecontrol.DeviceAction;
import org.societies.api.schema.css.devicemgmt.devicecontrol.RemoteDeviceBeanResult;
import org.societies.api.schema.css.devicemgmt.devicecontrol.RemoteDevicesBean;
import org.societies.api.schema.css.devicemgmt.devicecontrol.MethodType;
import org.societies.css.devicemgmt.devicemanager.impl.DeviceManager;

/**
 *
 *This class handles Device mgmt remote calls.
 * @author Rafik
 *
 */
public class DeviceMgmtRemoteServer implements IFeatureServer{
	
	
	private static final List<String> NAMESPACES = Collections.unmodifiableList(
			  Arrays.asList("http://societies.org/api/schema/css/devicemgmt/devicecontrol"));
	private static final List<String> PACKAGES = Collections.unmodifiableList(
			  Arrays.asList("org.societies.api.schema.css.devicemgmt.devicecontrol"));


	@Override
	public List<String> getXMLNamespaces() {
		return NAMESPACES;
	}


	@Override
	public List<String> getJavaPackages() {
		return PACKAGES;
	}


	@Override
	public void receiveMessage(Stanza stanza, Object payload) {
		
	}


	@Override
	public Object getQuery(Stanza stanza, Object payload) throws XMPPError {
		
		if (payload instanceof RemoteDevicesBean) {
			
			RemoteDevicesBean devicesBean = (RemoteDevicesBean) payload;
			
			switch (devicesBean.getMethod()) {
			case GET_DEVICE_DESCRIPTION:
				
				RemoteDeviceBeanResult remoteDeviceBeanResult = new RemoteDeviceBeanResult();
				DriverService
				
				//get the DeviceImpl object from the DeviceManager to be able to its description 
				IDevice iDevice =  DeviceManager.deviceFamilyContainer.get(devicesBean.getDeviceFamily()).get(devicesBean.getDeviceId());
				
				//get services provided by the 
				IDriverService iDriverServiceList [] = iDevice.getServices();
				
				if (null != iDriverServiceList) 
				{
					for (IDriverService ds : iDriverServiceList) {
						
						
					}
				}
				
				
				break;
				
				
			case INVOKE_ACTION:
				
				//get the DeviceImpl object from the DeviceManager to be able to its description
			
			break;

			default:
				break;
			}
			
		}
		
		
		return null;
	}


	@Override
	public Object setQuery(Stanza stanza, Object payload) throws XMPPError {	
		return null;
	}

}
