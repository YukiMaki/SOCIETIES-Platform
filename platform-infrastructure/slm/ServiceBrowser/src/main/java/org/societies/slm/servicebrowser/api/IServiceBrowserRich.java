package org.societies.slm.servicebrowser.api;

import java.util.List;

public interface IServiceBrowserRich {
	/*
	 * Description: Based on a filter this method returns the services list 
	 * 				matching that particular filter
	 * @return the whole list of services retrieved
	 */
	public List<Object> GetServices (Object filter);

	/* 
	 * Description: This method invokes the selected Services giving access to 
	 * 				the Service RTE to make the service runs 
	 * @return
	 */

	public void InvokeSelectedService (Object serviceID);
	
	/* 
	 * Description: This method gives out more details on a particular selected service 
	 * @return represents an Object containing details on the service
	 */

	public Object GetServiceDetail (Object serviceID);
}