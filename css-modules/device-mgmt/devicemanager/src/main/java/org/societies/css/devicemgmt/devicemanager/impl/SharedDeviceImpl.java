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
package org.societies.css.devicemgmt.devicemanager.impl;

import java.util.List;

import org.societies.api.css.devicemgmt.IDevice;
import org.societies.api.css.devicemgmt.IDriverService;
import org.societies.api.internal.css.devicemgmt.model.DeviceCommonInfo;

/**
 * This class is an implementation of the IDevice interface for shared devices
 * @author Rafik
 *
 */
public class SharedDeviceImpl implements IDevice {


	private DeviceManager deviceManager;
	private DeviceCommonInfo deviceCommonInfo;
	private String deviceNodeId;
	private boolean status = true;
	
	public SharedDeviceImpl(DeviceManager deviceMgr, String deviceNodeId, DeviceCommonInfo deviceCommonInfo) {
		
		this.deviceManager = deviceMgr;
		this.deviceCommonInfo = deviceCommonInfo;
		this.deviceNodeId = deviceNodeId;
	}
	
	@Override
	public String getDeviceName() {
		if (null != deviceCommonInfo) {
			return deviceCommonInfo.getDeviceName();
		}
		return null;
	}


	@Override
	public String getDeviceNodeId() {
			return deviceNodeId;
	}


	@Override
	public String getDeviceId() {	
		if (null != deviceCommonInfo) {
			return deviceCommonInfo.getDeviceID();
		}
		return null;
	}


	@Override
	public String getDeviceType() {
		if (null != deviceCommonInfo) {
			return deviceCommonInfo.getDeviceType();
		}	
		return null;
	}


	@Override
	public String getDeviceDescription() {
		return deviceCommonInfo.getDeviceDescription();
	}


	@Override
	public String getDeviceConnectionType() {
		if (null != deviceCommonInfo) {
			return deviceCommonInfo.getDeviceConnectionType();
		}	
		return null;
		
		
	}


	@Override
	public boolean isEnable() {
		return this.status;
	}


	@Override
	public String getDeviceLocation() {
		if (null != deviceCommonInfo) {
			return deviceCommonInfo.getDeviceLocation();
		}	
		return null;
		
		
	}

	
	@Override
	public String getDeviceProvider() {
		if (null != deviceCommonInfo) {
			return deviceCommonInfo.getDeviceProvider();
		}	
		return null;
	}


	@Override
	public boolean isContextSource() {
		if (null != deviceCommonInfo) {
			return deviceCommonInfo.getContextSource();
		}	
		return false;
	}

	
	@Override
	public IDriverService getService(String serviceName) {
		return null;
	}


	@Override
	public IDriverService[] getServices() {
		return null;
	}

	
	@Override
	public List<String> getEventNameList() {
		return null;
	}

}
