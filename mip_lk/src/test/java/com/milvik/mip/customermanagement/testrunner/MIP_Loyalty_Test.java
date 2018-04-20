/*package com.milvik.mip.customermanagement.testrunner;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
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
import com.milvik.mip.dataprovider.MIP_Lotalty_TestData;
import com.milvik.mip.dbqueries.MIP_Loyalty_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_LoyaltyPage;
import com.milvik.mip.testconfig.MIP_Test_Configuration;
import com.milvik.mip.utility.MIP_BrowserFactory;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_LaunchApplication;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;
import com.milvik.mip.utility.MIP_ScreenShots;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MIP_Loyalty_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;
	MIP_LoyaltyPage loyaltypage = null;

	@BeforeTest
	@Parameters({ "flag", "browser" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag) {
		log = MIP_Logging.logDetails("MIP_Loyalty_Test");
		report = new ExtentReports(".\\Test_Reports\\Loyalty_Test.html");
		if (flag.equals("0")) {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
			} catch (Exception e) {
				log.info("No exe found");
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
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.LOYALTY);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			try {
				if (!homepage.waitForElementToVisible(
						By.linkText(MIP_Menu_Constants.LOYALTY)).isDisplayed()) {
					homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
					homepage.clickOnMenu(MIP_Menu_Constants.LOYALTY);
				}
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.LOYALTY);
			}
		}

	}

	@Test(testName = "TC739-TC740-TC751", dataProvider = "loyaltymsisdn", dataProviderClass = MIP_Lotalty_TestData.class)
	public void loyaltyOne(String misdn) throws Throwable {
		try {
			logger = report.startTest("Search Customer-TC739-TC740");
			log.info("Running test case -TC739-TC740");
			try {
				homepage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'Loyalty')]"));
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.LOYALTY);
			}
			loyaltypage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_LoyaltyPage.class);
			loyaltypage.waitForElementToVisible(By.id("msisdn")).clear();
			Assert.assertTrue(loyaltypage.mobileNum.isDisplayed());
			Assert.assertTrue(loyaltypage.searchIcon.isDisplayed());
			loyaltypage.enterMsisdn(misdn);
			loyaltypage.clickOnSearchButton();
			loyaltypage.clickOnBackBtn();
			loyaltypage.waitForElementToVisible(By.id("msisdn"));
			Assert.assertTrue(loyaltypage.mobileNum.isDisplayed());
			Assert.assertTrue(loyaltypage.searchIcon.isDisplayed());

		} catch (Throwable t) {
			log.info("Testcase  TC739-TC740 Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC742-TC747", dataProvider = "msisdnValidation", dataProviderClass = MIP_Lotalty_TestData.class)
	public void loyaltyTwo(String alphamsisdn, String alphanumericmsisdn,
			String less10digit, String invalidcode, String notinDb,
			String deactivatedcust, String errormsg1, String errmsg2,
			String errmsg3) throws Throwable {
		MIP_LoyaltyPage loyaltypage = null;
		try {
			logger = report.startTest("Search Customer-TC739-TC740");
			log.info("Running test case -TC739-TC740");

			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Loyalty')]"));
			loyaltypage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_LoyaltyPage.class);
			loyaltypage.waitForElementToVisible(By.id("msisdn")).clear();
			;
			loyaltypage.enterMsisdn(alphamsisdn);
			loyaltypage.clickOnSearchButton();
			Assert.assertTrue(loyaltypage.getValidationMessage()
					.equalsIgnoreCase(errormsg1));
			loyaltypage.mobileNum.clear();
			loyaltypage.enterMsisdn(alphanumericmsisdn);
			loyaltypage.clickOnSearchButton();
			Assert.assertTrue(loyaltypage.getValidationMessage()
					.equalsIgnoreCase(errormsg1));
			loyaltypage.mobileNum.clear();
			loyaltypage.enterMsisdn(less10digit);
			loyaltypage.clickOnSearchButton();
			Assert.assertTrue(loyaltypage.getValidationMessage()
					.equalsIgnoreCase(errormsg1));
			loyaltypage.mobileNum.clear();
			loyaltypage.enterMsisdn(invalidcode);
			loyaltypage.clickOnSearchButton();
			Assert.assertTrue(loyaltypage.getErrorMessage().equalsIgnoreCase(
					errmsg2));
			loyaltypage.mobileNum.clear();
			loyaltypage.enterMsisdn(notinDb);
			loyaltypage.clickOnSearchButton();
			Assert.assertTrue(loyaltypage.getErrorMessage().equalsIgnoreCase(
					errmsg2));
			loyaltypage.mobileNum.clear();
			loyaltypage.enterMsisdn(deactivatedcust);
			loyaltypage.clickOnSearchButton();
			Assert.assertTrue(loyaltypage.getErrorMessage().equalsIgnoreCase(
					errmsg2));
			loyaltypage.mobileNum.clear();
			loyaltypage.clickOnSearchButton();
			Assert.assertTrue(loyaltypage.getValidationMessage()
					.equalsIgnoreCase(errmsg3));
		} catch (Throwable t) {
			log.info("Testcase  TC739-TC740 Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "TC741-TC764-TC765", dataProvider = "loyaltymsisdn", dataProviderClass = MIP_Lotalty_TestData.class)
	public void loyaltyThree(String msisdn) throws Throwable {
		MIP_LoyaltyPage loyaltypage = null;
		try {
			logger = report.startTest("Search Customer-TC741-TC764-TC765");
			log.info("Running test case -TC741-TC764-TC765");

			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Loyalty')]"));
			loyaltypage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_LoyaltyPage.class);
			loyaltypage.waitForElementToVisible(By.id("msisdn")).clear();
			;
			loyaltypage.enterMsisdn(msisdn);
			loyaltypage.clickOnSearchButton();
			List<String> lis1 = loyaltypage.getLoyaltyInformation();
			List<String> lis2 = MIP_Loyalty_Queries.getLoyaltyDetails(msisdn);

			Collections.sort(lis1);
			Collections.sort(lis2);
			Assert.assertTrue(lis2.toString().equalsIgnoreCase(lis1.toString()));
			loyaltypage.clickOnBackBtn();
		} catch (Throwable t) {
			log.info("Testcase  TC741-TC764-TC765 Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(enabled = false, priority = 10, testName = "TC741-TC768", dataProvider = "creditloyalty", dataProviderClass = MIP_Lotalty_TestData.class)
	public void loyaltyFour(String msisdn, String xtraproduct,
			String XLpackage, String hpproduct, String HPpackage,
			String successmsg, String productToSelect, String errmsg1,
			String errmsg2, String sms_text) throws Throwable {
		MIP_LoyaltyPage loyaltypage = null;
		try {
			sms_text = sms_text.replaceAll("AAAAA",
					MIP_Loyalty_Queries.getcustFName(msisdn));
			logger = report.startTest("Search Customer-TC741");
			log.info("Running test case -TC741");

			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Loyalty')]"));
			loyaltypage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_LoyaltyPage.class);
			loyaltypage.waitForElementToVisible(By.id("msisdn")).clear();
			loyaltypage.enterMsisdn(msisdn);
			loyaltypage.clickOnSearchButton();
			String errormsg = loyaltypage.clickOnCreditLoyaltyPackage()
					.getValidationMessage();
			Assert.assertTrue(errormsg.contains(errmsg1));
			if (!xtraproduct.equals("")) {
				String errmsg = loyaltypage.clickOnXtraLife()
						.clickOnCreditLoyaltyPackage().getValidationMessage();
				Assert.assertTrue(errmsg.contains(errmsg2));
			}
			if (!hpproduct.equals("")) {
				String errmsg = loyaltypage.clickOnHospitalization()
						.clickOnCreditLoyaltyPackage().getValidationMessage();
				Assert.assertTrue(errmsg.contains(errmsg2));
			}
			if (productToSelect.equalsIgnoreCase("Xtra-Life")) {
				successmsg = successmsg.replaceAll("XXXXX", "XL");
				if (XLpackage.equalsIgnoreCase("Data")) {
					sms_text = sms_text.replaceAll("BBBBB", "100MB");
				} else {
					sms_text = sms_text.replaceAll("BBBBB", "20SMS");
				}

				loyaltypage.clickOnXtraLife();
				loyaltypage.selectXLPackage(XLpackage)
						.clickOnCreditLoyaltyPackage()
						.confirmCustManagementPopup("yes");
				Assert.assertTrue(loyaltypage.getSuccessMessage()
						.equalsIgnoreCase(successmsg));
			} else if (productToSelect.equalsIgnoreCase("Hospitalization")) {
				successmsg = successmsg.replaceAll("XXXXX", "HP");
				if (HPpackage.equalsIgnoreCase("Data")) {
					sms_text = sms_text.replaceAll("BBBBB", "100MB");
				} else {
					sms_text = sms_text.replaceAll("BBBBB", "20SMS");
				}
				loyaltypage.clickOnHospitalization();
				loyaltypage.selectHPPackage(HPpackage)
						.clickOnCreditLoyaltyPackage().confirmPopUp("yes");
				Assert.assertTrue(loyaltypage.getSuccessMessage()
						.equalsIgnoreCase(successmsg));

			}
			Map<String, String> details = MIP_Loyalty_Queries
					.getLoyaltyDetailsAfterLoyaltyCredit(msisdn,
							productToSelect);
			String data = productToSelect.equalsIgnoreCase("Xtra-Life") ? XLpackage
					: HPpackage;
			Assert.assertTrue(details.get("Data").equalsIgnoreCase(data));
			Assert.assertTrue(details.get("is_processed").equals("1"));
			Assert.assertTrue(details.get("is_loyalty_provided").equals("1"));
			Assert.assertTrue(Integer.parseInt(details.get("attempt_count")) >= 1);
			// String date = MIP_DateFunctionality.getCurrentDate("YYYY-MM-dd");
			// Assert.assertTrue(details.get("loyalty_provided_date").equals(date));
			Assert.assertTrue(details.get("response_code").equals("405000000"));
			Assert.assertTrue(sms_text.equalsIgnoreCase(MIP_Loyalty_Queries
					.getLoyaltySMS(msisdn)));
		} catch (Throwable t) {
			log.info("Testcase  TC741-TC768 Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void after_test(ITestResult res) {

		if (res.getStatus() == ITestResult.FAILURE) {
			MIP_ScreenShots.takeScreenShot(MIP_Test_Configuration.driver,
					res.getName());
			logger.log(LogStatus.FAIL, "Test Failed");
			logger.log(LogStatus.ERROR, res.getThrowable());
		} else {
			logger.log(LogStatus.PASS, "Test passed");
		}
	}

	@AfterTest(alwaysRun = true)
	@Parameters("flag")
	public void tear_down(@Optional("0") String flag) {
		if (flag.equals("0")) {
			MIP_BrowserFactory.closeDriver(MIP_Test_Configuration.driver);
		} else {
			new MIP_HomePage(MIP_Test_Configuration.driver)
					.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
		}
		report.endTest(logger);
		report.flush();
	}
}
*/