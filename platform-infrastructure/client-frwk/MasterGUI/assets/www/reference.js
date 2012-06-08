/**
Copyright (c) 2011, SOCIETIES Consortium (WATERFORD INSTITUTE OF TECHNOLOGY (TSSG), HERIOT-WATT UNIVERSITY (HWU), SOLUTA.NET 

(SN), GERMAN AEROSPACE CENTRE (Deutsches Zentrum fuer Luft- und Raumfahrt e.V.) (DLR), Zavod za varnostne tehnologije
informacijske družbe in elektronsko poslovanje (SETCCE), INSTITUTE OF COMMUNICATION AND COMPUTER SYSTEMS (ICCS), LAKE
COMMUNICATIONS (LAKE), INTEL PERFORMANCE LEARNING SOLUTIONS LTD (INTEL), PORTUGAL TELECOM INOVAÇÃO, SA (PTIN), IBM Corp (IBM),
INSTITUT TELECOM (ITSUD), AMITEC DIACHYTI EFYIA PLIROFORIKI KAI EPIKINONIES ETERIA PERIORISMENIS EFTHINIS (AMITEC), TELECOM 
ITALIA S.p.a.(TI), TRIALOG (TRIALOG), Stiftelsen SINTEF (SINTEF), NEC EUROPE LTD (NEC))
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */



/**
 * Societies PhoneGap/Cordova plugins namespace
 * 
 * @namespace Societies
 */

var Societies = {
		
		/**
		 * Provides an API to access device status indicators
		 * 
		 * @memberOf Societies
		 * @namespace Societies.DeviceStatus
		 */
		DeviceStatus: {
				/**
				 * 
				 * @methodOf Societies.DeviceStatus#
				 * @description To retrieve the connectivity provider status
				 * 
				 * @param {Object} successCallback The callback which will be called when result is successful.
				 * Example of JSON result:
				 * <pre>
				 * {"isInternetEnabled":true, "providerList":[{"name":"WiFi", "enabled":true}, {"name":"mobile mms", "enabled":false}]}
				 * </pre>
				 * Schema of the JSON result:
				 * <pre>
				 * {
				 *  	"name":"ConnectivityProviderStatus",
				 *  	"properties":{
				 *  		"isInternetEnabled":{
				 *  			"required":true,
				 *  			"type":"boolean",
				 *  			"description":"To know if Internet is available or not"
				 *  		},
				 *  		"providerList":{
				 *  			"required":false,
				 *  			"type":"array",
				 *  			"description":"List of connectivity providers",
				 *  			"items":{
				 *  				"name":{
				 *  					"required":true,
				 *  					"type":"string",
				 *  					"description":"Name of the connectivity provider"
				 *  				},
				 *  				"enabled":{
				 *  					"required":true,
				 *  					"type":"boolean",
				 *  					"description":"To know if this provider is available or not"
				 *  				}
				 *  			}
				 *  		}
				 *  	}
				 * }
				 * </pre>
				 * @param {Object} failureCallback The callback which will be called when result encounters an error. (String result)
				 */
				getConnectivityStatus: function(successCallback, failureCallback){
					var parameters = null;
					return cordova.exec(
							successCallback,
							failureCallback,
							'DeviceStatus',
							'getConnectivityStatus',
							[parameters]);
				},


				/**
				 * @methodOf Societies.DeviceStatus#
				 * @description To retrieve the location provider status
				 * 
				 * @param {Object} successCallback The callback which will be called when result is successful.
				 * Example of JSON result:
				 * <pre>
				 * {"providerList":[{"name":"gps", "enabled":true}, {"name":"network", "enabled":false}]}
				 * </pre>
				 * Schema of the JSON result:
				 * <pre>
				 * {
				 *  	"name":"LocationProviderStatus",
				 *  	"properties":{
				 *  		"providerList":{
				 *  			"required":false,
				 *  			"type":"array",
				 *  			"description":"List of location providers",
				 *  			"items":{
				 *  				"name":{
				 *  					"required":true,
				 *  					"type":"string",
				 *  					"description":"Name of the location provider"
				 *  				},
				 *  				"enabled":{
				 *  					"required":true,
				 *  					"type":"boolean",
				 *  					"description":"To know if this provider is available or not"
				 *  				}
				 *  			}
				 *  		}
				 *  	}
				 * }
				 * </pre>
				 * @param {Object} failureCallback The callback which will be called when result encounters an error. (String result)
				 */
				getLocationStatus: function(successCallback, failureCallback){
					var parameters = null;
					return cordova.exec(
							successCallback,
							failureCallback,
							'DeviceStatus',
							'getLocationStatus',
							[parameters]);
				},

				/**
				 * 
				 * @methodOf Societies.DeviceStatus#
				 * @description To retrieve the battery status
				 * @param {Object} successCallback The callback which will be called when result is successful.
				 * Example of JSON result:
				 * <pre>
				 * {"scale":100,"plugged":1,"level":50,"status":2,"voltage":0,"temperature":0}
				 * </pre>
				 * Schema of the JSON result:
				 * <pre>
				 * {
				 *  	"name":"BatteryStatus",
				 *  	"properties":{
				 *  		"scale":{
				 *  			"required":false,
				 *  			"type":"number",
				 *  			"min":0,
				 *  			"description":"Scale"
				 *  		},
				 *  		"plugged":{
				 *  			"required":false,
				 *  			"type":"number",
				 *  			"enum": [BATTERY_NOT_PLUGGED, BATTERY_PLUGGED_AC, BATTERY_PLUGGED_USB],
				 *  			"description":"To know if the mobile is plugged or not"
				 *  		},
				 *  		"level":{
				 *  			"required":false,
				 *  			"type":"number",
				 *  			"min":0,
				 *  			"max":100,
				 *  			"description":"Level of battery (%)"
				 *  		},
				 *  		"status":{
				 *  			"required":false,
				 *  			"type":"number",
				 *  			"min":1,
				 *  			"max":5,
				 *  			"enum": [BATTERY_STATUS_UNKNOWN, BATTERY_STATUS_CHARGING, BATTERY_STATUS_DISCHARGING, BATTERY_STATUS_NOT_CHARGING, BATTERY_STATUS_FULL],
				 *  			"description":"Level of battery (%)"
				 *  		},
				 *  		"voltage":{
				 *  			"required":false,
				 *  			"type":"nomber",
				 *  			"description":"Voltage"
				 *  		},
				 *  		"temperature":{
				 *  			"required":false,
				 *  			"type":"number",
				 *  			"description":"Temperatue (°C)"
				 *  		}
				 *  	}
				 * }
				 * </pre>
				 * @param {Object} failureCallback The callback which will be called when result encounters an error. (String result)
				 */
				getBatteryStatus: function(successCallback, failureCallback){
					var parameters = null;
					return cordova.exec(
							successCallback,
							failureCallback,
							'DeviceStatus',
							'getBatteryStatus',
							[parameters]);
				},

				/**
				 * 
				 * @methodOf Societies.DeviceStatus#
				 * @description To register to battery status
				 * @param {Object} successCallback The callback which will be called when result is successful.
				 * Example of JSON result:
				 * <pre>
				 * {"scale":100,"plugged":1,"level":50,"status":2,"voltage":0,"temperature":0}
				 * </pre>
				 * Schema of the JSON result:
				 * <pre>
				 * {
				 *  	"name":"BatteryStatus",
				 *  	"properties":{
				 *  		"scale":{
				 *  			"required":false,
				 *  			"type":"number",
				 *  			"min":0,
				 *  			"description":"Scale"
				 *  		},
				 *  		"plugged":{
				 *  			"required":false,
				 *  			"type":"number",
				 *  			"enum": [BATTERY_NOT_PLUGGED, BATTERY_PLUGGED_AC, BATTERY_PLUGGED_USB],
				 *  			"description":"To know if the mobile is plugged or not"
				 *  		},
				 *  		"level":{
				 *  			"required":false,
				 *  			"type":"number",
				 *  			"min":0,
				 *  			"max":100,
				 *  			"description":"Level of battery (%)"
				 *  		},
				 *  		"status":{
				 *  			"required":false,
				 *  			"type":"number",
				 *  			"min":1,
				 *  			"max":5,
				 *  			"enum": [BATTERY_STATUS_UNKNOWN, BATTERY_STATUS_CHARGING, BATTERY_STATUS_DISCHARGING, BATTERY_STATUS_NOT_CHARGING, BATTERY_STATUS_FULL],
				 *  			"description":"Level of battery (%)"
				 *  		},
				 *  		"voltage":{
				 *  			"required":false,
				 *  			"type":"nomber",
				 *  			"description":"Voltage"
				 *  		},
				 *  		"temperature":{
				 *  			"required":false,
				 *  			"type":"number",
				 *  			"description":"Temperatue (°C)"
				 *  		}
				 *  	}
				 * }
				 * </pre>
				 * @param {Object} failureCallback The callback which will be called when result encounters an error. (String result)
				 */
				registerToBatteryStatus: function(successCallback, failureCallback){
					var parameters = {"register":true};
					return cordova.exec(
							successCallback,
							failureCallback,
							'DeviceStatus',
							'getBatteryStatus',
							[parameters]);
				}
		},
		/**
		 * Provides an API to retrieve application preferences
		 * 
		 * @memberOf Societies
		 * @namespace Societies.AppPreferences
		 */

		AppPreferences: {
				/**
				 * @methodOf Societies.AppPreferences#
				 * @description Retrieve a String preference value for a given preference name
				 * @param {Object} successCallback The callback which will be called when result is successful
				 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
				 * @param {String} prefName The name of preference
				 * @returns value of preference
				 */
				getStringPrefValue: function(successCallback, failureCallback, prefName) {
					console.log("Call Preferences - getStringPrefValue");
					
					return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
					failureCallback,     //Callback which will be called when plugin action encounters an error
					'PluginPreferences',  //Telling PhoneGap that we want to run specified plugin
					'getStringPrefValue',          //Telling the plugin, which action we want to perform
					[prefName]);        //Passing a list of arguments to the plugin
				},

				/**
				 * @methodOf Societies.AppPreferences#
				 * @description Retrieve an Integer preference value for a given preference name
				 * @param {Object} successCallback The callback which will be called when result is successful
				 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
				 * @param {String} prefName The name of preference
				 * @returns value of preference
				 */
				getIntegerPrefValue: function(successCallback, failureCallback, prefName) {
					console.log("Call Preferences - getIntegerPrefValue");
					
					return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
					failureCallback,     //Callback which will be called when plugin action encounters an error
					'PluginPreferences',  //Telling PhoneGap that we want to run specified plugin
					'getIntegerPrefValue',          //Telling the plugin, which action we want to perform
					[prefName]);        //Passing a list of arguments to the plugin
				},
				/**
				 * @methodOf Societies.AppPreferences#
				 * @description Retrieve a Long preference value for a given preference name
				 * @param {Object} successCallback The callback which will be called when result is successful
				 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
				 * @param {String} prefName The name of preference
				 * @returns value of preference
				 */
				getLongPrefValue: function(successCallback, failureCallback, prefName) {
					console.log("Call Preferences - getLongPrefValue");
					
					return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
					failureCallback,     //Callback which will be called when plugin action encounters an error
					'PluginPreferences',  //Telling PhoneGap that we want to run specified plugin
					'getLongPrefValue',          //Telling the plugin, which action we want to perform
					[prefName]);        //Passing a list of arguments to the plugin
				},
				/**
				 * @methodOf Societies.AppPreferences#
				 * @description Retrieve a Float preference value for a given preference name
				 * @param {Object} successCallback The callback which will be called when result is successful
				 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
				 * @param {String} prefName The name of preference
				 * @returns value of preference
				 */
				getFloatPrefValue: function(successCallback, failureCallback, prefName) {
					console.log("Call Preferences - getFloatPrefValue");
					
					return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
					failureCallback,     //Callback which will be called when plugin action encounters an error
					'PluginPreferences',  //Telling PhoneGap that we want to run specified plugin
					'getFloatPrefValue',          //Telling the plugin, which action we want to perform
					[prefName]);        //Passing a list of arguments to the plugin
				}, 
				/**
				 * @methodOf Societies.AppPreferences#
				 * @description Retrieve a Boolean preference value for a given preference name
				 * @param {Object} successCallback The callback which will be called when result is successful
				 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
				 * @param {String} prefName The name of preference
				 * @returns value of preference
				 */
				getBooleanPrefValue: function(successCallback, failureCallback, prefName) {
					console.log("Call Preferences - getBooleanPrefValue");
					
					return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
					failureCallback,     //Callback which will be called when plugin action encounters an error
					'PluginPreferences',  //Telling PhoneGap that we want to run specified plugin
					'getBooleanPrefValue',          //Telling the plugin, which action we want to perform
					[prefName]);        //Passing a list of arguments to the plugin
				}
		},
		
		/**
		 * Provides an API to the local CSS Manager
		 * 
		 * @memberOf Societies
		 * @namespace Societies.LocalCSSManagerService
		 */
		LocalCSSManagerService: {
			
			/**
			 * @methodOf Societies.LocalCSSManagerService#
			 * @description Connects the GUI to native service implementation
			 * @param {Object} successCallback The callback which will be called when result is successful
			 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
			 * @returns null
			 */
			connectService: function(successCallback, failureCallback) {
				console.log("Call LocalCSSManagerService - connectService");

				return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
				failureCallback,     //Callback which will be called when plugin action encounters an error
				'PluginCSSManager',  //Telling PhoneGap that we want to run specified plugin
				'connectService',          //Telling the plugin, which action we want to perform
				[]);        //Passing a list of arguments to the plugin
			},

			/**
			 * @methodOf Societies.LocalCSSManagerService#
			 * @description Disconnects the GUI to native service implementation
			 * @param {Object} successCallback The callback which will be called when result is successful
			 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
			 * @returns null
			 */

			disconnectService: function(successCallback, failureCallback) {
				console.log("Call LocalCSSManagerService - disconnectService");

				return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
				failureCallback,     //Callback which will be called when plugin action encounters an error
				'PluginCSSManager',  //Telling PhoneGap that we want to run specified plugin
				'disconnectService',          //Telling the plugin, which action we want to perform
				[]);        //Passing a list of arguments to the plugin
			},
			
			/**
			 * @methodOf Societies.LocalCSSManagerService#
			 * @description Log in a device to the relevant CSS
			 * @param {Object} successCallback The callback which will be called when result is successful
			 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
			 * @returns CSSrecord an updated version 
			 */
			loginCSS: function(successCallback, failureCallback) {
				var client = "org.societies.android.platform.gui";
				var cssRecord = {
						  			"archiveCSSNodes": [],
				                    "cssIdentity": jQuery("#username").val(),
				                    "cssInactivation": null,
				                    "cssNodes": [{
				                        "identity": "android@societies.local/androidOne",
				                        "status": 0,
				                        "type": 0}],
				                    "cssRegistration": null,
				                    "cssHostingLocation" : null,
				                    "domainServer" : null,
				                    "cssUpTime": 0,
				                    "emailID": null,
				                    "entity": 0,
				                    "foreName": null,
				                    "homeLocation": null,
				                    "identityName": null,
				                    "imID": null,
				                    "name": null,
				                    "password": jQuery("#userpass").val(),
				                    "presence": 0,
				                    "sex": 0,
				                    "socialURI": null,
				                    "status": 0
						                  }


				console.log("Call LocalCSSManagerService - loginCSS");

				return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
				failureCallback,     //Callback which will be called when plugin action encounters an error
				'PluginCSSManager',  //Telling PhoneGap that we want to run specified plugin
				'loginCSS',          //Telling the plugin, which action we want to perform
				[client, cssRecord]);        //Passing a list of arguments to the plugin
			},

			/**
			 * @methodOf Societies.LocalCSSManagerService#
			 * @description Log out a device from the relevant CSS
			 * @param {Object} successCallback The callback which will be called when result is successful
			 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
			 * @returns CSSrecord an updated version 
			 */

			logoutCSS: function(successCallback, failureCallback) {
				var client = "org.societies.android.platform.gui";
				var cssRecord = {
						  			"archiveCSSNodes": [],
				                    "cssIdentity": "android",
				                    "cssInactivation": null,
				                    "cssNodes": [{
				                        "identity": "android@societies.local/androidOne",
				                        "status": 0,
				                        "type": 0}],
				                    "cssRegistration": null,
				                    "cssHostingLocation" : null,
				                    "domainServer" : null,
				                    "cssUpTime": 0,
				                    "emailID": null,
				                    "entity": 0,
				                    "foreName": null,
				                    "homeLocation": null,
				                    "identityName": null,
				                    "imID": null,
				                    "name": null,
				                    "password": "androidpass",
				                    "presence": 0,
				                    "sex": 0,
				                    "socialURI": null,
				                    "status": 0
						                  }


				console.log("Call LocalCSSManagerService - logoutCSS");

				return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
				failureCallback,     //Callback which will be called when plugin action encounters an error
				'PluginCSSManager',  //Telling PhoneGap that we want to run specified plugin
				'logoutCSS',          //Telling the plugin, which action we want to perform
				[client, cssRecord]);        //Passing a list of arguments to the plugin
			}
		},

		/**
		 * Provides an API to the Service Monitor
		 * 
		 * @memberOf Societies
		 * @namespace Societies.CoreServiceMonitorService
		 */

		CoreServiceMonitorService: {
			
			/**
			 * @methodOf Societies.CoreServiceMonitorService#
			 * @description Connects the GUI to native service implementation
			 * @param {Object} successCallback The callback which will be called when result is successful
			 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
			 * @returns null
			 */
			connectService: function(successCallback, failureCallback) {

				console.log("Call CoreServiceMonitorService - connectService");

				return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
				failureCallback,     //Callback which will be called when plugin action encounters an error
				'PluginCoreServiceMonitor',  //Telling PhoneGap that we want to run specified plugin
				'connectService',              //Telling the plugin, which action we want to perform
				[]);        //Passing a list of arguments to the plugin
			},
			
			/**
			 * @methodOf Societies.CoreServiceMonitorService#
			 * @description Disconnects the GUI to native service implementation
			 * @param {Object} successCallback The callback which will be called when result is successful
			 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
			 * @returns null
			 */
			disconnectService: function(successCallback, failureCallback) {

				console.log("Call CoreServiceMonitorService - disconnectService");

				return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
				failureCallback,     //Callback which will be called when plugin action encounters an error
				'PluginCoreServiceMonitor',  //Telling PhoneGap that we want to run specified plugin
				'disconnectService',              //Telling the plugin, which action we want to perform
				[]);        //Passing a list of arguments to the plugin
			},
			
			/**
			 * @methodOf Societies.CoreServiceMonitorService#
			 * @description Get a list of active services
			 * @param {Object} successCallback The callback which will be called when result is successful
			 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
			 * @returns List of active services
			 */
			activeServices: function(successCallback, failureCallback) {

				console.log("Call CoreServiceMonitorService - activeServices");

				return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
				failureCallback,     //Callback which will be called when plugin action encounters an error
				'PluginCoreServiceMonitor',  //Telling PhoneGap that we want to run specified plugin
				'activeServices',              //Telling the plugin, which action we want to perform
				["org.societies.android.platform.gui"]);        //Passing a list of arguments to the plugin
			},
			
			/**
			 * @methodOf Societies.CoreServiceMonitorService#
			 * @description Get a list of active apps
			 * @param {Object} successCallback The callback which will be called when result is successful
			 * @param {Object} failureCallback The callback which will be called when result is unsuccessful
			 * @returns List of active apps
			 */
			activeTasks: function(successCallback, failureCallback) {
				var clientPackage = "org.societies.android.platform.gui";

				console.log("Call CoreServiceMonitorService - activeTasks");

				return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
				failureCallback,     //Callback which will be called when plugin action encounters an error
				'PluginCoreServiceMonitor',  //Telling PhoneGap that we want to run specified plugin
				'activeTasks',              //Telling the plugin, which action we want to perform
				["org.societies.android.platform.gui"]);        //Passing a list of arguments to the plugin
			}
		}
};


/**
 * Societies GUI utility functions namespace
 * 
 * @namespace SocietiesGUI
 */


var SocietiesGUI = {
		/**
		 * @methodOf SocietiesGUI#
		 * @description Disconnect from CSSManager native service
		 * @returns null
		 */
		disconnectFromLocalCSSManager: function() {
			console.log("Disconnect from LocalCSSManager");
				
			function success(data) {
				console.log(data);
			}
			
			function failure(data) {
				alert("disconnectFromLocalCSSManager - failure: " + data);
			}
		    window.plugins.LocalCSSManagerService.disconnectService(success, failure);
		},

		/**
		 * @methodOf SocietiesGUI#
		 * @description Carry out the logout process
		 * @returns null
		 */
		successfulLogout: function() {
			console.log("Logout from CSS");

			function success(data) {
				jQuery("#username").val("");
				jQuery("#userpass").val("");
				SocietiesGUI.disconnectFromLocalCSSManager();

			}

			function failure(data) {
				alert("successfulLogout : " + "failure: " + data);
			}
			
		    window.plugins.LocalCSSManagerService.logoutCSS(success, failure);

		},

		/**
		 * @methodOf SocietiesGUI#
		 * @description Connect to CSSManager native service
		 * @param {Object} function to be executed if connection successful
		 * @returns null
		 */

		connectToLocalCSSManager: function(actionFunction) {
			console.log("Connect to LocalCSSManager");
				
			function success(data) {
				console.log(data);
				actionFunction();
			}
			
			function failure(data) {
				alert("connectToLocalCSSManager - failure: " + data);
			}
		    window.plugins.LocalCSSManagerService.connectService(success, failure);
		},
		
		/**
		 * @methodOf SocietiesGUI#
		 * @description Android Backbutton handler
		 * @param {Object} backbutton event
		 * @returns null
		 */

		backButtonHandler: function(e) {
			console.log("Back button handling");
			
			console.log("Back button handling on page: " + $.mobile.activePage[0].id );
			
		    if ($.mobile.activePage[0].id === "main"){
		        e.preventDefault();
		        navigator.app.exitApp();
		    }
		    else if ($.mobile.activePage[0].id === "menu"){
		        e.preventDefault();
		        connectToLocalCSSManager(successfulLogout);
		    } else {
		        navigator.app.backHistory();
		    }
		},
		
		/**
		 * @methodOf SocietiesGUI#
		 * @description Registers PhoneGap/Cordova plugins when PhoneGap is loaded and ready. N.B. Ensure that res/xml/plugins.xml file is updated.
		 * @returns null
		 */
		onDeviceReady: function() {
			console.log("PhoneGap Loaded, Device Ready");
			
			//Register any PhoneGap plugins here. Example shown for illustration
			 
			cordova.addConstructor(function() {
				//Register the javascript plugin with PhoneGap
				console.log("Register CoreServiceMonitorService plugin ");
				cordova.addPlugin('CoreServiceMonitorService', Societies.CoreServiceMonitorService);
				
				console.log("Register LocalCSSManagerService plugin ");
				cordova.addPlugin('LocalCSSManagerService', Societies.LocalCSSManagerService);
				
				console.log("Register DeviceStatus Service plugin ");
				cordova.addPlugin("DeviceStatus", Societies.DeviceStatus);

				console.log("Register Preferences plugin ");
				cordova.addPlugin("AppPreferences", Societies.AppPreferences);

			});
			
			//handle the Android Back button 
			//PhoneGap/ HTML views break semantics of Back button unless
			//app intercepts button and simulates back button behaviour
			document.addEventListener("backbutton", SocietiesGUI.backButtonHandler, false);

		},
		/**
		 * @methodOf SocietiesGUI#
		 * @description Populate Device Info page HTML elements with device information
		 * @returns null
		 */
		deviceInfo: function() {
			console.log("Get device information");
			
			jQuery("#phoneGapVer").text(device.cordova);
			jQuery("#platform").text(device.platform);
			jQuery("#version").text(device.version);
			jQuery("#uuid").text(device.uuid);
			jQuery("#name").text(device.name);
			jQuery("#width").text(screen.width);
			jQuery("#height").text(screen.height);
			jQuery("#colorDepth").text(screen.colorDepth);
			jQuery("#pixelDepth").text(screen.pixelDepth);
			jQuery("#browserAgent").text(navigator.userAgent);

		},
		/**
		 * @methodOf SocietiesGUI#
		 * @description Actions carried in the event that a successful CSS login occurs
		 * @returns null
		 */

		successfulLogin: function() {
			console.log("Login to CSS");

			function success(data) {
				
				var status = ["Available for Use", "Unavailable", "Not active but on alert"];
				var type = ["Android based client", "Cloud Node", "JVM based client"];
				
				jQuery("#cssrecordforename").val(data.foreName);
				jQuery("#cssrecordname").val(data.name);
				jQuery("#cssrecordemaildetails").val(data.emailID);
				jQuery("#cssrecordimdetails").val(data.imID);
				jQuery("#cssrecorduserlocation").val(data.homeLocation);
				jQuery("#cssrecordsnsdetails").val(data.socialURI);
				jQuery("#cssrecordidentity").val(data.cssIdentity);
				jQuery("#cssrecordorgtype").val(data.entity);
				jQuery("#cssrecordsextype").val(data.sex);
				
				//empty table
				jQuery('#cssNodesTable tbody').remove();
				
				for (i  = 0; i < data.cssNodes.length; i++) {
					var tableEntry = "<tr>" + 
					"<td>" + data.cssNodes[i].identity + "</td>" + 
					"<td>" + status[data.cssNodes[i].status] + "</td>" + 
					"<td>" + type[data.cssNodes[i].type] + "</td>" + 
						+ "</tr>"

					jQuery('#cssNodesTable').append(tableEntry);
				}

				console.log("Current page: " + $.mobile.activePage[0].id);

				
				$.mobile.changePage( ($("#menu")), { transition: "slideup"} );
			}
			
			function failure(data) {
				alert("successfulLogin - failure: " + data);
			}
		    window.plugins.LocalCSSManagerService.loginCSS(success, failure);

		},
		/**
		 * @methodOf SocietiesGUI#
		 * @description Reset the Device Manager page HTML elements 
		 * @returns null
		 */

		resetDeviceMgr: function(){
		    jQuery("#connStatuslist").text("");
		    jQuery("#battStatuslist").text("");
		    jQuery("#locStatuslist").text("");
		    
		    
		    
		},
		/**
		 * @methodOf SocietiesGUI#
		 * @description Connect to Service Monitor native service
		 * @param {Object} function to be executed if connection successful
		 * @returns null
		 */

		connectToCoreServiceMonitor: function(actionFunction) {
			console.log("Connect to CoreServiceMonitor");
				
			function success(data) {
				actionFunction();
			}
			
			function failure(data) {
				alert("connectToCoreServiceMonitor - failure: " + data);
			}
		    window.plugins.CoreServiceMonitorService.connectService(success, failure);
		},
		/**
		 * @methodOf SocietiesGUI#
		 * @description Refresh the Active Service page with currently active services
		 * @returns null
		 */

		refreshActiveServices: function() {
			console.log("Refresh Active Service");

			function success(data) {
				//empty table
				jQuery('#activeServicesTable tbody').remove();
				
				for (i  = 0; i < data.length; i++) {
					var tableEntry = "<tr>" + 
					"<td>" + data[i].className + "</td>" + 
					"<td>" + SocietiesGUI.convertMilliseconds(data[i].activeSince) + "</td>" + 
						+ "</tr>"

					jQuery('#activeServicesTable').append(tableEntry);
				}
			}
			
			function failure(data) {
				alert("refreshActiveServices - failure: " + data);
			}
			
			window.plugins.CoreServiceMonitorService.activeServices(success, failure);
			
		},
		/**
		 * @methodOf SocietiesGUI#
		 * @description Refresh the Active Apps page with the current active apps
		 * @returns null
		 */

		refreshActiveTasks: function() {
			console.log("Refresh Active Tasks");

			function success(data) {
				//empty table
				jQuery('#activeTasksTable tbody').remove();

				//add rows
				for (i  = 0; i < data.length; i++) {
					var tableEntry = "<tr>" + 
					"<td>" + data[i].className + "</td>" + 
					"<td>" + data[i].numRunningActivities + "</td>" + 
						+ "</tr>"

					jQuery('#activeTasksTable').append(tableEntry);
				}
			}
			
			function failure(data) {
				alert("refreshActiveTasks - failure: " + data);
			}
			
			window.plugins.CoreServiceMonitorService.activeTasks(success, failure);
		},
		/**
		 * @methodOf SocietiesGUI#
		 * @description Convert an uptime in milliseconds to conventional time units
		 * @param {Object} uptime in milliseconds
		 * @returns String uptime
		 */

		convertMilliseconds: function(milliseconds) {
			value = milliseconds / 1000
			seconds = value % 60
			value /= 60
			minutes = value % 60
			value /= 60
			hours = value % 24
			value /= 24
			days = value	
			return "d: " + days + " h: " + hours + " m:" + minutes;
		},
		/**
		 * @methodOf SocietiesGUI#
		 * @description Validate that viable login credentials have been entered
		 * @param {Object} username
		 * @param {Object} password
		 * @returns boolean true if credentials viable
		 */

		validateCredentials: function(name, password) {
			var retValue = true;

			if (name.length === 0 || password.length === 0) {
				retValue  = false;
				alert("validateCredentials: " + "User credentials must be entered");
			}
			return retValue;
		},
		/**
		 * @methodOf SocietiesGUI#
		 * @description Success action 
		 * @param {Object} data
		 * @returns null
		 */

		onSuccess: function(data) {
			console.log("JS Success");
			console.log(JSON.stringify(data));
			$('.result').remove();
			$('.error').remove();
			$('<span>').addClass('result').html("Result: "+JSON.stringify(data)).appendTo('#main article[data-role=content]');
		},
		/**
		 * @methodOf SocietiesGUI#
		 * @description Failure action 
		 * @param {Object} error
		 * @returns null
		 */
		onFailure: function(e) {
			console.log("JS Error");
			console.log(e);
			$('.result').remove();
			$('.error').remove();
			$('<span>').addClass('error').html(e).appendTo('#main article[data-role=content]');
		},
		/**
		 * @methodOf SocietiesGUI#
		 * @description Get an app preference value
		 * @returns null
		 */

		getAppPref: function() {
			console.log("Get app preference");
			
			jQuery("#prefValue").val("");
			prefName = jQuery("#prefName").val();
			type = jQuery("#prefType").val();

			function success(data) {
				console.log("getAppPref - successful: " + data.value);
				jQuery("#prefValue").val(data.value);
				
			};
			
			function failure(data) {
				console.log("getAppPref - failure: " + data.value);
			};

			console.log("Preference type: " + type);
			
			switch(type)
			{
			case "string":
				window.plugins.AppPreferences.getStringPrefValue(success, failure, prefName, type);
				break;
			case "integer":
				window.plugins.AppPreferences.getIntegerPrefValue(success, failure, prefName, type);
				break;
			case "long":
				window.plugins.AppPreferences.getLongPrefValue(success, failure, prefName, type);
				break;
			case "float":
				window.plugins.AppPreferences.getFloatPrefValue(success, failure, prefName, type);
				break;
			case "boolean":
				window.plugins.AppPreferences.getBooleanPrefValue(success, failure, prefName, type);
				break;
			default:
			  console.log("Error - Preference type is not defined");
			}

		},
};


/**
 * JQuery boilerplate to attach JS functions to relevant HTML elements
 * 
 * @description Add Javascript functions to various HTML tags using JQuery
 * @returns null
 */

jQuery(function() {
	console.log("jQuery calls");

	document.addEventListener("deviceready", SocietiesGUI.onDeviceReady, false);
	

	
	$('#deviceChar').click(function() {
		SocietiesGUI.deviceInfo();
	});

	$('#connectXMPP').click(function() {
		if (SocietiesGUI.validateCredentials(jQuery("#username").val(), jQuery("#userpass").val())) {
			SocietiesGUI.connectToLocalCSSManager(SocietiesGUI.successfulLogin);
		}
	});
	
	$('#resetDeviceManager').click(function() {
		SocietiesGUI.resetDeviceMgr();
	});

	$('#refreshServices').click(function() {
		SocietiesGUI.connectToCoreServiceMonitor(SocietiesGUI.refreshActiveServices);
	});

	$('#refreshTasks').click(function() {
		SocietiesGUI.connectToCoreServiceMonitor(SocietiesGUI.refreshActiveTasks);
	});
	$("#logoutIcon").click(function() {
		SocietiesGUI.connectToLocalCSSManager(SocietiesGUI.successfulLogout);
	});
	
	$('#connectivity').click(function() {
		window.plugins.DeviceStatus.getConnectivityStatus(SocietiesGUI.onSuccess, SocietiesGUI.onFailure);
	});
	
	$('#location').click(function() {
		window.plugins.DeviceStatus.getLocationStatus(SocietiesGUI.onSuccess, SocietiesGUI.onFailure);
	});
	
	$('#battery').click(function() {
		window.plugins.DeviceStatus.getBatteryStatus(SocietiesGUI.onSuccess, SocietiesGUI.onFailure);
	});
	$('#registerBattery').click(function() {
		window.plugins.DeviceStatus.registerToBatteryStatus(SocietiesGUI.onSuccess, SocietiesGUI.onFailure);
	});

	$('#getPref').click(function() {
		SocietiesGUI.getAppPref();
	});

});

//var Preferences = function() { 
//};
//
///**
//* Get a String Preference value
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//* @param prefName name of preference to be retrieved
//*/
//Preferences.prototype.getStringPrefValue = function(successCallback, failureCallback, prefName) {
//	console.log("Call Preferences - getStringPrefValue");
//	
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginPreferences',  //Telling PhoneGap that we want to run specified plugin
//	'getStringPrefValue',          //Telling the plugin, which action we want to perform
//	[prefName]);        //Passing a list of arguments to the plugin
//};
//
///**
//* Get a Integer Preference value
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//* @param prefName name of preference to be retrieved
//*/
//Preferences.prototype.getIntegerPrefValue = function(successCallback, failureCallback, prefName) {
//	console.log("Call Preferences - getIntegerPrefValue");
//	
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginPreferences',  //Telling PhoneGap that we want to run specified plugin
//	'getIntegerPrefValue',          //Telling the plugin, which action we want to perform
//	[prefName]);        //Passing a list of arguments to the plugin
//};
//
///**
//* Get a Long Preference value
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//* @param prefName name of preference to be retrieved
//*/
//Preferences.prototype.getLongPrefValue = function(successCallback, failureCallback, prefName) {
//	console.log("Call Preferences - getLongPrefValue");
//	
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginPreferences',  //Telling PhoneGap that we want to run specified plugin
//	'getLongPrefValue',          //Telling the plugin, which action we want to perform
//	[prefName]);        //Passing a list of arguments to the plugin
//};
//
///**
//* Get a Float Preference value
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//* @param prefName name of preference to be retrieved
//*/
//Preferences.prototype.getFloatPrefValue = function(successCallback, failureCallback, prefName) {
//	console.log("Call Preferences - getFloatPrefValue");
//	
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginPreferences',  //Telling PhoneGap that we want to run specified plugin
//	'getFloatPrefValue',          //Telling the plugin, which action we want to perform
//	[prefName]);        //Passing a list of arguments to the plugin
//};
//
///**
//* Get a Boolean Preference value
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//* @param prefName name of preference to be retrieved
//*/
//Preferences.prototype.getBooleanPrefValue = function(successCallback, failureCallback, prefName) {
//	console.log("Call Preferences - getBooleanPrefValue");
//	
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginPreferences',  //Telling PhoneGap that we want to run specified plugin
//	'getBooleanPrefValue',          //Telling the plugin, which action we want to perform
//	[prefName]);        //Passing a list of arguments to the plugin
//};

///**
//* LocalCSSManagerService object
//*/
//var LocalCSSManagerService = function() { 
//}
//
///**
//* Connect to LocalCSSManager service
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//*/
//LocalCSSManagerService.prototype.connectService = function(successCallback, failureCallback) {
//	console.log("Call LocalCSSManagerService - connectService");
//
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginCSSManager',  //Telling PhoneGap that we want to run specified plugin
//	'connectService',          //Telling the plugin, which action we want to perform
//	[]);        //Passing a list of arguments to the plugin
//};
//
///**
//* Disconnect from LocalCSSManager service
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//*/
//LocalCSSManagerService.prototype.disconnectService = function(successCallback, failureCallback) {
//	console.log("Call LocalCSSManagerService - disconnectService");
//
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginCSSManager',  //Telling PhoneGap that we want to run specified plugin
//	'disconnectService',          //Telling the plugin, which action we want to perform
//	[]);        //Passing a list of arguments to the plugin
//};
//
//
///**
//* Login to CSS on cloud/rich node
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//*/
//LocalCSSManagerService.prototype.loginCSS = function(successCallback, failureCallback) {
//	var client = "org.societies.android.platform.gui";
//	var cssRecord = {
//			  			"archiveCSSNodes": [],
//	                    "cssIdentity": jQuery("#username").val(),
//	                    "cssInactivation": null,
//	                    "cssNodes": [{
//	                        "identity": "android@societies.local/androidOne",
//	                        "status": 0,
//	                        "type": 0}],
//	                    "cssRegistration": null,
//	                    "cssHostingLocation" : null,
//	                    "domainServer" : null,
//	                    "cssUpTime": 0,
//	                    "emailID": null,
//	                    "entity": 0,
//	                    "foreName": null,
//	                    "homeLocation": null,
//	                    "identityName": null,
//	                    "imID": null,
//	                    "name": null,
//	                    "password": jQuery("#userpass").val(),
//	                    "presence": 0,
//	                    "sex": 0,
//	                    "socialURI": null,
//	                    "status": 0
//			                  }
//
//
//	console.log("Call LocalCSSManagerService - loginCSS");
//
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginCSSManager',  //Telling PhoneGap that we want to run specified plugin
//	'loginCSS',          //Telling the plugin, which action we want to perform
//	[client, cssRecord]);        //Passing a list of arguments to the plugin
//};
//
///**
//* Login to CSS on cloud/rich node
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//*/
//LocalCSSManagerService.prototype.logoutCSS = function(successCallback, failureCallback) {
//	var client = "org.societies.android.platform.gui";
//	var cssRecord = {
//			  			"archiveCSSNodes": [],
//	                    "cssIdentity": "android",
//	                    "cssInactivation": null,
//	                    "cssNodes": [{
//	                        "identity": "android@societies.local/androidOne",
//	                        "status": 0,
//	                        "type": 0}],
//	                    "cssRegistration": null,
//	                    "cssHostingLocation" : null,
//	                    "domainServer" : null,
//	                    "cssUpTime": 0,
//	                    "emailID": null,
//	                    "entity": 0,
//	                    "foreName": null,
//	                    "homeLocation": null,
//	                    "identityName": null,
//	                    "imID": null,
//	                    "name": null,
//	                    "password": "androidpass",
//	                    "presence": 0,
//	                    "sex": 0,
//	                    "socialURI": null,
//	                    "status": 0
//			                  }
//
//
//	console.log("Call LocalCSSManagerService - logoutCSS");
//
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginCSSManager',  //Telling PhoneGap that we want to run specified plugin
//	'logoutCSS',          //Telling the plugin, which action we want to perform
//	[client, cssRecord]);        //Passing a list of arguments to the plugin
//};

///**
//* CoreServiceMonitorService object
//*/
//var CoreServiceMonitorService = function() { 
//}
//
///**
//* Connect to CoreServiceMonitorService service
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//*/
//CoreServiceMonitorService.prototype.connectService = function(successCallback, failureCallback) {
//
//	console.log("Call CoreServiceMonitorService - connectService");
//
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginCoreServiceMonitor',  //Telling PhoneGap that we want to run specified plugin
//	'connectService',              //Telling the plugin, which action we want to perform
//	[]);        //Passing a list of arguments to the plugin
//};
//
///**
//* Disconnect from CoreServiceMonitorService service
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//*/
//CoreServiceMonitorService.prototype.disconnectService = function(successCallback, failureCallback) {
//
//	console.log("Call CoreServiceMonitorService - disconnectService");
//
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginCoreServiceMonitor',  //Telling PhoneGap that we want to run specified plugin
//	'disconnectService',              //Telling the plugin, which action we want to perform
//	[]);        //Passing a list of arguments to the plugin
//};
//
///**
//* Get all active services
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//*/
//CoreServiceMonitorService.prototype.activeServices = function(successCallback, failureCallback) {
//
//	console.log("Call CoreServiceMonitorService - activeServices");
//
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginCoreServiceMonitor',  //Telling PhoneGap that we want to run specified plugin
//	'activeServices',              //Telling the plugin, which action we want to perform
//	["org.societies.android.platform.gui"]);        //Passing a list of arguments to the plugin
//};
//
///**
//* Get all active tasks, i.e. apps
//* 
//* @param successCallback The callback which will be called when Java method is successful
//* @param failureCallback The callback which will be called when Java method has an error
//*/
//CoreServiceMonitorService.prototype.activeTasks = function(successCallback, failureCallback) {
//	var clientPackage = "org.societies.android.platform.gui";
//
//	console.log("Call CoreServiceMonitorService - activeTasks");
//
//	return cordova.exec(successCallback,    //Callback which will be called when plugin action is successful
//	failureCallback,     //Callback which will be called when plugin action encounters an error
//	'PluginCoreServiceMonitor',  //Telling PhoneGap that we want to run specified plugin
//	'activeTasks',              //Telling the plugin, which action we want to perform
//	["org.societies.android.platform.gui"]);        //Passing a list of arguments to the plugin
//};

///**
// * Connect to CSSManager 
// * @param actionFunction function to be called if successful
// */
//var connectToLocalCSSManager = function(actionFunction) {
//	console.log("Connect to LocalCSSManager");
//		
//	function success(data) {
//		console.log(data);
//		actionFunction();
//	}
//	
//	function failure(data) {
//		alert("connectToLocalCSSManager - failure: " + data);
//	}
//    window.plugins.LocalCSSManagerService.connectService(success, failure);
//}
//
//
//function backButtonHandler(e) {
//	console.log("Back button handling");
//	
//	console.log("Back button handling on page: " + $.mobile.activePage[0].id );
//	
//    if ($.mobile.activePage[0].id === "main"){
//        e.preventDefault();
//        navigator.app.exitApp();
//    }
//    else if ($.mobile.activePage[0].id === "menu"){
//        e.preventDefault();
//        connectToLocalCSSManager(successfulLogout);
//    } else {
//        navigator.app.backHistory();
//    }
//}
///**
// * Called when HTML page has been loaded
// * Add custom PhoneGap plugins
// * All PhoneGap supported event registrations should be occur here
// * 
// * N.B. Ensure that res/xml/plugins.xml file is updated
// */
//function onDeviceReady() {
//	console.log("PhoneGap Loaded, Device Ready");
//	
//	//Register any PhoneGap plugins here. Example shown for illustration
//	 
//	cordova.addConstructor(function() {
//		//Register the javascript plugin with PhoneGap
//		console.log("Register CoreServiceMonitorService plugin ");
//		cordova.addPlugin('CoreServiceMonitorService', Societies.CoreServiceMonitorService);
//		
//		console.log("Register LocalCSSManagerService plugin ");
//		cordova.addPlugin('LocalCSSManagerService', Societies.LocalCSSManagerService);
//		
//		console.log("Register DeviceStatus Service plugin ");
//		cordova.addPlugin("DeviceStatus", Societies.DeviceStatus);
//
//		console.log("Register Preferences plugin ");
//		cordova.addPlugin("AppPreferences", Societies.AppPreferences);
//
//	});
//	
//	//handle the Android Back button 
//	//PhoneGap/ HTML views break semantics of Back button unless
//	//app intercepts button and simulates back button behaviour
//	document.addEventListener("backbutton", backButtonHandler, false);
//
//}
//
//
//
//
///**
// * Populate HTML elements with device information
// */
//var deviceInfo = function() {
//	console.log("Get device information");
//	
//	jQuery("#phoneGapVer").text(device.cordova);
//	jQuery("#platform").text(device.platform);
//	jQuery("#version").text(device.version);
//	jQuery("#uuid").text(device.uuid);
//	jQuery("#name").text(device.name);
//	jQuery("#width").text(screen.width);
//	jQuery("#height").text(screen.height);
//	jQuery("#colorDepth").text(screen.colorDepth);
//	jQuery("#pixelDepth").text(screen.pixelDepth);
//	jQuery("#browserAgent").text(navigator.userAgent);
//
//};
///**
// * Actions carried in the event that a successful CSS login occurs
// */
//var successfulLogin = function() {
//	console.log("Login to CSS");
//
//	function success(data) {
//		
//		var status = ["Available for Use", "Unavailable", "Not active but on alert"];
//		var type = ["Android based client", "Cloud Node", "JVM based client"];
//		
//		jQuery("#cssrecordforename").val(data.foreName);
//		jQuery("#cssrecordname").val(data.name);
//		jQuery("#cssrecordemaildetails").val(data.emailID);
//		jQuery("#cssrecordimdetails").val(data.imID);
//		jQuery("#cssrecorduserlocation").val(data.homeLocation);
//		jQuery("#cssrecordsnsdetails").val(data.socialURI);
//		jQuery("#cssrecordidentity").val(data.cssIdentity);
//		jQuery("#cssrecordorgtype").val(data.entity);
//		jQuery("#cssrecordsextype").val(data.sex);
//		
//		//empty table
//		jQuery('#cssNodesTable tbody').remove();
//		
//		for (i  = 0; i < data.cssNodes.length; i++) {
//			var tableEntry = "<tr>" + 
//			"<td>" + data.cssNodes[i].identity + "</td>" + 
//			"<td>" + status[data.cssNodes[i].status] + "</td>" + 
//			"<td>" + type[data.cssNodes[i].type] + "</td>" + 
//				+ "</tr>"
//
//			jQuery('#cssNodesTable').append(tableEntry);
//		}
//
//		console.log("Current page: " + $.mobile.activePage[0].id);
//
//		
//		$.mobile.changePage( ($("#menu")), { transition: "slideup"} );
//	}
//	
//	function failure(data) {
//		alert("successfulLogin - failure: " + data);
//	}
//    window.plugins.LocalCSSManagerService.loginCSS(success, failure);
//
//};
//
//
//var resetDeviceMgr = function(){
//    jQuery("#connStatuslist").text("");
//    jQuery("#battStatuslist").text("");
//    jQuery("#locStatuslist").text("");
//    
//    
//    
//}
///**
// * Bind to the CoreServiceMonitor service
// * @param actionFunction function to be invoked if action successful
// */
//var connectToCoreServiceMonitor = function(actionFunction) {
//	console.log("Connect to CoreServiceMonitor");
//		
//	function success(data) {
//		actionFunction();
//	}
//	
//	function failure(data) {
//		alert("connectToCoreServiceMonitor - failure: " + data);
//	}
//    window.plugins.CoreServiceMonitorService.connectService(success, failure);
//}
///**
// * List active services
// */
//var refreshActiveServices = function() {
//	console.log("Refresh Active Service");
//
//	function success(data) {
//		//empty table
//		jQuery('#activeServicesTable tbody').remove();
//		
//		for (i  = 0; i < data.length; i++) {
//			var tableEntry = "<tr>" + 
//			"<td>" + data[i].className + "</td>" + 
//			"<td>" + convertMilliseconds(data[i].activeSince) + "</td>" + 
//				+ "</tr>"
//
//			jQuery('#activeServicesTable').append(tableEntry);
//		}
//	}
//	
//	function failure(data) {
//		alert("refreshActiveServices - failure: " + data);
//	}
//	
//	window.plugins.CoreServiceMonitorService.activeServices(success, failure);
//	
//};
///**
// * List active tasks, i.e. apps
// */
//var refreshActiveTasks = function() {
//	console.log("Refresh Active Tasks");
//
//	function success(data) {
//		//empty table
//		jQuery('#activeTasksTable tbody').remove();
//
//		//add rows
//		for (i  = 0; i < data.length; i++) {
//			var tableEntry = "<tr>" + 
//			"<td>" + data[i].className + "</td>" + 
//			"<td>" + data[i].numRunningActivities + "</td>" + 
//				+ "</tr>"
//
//			jQuery('#activeTasksTable').append(tableEntry);
//		}
//	}
//	
//	function failure(data) {
//		alert("refreshActiveTasks - failure: " + data);
//	}
//	
//	window.plugins.CoreServiceMonitorService.activeTasks(success, failure);
//};
//
///**
// * Convert an elapsed time in milliseconds
// * @param milliseconds time elapsed
// */
//var convertMilliseconds = function(milliseconds) {
//	value = milliseconds / 1000
//	seconds = value % 60
//	value /= 60
//	minutes = value % 60
//	value /= 60
//	hours = value % 24
//	value /= 24
//	days = value	
//	return "d: " + days + " h: " + hours + " m:" + minutes;
//}
///**
// * Validate user login credentials
// * @param name username
// * @param password 
// */
//var validateCredentials = function(name, password) {
//	var retValue = true;
//
//	if (name.length === 0 || password.length === 0) {
//		retValue  = false;
//		alert("validateCredentials: " + "User credentials must be entered");
//	}
//	return retValue;
//}
//
//function onSuccess(data) {
//	console.log("JS Success");
//	console.log(JSON.stringify(data));
//	$('.result').remove();
//	$('.error').remove();
//	$('<span>').addClass('result').html("Result: "+JSON.stringify(data)).appendTo('#main article[data-role=content]');
//}
//function onFailure(e) {
//	console.log("JS Error");
//	console.log(e);
//	$('.result').remove();
//	$('.error').remove();
//	$('<span>').addClass('error').html(e).appendTo('#main article[data-role=content]');
//}
//
//var getAppPref = function() {
//	console.log("Get app preference");
//	
//	jQuery("#prefValue").val("");
//	prefName = jQuery("#prefName").val();
//	type = jQuery("#prefType").val();
//
//	function success(data) {
//		console.log("getAppPref - successful: " + data.value);
//		jQuery("#prefValue").val(data.value);
//		
//	};
//	
//	function failure(data) {
//		console.log("getAppPref - failure: " + data.value);
//	};
//
//	console.log("Preference type: " + type);
//	
//	switch(type)
//	{
//	case "string":
//		window.plugins.AppPreferences.getStringPrefValue(success, failure, prefName, type);
//		break;
//	case "integer":
//		window.plugins.AppPreferences.getIntegerPrefValue(success, failure, prefName, type);
//		break;
//	case "long":
//		window.plugins.AppPreferences.getLongPrefValue(success, failure, prefName, type);
//		break;
//	case "float":
//		window.plugins.AppPreferences.getFloatPrefValue(success, failure, prefName, type);
//		break;
//	case "boolean":
//		window.plugins.AppPreferences.getBooleanPrefValue(success, failure, prefName, type);
//		break;
//	default:
//	  console.log("Error - Preference type is not defined");
//	}
//
//};
//
//var disconnectFromLocalCSSManager = function() {
//	console.log("Disconnect from LocalCSSManager");
//		
//	function success(data) {
//		console.log(data);
//	}
//	
//	function failure(data) {
//		alert("disconnectFromLocalCSSManager - failure: " + data);
//	}
//    window.plugins.LocalCSSManagerService.disconnectService(success, failure);
//}
//
//var successfulLogout = function() {
//	console.log("Logout from CSS");
//
//	function success(data) {
//		jQuery("#username").val("");
//		jQuery("#userpass").val("");
//		disconnectFromLocalCSSManager();
//
//	}
//
//	function failure(data) {
//		alert("successfulLogout : " + "failure: " + data);
//	}
//	
//    window.plugins.LocalCSSManagerService.logoutCSS(success, failure);
//
//};

