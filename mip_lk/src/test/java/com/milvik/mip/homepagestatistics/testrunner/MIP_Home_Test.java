package com.milvik.mip.homepagestatistics.testrunner;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.milvik.mip.constants.MIP_Menu_Constants;
import com.milvik.mip.dataprovider.MIP_Home_TestData;
import com.milvik.mip.dbqueries.MIP_Homepage_CustomerStatistics_Queries;
import com.milvik.mip.listeners.MIP_RetryAnalyzer;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.testconfig.MIP_Test_Configuration;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_DateFunctionality;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.milvik.mip.utility.MIP_ScreenShots;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MIP_Home_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;

	@BeforeTest
	@Parameters({ "browser", "flag" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag) {
		log = MIP_Logging.logDetails("MIP_Home_Test");
		report = new ExtentReports(
				".\\Test_Reports\\Home Page Statistics_Test.html");
		if (flag.equals("0")) {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
			} catch (Exception e) {
				System.out.println("No exe found");
			}
			MIP_Test_Configuration.driver = MIP_BrowserFactory.openBrowser(
					MIP_Test_Configuration.driver, browser);

			MIP_ReadPropertyFile.loadProperty("config");
			MIP_DataBaseConnection.connectToDatabase();
			MIP_LaunchApplication
					.openApplication(MIP_Test_Configuration.driver);
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			homepage = loginpage.login(
					MIP_ReadPropertyFile.getPropertyValue("username"),
					MIP_ReadPropertyFile.getPropertyValue("password"));

		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.HOME);
		}
	}

	@Test(testName = "TC17", dataProvider = "homeobjects", dataProviderClass = MIP_Home_TestData.class)
	public void homeTestOne(String home, String branch, String user,
			String leave, String cust, String SMS, String role, String repot,
			String admin, String claims, String pass, String loguot,
			String announ, String cust_stat, String cust_statistics)
			throws Throwable {
		try {
			logger = report.startTest("Home Page Test-TC17");
			log.info("Running testcase--TC17");

			Assert.assertTrue(homepage.validateHomePageObjects(home, branch,
					user, leave, cust, SMS, role, repot, admin, claims, pass,
					loguot, announ, cust_stat, cust_statistics));
		} catch (Throwable t) {
			log.info("Testcase TC17  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC19", dataProvider = "UserLogin", dataProviderClass = MIP_Home_TestData.class, retryAnalyzer = MIP_RetryAnalyzer.class)
	public void homeTestTwo(String username, String password) throws Throwable {
		try {
			logger = report.startTest("Home page Statistics-TC19");
			log.info("Running testcase--TC19");

			String userdata = homepage.getUserDetails();
			Assert.assertTrue(userdata
					.contains(MIP_Homepage_CustomerStatistics_Queries
							.userDetails(username)));
			Assert.assertTrue(userdata.contains(MIP_DateFunctionality
					.getCurrentDate("dd/MM/yyyy")));
		} catch (Throwable t) {
			log.info("Testcase TC19  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "checkHomePageStatistics", retryAnalyzer = MIP_RetryAnalyzer.class)
	public void homeTestThree() throws Throwable {
		List<String> details = new ArrayList<String>();
		try {
			logger = report
					.startTest("Home page Statistics-checkHomePageStatistics");
			log.info("Running testcase--checkHomePageStatistics");
			loginpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_LoginPage.class);
			String username = MIP_ReadPropertyFile.getPropertyValue("username");

			details.addAll(MIP_Homepage_CustomerStatistics_Queries
					.getHomePageStatistics(username, "PA"));
			details.addAll(MIP_Homepage_CustomerStatistics_Queries
					.getHomePageStatistics(username, "HP"));
			details.addAll(MIP_Homepage_CustomerStatistics_Queries
					.getHomePageStatistics(username, "HMP"));
			details.addAll(MIP_Homepage_CustomerStatistics_Queries
					.getHomePageStatistics(username, "Life 2017"));
			details.addAll(MIP_Homepage_CustomerStatistics_Queries
					.getHomePageStatistics(username, "PA 2017"));
			List<String> home_statistics = homepage.getHomeStatisticsData();
			Assert.assertEquals(details.size(), home_statistics.size());
			Assert.assertEquals(details, home_statistics);
		} catch (Throwable t) {
			log.info("Testcase checkHomePageStatistics  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@AfterMethod
	public void after_test(ITestResult res) {
		if (res.getStatus() == ITestResult.FAILURE) {
			MIP_ScreenShots.takeScreenShot(MIP_Test_Configuration.driver,
					res.getName());
			logger.log(LogStatus.FAIL, "Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
		} else if ((res.getStatus() == ITestResult.SUCCESS)) {
			logger.log(LogStatus.PASS, "Test passed");
		}
	}

	@AfterTest(alwaysRun = true)
	@Parameters("flag")
	public void tear_down(@Optional("0") String flag) {
		if (flag.equals("0"))
			MIP_BrowserFactory.closeDriver(MIP_Test_Configuration.driver);
		report.endTest(logger);
		report.flush();
	}

}
