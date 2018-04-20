package com.milvik.mip.offermanagement.testrunner;

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
import com.milvik.mip.dataprovider.MIP_OfferManagement_TestData;
import com.milvik.mip.dbqueries.MIP_OfferManagement_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_ListOfferPage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
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

public class MIP_ListOffer_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_ListOfferPage listofferpage = null;
	MIP_HomePage homepage = null;

	@BeforeTest
	@Parameters({ "flag", "browser" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag) {
		log = MIP_Logging.logDetails("MIP_SearchUser_Test");
		report = new ExtentReports(".\\Test_Reports\\Search_User_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.OFFER);
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_OFFERS);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.OFFER);
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_OFFERS);
		}
	}

	@Test(testName = "validateOfferPresent", dataProvider = "offerName", dataProviderClass = MIP_OfferManagement_TestData.class)
	public void offerManagementOne(String offerName) throws Throwable {
		try {
			logger = report.startTest("Offer Management-TC238-TC239");
			log.info("Running test case - validateOfferPresent");
			listofferpage = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_ListOfferPage.class);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'List Offers')]"));
			Assert.assertTrue(listofferpage.validateListOffersTable());
			Assert.assertEquals(listofferpage.getListOfferCount(),
					MIP_OfferManagement_Queries.getOfferCount());
			Map<String, String> offer_details = MIP_OfferManagement_Queries
					.getOfferNameAndDescription();
			Assert.assertEquals(offer_details,
					listofferpage.getOfferNameAndDescription());
			listofferpage.clickOnOffer(offerName);
			Assert.assertTrue(listofferpage.validateViewOfferDetails());
			Assert.assertTrue(offerName.replaceAll("\\s", "").equalsIgnoreCase(
					listofferpage.getOfferNameInViewOfferDetails().replaceAll(
							"\\s", "")));
			Assert.assertTrue(offer_details
					.get(offerName.replaceAll("\\s", ""))
					.replaceAll("\\s", "")
					.equalsIgnoreCase(
							listofferpage
									.getOfferDescriptionInViewOfferDetails()
									.replaceAll("\\s", "")));
			List<String> dashboard_offer_details = listofferpage
					.getOfferedCover();
			List<String> db_offer_details = MIP_OfferManagement_Queries
					.getOfferdCover(offerName);
			Collections.sort(dashboard_offer_details);
			Collections.sort(db_offer_details);
			Assert.assertEquals(dashboard_offer_details, db_offer_details);
			if (listofferpage.getNaturalDeathCover().size() != 0)
				Assert.assertEquals(listofferpage.getNaturalDeathCover(),
						MIP_OfferManagement_Queries.getLifeCover(offerName));
			Assert.assertEquals(listofferpage.getOfferedCoverCharges(),
					MIP_OfferManagement_Queries.getOfferCoverCharges(offerName));
			listofferpage.clickOnBack();
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_OFFERS);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.LIST_OFFERS);
			log.info("Testcase validateOfferPresent  Failed");
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
		if (flag.equals("0"))
			MIP_BrowserFactory.closeDriver(MIP_Test_Configuration.driver);
		else
			homepage.clickOnMenu(MIP_Menu_Constants.OFFER);
		report.endTest(logger);
		report.flush();
	}
}
