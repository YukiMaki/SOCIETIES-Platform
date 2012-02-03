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
package org.societies.slm.servicemgmt.api;

import java.util.Collection;
import org.societies.api.internal.servicelifecycle.model.Service;
import org.societies.api.internal.servicelifecycle.model.ServiceResourceIdentifier;
import org.societies.slm.servicemgmt.impl.ServiceMgmtException;
/**
 * 
 * @author pkuppuud
 *
 */
public interface IServiceManagement {
	/**
	 * This method checks virgo-osgi container whether the service registry
	 * is available  
	 * @return true if service registry is running.
	 */
	public boolean IsServiceRegistryActive();
	/**
	 * This method is used to delete the existing service entries 
	 * from service registry data base.
	 * @return void
	 * @throws ServiceMgmtException
	 */
	public void cleanServiceRegistry() throws ServiceMgmtException;
	/**
	 * This method starts the service by using service resource identifier
	 * @param serviceId unique service identifier
	 * @throws ServiceMgmtException if fails to start service
	 */
	public void startService(ServiceResourceIdentifier serviceId) throws ServiceMgmtException;
	/**
	 * This method Stop the service by using service resource identifier
	 * @param serviceId unique service identifier
	 * @throws ServiceMgmtException if failed to identify or stop the service
	 */
	public void stopService(ServiceResourceIdentifier serviceId) throws ServiceMgmtException;
	/**
	 * This method returns the status of the service
	 * @return ServiceStatus
	 * @throws ServiceMgmtException if service not found
	 */
	public ServiceStatus getServiceStatus(ServiceResourceIdentifier serviceId) throws ServiceMgmtException;
	/**
	 * add services to service registry
	 * @throws ServiceMgmtException
	 */
	public void addServices() throws ServiceMgmtException;
	/**
	 * Remove service from service registry
	 * Returns a collection of CSS Service objects
	 */
	public void removeServices() throws ServiceMgmtException;
	/**
	 * This method updates service registry with updated service meta data
	 * @throws ServiceMgmtException
	 */
	public void updateServices() throws ServiceMgmtException;
	/**
	 * This method is used to query list of services registered in service registry
	 * @return collection of services  
	 * @throws ServiceMgmtException
	 */	
	public Collection<Service> findAllServices() throws ServiceMgmtException;
	/**
	 * Find the service by service resource identifier
	 * @return service object
	 * @param serviceId
	 */
	public Service findService(ServiceResourceIdentifier serviceId)
			throws ServiceMgmtException;

}
