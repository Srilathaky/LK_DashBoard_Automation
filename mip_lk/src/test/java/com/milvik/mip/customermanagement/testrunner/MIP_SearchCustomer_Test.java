package com.milvik.mip.customermanagement.testrunner;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.milvik.mip.dataprovider.MIP_SearchCustomer_TestData;
import com.milvik.mip.dbqueries.MIP_SearchCustomer_Queries;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_SearchCustomerPage;
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

public class MIP_SearchCustomer_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	static String testcaseName = "";
	MIP_LoginPage loginpage = null;
	MIP_HomePage homepage = null;

	@BeforeTest
	@Parameters({ "flag", "browser" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag) {
		log = MIP_Logging.logDetails("MIP_Search_Customer_Test");
		report = new ExtentReports(".\\Test_Reports\\SearchCustomer_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			try {
				if (!homepage.waitForElementToVisible(
						By.linkText(MIP_Menu_Constants.SEARCH_CUSTOMER))
						.isDisplayed()) {
					homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
					homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
				}
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
			}
		}
	}

	@Test(priority = 0, testName = "TC610 to TC615", dataProvider = "SearchCustomerNegative", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustOne(String testcase, String name, String msisdn,
			String nic, String ref_num, String errmsg) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		testcaseName = testcase;
		try {
			logger = report.startTest("Search Customer-TC150 to TC152");
			log.info("Running test case -" + testcase);
			try {
				homepage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'Search Customer')]"));
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
			}
			searchpage = new MIP_SearchCustomerPage(
					MIP_Test_Configuration.driver);
			searchpage.waitForElementToVisible(By.id("msisdn"));
			searchpage.enterMSISDN(msisdn).enterCustomerName(name)
					.enterRefNum(ref_num).enterNic(nic);
			searchpage.clickOnSearch();
			Assert.assertTrue(searchpage.getValidationMsg().trim()
					.replaceAll("\\s", "")
					.equalsIgnoreCase(errmsg.trim().replaceAll("\\s", "")));
			searchpage.clickOnClear();
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
			log.info("Testcase " + testcase + " Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 1, testName = "Test Clear Button Functionality", dataProvider = "searchCriteria", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustTwo(String testcase, String name, String mobilenum,
			String nic, String ref_num, String channel) throws Throwable {
		testcaseName = testcase;
		MIP_SearchCustomerPage searchpage = null;
		try {
			logger = report.startTest("Search Customer-TC154");
			log.info("Running test case -" + testcaseName);
			try {
				homepage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'Search Customer')]"));
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
			}
			searchpage = new MIP_SearchCustomerPage(
					MIP_Test_Configuration.driver);
			Assert.assertTrue(searchpage.validateSearchCustObjects());
			searchpage.enterCustomerName(name).enterMSISDN(mobilenum)
					.enterRefNum(ref_num).enterNic(nic);
			searchpage.selectSubscriberChannelType(channel);
			searchpage.clickOnClear();
			Assert.assertTrue(searchpage.getCustname().equals(""));
			Assert.assertTrue(searchpage.getNIC().equals(""));
			Assert.assertTrue(searchpage.getmsisdn().equals(""));
			Assert.assertTrue(searchpage.getRefNum().equals(""));
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
			log.info("Testcase TC154 is  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 2, testName = "SearchbyallSearchCrteria", dataProvider = "searchCriteria", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustThree(String testcase, String name, String msisdn,
			String nic, String ref_num, String channel) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		testcaseName = testcase;
		try {
			logger = report
					.startTest("Search Customer-SearchbyallSearchCrteria----"
							+ testcase);
			log.info("Running test case -SearchbyallSearchCrteria----"
					+ testcase);

			try {
				homepage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'Search Customer')]"));
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
			}
			searchpage = new MIP_SearchCustomerPage(
					MIP_Test_Configuration.driver);
			searchpage.waitForElementToVisible(By.id("msisdn"));
			if (!name.equals(""))
				searchpage.enterCustomerName(name);
			if (!msisdn.equals(""))
				searchpage.enterMSISDN(msisdn);
			if (!nic.equals(""))
				searchpage.enterNic(nic);
			if (!ref_num.equals(""))
				searchpage.enterRefNum(ref_num);
			searchpage.clickOnSearch();
			Assert.assertTrue(searchpage.validateSearchTableContent());
			if (!name.equals(""))
				Assert.assertTrue(searchpage.ValidateCustomerName(name));
			if (!msisdn.equals("")) {
				Assert.assertEquals(MIP_SearchCustomer_Queries
						.countCustomerRecordByMSISDN(msisdn), searchpage
						.ValidateMSISDNSearch());
			}
			if (!nic.equals("")) {
				Map<String, String> dashboard_details = new HashMap<String, String>();
				Map<String, String> db_details = new HashMap<String, String>();
				int count = 0;
				String chn_name = "";
				Assert.assertEquals(MIP_SearchCustomer_Queries
						.countCustomerRecordByNic(nic), searchpage
						.ValidateNicsearch());
				count = searchpage.getSearchCustomerTableRowCount();
				for (int i = 1; i <= count; i++) {
					dashboard_details = searchpage
							.getCustomerDeailsFromSearchTable(i);
					chn_name = (dashboard_details.get("subs_chnl").contains(
							MIP_CustomerManagementPage.GSM) ? MIP_CustomerManagementPage.GSM
							: ((dashboard_details.get("subs_chnl").contains(
									MIP_CustomerManagementPage.LTE) ? MIP_CustomerManagementPage.LTE
									: ((dashboard_details.get("subs_chnl"))
											.contains(MIP_CustomerManagementPage.CDMA) ? MIP_CustomerManagementPage.CDMA
											: MIP_CustomerManagementPage.DTV))));
					db_details = MIP_SearchCustomer_Queries
							.getCustomerSearchDataWithNic(nic,
									dashboard_details.get("Name"), chn_name);
					Assert.assertEquals(dashboard_details.get("Mobile Number"),
							db_details.get("Mobile Number"));
					String[] dash_reg_date = dashboard_details.get("reg_date")
							.split("\\,");
					String[] db_reg_date = db_details.get("reg_date").split(
							"\\,");
					for (int j = 0; j < dash_reg_date.length; j++) {
						Assert.assertTrue(dash_reg_date[j]
								.contains(db_reg_date[j]));
					}
					String[] dash_conf_date = dashboard_details
							.get("conf_date").split("\\,");
					String[] db_conf_date = db_details.get("conf_date").split(
							"\\,");
					for (int j = 0; j < dash_conf_date.length; j++) {
						Assert.assertTrue(dash_conf_date[j]
								.contains(db_conf_date[j]));
					}
					String[] dash_reg_by = dashboard_details.get("reg_by")
							.split("\\,");
					String[] db_reg_by = db_details.get("reg_by").split("\\,");
					for (int j = 0; j < dash_conf_date.length; j++) {
						Assert.assertTrue(dash_reg_by[j]
								.equalsIgnoreCase(db_reg_by[j]));
					}
					String[] dash_sub_prd = dashboard_details.get("subs_prdct")
							.split("\\,");
					String[] db_sub_prd = db_details.get("subs_prdct").split(
							"\\,");

					for (int j = 0; j < dash_sub_prd.length; j++) {
						System.out.println(db_sub_prd[j] = db_sub_prd[j]
								.substring(0, db_sub_prd[j].indexOf(".")));
						System.out.println(dash_sub_prd[j] = dash_sub_prd[j]
								.substring(0, dash_sub_prd[j].indexOf(".")));
						Assert.assertTrue(db_sub_prd[j]
								.equalsIgnoreCase(dash_sub_prd[j]));
						/*
						 * Assert.assertTrue(db_sub_prd[j] .replaceAll("\\.",
						 * "") .contains(dash_sub_prd[j].replaceAll("\\.",
						 * "")));
						 */
					}

				}
			}
			if (!ref_num.equals(""))
				Assert.assertEquals(MIP_SearchCustomer_Queries
						.countCustomerRecordByRefNum(ref_num), searchpage
						.ValidateRefNumsearch());
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
			log.info("Testcase TC155-TC153 is  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(priority = 3, testName = "validateCustInfo", dataProvider = "searchAndValidateCust", dataProviderClass = MIP_SearchCustomer_TestData.class)
	public void searchCustFour(String testcase, String name, String msisdn,
			String nic, String ref_num, String channel) throws Throwable {
		MIP_SearchCustomerPage searchpage = null;
		testcaseName = testcase;
		String search = "";
		List<String> sub_channel = new ArrayList<String>();
		try {
			logger = report.startTest("Search Customer-validateCustInfo----"
					+ testcase);
			log.info("Running test case -validateCustInfo----" + testcase);

			try {
				homepage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'Search Customer')]"));
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
			}
			searchpage = new MIP_SearchCustomerPage(
					MIP_Test_Configuration.driver);
			searchpage.waitForElementToVisible(By.id("msisdn"));
			if (!name.equals(""))
				searchpage.enterCustomerName(name);
			if (!msisdn.equals("")) {
				searchpage.enterMSISDN(msisdn);
				search = msisdn;
			}
			if (!nic.equals("")) {
				searchpage.enterNic(nic);
				search = nic;
			}

			if (!ref_num.equals(""))
				searchpage.enterRefNum(ref_num);
			searchpage.selectSubscriberChannelType(channel);
			searchpage.clickOnSearch();
			searchpage.clickOnCustomerNameLink(search);
			sub_channel = MIP_SearchCustomer_Queries.getSubscribedChannel(nic,
					channel);
			homepage.waitForElementToVisible(By
					.xpath("//*[contains(text(),'View Customer Details')]"));
			Map<String, String> db_custAndNominee_details = MIP_SearchCustomer_Queries
					.getCustNomineeDetails(nic, channel);
			Map<String, String> dashboard_cutsinfo = searchpage.getCustInfo();
			Assert.assertEquals(db_custAndNominee_details.get("Cust_Name")
					.trim(), dashboard_cutsinfo.get("Cust_Name").trim());
			Assert.assertEquals(db_custAndNominee_details.get("Language")
					.trim(), dashboard_cutsinfo.get("Language").trim());
			if (sub_channel.toString().contains(
					MIP_CustomerManagementPage.HOSPITALIZATION)) {
				Map<String, String> dashboard_hp_info = searchpage.getHPInfo();
				Map<String, String> db_hp_info = MIP_SearchCustomer_Queries
						.getHPProductDetails(nic, channel);
				Assert.assertEquals(dashboard_hp_info.get("msisdn"),
						db_hp_info.get("msisdn"));
				Assert.assertEquals(dashboard_hp_info.get("ref_num"),
						db_hp_info.get("ref_num"));
				Assert.assertEquals(dashboard_hp_info.get("account_id"),
						db_hp_info.get("account_id"));
				Assert.assertEquals(
						dashboard_hp_info.get("spouse_name").trim(), db_hp_info
								.get("spouse_name").trim());
				Assert.assertEquals(
						dashboard_hp_info.get("child1_name").trim(), db_hp_info
								.get("child1_name").trim());
				Assert.assertEquals(
						dashboard_hp_info.get("child2_name").trim(), db_hp_info
								.get("child2_name").trim());
				Assert.assertEquals(
						dashboard_hp_info.get("child3_name").trim(), db_hp_info
								.get("child3_name").trim());
				Assert.assertEquals(dashboard_hp_info.get("alt_msisdn").trim(),
						db_hp_info.get("alt_msisdn").trim());
				Assert.assertTrue(db_hp_info.get("offer_cover").trim()
						.contains(dashboard_hp_info.get("hp_seg").trim()));

			}
			if (sub_channel.toString().contains(MIP_CustomerManagementPage.HMP)) {
				Map<String, String> dashboard_hmp_info = searchpage
						.getHMPInfo();
				Map<String, String> db_hmp_info = MIP_SearchCustomer_Queries
						.getHMPProductDetails(nic, channel);
				Assert.assertEquals(dashboard_hmp_info.get("msisdn"),
						db_hmp_info.get("msisdn"));
				Assert.assertEquals(dashboard_hmp_info.get("ref_num"),
						db_hmp_info.get("ref_num"));
				Assert.assertEquals(dashboard_hmp_info.get("account_id"),
						db_hmp_info.get("account_id"));
				Assert.assertEquals(dashboard_hmp_info.get("addressLine1")
						.trim(), db_hmp_info.get("addressLine1").trim());
				Assert.assertEquals(dashboard_hmp_info.get("addressLine2")
						.trim(), db_hmp_info.get("addressLine2").trim());
				Assert.assertEquals(
						dashboard_hmp_info.get("postalCode").trim(),
						db_hmp_info.get("postalCode").trim());
				/*
				 * Assert.assertEquals(
				 * dashboard_hmp_info.get("alt_msisdn").trim(),
				 * db_hmp_info.get("alt_msisdn").trim());
				 */
				Assert.assertTrue(db_hmp_info.get("offer_cover").trim()
						.contains(dashboard_hmp_info.get("hmp_seg").trim()));

			}
			if (sub_channel.toString().contains(
					MIP_CustomerManagementPage.LIFE_2017)) {
				Map<String, String> dashboard_life_info = searchpage
						.getLife2017Info();
				Map<String, String> dashboard_nominee_info = searchpage
						.getNomineeInfo();
				Map<String, String> db_life_info = MIP_SearchCustomer_Queries
						.getLife2017ProductDetails(nic, channel);
				Assert.assertEquals(dashboard_life_info.get("msisdn"),
						db_life_info.get("msisdn"));
				Assert.assertEquals(dashboard_life_info.get("ref_num"),
						db_life_info.get("ref_num"));
				Assert.assertEquals(dashboard_life_info.get("account_id"),
						db_life_info.get("account_id"));
				Assert.assertEquals(dashboard_life_info.get("alt_msisdn")
						.trim(), db_life_info.get("alt_msisdn").trim());
				Assert.assertTrue(db_life_info.get("offer_cover").trim()
						.contains(dashboard_life_info.get("life_seg").trim()));

				Assert.assertEquals(dashboard_nominee_info.get("nominee_name")
						.trim(), db_custAndNominee_details.get("nominee_name")
						.trim());
				Assert.assertEquals(dashboard_nominee_info
						.get("nominee_msisdn").trim(),
						db_custAndNominee_details.get("nominee_msisdn").trim());
				Assert.assertEquals(
						dashboard_nominee_info.get("nominee_relationship")
								.trim(),
						db_custAndNominee_details.get("nominee_relationship")
								.trim());

			}
			if (sub_channel.toString().contains(
					MIP_CustomerManagementPage.PA_2017)) {
				Map<String, String> dashboard_life_info = searchpage
						.getPA2017Info();
				Map<String, String> dashboard_nominee_info = searchpage
						.getNomineeInfo();
				Map<String, String> db_life_info = MIP_SearchCustomer_Queries
						.getPA2017ProductDetails(nic, channel);
				Assert.assertEquals(dashboard_life_info.get("msisdn"),
						db_life_info.get("msisdn"));
				Assert.assertEquals(dashboard_life_info.get("ref_num"),
						db_life_info.get("ref_num"));
				Assert.assertEquals(dashboard_life_info.get("account_id"),
						db_life_info.get("account_id"));
				Assert.assertEquals(dashboard_life_info.get("alt_msisdn")
						.trim(), db_life_info.get("alt_msisdn").trim());
				Assert.assertTrue(db_life_info.get("offer_cover").trim()
						.contains(dashboard_life_info.get("pa_seg").trim()));

				Assert.assertEquals(dashboard_nominee_info.get("nominee_name")
						.trim(), db_custAndNominee_details.get("nominee_name")
						.trim());
				Assert.assertEquals(dashboard_nominee_info
						.get("nominee_msisdn").trim(),
						db_custAndNominee_details.get("nominee_msisdn").trim());

			}
			searchpage.clickOnBack();
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.SEARCH_CUSTOMER);
			log.info("Testcase TC155-TC153 is  Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void after_test(ITestResult res) {

		if (res.getStatus() == ITestResult.FAILURE) {
			MIP_ScreenShots.takeScreenShot(MIP_Test_Configuration.driver,
					res.getName() + "--" + testcaseName);
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
