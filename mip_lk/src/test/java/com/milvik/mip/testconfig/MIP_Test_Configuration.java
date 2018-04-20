package com.milvik.mip.testconfig;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;

public class MIP_Test_Configuration {
	public static WebDriver driver;
	static Logger log;

	@BeforeSuite
	@Parameters({ "browser" })
	public void configBeforeSuit(@Optional("firefox") String browser) {
		log = MIP_Logging.logDetails("MIP_Test_Configuration");
		try {
			Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
		} catch (Exception e) {
			log.info("No exe found");
		}
		driver = MIP_BrowserFactory.openBrowser(driver, browser);
		MIP_ReadPropertyFile.loadProperty("config");
		MIP_DataBaseConnection.connectToDatabase();
		MIP_LaunchApplication.openApplication(driver);
		PageFactory.initElements(MIP_Test_Configuration.driver,
				MIP_LoginPage.class).login(
				MIP_ReadPropertyFile.getPropertyValue("username"),
				MIP_ReadPropertyFile.getPropertyValue("password"));
	}

	@AfterSuite
	public void configAfterSuit() {
		MIP_BrowserFactory.closeDriver(driver);
	}

}
