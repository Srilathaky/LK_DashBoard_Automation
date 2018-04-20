package com.milvik.mip.utility;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class MIP_LaunchApplication {
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_LaunchApplication");
	}

	/**
	 * This method launches the application
	 */
	public static void openApplication(WebDriver driver) {
		logger.info("Launching the application");
		driver.get(MIP_ReadPropertyFile.getPropertyValue("appurl"));
		logger.info("Application Launched successfuly");

	}
}
