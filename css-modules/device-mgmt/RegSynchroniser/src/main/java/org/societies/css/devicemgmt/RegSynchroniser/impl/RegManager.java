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

package org.societies.css.devicemgmt.RegSynchroniser.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.LogManager;

import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.osgi.context.BundleContextAware;
import org.societies.api.internal.css.devicemgmt.IDeviceRegistry;
import org.societies.css.devicemgmt.deviceregistry.DeviceRegistry;
//import org.societies.css.devicemgmt.deviceregistry.CSSDevice;
//import org.societies.css.devicemgmt.deviceregistry.IDeviceRegistry;
import org.societies.api.internal.css.devicemgmt.ILocalDevice;
//import org.societies.css.devicemgmt.RegSynchroniser.impl.LocalDevices;
import org.societies.api.internal.css.devicemgmt.model.DeviceCommonInfo;

public class RegManager implements ILocalDevice, BundleContextAware{

	//private static org.apache.commons.logging.Log LOG = LogFactory.getLog(RegManager.class);
    private IDeviceRegistry deviceRegistry;
    private BundleContext bundleContext;
    
    
    
    /**
     * Default Constructor
     * 
     */
    public RegManager() {
    
    }

    /**
     * Constructor
     * 
     * @param context
     */
    
    public RegManager(BundleContext bundlecontext) {
                
        //Log("Synchroniser Manager created", this.LOG);
        
    	this.bundleContext = bundlecontext;
        
        this.deviceRegistry = DeviceRegistry.getInstance();

    }

   // public void initiateSearch() {
   // }

    /**
     * Register an events listener with the container
     * 
     * @param listener
     * @param filterOption
     */

////////////////////////////////////////////////////////////////////////////////////////////////////////
// need to register event listeners and call commsMgr eventing system to fire new events
////////////////////////////////////////////////////////////////////////////////////////////////////////
    

    /**
     * Add a device to the device Registry
     * 
     * @param device
     */
    public boolean addDevice(DeviceCommonInfo device, String CSSID) throws Exception {

        boolean retValue = true;
        
        retValue = LocalDevices.addDevice(device, CSSID);
        
        return retValue;
    }

    /**
     * Convenience method to add a collection of devices
     */

    public boolean addDevices(Collection<DeviceCommonInfo> deviceCollection, String CSSID)
            throws Exception {
        boolean retValue = true;

        for (DeviceCommonInfo device : deviceCollection) {
            if (!this.addDevice(device, CSSID)) {
                retValue = false;
                break;
            }
        }
        return retValue;
    }

    /**
     * Remove a device
     * 
     * @param device
     */
    public boolean removeDevice(DeviceCommonInfo device, String CSSID)
            throws Exception {
        

        return LocalDevices.removeDevice(device, CSSID);
    }

    /**
     * Convenience method to remove a collection of devices
     */
    public boolean removeDevices(
            Collection<DeviceCommonInfo> deviceCollection, String CSSID)
            throws Exception {

        boolean retValue = true;

        for (DeviceCommonInfo device : deviceCollection) {
            if (!this.removeDevice(device, CSSID)) {
                retValue = false;
                break;
            }
        }
        return retValue;
    }


    /**
     * Clear the registry
     */
    public boolean clearRegistry() throws Exception {
        boolean retValue = false;

        this.deviceRegistry.clearRegistry();

        if (0 == this.deviceRegistry.registrySize()) {
            retValue = true;
        }
        return retValue;
    }

	@Override
	public void setBundleContext(BundleContext arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean removedevice(String deviceID, String CSSID) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

//	public boolean removedevices(Collection<String> deviceCollection)
	//		throws Exception {
		// TODO Auto-generated method stub
		//return false;
	//}

	//@Override
//	public boolean removeDevice(String deviceID, String CSSID) throws Exception {
		// TODO Auto-generated method stub
	//	return false;
	//}
}