/**
 * Copyright (c) 2011, SOCIETIES Consortium (WATERFORD INSTITUTE OF TECHNOLOGY (TSSG), HERIOT-WATT UNIVERSITY (HWU), SOLUTA.NET 
 * (SN), GERMAN AEROSPACE CENTRE (Deutsches Zentrum fuer Luft- und Raumfahrt e.V.) (DLR), Zavod za varnostne tehnologije
 * informacijske druÅ¾be in elektronsko poslovanje (SETCCE), INSTITUTE OF COMMUNICATION AND COMPUTER SYSTEMS (ICCS), LAKE
 * COMMUNICATIONS (LAKE), INTEL PERFORMANCE LEARNING SOLUTIONS LTD (INTEL), PORTUGAL TELECOM INOVAÃ‡ÃƒO, SA (PTIN), IBM Corp., 
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
package org.societies.slm.servicecontrol;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.societies.api.internal.servicelifecycle.serviceRegistry.IServiceRegistry;
import org.societies.api.internal.servicelifecycle.serviceRegistry.exception.ServiceNotFoundException;
import org.societies.api.internal.servicelifecycle.serviceRegistry.exception.ServiceRegistrationException;
import org.societies.api.internal.servicelifecycle.serviceRegistry.exception.ServiceRetrieveException;
import org.societies.api.schema.servicelifecycle.model.Service;
import org.societies.api.schema.servicelifecycle.model.ServiceImplementation;
import org.societies.api.schema.servicelifecycle.model.ServiceLocation;
import org.societies.api.schema.servicelifecycle.model.ServiceResourceIdentifier;
import org.societies.api.schema.servicelifecycle.model.ServiceStatus;
import org.societies.api.schema.servicelifecycle.model.ServiceInstance;
import org.societies.api.schema.servicelifecycle.model.ServiceType;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.osgi.util.OsgiListenerUtils;

/**
 * 
 * This class implements the Service Registry Listener, that registers and unregisters services
 * 
 * @author pkuppuud
 * @author mmanniox
 * @author <a href="mailto:sanchocsa@gmail.com">Sancho Rêgo</a> (PTIN)
 * 
 */

public class ServiceRegistryListener implements BundleContextAware,
		ServiceListener, BundleListener {

	private BundleContext bctx;
	private static Logger log = LoggerFactory.getLogger(ServiceRegistryListener.class);
	private IServiceRegistry serviceReg;
	private ICommManager commMngr;
	

	public IServiceRegistry getServiceReg() {
		return serviceReg;
	}

	public void setServiceReg(IServiceRegistry serviceReg) {
		this.serviceReg = serviceReg;
	}

	/**
	 * @return the commMngr
	 */
	public ICommManager getCommMngr() {
		return commMngr;
	}
	
	/**
	 * @param commMngr the commMngr to set
	 */
	public void setCommMngr(ICommManager commMngr) {
		this.commMngr = commMngr;
	}
	
	public ServiceRegistryListener() {
		log.info("Service RegistryListener Bean Instantiated");
	}

	public void registerListener() {
		Filter fltr = null;
		
		if(log.isDebugEnabled()) 
			log.debug("Registering Listener!");
		
		try {
			fltr = this.bctx.createFilter("(TargetPlatform=SOCIETIES)");
			log.info("Service Filter Registered");
		} catch (InvalidSyntaxException e) {
			log.error("Error creating Service Listener Filter");
			e.printStackTrace();
		}
		OsgiListenerUtils.addServiceListener(this.bctx, this, fltr);
		
		log.info("Bundle Listener Registered");
		this.bctx.addBundleListener(this);
	}

	public void unRegisterListener() {
		log.info("Service Management unregistering service listener");
		OsgiListenerUtils.removeServiceListener(this.bctx, this);
		log.info("Service Management unregistering bundle listener");
		this.bctx.removeBundleListener(this);
	}

	@Override
	public void setBundleContext(BundleContext ctx) {
		this.bctx = ctx;
	}

	@Override
	public void serviceChanged(ServiceEvent event) {

		// Map<String, Object> serviceMeteData = new HashMap<String, Object>();

		if(log.isDebugEnabled())
			log.debug("Service Listener event received");
		
		Bundle serBndl = event.getServiceReference().getBundle();
		String propKeys[] = event.getServiceReference().getPropertyKeys();

		if(log.isDebugEnabled()){
			for (String key : propKeys) {
				log.debug("Property Key: " + key);
				Object value = event.getServiceReference().getProperty(key);
				log.debug("Property value: " + value);
				// serviceMeteData.put(key, value);
			}
		
		log.debug("Bundle Id: " + serBndl.getBundleId() + " Bundle State: "
				+ serBndl.getState() + "  Bundle Symbolic Name: "
				+ serBndl.getSymbolicName());
		}
		
		Service service = (Service) event.getServiceReference().getProperty(
				"ServiceMetaModel");
		
		if(service==null || (!(service instanceof Service) )){
			if(log.isDebugEnabled()) log.debug("**Service MetadataModel object is null**");
			return;
		}
		
		
		service.setServiceLocation(ServiceLocation.LOCAL);
		service.setServiceType(ServiceType.THIRD_PARTY_SERVICE);
		
		service.setServiceEndpoint(commMngr.getIdManager().getThisNetworkNode().getJid()  + "/" +  service.getServiceName().replaceAll(" ", ""));

		//TODO: Do this properly!
		ServiceInstance si = new ServiceInstance();
		si.setFullJid(commMngr.getIdManager().getThisNetworkNode().getJid());
		si.setXMPPNode(commMngr.getIdManager().getThisNetworkNode().getJid());
		
		ServiceImplementation servImpl = new ServiceImplementation();
		servImpl.setServiceVersion((String)event.getServiceReference().getProperty("Bundle-Version"));
		servImpl.setServiceNameSpace(serBndl.getSymbolicName());
		servImpl.setServiceProvider((String) event.getServiceReference().getProperty("ServiceProvider"));
		
		si.setServiceImpl(servImpl);
		service.setServiceInstance(si);
		
		if(log.isDebugEnabled()){
			log.debug("**Service MetadataModel Data Read**");
			log.debug("Service Name: "+service.getServiceName());
			log.debug("Service Description: "+service.getServiceDescription());
			log.debug("Service type: "+service.getServiceType().toString());
			log.debug("Service Location: "+service.getServiceLocation().toString());
			log.debug("Service Endpoint: "+service.getServiceEndpoint());
			log.debug("Service Provider: "+service.getServiceInstance().getServiceImpl().getServiceProvider());
			log.debug("Service Namespace: "+service.getServiceInstance().getServiceImpl().getServiceNameSpace());
			log.debug("Service Version: "+service.getServiceInstance().getServiceImpl().getServiceVersion());
			log.debug("Service XMPPNode: "+service.getServiceInstance().getXMPPNode());
			log.debug("Service FullJid: "+service.getServiceInstance().getFullJid());
		}
		
		service.setServiceStatus(ServiceStatus.STARTED);
		
		List<Service> serviceList = new ArrayList<Service>();
		switch (event.getType()) {

		case ServiceEvent.MODIFIED:
			if(log.isDebugEnabled()) log.debug("Service Modification");
			service.setServiceIdentifier(ServiceMetaDataUtils.generateServiceResourceIdentifier(service, serBndl));
			
			try {
				serviceList.add(service);
				this.getServiceReg().unregisterServiceList(serviceList);
				this.getServiceReg().registerServiceList(serviceList);
				
			} catch (ServiceRegistrationException e) {
				log.debug("Error while modifying service meta data");
				e.printStackTrace();
			}
			break;
		case ServiceEvent.REGISTERED:
			
			if(log.isDebugEnabled()) log.debug("Service Registered");			
			service.setServiceIdentifier(ServiceMetaDataUtils.generateServiceResourceIdentifier(service, serBndl));
			serviceList.add(service);
			
			try {
				if(log.isDebugEnabled()) log.debug("First, checking if the service already exists...");
			
				Service existService = this.getServiceReg().retrieveService(service.getServiceIdentifier());
				
				if(existService == null){
					if(log.isDebugEnabled()) log.debug("Registering Service: " + service.getServiceName());
					this.getServiceReg().registerServiceList(serviceList);
					
					//The service is now registered, so we update the hashmap
					if(ServiceControl.installingBundle(serBndl.getBundleId())){
						if(log.isDebugEnabled())
							log.debug("ServiceControl is installing the bundle, so we need to tell it it's done");
						ServiceControl.serviceInstalled(serBndl.getBundleId(), service);
					}
					
				} else{
					if(log.isDebugEnabled()) log.debug(service.getServiceName() + " already exists, setting status to STARTED");
					this.getServiceReg().changeStatusOfService(service.getServiceIdentifier(), ServiceStatus.STARTED);
				}
				
			} catch (Exception e) {
				log.error("Error while persisting service meta data");
				e.printStackTrace();
			}
			break;
			
		case ServiceEvent.UNREGISTERING:
			if(log.isDebugEnabled()) log.debug("Service Unregistered, so we set it to stopped but do not remove from registry");			
			service.setServiceIdentifier(ServiceMetaDataUtils.generateServiceResourceIdentifier(service, serBndl));
			
			try {
				this.getServiceReg().changeStatusOfService(service.getServiceIdentifier(), ServiceStatus.STOPPED);
			} catch (ServiceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void bundleChanged(BundleEvent event) {
				
		if( event.getType() != BundleEvent.UNINSTALLED ){
			return;
		}
		
		if(log.isDebugEnabled())
			log.debug("Bundle Uninstalled Event arrived!");
		// Now we search for services in the registry corresponding to this bundle.
		Service serviceToRemove = getServiceFromBundle(event.getBundle());
		
		if(serviceToRemove == null){
			if(log.isDebugEnabled())
				log.debug("It was not a SOCIETIES-related bundle, ignoring event!");
			return;
		}
		
		if(log.isDebugEnabled())
			log.debug("Uninstalled bundle that had service: " + serviceToRemove.getServiceEndpoint());
			
			
		List<Service> servicesToRemove = new ArrayList<Service>();
		servicesToRemove.add(serviceToRemove);
			
		if(log.isDebugEnabled()) log.debug("Removing service: " + serviceToRemove.getServiceName() + " from SOCIETIES Registry");
			
		try {
			getServiceReg().unregisterServiceList(servicesToRemove);
			log.info("Service " + serviceToRemove.getServiceName() + " has been uninstalled");

		} catch (ServiceRegistrationException e) {
			e.printStackTrace();
			log.error("Exception while unregistering service: " + e.getMessage());
		}
		
	}
	
	
	/**
	 * This method is used to obtain the Service that is exposed by given Bundle
	 * 
	 * @param The Bundle that exposes this service
	 * @return The Service object whose bundle we wish to find
	 */
	private Service getServiceFromBundle(Bundle bundle) {
		
		if(log.isDebugEnabled()) log.debug("Obtaining Service that corresponds to a bundle: " + bundle.getSymbolicName() + " with Id " + bundle.getBundleId());
		
		// Preparing the search filter		
		Service filter = ServiceMetaDataUtils.generateEmptyFilter();
		filter.getServiceIdentifier().setServiceInstanceIdentifier(String.valueOf(bundle.getBundleId()));
		filter.getServiceInstance().getServiceImpl().setServiceVersion(bundle.getVersion().toString());
		
		List<Service> listServices;
		try {
			listServices = getServiceReg().findServices(filter);
		} catch (ServiceRetrieveException e) {
			log.error("Exception while searching for services:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		

		if(listServices == null || listServices.isEmpty()){
			if(log.isDebugEnabled()) log.debug("Couldn't find any services that fulfill the criteria");
			return null;
		} 
		
		Service result = null;
		/*
		if(listServices.size() > 1){
			if(log.isDebugEnabled()) log.debug("More than one service found... this is not good! Time to fix it.");
			
			for(Service service: listServices){
				Long serBundleId = Long.parseLong(service.getServiceIdentifier().getServiceInstanceIdentifier());
				
				if(log.isDebugEnabled())
					log.debug("Checking service " + service.getServiceName() + " with bundleId " + service.getServiceIdentifier().getServiceInstanceIdentifier());
				
				if(serBundleId == bundle.getBundleId()){
					result = service;
					break;
				}
			}
			
		} else{
			result = listServices.get(0);
		}
		
		*/
		
		for(Service service: listServices){
			Long serBundleId = Long.parseLong(service.getServiceIdentifier().getServiceInstanceIdentifier());
			
			if(log.isDebugEnabled())
				log.debug("Checking service " + service.getServiceName() + " with bundleId " + service.getServiceIdentifier().getServiceInstanceIdentifier());
			
			if(serBundleId == bundle.getBundleId()){
				result = service;
				break;
			}
		}
		
		 if(log.isDebugEnabled() && result != null) 
			 log.debug("The service corresponding to bundle " + bundle.getSymbolicName() + "is "+ result.getServiceName() );
			
		// Finally, we return
		 return result;
		 
	}
}
