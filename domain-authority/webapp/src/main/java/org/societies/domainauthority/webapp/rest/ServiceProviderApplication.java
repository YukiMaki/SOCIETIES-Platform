package org.societies.domainauthority.webapp.rest;

import java.security.Security;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class ServiceProviderApplication extends Application {
	
	/**
	 * We use this constructor for application wide initializations.
	 */
	public ServiceProviderApplication() {
		
		//Security.addProvider(new BouncyCastleProvider());
				
		//org.apache.xml.security.Init.init();
	}
	
	
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> result = new HashSet<Class<?>>();
		
		result.add(ServiceJar.class);
		
		return result;
	}	
}
