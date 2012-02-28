package org.societies.slm.servicemgmt.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.societies.api.internal.servicelifecycle.model.Service;
import org.societies.api.internal.servicelifecycle.model.ServiceResourceIdentifier;
import org.societies.api.internal.servicelifecycle.serviceMgmt.IServiceManagement;
import org.societies.api.internal.servicelifecycle.serviceMgmt.ServiceMgmtException;
import org.societies.api.internal.servicelifecycle.serviceMgmt.ServiceStatus;
import org.societies.api.internal.servicelifecycle.serviceRegistry.IServiceRegistry;
import org.societies.api.internal.servicelifecycle.serviceRegistry.exception.ServiceRegistrationException;


public class ServiceManagement implements IServiceManagement{

	private XMLServiceMetaDataToObjectConverter converter;
	private IServiceRegistry serviceReg;
	
	public XMLServiceMetaDataToObjectConverter getConverter() {
		return converter;
	}

	public void setConverter(XMLServiceMetaDataToObjectConverter converter) {
		this.converter = converter;
	}	
	
	public IServiceRegistry getServiceReg() {
		return serviceReg;
	}

	public void setServiceReg(IServiceRegistry serviceReg) {
		this.serviceReg = serviceReg;
	}

	public boolean IsServiceRegistryActive() {
		// TODO Auto-generated method stub
		return false;
	}

	public void cleanServiceRegistry() {
		// TODO Auto-generated method stub
		
	}

	public void startService(ServiceResourceIdentifier serviceId) {
		// TODO Auto-generated method stub
		
	}

	public void stopService(ServiceResourceIdentifier serviceId) {
		// TODO Auto-generated method stub
		
	}

	public ServiceStatus getServiceStatus(ServiceResourceIdentifier serviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addServices() {
		// TODO Auto-generated method stub
		
	}

	public void removeServices() {
		// TODO Auto-generated method stub
		
	}

	public void updateServices() {
		// TODO Auto-generated method stub
		
	}

	public Collection<Service> findAllServices() {
		// TODO Auto-generated method stub
		return null;
	}

	public Service findService(ServiceResourceIdentifier serviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processServiceMetaData(List<File> serviceMetaFileList) throws ServiceMgmtException {
	
		if(serviceMetaFileList.isEmpty()){
			throw (new ServiceMgmtException("Service Metadata File list is empty"));			
		}
		Iterator<File> itr = serviceMetaFileList.iterator();
		while (itr.hasNext()) {
		System.out.println("***file Url****" + itr.next().getAbsolutePath());	
		}			
		
		List<Service> serviceList=new ArrayList<Service>();	
		try {		
			Iterator<File> iterator = serviceMetaFileList.iterator();
			while (iterator.hasNext()) {
				Service service =(Service) converter.convertFromXMLFileToObject(iterator.next());
				serviceList.add(service);}			
			
		} catch (IOException e) {
			System.out.println("IOException...while calling convertFromXMLFileToObject method");
			e.printStackTrace();
			throw (new ServiceMgmtException("Service Metadata file error", e));			
		}
		
		try {
			this.serviceReg.registerServiceList(serviceList);
		} catch (ServiceRegistrationException e) {
			throw (new ServiceMgmtException("Service Registration error", e));
		}
	}	
}