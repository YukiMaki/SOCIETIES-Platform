package org.societies.integration.test.bit.useservice;

import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.internal.servicelifecycle.ServiceDiscoveryException;
import org.societies.api.schema.servicelifecycle.model.Service;
import org.societies.api.schema.servicelifecycle.model.ServiceResourceIdentifier;
import org.societies.api.schema.servicelifecycle.model.ServiceStatus;
import org.societies.api.schema.servicelifecycle.servicecontrol.ResultMessage;
import org.societies.api.schema.servicelifecycle.servicecontrol.ServiceControlResult;
import org.societies.example.calculator.ICalc;
import org.societies.integration.test.IntegrationTestUtils;

/**
 * @author Olivier Maridat (Trialog)
 *
 */
public class NominalTestCaseLowerTester {
	private static Logger LOG = LoggerFactory.getLogger(NominalTestCaseLowerTester.class);

	/**
	 * URL of the JAR of the Calculator 3P service Bundle
	 */
	private static String serviceBundleUrl;
	/**
	 * Id of the Calculator 3P service
	 */
	public static ServiceResourceIdentifier calculatorServiceId;
	/**
	 * Injection of ICalc interface
	 */
	public static ICalc calculatorService;
	/**
	 * Tools for integration test
	 */
	public IntegrationTestUtils integrationTestUtils;
	/**
	 * Test case number
	 */
	public static int testCaseNumber;


	public NominalTestCaseLowerTester() {
		integrationTestUtils = new IntegrationTestUtils();
	}


	/**
	 * This method is called only one time, at the very beginning of the process
	 * (after the constructor) in order to initialize the process.
	 * Select the relevant service example: the Calculator
	 */
	@BeforeClass
	public static void initialization() {
		LOG.info("[#759] Initialization");
		LOG.info("[#759] Prerequisite: The CSS is created");
		LOG.info("[#759] Prerequisite: The user is logged to the CSS");

		serviceBundleUrl = "file:C:/Application/Virgo/repository/usr/Calculator.jar";
		calculatorServiceId = null;
	}

	/**
	 * This method is called before every @Test methods.
	 * Verify that the Calculator Service is installed
	 */
	@Before
	public void setUp() {
		LOG.info("[#759] NominalTestCaseLowerTester::setUp");

		Future<ServiceControlResult> asyncinstallResult = null;
		ServiceControlResult installResult = null;

		try {
			// -- Install the service
			LOG.info("[#759] Preamble: Install the service");
			URL serviceUrl = new URL(serviceBundleUrl);
			asyncinstallResult = TestCase759.serviceControl.installService(serviceUrl, "");
			installResult = asyncinstallResult.get();
			if (!installResult.getMessage().equals(ResultMessage.SUCCESS)) {
				throw new Exception("Can't install the service. Returned value: "+installResult.getMessage());
			}
			calculatorServiceId = installResult.getServiceId();
		}
		catch (ServiceDiscoveryException e) {
			LOG.info("[#759] ServiceDiscoveryException", e);
			fail("[#759] ServiceDiscoveryException: "+e.getMessage());
			return;
		}
		catch (Exception e) {
			LOG.info("[#759] Preamble installService: Unknown Exception", e);
			fail("[#759] Preamble installService: Unknown Exception: "+e.getMessage());
			return;
		}
	}

	/**
	 * This method is called after every @Test methods
	 * Stop and uninstal the Calculator Service
	 */
	@After
	public void tearDown() {
		LOG.info("[#759] tearDown");
	}


	/**
	 * Try to consume the calculator service
	 * Part 1: select the service and start it if necessary
	 */
	@Test
	public void bodyUseService() {
		LOG.info("[#759] bodyUseService part 1");

		Future<List<Service>> asyncServices = null;
		List<Service> services =  new ArrayList<Service>();

		try {
			// Start the service
			LOG.info("[#759] Calculator service starting");
			Future<ServiceControlResult> asyncStartResult = TestCase759.serviceControl.startService(calculatorServiceId);
			ServiceControlResult startResult = asyncStartResult.get();
			// Service can't be started
			if (!startResult.getMessage().equals(ResultMessage.SUCCESS)) {
				throw new Exception("Can't start the service. Returned value: "+startResult.getMessage());
			}
			LOG.info("[#759] Calculator service started");

			// -- Test case is now ready to consume the service
			// The injection of ICalc will launch the UpperTester
		}
		catch (ServiceDiscoveryException e) {
			LOG.info("[#759] ServiceDiscoveryException", e);
			fail("[#759] ServiceDiscoveryException: "+e.getMessage());
			return;
		}
		catch (Exception e) {
			LOG.info("[#759] Preamble installService: Unknown Exception", e);
			fail("[#759] Preamble installService: Unknown Exception: "+e.getMessage());
			return;
		}
	}

	public void setCalculatorService(ICalc calculatorService) {
		LOG.info("[#759] Calculator Service injected");
		this.calculatorService = calculatorService;
		// -- Launch the UpperTester to continue the test case by consuming a service
		NominalTestCaseUpperTester.calculatorServiceId = calculatorServiceId;
		integrationTestUtils.run(testCaseNumber, NominalTestCaseUpperTester.class);
	}
}