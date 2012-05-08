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
package org.societies.css.devicemgmt.DeviceDriverSimulator;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.css.devicemgmt.IDriverService;
import org.societies.api.internal.css.devicemgmt.IDeviceManager;
import org.societies.api.internal.css.devicemgmt.model.DeviceCommonInfo;
import org.societies.api.osgi.event.IEventMgr;
import org.springframework.osgi.context.BundleContextAware;

/**
 * OSGi activator to launch Driver Services dinamicaly
 * @author Olivier Maridat (Trialog)
 *
 */
public class SampleActivatorDriver implements BundleContextAware{
	private static Logger LOG = LoggerFactory.getLogger(SampleActivatorDriver.class.getSimpleName());
	  
	/** Injected values */
	private BundleContext bc;
	private IDeviceManager deviceManager;
	private IEventMgr eventManager;
	
	/** Sensors */
	private LightSensor ls;
	private LightSensor ls2;
	private LightSensor ls3;
	
	private String deviceServiceId1 = "lightSensor1";
	private String deviceServiceId2 = "lightSensor2";
	private String deviceServiceId3 = "lightSensor3";
	
	private String deviceMacAddress1 = "lightSensorService1";
	private String deviceMacAddress2 = "lightSensorService2";
	private String deviceMacAddress3 = "lightSensorService3";
	
	private DeviceCommonInfo deviceCommonInfo1;
	private DeviceCommonInfo deviceCommonInfo2;
	private DeviceCommonInfo deviceCommonInfo3;
	
	private String [] serviceIds1 = {"lightSensor1"};
	private String [] serviceIds2 = {"lightSensor2"};
	private String [] serviceIds3 = {"lightSensor3"};
	
	private int lightSensorCount = 1;
	
	private Dictionary<String, String> properties;
	
	/** Service registration */
    private ServiceRegistration lsReg;
    private ServiceRegistration lsReg2;
    private ServiceRegistration lsReg3;
    private ServiceRegistration laReg;

	/* --- Main code --- */
	/**
	 * Starts each actuator/sensor
	 */
	public void startSimulation() 
	{
		// -- Creation of sensors
		// Create devices info
		deviceCommonInfo1 = new DeviceCommonInfo("family1", deviceMacAddress1, "Light Sensor", "LightSensor", "Light Sensor test 1", "Zigbee", "Room1", "Trialog", true);
		deviceCommonInfo2 = new DeviceCommonInfo("family1", deviceMacAddress2, "Light Sensor", "LightSensor", "Light Sensor test 1", "Zigbee", "Room1", "Trialog", true);
		deviceCommonInfo3 = new DeviceCommonInfo("family1", deviceMacAddress3, "Light Sensor", "LightSensor", "Light Sensor test 1", "Zigbee", "Room1", "Trialog", true);
		// Register new devices as services in the device manager
		String deviceId1 = deviceManager.fireNewDeviceConnected(deviceMacAddress1, deviceCommonInfo1, serviceIds1);
		String deviceId2 = deviceManager.fireNewDeviceConnected(deviceMacAddress2, deviceCommonInfo2, serviceIds2);
		String deviceId3 = deviceManager.fireNewDeviceConnected(deviceMacAddress3, deviceCommonInfo3, serviceIds3);
		// Instantiate new devices
		ls = new LightSensor(deviceServiceId1, deviceMacAddress1, deviceId1, eventManager);
		lightSensorCount++;
		ls2 = new LightSensor(deviceServiceId2, deviceMacAddress2, deviceId2, eventManager);
		lightSensorCount++;
		ls3 = new LightSensor(deviceServiceId3, deviceMacAddress3, deviceId3, eventManager);
		lightSensorCount++;
 
		Object lock = new Object();			
		synchronized(lock) {
			properties = new Hashtable<String, String>();
			properties.put("serviceId", deviceServiceId1);
			properties.put("deviceMacAddress", deviceMacAddress1);
			lsReg = bc.registerService(IDriverService.class.getName(), ls, properties);
			
			properties = new Hashtable<String, String>();
			properties.put("serviceId", deviceServiceId2);
			properties.put("deviceMacAddress", deviceMacAddress2);
			lsReg2 = bc.registerService(IDriverService.class.getName(), ls2, properties);
			
			properties = new Hashtable<String, String>();
			properties.put("serviceId", deviceServiceId3);
			properties.put("deviceMacAddress", deviceMacAddress3);
			lsReg3 = bc.registerService(IDriverService.class.getName(), ls3, properties);
		}
	}
	
	
	public void stop(BundleContext context) throws Exception {
		
		stopappli();
		
		if(lsReg != null){
			lsReg.unregister();
		}	
		if(lsReg2 != null){
			lsReg2.unregister();
		}
		if(lsReg3 != null){
			lsReg3.unregister();
		}
		
		if(laReg != null){
			laReg.unregister();
		}
	}
	
	public void stopappli(){

		/* Light sensors */
		if (ls!=null){
			deviceManager.fireDeviceDisconnected("family1", deviceMacAddress1);
			ls = null;
		}
		else {
			System.out.println("no ls");
		}
		if (ls2!=null){
			deviceManager.fireDeviceDisconnected("family1", deviceMacAddress2);
			ls2 = null;
		}
		else {
			System.out.println("no ls2");
		}
		if (ls3!=null){
			deviceManager.fireDeviceDisconnected("family1", deviceMacAddress3);
			ls3 = null;
		}
		else {
			System.out.println("no ls3");
		}
	}
	
	/* --- Injections --- */
	public IEventMgr getEventManager() { return eventManager; }
	public void setEventManager(IEventMgr eventManager) { 
		if (null == eventManager) {
			LOG.error("[COMM02] EventManager not available");
		}
		this.eventManager = eventManager;
	}
	
	public IDeviceManager getDeviceManager () { return this.deviceManager; }
	public void setDeviceManager (IDeviceManager deviceManager) {
		if (null == deviceManager) {
			LOG.error("[DEVI01] DeviceManager not available");
		}
		this.deviceManager = deviceManager;
	}
	
	public void setBundleContext(BundleContext bc) { this.bc =  bc; }
}