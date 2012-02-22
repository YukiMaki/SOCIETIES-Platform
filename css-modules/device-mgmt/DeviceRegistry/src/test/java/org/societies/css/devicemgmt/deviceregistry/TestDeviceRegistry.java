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

package org.societies.css.devicemgmt.deviceregistry;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.societies.css.devicemgmt.deviceregistry.CSSDevice;

public class TestDeviceRegistry {
	
	private String deviceName_1 = "Device1";
    private String deviceDescription = "this is a good device";
    private String deviceId = "liam.societies.org/first/service";
    private String deviceType = "lightSensor";
    private String deviceName_2 = "Device2";
    private String deviceDescription2 = "this is a fair device";
    private String deviceId2 = "liam.societies.org/second/service";
    private String deviceType2 = "TempSensor";
    private String deviceName_3 = "Device3";
    private String deviceDescription3 = "this is a bad device";
    private String deviceId3 = "liam.societies.org/third/service";
    private String deviceType3 = "GPSSensor";
	
	private DeviceRegistry registry;
	private CSSDevice device_1;
	private CSSDevice device_2;
	private CSSDevice device_3;
	
	
	private String CSSID = "liam@societies.org";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
        
        registry = DeviceRegistry.getInstance();
        assertTrue(null != registry);
        registry.clearRegistry();

        //Create mock CSSDevice
        
        
        device_1 = new CSSDevice(deviceName_1, deviceDescription, deviceId, deviceType);
        assertTrue(null != device_1);
        
        device_2 = new CSSDevice(deviceName_2, deviceDescription2, deviceId2, deviceType2);
        assertTrue(null != device_2);
        
        device_3 = new CSSDevice(deviceName_3, deviceDescription3, deviceId3, deviceType3);
        assertTrue(null != device_3);
        
	}

	@After
	public void tearDown() throws Exception {
		
		registry.clearRegistry();
        assertEquals(0, registry.registrySize());
        registry = null;
        device_1 = null;
        device_2 = null;
        device_3 = null;
	}

	@Test
	public void addaDevice() throws Exception{
		//fail("Tests Not yet implemented just putting in place holder");
		String result =  registry.addDevice(device_1, CSSID);
		assertTrue(null != result);
		assertEquals(1, registry.registrySize());
		registry.clearRegistry();
        assertEquals(0, registry.registrySize());
	}
	
	@Test
	public void addmoreDevices() throws Exception{
		//fail("Tests Not yet implemented just putting in place holder");
		String result1 =  registry.addDevice(device_1, CSSID);
		assertTrue(null != result1);
		String result2 =  registry.addDevice(device_2, CSSID);
		assertTrue(null != result2);
		String result3 =  registry.addDevice(device_3, CSSID);
		assertTrue(null != result3);
		assertEquals(3, registry.registrySize());
		//registry.clearRegistry();
        //assertEquals(0, registry.registrySize());
		System.out.println("Device ID is  = " + device_1.getdeviceId());
		System.out.println("Device Name is  = " + device_1.getdeviceName());
		System.out.println("Device Type is  = " + device_1.getdeviceType());
		System.out.println("Device Description is  = " + device_1.getdeviceDescription());
		System.out.println("CSSID is  = " + CSSID);
        Collection<CSSDevice> alldevices =  registry.findAllDevices();
        assertTrue(null != alldevices);
        assertEquals(3, alldevices.size());
            
	}
	
	@Test
	public void allDevices() throws Exception{
		String result1 =  registry.addDevice(device_1, CSSID);
		assertTrue(null != result1);
		String result2 =  registry.addDevice(device_2, CSSID);
		assertTrue(null != result2);
		String result3 =  registry.addDevice(device_3, CSSID);
		assertTrue(null != result3);
		assertEquals(3, registry.registrySize());
		
        Collection<CSSDevice> alldevices =  registry.findAllDevices();
        assertTrue(null != alldevices);
        assertEquals(3, alldevices.size());
            
	}
	
	
	@Test
	public void removeDevice() throws Exception{
		String result1 =  registry.addDevice(device_1, CSSID);
		assertTrue(null != result1);
		String result2 =  registry.addDevice(device_2, CSSID);
		assertTrue(null != result2);
		String result3 =  registry.addDevice(device_3, CSSID);
		assertTrue(null != result3);
		assertEquals(3, registry.registrySize());
		
		assertTrue(registry.deleteDevice(device_1, CSSID));
		assertTrue(registry.unregisterDevice(deviceId2));
		
        Collection<CSSDevice> alldevices =  registry.findAllDevices();
        assertTrue(null != alldevices);
        assertEquals(1, alldevices.size());
            
	}
	
	@Test
	public void findDevice() throws Exception{
		String result1 =  registry.addDevice(device_1, CSSID);
		assertTrue(null != result1);
		String result2 =  registry.addDevice(device_2, CSSID);
		assertTrue(null != result2);
		String result3 =  registry.addDevice(device_3, CSSID);
		assertTrue(null != result3);
		assertEquals(3, registry.registrySize());

        CSSDevice retrievedevice = registry.findDevice(deviceId2);
        
        System.out.println("retrievedevice ID is  = " + retrievedevice.getdeviceId());
		System.out.println("retrievedevice Name is  = " + retrievedevice.getdeviceName());
		System.out.println("retrievedevice Type is  = " + retrievedevice.getdeviceType());
		System.out.println("retrievedevice Description is  = " + retrievedevice.getdeviceDescription());

        assertTrue(null != retrievedevice);
        assertTrue(retrievedevice instanceof CSSDevice);
            
	}
	
	@Test
	public void findDeviceType() throws Exception{
		Collection<CSSDevice> Result = null;
		
		String result1 =  registry.addDevice(device_1, CSSID);
		assertTrue(null != result1);
		String result2 =  registry.addDevice(device_2, CSSID);
		assertTrue(null != result2);
		String result3 =  registry.addDevice(device_3, CSSID);
		assertTrue(null != result3);
		assertEquals(3, registry.registrySize());
		System.out.println("reg size = " + registry.registrySize());
		
		Result = registry.findByDeviceType(device_1.getdeviceType());
        assertEquals(1, Result.size());
		System.out.println("query Result size = " + Result.size());

        Result = registry.findByDeviceType(deviceType2);
        assertEquals(1, Result.size());

        Result = registry.findByDeviceType(deviceType3);
        assertEquals(1, Result.size());

            
	}

}
