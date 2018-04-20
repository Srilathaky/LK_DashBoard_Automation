/*package com.milvik.mip.customermanagement.testrunner;

import java.util.ArrayList;
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
import com.milvik.mip.dataprovider.MIP_CustomerManagement_TestData;
import com.milvik.mip.dataprovider.MIP_DeRegisterCustomer_TestData;
import com.milvik.mip.dbqueries.MIP_DeRegisterCustomer_Queries;
import com.milvik.mip.dbqueries.MIP_RegisterCustomer_Queries;
import com.milvik.mip.dbqueries.MIP_SearchCustomer_Queries;
import com.milvik.mip.pageobjects.MIP_DeRegisterCustomerPage;
import com.milvik.mip.pageobjects.MIP_HomePage;
import com.milvik.mip.pageobjects.MIP_LoginPage;
import com.milvik.mip.pageobjects.MIP_RegisterCustomerPage;
import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
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

public class MIP_DeRegisterCustomer_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage;
	MIP_HomePage homepage;

	@BeforeTest
	@Parameters({ "flag", "browser" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag) {
		log = MIP_Logging.logDetails("MIP_DeRegisterCustomer_Test");
		report = new ExtentReports(
				".\\Test_Reports\\MIP_DeRegisterCustomer_Test.html");
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
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			try {
				if (!homepage.waitForElementToVisible(
						By.linkText(MIP_Menu_Constants.DE_REGISTER_CUSTOMER))
						.isDisplayed()) {
					homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
					homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
				}
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
			}
		}
	}

	@Test(testName = "TC297", dataProvider = "msisdnValidation", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void deRegCustOne(String msisdn, String errormsg) throws Throwable {
		MIP_DeRegisterCustomerPage deregcust = null;
		try {
			logger = report.startTest("DeRegister Customer-TC297");
			log.info("Running the test case TC297");

			deregcust = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_DeRegisterCustomerPage.class);
			try {
				homepage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'De-Register Customer')]"));
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);

			}
			deregcust.waitForElementToVisible(By.id("msisdn"));
			Assert.assertTrue(deregcust.mobileNum.isDisplayed());
			Assert.assertTrue(deregcust.searchIcon.isDisplayed());
			deregcust.enterMsisdn(msisdn);
			deregcust.clickOnSearchButton();
			Assert.assertTrue(deregcust.getValidationMessage()
					.equalsIgnoreCase(errormsg));
			deregcust.mobileNum.clear();
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
			log.info("TC297 Test Failed");
			log.info("Error occured in the test case TC297", t);
			throw t;
		}
	}

	@Test(testName = "TC297", dataProvider = "deRegisterNegativeTest", dataProviderClass = MIP_CustomerManagement_TestData.class)
	public void deRegCustTwo(String msisdn, String errormsg) throws Throwable {
		MIP_DeRegisterCustomerPage deregcust = null;
		try {
			logger = report.startTest("DeRegister Customer-TC297");
			log.info("Running the test case TC297");

			try {
				homepage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'De-Register Customer')]"));
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);

			}
			deregcust = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_DeRegisterCustomerPage.class);
			deregcust.waitForElementToVisible(By.id("msisdn"));
			deregcust.enterMsisdn(msisdn);
			deregcust.clickOnSearchButton();
			Assert.assertTrue((new MIP_CustomerManagementPage(
					MIP_Test_Configuration.driver)).getSuccessMessage()
					.equalsIgnoreCase(errormsg));
			deregcust.mobileNum.clear();
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
			log.info("TC297 Test Failed");
			log.info("Error occured in the test case TC297", t);
			throw t;
		}
	}

	@Test(enabled = false, testName = "DeRegisterNon-ConfrimedNewCustomer", dataProvider = "deRegisterCustomerPositive", dataProviderClass = MIP_DeRegisterCustomer_TestData.class)
	public void deRegCustThree(String testcase, String msisdn, String product,
			String fname, String sname, String dob, String age, String gender,
			String xl_relation, String xl_rel_fname, String xl_rel_sname,
			String xl_rel_dob, String xl_age, String xl_inform_ben,
			String xl_rel_msisdn, String hp_relation, String hp_fname,
			String hp_sname, String hp_dob, String hp_age,
			String inform_ben_hp, String hp_rel_mobilenum, String offer_cover,
			String nominee_fname, String nominee_sname, String nominee_age,
			String inform_nominee, String nominee_mobilenum,
			String product_dereg, String message) throws Throwable {
		MIP_RegisterCustomerPage regpage = null;
		try {
			logger = report.startTest("DeRegisterNon-ConfrimedNewCustomer");
			log.info("msisdn= " + msisdn);

			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);

			regpage = new MIP_RegisterCustomerPage(
					MIP_Test_Configuration.driver);
			homepage.waitForElementToVisible(By
					.xpath("//h3[starts-with(normalize-space(text()),'Register Customer')]"));
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
			} else if (product.trim().contains(MIP_CustomerManagementPage.IP)
					&& product.trim().contains(
							MIP_CustomerManagementPage.XTRALIFE)) {
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

			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
			MIP_DeRegisterCustomerPage deregpage = PageFactory.initElements(
					MIP_Test_Configuration.driver,
					MIP_DeRegisterCustomerPage.class);
			deregpage.waitForElementToVisible(By
					.xpath("//h3[contains(text(),'De-Register Customer')]"));
			deregpage.enterMsisdn(msisdn);
			deregpage.clickOnSearchButton();
			Assert.assertTrue(deregpage.validateTableHeading());
			List<String> product_detils1 = deregpage.getCustomerProducts();
			Collections.sort(product_detils1);
			List<String> product_detils2 = MIP_DeRegisterCustomer_Queries
					.getRegisteredProduct(msisdn);
			Collections.sort(product_detils2);
			Assert.assertTrue((product_detils1.equals(product_detils2)));
			Assert.assertTrue(deregpage.getProductCount() == MIP_DeRegisterCustomer_Queries
					.getActiveProductCount(msisdn));
			for (String prd : product_detils1) {
				Map<String, String> dashboard_value = deregpage
						.getCustomerInformation(prd);
				Map<String, String> db_value = MIP_DeRegisterCustomer_Queries
						.getCustomerDetails(msisdn, prd);
				List<String> dashboard_list = new ArrayList<String>(
						dashboard_value.values());
				List<String> db_list = new ArrayList<String>(db_value.values());
				Collections.sort(dashboard_list);
				Collections.sort(db_list);
				Assert.assertTrue(dashboard_list.size() == db_list.size());
				for (int i = 0; i < db_list.size(); i++) {
					Assert.assertTrue(db_list.get(i).contains(
							dashboard_list.get(i)));
				}
			}
			deregpage.selectProductToDeRegister(product_dereg)
					.clickOnDeRegisterBtn().confirmPopUp("yes");
			message = message.replaceAll("XXXXX", msisdn);

			Assert.assertTrue(message.replaceAll("\\s", "").equalsIgnoreCase(
					deregpage.getSuccessMessage().replaceAll("\\s", "")));

			if (product_dereg
					.equalsIgnoreCase(MIP_CustomerManagementPage.XTRALIFE)) {
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getCustomerStatus(msisdn, product_dereg));

				Map<String, String> rel_details = MIP_SearchCustomer_Queries
						.getDeactivatedRelativeInfo(msisdn, product_dereg);
				Assert.assertTrue(rel_details.get("fname").equalsIgnoreCase(
						xl_rel_fname));
				Assert.assertTrue(rel_details.get("sname").equalsIgnoreCase(
						xl_rel_sname));
				Assert.assertTrue(rel_details.get("dob").contains(
						MIP_DateFunctionality
								.converDateToDBDateFormat(xl_rel_dob
										.split("\\s")[0])));
				if (!xl_age.equals(""))
					Assert.assertTrue(rel_details.get("age").equalsIgnoreCase(
							xl_age));
				Assert.assertTrue(rel_details.get("cust_relationship")
						.equalsIgnoreCase(xl_relation));
				Assert.assertTrue(rel_details.get("msisdn").equalsIgnoreCase(
						xl_rel_msisdn));

				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.validateProductCancelation(msisdn, product_dereg));
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getDeRegisteredSMS(msisdn, product_dereg));
				deregpage.clickOnHereLink().enterMsisdn(msisdn);
				deregpage.clickOnSearchButton();
				Assert.assertTrue(deregpage
						.validateTableHeadingAfterDeRegister());
				Map<String, String> dashboard_value = deregpage
						.getCustomerInformationAfterDeRegistration(product_dereg);
				Map<String, String> db_value1 = MIP_DeRegisterCustomer_Queries
						.getDeRegisterInfo(msisdn, product_dereg);
				Assert.assertTrue(dashboard_value.get("De-registered Date")
						.equalsIgnoreCase(db_value1.get("De_registered_Date")));
				Assert.assertTrue(dashboard_value.get(
						"Date of customer removal ").equalsIgnoreCase(
						db_value1.get("Date_of_customer_removal")));
				Assert.assertTrue(dashboard_value.get("De-registered by")
						.equalsIgnoreCase(db_value1.get("De_registered_by")));
			} else if (product_dereg
					.equalsIgnoreCase(MIP_CustomerManagementPage.HOSPITAL)) {
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getCustomerStatus(msisdn, product_dereg));

				Map<String, String> rel_details = MIP_SearchCustomer_Queries
						.getDeactivatedRelativeInfo(msisdn, product_dereg);
				Assert.assertTrue(rel_details.get("fname").equalsIgnoreCase(
						hp_fname));
				Assert.assertTrue(rel_details.get("sname").equalsIgnoreCase(
						hp_sname));
				if (!hp_age.equals(""))
					Assert.assertTrue(rel_details.get("age").equalsIgnoreCase(
							hp_age));
				Assert.assertTrue(rel_details.get("dob").contains(
						MIP_DateFunctionality.converDateToDBDateFormat(hp_dob
								.split("\\s")[0])));
				Assert.assertTrue(rel_details.get("cust_relationship")
						.equalsIgnoreCase(hp_relation));
				Assert.assertTrue(rel_details.get("msisdn").equalsIgnoreCase(
						hp_rel_mobilenum));

				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.validateProductCancelation(msisdn, product_dereg));
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getDeRegisteredSMS(msisdn, product_dereg));
				homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			} else if (product_dereg
					.equalsIgnoreCase(MIP_CustomerManagementPage.IP)) {
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getCustomerStatus(msisdn, product_dereg));

				Map<String, String> rel_details = MIP_SearchCustomer_Queries
						.getDeactivatedRelativeInfo(msisdn, product_dereg);
				Assert.assertTrue(rel_details.get("fname").equalsIgnoreCase(
						nominee_fname));
				Assert.assertTrue(rel_details.get("sname").equalsIgnoreCase(
						nominee_sname));
				Assert.assertTrue(rel_details.get("age").equalsIgnoreCase(
						nominee_age));
				Assert.assertTrue(rel_details.get("msisdn").equalsIgnoreCase(
						nominee_mobilenum));

				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.validateProductCancelation(msisdn, product_dereg));
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getDeRegisteredSMS(msisdn, product_dereg));
				homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			} else if (product
					.trim()
					.toUpperCase()
					.contains(MIP_CustomerManagementPage.XTRALIFE.toUpperCase())
					&& product
							.trim()
							.toUpperCase()
							.contains(
									MIP_CustomerManagementPage.IP.toUpperCase())) {
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getCustomerStatus(msisdn,
								MIP_CustomerManagementPage.XTRALIFE));
				Map<String, String> rel_details = MIP_SearchCustomer_Queries
						.getDeactivatedRelativeInfo(msisdn,
								MIP_CustomerManagementPage.XTRALIFE);
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getCustomerStatus(msisdn,
								MIP_CustomerManagementPage.XTRALIFE));

				Assert.assertTrue(rel_details.get("fname").equalsIgnoreCase(
						xl_rel_fname));
				Assert.assertTrue(rel_details.get("sname").equalsIgnoreCase(
						xl_rel_sname));
				Assert.assertTrue(rel_details.get("dob").contains(
						MIP_DateFunctionality
								.converDateToDBDateFormat(xl_rel_dob
										.split("\\s")[0])));
				if (!xl_age.equals(""))
					Assert.assertTrue(rel_details.get("age").equalsIgnoreCase(
							xl_age));
				Assert.assertTrue(rel_details.get("cust_relationship")
						.equalsIgnoreCase(xl_relation));
				Assert.assertTrue(rel_details.get("msisdn").equalsIgnoreCase(
						xl_rel_msisdn));

				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.validateProductCancelation(msisdn,
								MIP_CustomerManagementPage.XTRALIFE));

				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getCustomerStatus(msisdn,
								MIP_CustomerManagementPage.IP));

				Map<String, String> rel_details1 = MIP_SearchCustomer_Queries
						.getDeactivatedRelativeInfo(msisdn,
								MIP_CustomerManagementPage.IP);
				Assert.assertTrue(rel_details1.get("fname").equalsIgnoreCase(
						nominee_fname));
				Assert.assertTrue(rel_details1.get("sname").equalsIgnoreCase(
						nominee_sname));
				Assert.assertTrue(rel_details1.get("age").equalsIgnoreCase(
						nominee_age));
				Assert.assertTrue(rel_details1.get("msisdn").equalsIgnoreCase(
						nominee_mobilenum));

				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.validateProductCancelation(msisdn,
								MIP_CustomerManagementPage.IP));
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getDeRegisteredSMS(msisdn, product_dereg));
				homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			} else if (product
					.trim()
					.toUpperCase()
					.contains(MIP_CustomerManagementPage.XTRALIFE.toUpperCase())
					&& product
							.trim()
							.toUpperCase()
							.contains(
									MIP_CustomerManagementPage.HOSPITAL
											.toUpperCase())) {
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getCustomerStatus(msisdn,
								MIP_CustomerManagementPage.XTRALIFE));
				Map<String, String> rel_details = MIP_SearchCustomer_Queries
						.getDeactivatedRelativeInfo(msisdn,
								MIP_CustomerManagementPage.XTRALIFE);

				Assert.assertTrue(rel_details.get("fname").equalsIgnoreCase(
						xl_rel_fname));
				Assert.assertTrue(rel_details.get("sname").equalsIgnoreCase(
						xl_rel_sname));
				Assert.assertTrue(rel_details.get("dob").contains(
						MIP_DateFunctionality
								.converDateToDBDateFormat(xl_rel_dob
										.split("\\s")[0])));
				if (!xl_age.equals(""))
					Assert.assertTrue(rel_details.get("age").equalsIgnoreCase(
							xl_age));
				Assert.assertTrue(rel_details.get("cust_relationship")
						.equalsIgnoreCase(xl_relation));
				Assert.assertTrue(rel_details.get("msisdn").equalsIgnoreCase(
						xl_rel_msisdn));

				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.validateProductCancelation(msisdn,
								MIP_CustomerManagementPage.XTRALIFE));
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getCustomerStatus(msisdn,
								MIP_CustomerManagementPage.HOSPITAL));
				Map<String, String> rel_details1 = MIP_SearchCustomer_Queries
						.getDeactivatedRelativeInfo(msisdn,
								MIP_CustomerManagementPage.HOSPITAL);
				Assert.assertTrue(rel_details1.get("fname").equalsIgnoreCase(
						hp_fname));
				Assert.assertTrue(rel_details1.get("sname").equalsIgnoreCase(
						hp_sname));
				if (!hp_age.equals(""))
					Assert.assertTrue(rel_details1.get("age").equalsIgnoreCase(
							hp_age));
				Assert.assertTrue(rel_details1.get("dob").contains(
						MIP_DateFunctionality.converDateToDBDateFormat(hp_dob
								.split("\\s")[0])));
				Assert.assertTrue(rel_details1.get("cust_relationship")
						.equalsIgnoreCase(hp_relation));
				Assert.assertTrue(rel_details1.get("msisdn").equalsIgnoreCase(
						hp_rel_mobilenum));

				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.validateProductCancelation(msisdn,
								MIP_CustomerManagementPage.HOSPITAL));
				Assert.assertTrue(MIP_DeRegisterCustomer_Queries
						.getDeRegisteredSMS(msisdn, product_dereg));
				homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			}

		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			log.info("DeRegisterNon-ConfrimedNewCustomer Test case Failed");
			log.info("Error occured in the test case", t);
			throw t;

		}
	}

	@Test(enabled = true, testName = "DeRegisterExistingCustomer", dataProvider = "deRegisterExistingCustomer", dataProviderClass = MIP_DeRegisterCustomer_TestData.class)
	public void deRegCustFour(String msisdn, String succ_message,
			String product_dereg) throws Throwable {
		MIP_DeRegisterCustomerPage deregpage = null;
		try {
			logger = report.startTest("DeRegisterExistingCustomer");
			log.info("msisdn=" + msisdn);

			deregpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_DeRegisterCustomerPage.class);
			try {
				homepage.waitForElementToVisible(By
						.xpath("//h3[contains(text(),'De-Register Customer')]"));
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);

			}
			Map<String, String> rel_details_before_dereg = MIP_RegisterCustomer_Queries
					.getRelativeInfo(msisdn, product_dereg);
			deregpage.waitForElementToVisible(By.id("msisdn"));
			deregpage.enterMsisdn(msisdn);
			deregpage.clickOnSearchButton();
			Assert.assertTrue(deregpage.validateTableHeading());
			List<String> product_detils1 = deregpage.getCustomerProducts();
			Collections.sort(product_detils1);
			List<String> product_detils2 = MIP_DeRegisterCustomer_Queries
					.getRegisteredProduct(msisdn);
			Collections.sort(product_detils2);
			Assert.assertTrue((product_detils1.equals(product_detils2)));
			Assert.assertTrue(deregpage.getProductCount() == MIP_DeRegisterCustomer_Queries
					.getActiveProductCount(msisdn));
			for (String prd : product_detils1) {
				Map<String, String> dashboard_value = deregpage
						.getCustomerInformation(prd);
				Map<String, String> db_value = MIP_DeRegisterCustomer_Queries
						.getCustomerDetails(msisdn, prd);
				List<String> dashboard_list = new ArrayList<String>(
						dashboard_value.values());
				List<String> db_list = new ArrayList<String>(db_value.values());
				Collections.sort(dashboard_list);
				Collections.sort(db_list);
				Assert.assertTrue(dashboard_list.size() == db_list.size());
				for (int i = 0; i < db_list.size(); i++) {
					Assert.assertTrue(db_list.get(i).contains(
							dashboard_list.get(i)));
				}
			}
			deregpage.selectProductToDeRegister(product_dereg)
					.clickOnDeRegisterBtn().confirmPopUp("yes");
			succ_message = succ_message.replaceAll("XXXXX", msisdn);

			Assert.assertTrue(succ_message
					.replaceAll("\\s", "")
					.equalsIgnoreCase(
							deregpage.getSuccessMessage().replaceAll("\\s", "")));

			Assert.assertTrue(MIP_DeRegisterCustomer_Queries.getCustomerStatus(
					msisdn, product_dereg));

			Map<String, String> rel_details = MIP_SearchCustomer_Queries
					.getDeactivatedRelativeInfo(msisdn, product_dereg);
			if (product_dereg
					.equalsIgnoreCase(MIP_CustomerManagementPage.XTRALIFE)) {

				Assert.assertTrue(rel_details.get("fname").equalsIgnoreCase(
						rel_details_before_dereg.get("fname")));
				Assert.assertTrue(rel_details.get("sname").equalsIgnoreCase(
						rel_details_before_dereg.get("sname")));
				Assert.assertTrue(rel_details.get("dob").contains(
						rel_details_before_dereg.get("dob")));
				Assert.assertTrue(rel_details.get("age").equalsIgnoreCase(
						rel_details_before_dereg.get("age")));
				Assert.assertTrue(rel_details.get("cust_relationship")
						.equalsIgnoreCase(
								rel_details_before_dereg
										.get("cust_relationship")));
				Assert.assertTrue(rel_details.get("msisdn").equalsIgnoreCase(
						rel_details_before_dereg.get("msisdn")));

			}
			if (product_dereg
					.equalsIgnoreCase(MIP_CustomerManagementPage.HOSPITAL)) {
				Assert.assertTrue(rel_details.get("fname").equalsIgnoreCase(
						rel_details_before_dereg.get("fname")));
				Assert.assertTrue(rel_details.get("sname").equalsIgnoreCase(
						rel_details_before_dereg.get("sname")));
				Assert.assertTrue(rel_details.get("age").equalsIgnoreCase(
						rel_details_before_dereg.get("age")));
				Assert.assertTrue(rel_details.get("dob").contains(
						rel_details_before_dereg.get("dob")));
				Assert.assertTrue(rel_details.get("cust_relationship")
						.equalsIgnoreCase(
								rel_details_before_dereg
										.get("cust_relationship")));
				Assert.assertTrue(rel_details.get("msisdn").equalsIgnoreCase(
						rel_details_before_dereg.get("msisdn")));
			}
			if (product_dereg.equalsIgnoreCase(MIP_CustomerManagementPage.IP)) {
				Assert.assertTrue(rel_details.get("fname").equalsIgnoreCase(
						rel_details_before_dereg.get("fname")));
				Assert.assertTrue(rel_details.get("sname").equalsIgnoreCase(
						rel_details_before_dereg.get("sname")));
				Assert.assertTrue(rel_details.get("age").equalsIgnoreCase(
						rel_details_before_dereg.get("age")));
				Assert.assertTrue(rel_details.get("msisdn").equalsIgnoreCase(
						rel_details_before_dereg.get("msisdn")));
			}

			Assert.assertTrue(MIP_DeRegisterCustomer_Queries
					.validateProductCancelation(msisdn, product_dereg));
			Assert.assertTrue(MIP_DeRegisterCustomer_Queries
					.getDeRegisteredSMS(msisdn, product_dereg));
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.DE_REGISTER_CUSTOMER);
			log.info("DeRegisterNon-ConfrimedNewCustomer Test case Failed");
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
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
		report.endTest(logger);
		report.flush();

	}
}
*/