/*package com.milvik.mip.customermanagement.testrunner;

import java.util.ArrayList;
import java.util.List;

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
import com.milvik.mip.dataprovider.MIP_CoverHistory_TestData;
import com.milvik.mip.dataprovider.MIP_CustomerManagement_TestData;
import com.milvik.mip.dbqueries.MIP_CoverHistory_Queries;
import com.milvik.mip.dbqueries.MIP_RegisterCustomer_Queries;
import com.milvik.mip.pageobjects.MIP_CoverHistoryPage;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_RegisterCustomerPage;
import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
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

public class MIP_CoverHistory_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;

	@BeforeTest
	@Parameters({ "flag", "browser" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag) {
		log = MIP_Logging.logDetails("MIP_CoverHistory_Test");
		report = new ExtentReports(
				".\\Test_Reports\\MIP_CoverHistory_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.COVER_HISTORY);
		} else {
			
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.COVER_HISTORY);
		}
	}

	@Test(priority = 0, testName = "TC337", dataProvider = "msisdnValidation", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void coverHistoryOne(String msisdn, String errormsg)
			throws Throwable {
		MIP_CoverHistoryPage coverhistory = null;
		try {
			logger = report.startTest("CoverHistory-TC337");
			log.info("Running the test case TC337");

			coverhistory = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_CoverHistoryPage.class);
			Assert.assertTrue(coverhistory.mobileNum.isDisplayed());
			Assert.assertTrue(coverhistory.searchIcon.isDisplayed());
			coverhistory.enterMsisdn(msisdn);
			coverhistory.clickOnSearchButton();
			System.out.println(coverhistory.getValidationMessage());
			Assert.assertTrue(coverhistory.getValidationMessage()
					.equalsIgnoreCase(errormsg));
			coverhistory.mobileNum.clear();
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.COVER_HISTORY);
			log.info("TC337 Test Failed");
			log.info("Error occured in the test case TC337", t);
			throw t;
		}
	}

	@Test(priority = 1, testName = "TC341-TC342", dataProvider = "coverHistoryNegativeTest", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void coverHistoryTwo(String msisdn, String errorMsg)
			throws Throwable {
		MIP_CoverHistoryPage coverhistory = null;
		try {
			logger = report.startTest("CoverHistory-TC341-TC342");
			log.info("Running the test case TC341-TC342");

			coverhistory = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_CoverHistoryPage.class);
			coverhistory.enterMsisdn(msisdn);
			coverhistory.clickOnSearchButton();
			Assert.assertTrue(coverhistory.getValidationMessage()
					.equalsIgnoreCase(errorMsg));
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.COVER_HISTORY);
			log.info("TC341-TC342 Test Failed");
			log.info("Error occured in the test case TC341-TC342", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "coverHIstoryForAllProduct", dataProvider = "coverHistoryPositive", dataProviderClass = MIP_CoverHistory_TestData.class)
	public void coverHistoryThree(String testcase, String msisdn,
			String product, String fname, String sname, String dob, String age,
			String gender, String xl_relation, String xl_rel_fname,
			String xl_rel_sname, String xl_rel_dob, String xl_age,
			String xl_inform_ben, String xl_rel_msisdn, String hp_relation,
			String hp_fname, String hp_sname, String hp_dob, String hp_age,
			String inform_ben_hp, String hp_rel_mobilenum, String offer_cover,
			String nominee_fname, String nominee_sname, String nominee_age,
			String inform_nominee, String nominee_mobilenum,
			String prev_month_usage, String cover_free, String charges_xl,
			String cover_xl, String charges_hp, String cover_hp,
			String charges_ip, String cover_ip, String month, String year)
			throws Throwable {
		MIP_CoverHistoryPage coverhistory = null;
		MIP_RegisterCustomerPage regpage = null;
		try {
			logger = report.startTest("CoverHistory-coverHIstoryForAllProduct");

			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			homepage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'Register Customer')]"));
			regpage = new MIP_RegisterCustomerPage(
					MIP_Test_Configuration.driver);
			regpage.waitForElementToVisible(By.id("msisdn"));
			regpage.enterMSISDN(msisdn).clickOnSearchButton();
			if (!fname.equals("")) {
				regpage.enterCustInfo(fname, sname, dob, age, gender);
			}

			if (product.equalsIgnoreCase(MIP_CustomerManagementPage.XTRALIFE)) {

				regpage.selectXtraLife();
				regpage.enterInsuredRelaiveInfo_XL(xl_relation, xl_rel_fname,
						xl_rel_sname, xl_rel_dob, xl_age, xl_inform_ben,
						xl_rel_msisdn);
				regpage.clickOnSave();
				regpage.confirmPopUp("yes");
				regpage.getSuccessMsg();
				regpage.validateCustomerDetails(msisdn, fname, sname, dob, age,
						gender);
				regpage.validateRelativeInfo(msisdn,
						MIP_CustomerManagementPage.XTRALIFE, xl_rel_fname,
						xl_rel_sname, xl_rel_dob, xl_age, xl_relation,
						xl_rel_msisdn);
				Assert.assertNotEquals(MIP_RegisterCustomer_Queries.getSMSText(
						msisdn, MIP_CustomerManagementPage.XTRALIFE), "");
				if (!xl_rel_msisdn.equals(""))
					Assert.assertNotEquals(MIP_RegisterCustomer_Queries
							.getBenSMSText(xl_rel_msisdn), "");

			} else if (product
					.equalsIgnoreCase(MIP_CustomerManagementPage.HOSPITAL)) {
				regpage.selectHospitalization();
				regpage.enterInsuredRelaiveInfo_HP(hp_relation, hp_fname,
						hp_sname, hp_dob, hp_age, inform_ben_hp,
						hp_rel_mobilenum);
				regpage.clickOnSave();
				regpage.confirmPopUp("yes");
				regpage.getSuccessMsg();
				regpage.validateCustomerDetails(msisdn, fname, sname, dob, age,
						gender);
				regpage.validateRelativeInfo(msisdn,
						MIP_CustomerManagementPage.HOSPITAL, hp_fname,
						hp_sname, hp_dob, hp_age, hp_relation, hp_rel_mobilenum);
				Assert.assertNotEquals(MIP_RegisterCustomer_Queries.getSMSText(
						msisdn, MIP_CustomerManagementPage.HOSPITAL), "");
				if (!hp_rel_mobilenum.equals(""))
					Assert.assertNotEquals(MIP_RegisterCustomer_Queries
							.getBenSMSText(hp_rel_mobilenum), "");

			} else if (product.equalsIgnoreCase(MIP_CustomerManagementPage.IP)) {
				regpage.selectIncomeProtection();
				regpage.selectIncomeProtectionCoverDetails(offer_cover)
						.enterNomineeInfo_IP(nominee_fname, nominee_sname,
								nominee_age, inform_nominee, nominee_mobilenum);
				regpage.clickOnSave();
				regpage.confirmPopUp("yes");
				regpage.getSuccessMsg();
				regpage.validateCustomerDetails(msisdn, fname, sname, dob, age,
						gender);
				regpage.validateRelativeInfo(msisdn,
						MIP_CustomerManagementPage.IP, nominee_fname,
						nominee_sname, "", nominee_age, "", nominee_mobilenum);
				Assert.assertNotEquals(MIP_RegisterCustomer_Queries.getSMSText(
						msisdn, MIP_CustomerManagementPage.IP), "");
				if (!nominee_mobilenum.equals(""))
					Assert.assertNotEquals(MIP_RegisterCustomer_Queries
							.getBenSMSText(nominee_mobilenum), "");
				Assert.assertTrue(MIP_RegisterCustomer_Queries
						.getCoverDetailsIP(msisdn)
						.equalsIgnoreCase(offer_cover));
			} else if (product.contains(MIP_CustomerManagementPage.IP)
					&& product.contains(MIP_CustomerManagementPage.XTRALIFE)) {
				regpage.selectXtraLife();
				regpage.selectIncomeProtection();
				regpage.enterInsuredRelaiveInfo_XL(xl_relation, xl_rel_fname,
						xl_rel_sname, xl_rel_dob, xl_age, xl_inform_ben,
						xl_rel_msisdn);
				regpage.selectIncomeProtectionCoverDetails(offer_cover)
						.enterNomineeInfo_IP(nominee_fname, nominee_sname,
								nominee_age, inform_nominee, nominee_mobilenum);
				regpage.clickOnSave();
				regpage.confirmPopUp("yes");
				regpage.getSuccessMsg();
				Thread.sleep(1000);
				regpage.validateCustomerDetails(msisdn, fname, sname, dob, age,
						gender);
				regpage.validateRelativeInfo(msisdn,
						MIP_CustomerManagementPage.XTRALIFE, xl_rel_fname,
						xl_rel_sname, xl_rel_dob, xl_age, xl_relation,
						xl_rel_msisdn);
				regpage.validateRelativeInfo(msisdn,
						MIP_CustomerManagementPage.IP, nominee_fname,
						nominee_sname, "", nominee_age, "", nominee_mobilenum);
				Assert.assertNotEquals(MIP_RegisterCustomer_Queries.getSMSText(
						msisdn, MIP_CustomerManagementPage.XTRALIFE), "");
				if (!xl_rel_msisdn.equals(""))
					Assert.assertNotEquals(MIP_RegisterCustomer_Queries
							.getBenSMSText(xl_rel_msisdn), "");
				Assert.assertNotEquals(MIP_RegisterCustomer_Queries.getSMSText(
						msisdn, MIP_CustomerManagementPage.IP), "");
				if (!nominee_mobilenum.equals(""))
					Assert.assertNotEquals(MIP_RegisterCustomer_Queries
							.getBenSMSText(nominee_mobilenum), "");
				Assert.assertTrue(MIP_RegisterCustomer_Queries
						.getCoverDetailsIP(msisdn)
						.equalsIgnoreCase(offer_cover));
			} else if (product.contains(MIP_CustomerManagementPage.HOSPITAL)
					&& product.contains(MIP_CustomerManagementPage.XTRALIFE)) {
				regpage.selectXtraLife();
				regpage.selectHospitalization();
				regpage.enterInsuredRelaiveInfo_XL(xl_relation, xl_rel_fname,
						xl_rel_sname, xl_rel_dob, xl_age, xl_inform_ben,
						xl_rel_msisdn);
				regpage.enterInsuredRelaiveInfo_HP(hp_relation, hp_fname,
						hp_sname, hp_dob, hp_age, inform_ben_hp,
						hp_rel_mobilenum);
				regpage.clickOnSave();
				regpage.confirmPopUp("yes");
				regpage.getSuccessMsg();
				regpage.validateCustomerDetails(msisdn, fname, sname, dob, age,
						gender);
				regpage.validateRelativeInfo(msisdn,
						MIP_CustomerManagementPage.HOSPITAL, hp_fname,
						hp_sname, hp_dob, hp_age, hp_relation, hp_rel_mobilenum);
				regpage.validateRelativeInfo(msisdn,
						MIP_CustomerManagementPage.XTRALIFE, xl_rel_fname,
						xl_rel_sname, xl_rel_dob, xl_age, xl_relation,
						xl_rel_msisdn);
				Assert.assertNotEquals(MIP_RegisterCustomer_Queries.getSMSText(
						msisdn, MIP_CustomerManagementPage.XTRALIFE), "");
				Assert.assertNotEquals(MIP_RegisterCustomer_Queries.getSMSText(
						msisdn, MIP_CustomerManagementPage.HOSPITAL), "");
				if (!xl_rel_msisdn.equals(""))
					Assert.assertNotEquals(MIP_RegisterCustomer_Queries
							.getBenSMSText(xl_rel_msisdn), "");

				if (!hp_rel_mobilenum.equals(""))
					Assert.assertNotEquals(MIP_RegisterCustomer_Queries
							.getBenSMSText(hp_rel_mobilenum), "");

			}

			homepage.clickOnMenu(MIP_Menu_Constants.COVER_HISTORY);
			coverhistory = PageFactory.initElements(
					MIP_Test_Configuration.driver, MIP_CoverHistoryPage.class);
			if (product.equalsIgnoreCase(MIP_CustomerManagementPage.XTRALIFE)) {

				Assert.assertTrue(MIP_CoverHistory_Queries
						.setCoverHistoryDetails(product, msisdn,
								prev_month_usage, cover_free, charges_xl,
								cover_xl, charges_hp, cover_hp, charges_ip,
								cover_ip, month, year));
				coverhistory.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'Cover History')]"));
				coverhistory.enterMsisdn(msisdn);
				coverhistory.clickOnSearchButton();
				Assert.assertTrue(coverhistory.validateTableHeadingXtraLife());
				List<String> details1 = coverhistory
						.getCoverHistoryDetailsXtraLife();
				List<String> input = new ArrayList<String>();
				input.add(month + "-" + year);
				input.add("GHC "
						+ (Float.valueOf(cover_free) + Float.valueOf(cover_xl)));
				input.add("GHC " + charges_xl);
				input.add("GHC " + prev_month_usage);
				for (int i = 0; i < details1.size(); i++) {

					Assert.assertTrue((input.get(i).trim()
							.replaceAll("\\s", "").contains(details1.get(i)
							.trim().replaceAll("\\s", "")))
							|| (details1.get(i).trim().replaceAll("\\s", "")
									.contains(input.get(i).trim()
											.replaceAll("\\s", ""))));
				}
			}

			else if (product
					.equalsIgnoreCase(MIP_CustomerManagementPage.HOSPITAL)) {

				Assert.assertTrue(MIP_CoverHistory_Queries
						.setCoverHistoryDetails(product, msisdn,
								prev_month_usage, cover_free, charges_xl,
								cover_xl, charges_hp, cover_hp, charges_ip,
								cover_ip, month, year));

				coverhistory.enterMsisdn(msisdn);
				coverhistory.clickOnSearchButton();
				Assert.assertTrue(coverhistory.validateTableHeadingHP());// cover_hp/30
				List<String> details1 = coverhistory.getCoverHistoryDetailsHP();
				List<String> input = new ArrayList<String>();
				input.add(month + "-" + year);
				input.add("GHC " + (Float.valueOf(cover_hp) / 30));
				input.add("GHC " + charges_hp);
				for (int i = 0; i < details1.size(); i++) {

					Assert.assertTrue((input.get(i).trim()
							.replaceAll("\\s", "").contains(details1.get(i)
							.trim().replaceAll("\\s", "")))
							|| (details1.get(i).trim().replaceAll("\\s", "")
									.contains(input.get(i).trim()
											.replaceAll("\\s", ""))));
				}
			}

			else if (product.equalsIgnoreCase(MIP_CustomerManagementPage.IP)) {

				Assert.assertTrue(MIP_CoverHistory_Queries
						.setCoverHistoryDetails(product, msisdn,
								prev_month_usage, cover_free, charges_xl,
								cover_xl, charges_hp, cover_hp, charges_ip,
								cover_ip, month, year));

				coverhistory.enterMsisdn(msisdn);
				coverhistory.clickOnSearchButton();
				Assert.assertTrue(coverhistory.validateTableHeadingIP());
				List<String> details1 = coverhistory.getCoverHistoryDetailsIP();
				List<String> input = new ArrayList<String>();
				input.add(month + "-" + year);
				input.add("GHC " + (cover_ip));
				input.add("GHC " + charges_ip);
				for (int i = 0; i < details1.size(); i++) {

					Assert.assertTrue((input.get(i).trim()
							.replaceAll("\\s", "").contains(details1.get(i)
							.trim().replaceAll("\\s", "")))
							|| (details1.get(i).trim().replaceAll("\\s", "")
									.contains(input.get(i).trim()
											.replaceAll("\\s", ""))));
				}
			}
			Assert.assertTrue(coverhistory.validateEarlierMonthButton());
			homepage.clickOnMenu(MIP_Menu_Constants.COVER_HISTORY);

		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.COVER_HISTORY);
			log.info("coverHIstoryForAllProduct Test Failed");
			log.info("Error occured in the test case TC338 to TC340", t);
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
		homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
		report.endTest(logger);
		report.flush();
	}
}
*/