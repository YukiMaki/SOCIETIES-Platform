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
package org.societies.css.devicemanager.schema;

/**
 * @author Rafik
 *
 */
public class DeviceDescription {
	
	
	public class DriverService {
		
		private String serviceName;
		private String serviceDescription;
		private String name;

		public String getServiceName() {
			return serviceName;
		}
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		public String getServiceDescription() {
			return serviceDescription;
		}
		public void setServiceDescription(String serviceDescription) {
			this.serviceDescription = serviceDescription;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	public class DeviceAction {
		
		private String serviceName;
		private String actionName;
		private String actionDescription;
		private String name;
		private String inputArgumentsNames [];
		private String outputArgumentsName [];

		public String getServiceName() {
			return serviceName;
		}
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		public String getActionName() {
			return actionName;
		}
		public void setActionName(String actionName) {
			this.actionName = actionName;
		}
		public String getActionDescription() {
			return actionDescription;
		}
		public void setActionDescription(String actionDescription) {
			this.actionDescription = actionDescription;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String[] getInputArgumentsNames() {
			return inputArgumentsNames;
		}
		public void setInputArgumentsNames(String[] inputArgumentsNames) {
			this.inputArgumentsNames = inputArgumentsNames;
		}
		public String[] getOutputArgumentsName() {
			return outputArgumentsName;
		}
		public void setOutputArgumentsName(String[] outputArgumentsName) {
			this.outputArgumentsName = outputArgumentsName;
		}
	}
	
	public class DeviceStateVariable {
		private String serviceName;
		private String actionName;
		private String allowedValues [];
		private String defaultValue;
		private String dataJavaType;
		private String maximumValue;
		private String minimumValue;
		private String name;
		private String step;
		private String deviceMgmtDataType;
		private boolean isEnventable;
		private String description;
		
		public String getServiceName() {
			return serviceName;
		}
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		public String getActionName() {
			return actionName;
		}
		public void setActionName(String actionName) {
			this.actionName = actionName;
		}
		public String[] getAllowedValues() {
			return allowedValues;
		}
		public void setAllowedValues(String[] allowedValues) {
			this.allowedValues = allowedValues;
		}
		public String getDefaultValue() {
			return defaultValue;
		}
		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}
		public String getDataJavaType() {
			return dataJavaType;
		}
		public void setDataJavaType(String dataJavaType) {
			this.dataJavaType = dataJavaType;
		}
		public String getMaximumValue() {
			return maximumValue;
		}
		public void setMaximumValue(String maximumValue) {
			this.maximumValue = maximumValue;
		}
		public String getMinimumValue() {
			return minimumValue;
		}
		public void setMinimumValue(String minimumValue) {
			this.minimumValue = minimumValue;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getStep() {
			return step;
		}
		public void setStep(String step) {
			this.step = step;
		}
		public String getDeviceMgmtDataType() {
			return deviceMgmtDataType;
		}
		public void setDeviceMgmtDataType(String deviceMgmtDataType) {
			this.deviceMgmtDataType = deviceMgmtDataType;
		}
		public boolean isEnventable() {
			return isEnventable;
		}
		public void setEnventable(boolean isEnventable) {
			this.isEnventable = isEnventable;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}
	
	
	private DriverService driverServices [];
	private DeviceAction deviceActions [];
	private DeviceStateVariable deviceStateVariables [];

	public DriverService[] getDriverServices() {
		return driverServices;
	}
	public void setDriverServices(DriverService[] driverServices) {
		this.driverServices = driverServices;
	}
	public DeviceAction[] getDeviceActions() {
		return deviceActions;
	}
	public void setDeviceActions(DeviceAction[] deviceActions) {
		this.deviceActions = deviceActions;
	}
	public DeviceStateVariable[] getDeviceStateVariables() {
		return deviceStateVariables;
	}
	public void setDeviceStateVariables(DeviceStateVariable[] deviceStateVariables) {
		this.deviceStateVariables = deviceStateVariables;
	}
}
