package com.milvik.mip.customermanagement.testrunner;

import java.util.Collections;
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
import com.milvik.mip.dataprovider.MIP_RegCust_TestData;
import com.milvik.mip.dbqueries.MIP_RegisterCustomer_Queries;
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

public class MIP_CustRegistration_Prepaid_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage;
	MIP_HomePage homepage;
	MIP_RegisterCustomerPage regpage = null;
	String username = "";

	@BeforeTest
	@Parameters({ "flag", "browser" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag) {
		log = MIP_Logging.logDetails("MIP_CustRegistration_Prepaid_Test");
		report = new ExtentReports(
				".\\Test_Reports\\RegisterCustomer_Test.html");
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
			username = MIP_ReadPropertyFile.getPropertyValue("username");
			homepage = loginpage.login(username,
					MIP_ReadPropertyFile.getPropertyValue("password"));
			homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
		} else {
			homepage = new MIP_HomePage(MIP_Test_Configuration.driver);
			try {
				if (!homepage.waitForElementToVisible(
						By.linkText(MIP_Menu_Constants.REGISTER_CUSTOMER))
						.isDisplayed()) {
					homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
					homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
				}
			} catch (Exception e) {
				homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			}
		}
	}

	@Test(testName = "RegisterCustomer-GSM-Prepaid", dataProvider = "RegisterCustomer-GSM-Prepaid", dataProviderClass = MIP_RegCust_TestData.class)
	public void registration_GSM_Prepaid(String testcase, String nic,
			String cust_name, String pref_lang, String ref_num,
			String mobile_num, String product_reg, String hp_alt_contactnum,
			String life_contact_num, String life_cov_seg, String nominee_name,
			String nominee_contact, String nominee_relation,
			String pa_contact_num) throws Throwable {
		try {
			logger = report.startTest("RegisterCustomer-GSM-Prepaid--"
					+ testcase);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);

			regpage.waitForElementToVisible(By.id("nic"));
			Thread.sleep(1000);
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(MIP_CustomerManagementPage.GSM);
			regpage.clickOnSeachButton();
			regpage.enterCustomerInfo(cust_name, pref_lang, ref_num, mobile_num)
					.selectAvailableOffers(product_reg);
			if (product_reg.trim().contains(
					MIP_CustomerManagementPage.HOSPITALIZATION)) {
				regpage.enterHPAlternateContactNum(hp_alt_contactnum);

			}
			if (product_reg.trim().contains(
					MIP_CustomerManagementPage.LIFE_2017)) {
				regpage.enterLife2017AlternateContactNum(life_contact_num)
						.selectLifeCoverSegment_Life2017(life_cov_seg.trim())
						.enterNomineeInformation(nominee_name, nominee_contact,
								nominee_relation);
			}
			if (product_reg.trim().contains(MIP_CustomerManagementPage.PA_2017)) {
				regpage.enterPA2017AlternateContactNum(pa_contact_num)
						.enterNomineeInformation(nominee_name, nominee_contact,
								"");
			}
			regpage.clickOnSave();
			regpage.confirmCustManagementPopup("yes");

			Assert.assertTrue(regpage.getSuccessMessage().equalsIgnoreCase(
					MIP_RegisterCustomerPage.SUCCESS_MSG));
			Map<String, String> cust_details = MIP_RegisterCustomer_Queries
					.getCustomerDetails(nic, MIP_CustomerManagementPage.GSM);
			Assert.assertEquals(cust_name.trim(),
					cust_details.get("customer_name").trim());
			Assert.assertTrue((pref_lang.charAt(0) + "")
					.equalsIgnoreCase(cust_details.get("preferred_lang").trim()));
			Assert.assertEquals(username.trim(), cust_details.get("user")
					.trim());

			if (product_reg.trim().contains(
					MIP_CustomerManagementPage.HOSPITALIZATION)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.HOSPITALIZATION);
				if (!ref_num.equals(""))
					Assert.assertTrue(ref_num.trim().equals(
							offer_details.get("ref_no").trim()));
				if (!mobile_num.equals(""))
					Assert.assertTrue(mobile_num.trim().equals(
							offer_details.get("msisdn").trim()));
				if (!hp_alt_contactnum.equals(""))
					Assert.assertTrue(hp_alt_contactnum.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				Assert.assertNotNull(offer_details.get("premium_charges"));
				Assert.assertTrue(MIP_RegCust_TestData.DEFAULT_OFFER_LEVEL_PREPAID_GSM
						.equalsIgnoreCase(offer_details.get("offer_cover")
								.trim()));
				Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
						.equals(offer_details.get("is_confirmed").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));

			}
			if (product_reg.trim().contains(
					MIP_CustomerManagementPage.LIFE_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.LIFE_2017);
				if (!ref_num.equals(""))
					Assert.assertTrue(ref_num.trim().equals(
							offer_details.get("ref_no").trim()));
				if (!mobile_num.equals(""))
					Assert.assertTrue(mobile_num.trim().equals(
							offer_details.get("msisdn").trim()));
				if (!life_contact_num.equals("")) {
					log.info("Assert expected and Found:" + life_contact_num
							+ "and" + offer_details.get("alt_msisdn").trim());
					Assert.assertTrue(life_contact_num.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}
				log.info("Assert expected and Found:" + nominee_name + " and "
						+ cust_details.get("nominee_name").trim());
				Assert.assertTrue(nominee_name.trim().equals(
						cust_details.get("nominee_name").trim()));

				if (!nominee_contact.equals("")) {
					log.info("Assert expected and Found:" + nominee_contact
							+ "and" + cust_details.get("nominee_msisdn").trim());
					Assert.assertTrue(nominee_contact.trim().equals(
							cust_details.get("nominee_msisdn").trim()));

				}
				Assert.assertTrue(nominee_relation.trim().equals(
						cust_details.get("relationship").trim()));
				Assert.assertTrue(life_cov_seg.replaceAll("\\,", "").contains(
						offer_details.get("offer_cover").split("\\.")[0]));
				Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
						.equals(offer_details.get("is_confirmed").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
			}
			if (product_reg.trim().contains(MIP_CustomerManagementPage.PA_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.PA_2017);
				if (!ref_num.equals(""))
					Assert.assertTrue(ref_num.trim().equals(
							offer_details.get("ref_no").trim()));
				if (!mobile_num.equals(""))
					Assert.assertTrue(mobile_num.trim().equals(
							offer_details.get("msisdn").trim()));
				if (!pa_contact_num.equals(""))

					Assert.assertTrue(pa_contact_num.trim().equals(
							offer_details.get("alt_msisdn").trim()));

				Assert.assertTrue(nominee_name.trim().equals(
						cust_details.get("nominee_name").trim()));

				if (!nominee_contact.equals(""))
					Assert.assertTrue(nominee_contact.trim().equals(
							cust_details.get("nominee_msisdn").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
						.equals(offer_details.get("is_confirmed").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
			}
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("RegisterCustomer-GSM-Prepaid--" + testcase
					+ " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "UpdateExistingCustomer-GSM-Prepaid", dataProvider = "updateExistingCustomer-GSM-Prepaid", dataProviderClass = MIP_RegCust_TestData.class)
	public void update_Existing_customer_GSM_Prepaid(String testcase,
			String nic, String update_nic, String update_cust_name,
			String update_pref_lang, String update_nominee_name,
			String update_nominee_contact, String update_nominee_relation,
			String suc_msg) throws Throwable {
		try {
			logger = report.startTest("UpdateExistingCustomer-GSM-Prepaid---"
					+ testcase);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);

			regpage.waitForElementToVisible(By.id("nic"));
			Thread.sleep(1000);
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(MIP_CustomerManagementPage.GSM);
			regpage.clickOnSeachButton();
			Map<String, String> db_cust_details = MIP_RegisterCustomer_Queries
					.getCustomerDetails(nic, MIP_CustomerManagementPage.GSM);
			Map<String, String> dashboard_cust_details = regpage
					.getCustomerInfo();
			log.info("Assert condition:found and expected----"
					+ dashboard_cust_details.get("cust_name") + " "
					+ db_cust_details.get("customer_name").trim());
			Assert.assertEquals(dashboard_cust_details.get("cust_name"),
					db_cust_details.get("customer_name").trim());
			log.info("Assert condition:found and expected----"
					+ dashboard_cust_details.get("language").charAt(0) + ""
					+ " " + db_cust_details.get("preferred_lang").trim());
			Assert.assertTrue((dashboard_cust_details.get("language").charAt(0) + "")
					.equalsIgnoreCase(db_cust_details.get("preferred_lang")
							.trim()));
			log.info("Assert condition:found and expected----" + nic + " "
					+ dashboard_cust_details.get("nic").trim());
			Assert.assertEquals(nic, dashboard_cust_details.get("nic").trim());

			List<String> dashboard_offer = regpage.getRegisteredOffer();
			List<String> db_offer = MIP_RegisterCustomer_Queries
					.getRegisteredProduct(nic, MIP_CustomerManagementPage.GSM);
			log.info("Assert condition:found and expected----"
					+ dashboard_offer.size() + " " + db_offer.size());
			Assert.assertEquals(dashboard_offer.size(), db_offer.size());
			Collections.sort(dashboard_offer);
			Collections.sort(db_offer);
			log.info("Assert condition:found and expected----"
					+ dashboard_offer + " " + db_offer);
			Assert.assertEquals(dashboard_offer, db_offer);
			if (dashboard_offer
					.contains(MIP_CustomerManagementPage.HOSPITALIZATION)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						db_cust_details.get("cust_id"),
						MIP_CustomerManagementPage.HOSPITALIZATION);
				Map<String, String> hp_info = regpage.getHPProductInformation();

				log.info("Assert condition:found and expected----"
						+ hp_info.get("hp_altnum".trim()) + " "
						+ offer_details.get("alt_msisdn").trim());
				Assert.assertTrue(hp_info.get("hp_altnum".trim()).equals(
						offer_details.get("alt_msisdn").trim()));

				log.info("Assert condition:found and expected----"
						+ hp_info.get("hp_segment").trim()
						+ " "
						+ offer_details.get("offer_cover").trim().split("\\.")[0]);
				Assert.assertTrue(hp_info
						.get("hp_segment")
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").trim()
										.split("\\.")[0]));
			}
			if (dashboard_offer.contains(MIP_CustomerManagementPage.LIFE_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						db_cust_details.get("cust_id"),
						MIP_CustomerManagementPage.LIFE_2017);
				Map<String, String> life_info = regpage.getLifeInformation();
				log.info("Assert condition:found and expected----"
						+ life_info.get("life_altnum").trim() + " "
						+ offer_details.get("alt_msisdn").trim());

				Assert.assertTrue(life_info.get("life_altnum".trim()).equals(
						offer_details.get("alt_msisdn").trim()));

				log.info("Assert condition:found and expected----"
						+ life_info.get("life_segment").trim() + " "
						+ offer_details.get("offer_cover").split("\\.")[0]);
				Assert.assertTrue(life_info
						.get("life_segment")
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));

				log.info("Assert condition:found and expected----"
						+ life_info.get("nominee_name").trim() + " "
						+ db_cust_details.get("nominee_name").trim());

				Assert.assertTrue(life_info.get("nominee_name").trim()
						.equals(db_cust_details.get("nominee_name").trim()));

				log.info("Assert condition:found and expected----"
						+ life_info.get("nominee_msisdn").trim() + " "
						+ db_cust_details.get("nominee_msisdn").trim());

				Assert.assertTrue(life_info.get("nominee_msisdn").trim()
						.equals(db_cust_details.get("nominee_msisdn").trim()));

				log.info("Assert condition:found and expected----"
						+ life_info.get("life_relation").trim() + " "
						+ db_cust_details.get("relationship").trim());

				Assert.assertTrue(life_info.get("life_relation").trim()
						.equals(db_cust_details.get("relationship").trim()));

			}
			if (dashboard_offer.contains(MIP_CustomerManagementPage.PA_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						db_cust_details.get("cust_id"),
						MIP_CustomerManagementPage.PA_2017);
				Map<String, String> pa_info = regpage.getPAInformation();
				log.info("Assert condition:found and expected----"
						+ pa_info.get("pa_altnum").trim() + " "
						+ offer_details.get("alt_msisdn").trim());

				Assert.assertTrue(pa_info.get("pa_altnum".trim()).equals(
						offer_details.get("alt_msisdn").trim()));

				log.info("Assert condition:found and expected----"
						+ pa_info.get("pa_segment").trim() + " "
						+ offer_details.get("offer_cover").split("\\.")[0]);
				Assert.assertTrue(pa_info
						.get("pa_segment")
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));

				log.info("Assert condition:found and expected----"
						+ pa_info.get("nominee_name").trim() + " "
						+ db_cust_details.get("nominee_name").trim());

				Assert.assertTrue(pa_info.get("nominee_name").trim()
						.equals(db_cust_details.get("nominee_name").trim()));

				log.info("Assert condition:found and expected----"
						+ pa_info.get("nominee_msisdn").trim() + " "
						+ db_cust_details.get("nominee_msisdn").trim());

				Assert.assertTrue(pa_info.get("nominee_msisdn").trim()
						.equals(db_cust_details.get("nominee_msisdn").trim()));

			}
			regpage.editCustomerInfo(update_nic, update_cust_name,
					update_pref_lang);
			if (dashboard_offer.contains(MIP_CustomerManagementPage.LIFE_2017)) {
				regpage.editNomineeInformation(update_nominee_name,
						update_nominee_contact, update_nominee_relation);
			}
			if (dashboard_offer.contains(MIP_CustomerManagementPage.PA_2017)
					&& (!dashboard_offer
							.contains(MIP_CustomerManagementPage.LIFE_2017))) {
				regpage.editNomineeInformation(update_nominee_name,
						update_nominee_contact, "");
			}
			regpage.clickOnSave();
			regpage.confirmCustManagementPopup("yes");
			Assert.assertTrue(regpage.getSuccessMessage().equalsIgnoreCase(
					suc_msg.trim()));
			if (!nic.equals(update_nic)) {
				nic = update_nic;
			}
			regpage.enterNic(update_nic);
			regpage.selectSubscriberChannelType(MIP_CustomerManagementPage.GSM);
			regpage.clickOnSeachButton();
			db_cust_details = MIP_RegisterCustomer_Queries.getCustomerDetails(
					nic, MIP_CustomerManagementPage.GSM);
			dashboard_cust_details = regpage.getCustomerInfo();
			String cust_name = dashboard_cust_details.get("cust_name");
			log.info("Assert condition:found and expected----" + cust_name
					+ " " + db_cust_details.get("customer_name").trim());
			Assert.assertEquals(cust_name, db_cust_details.get("customer_name")
					.trim());
			log.info("Assert condition:found and expected----" + cust_name
					+ " " + update_cust_name);
			Assert.assertEquals(cust_name, update_cust_name);
			String lang = dashboard_cust_details.get("language").charAt(0) + "";
			log.info("Assert condition:found and expected----" + lang + " "
					+ db_cust_details.get("preferred_lang").trim());
			Assert.assertTrue(lang.equalsIgnoreCase(db_cust_details
					.get("preferred_lang").trim().charAt(0)
					+ ""));
			log.info("Assert condition:found and expected----" + lang + " "
					+ update_pref_lang.charAt(0) + "");
			Assert.assertTrue(lang.equalsIgnoreCase(update_pref_lang.charAt(0)
					+ ""));
			log.info("Assert condition:found and expected----" + nic + " "
					+ dashboard_cust_details.get("nic").trim());
			Assert.assertEquals(nic, dashboard_cust_details.get("nic").trim());

			dashboard_offer = regpage.getRegisteredOffer();
			db_offer = MIP_RegisterCustomer_Queries.getRegisteredProduct(nic,
					MIP_CustomerManagementPage.GSM);
			log.info("Assert condition:found and expected----"
					+ dashboard_offer.size() + " " + db_offer.size());
			Assert.assertEquals(dashboard_offer.size(), db_offer.size());
			Collections.sort(dashboard_offer);
			Collections.sort(db_offer);
			log.info("Assert condition:found and expected----"
					+ dashboard_offer + " " + db_offer);
			Assert.assertEquals(dashboard_offer, db_offer);

			if (dashboard_offer
					.contains(MIP_CustomerManagementPage.HOSPITALIZATION)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						db_cust_details.get("cust_id"),
						MIP_CustomerManagementPage.HOSPITALIZATION);
				Map<String, String> hp_info = regpage.getHPProductInformation();
				log.info("Assert condition:found and expected----"
						+ hp_info.get("hp_altnum".trim()) + " "
						+ offer_details.get("alt_msisdn").trim());
				Assert.assertTrue(hp_info.get("hp_altnum".trim()).equals(
						offer_details.get("alt_msisdn").trim()));
				log.info("Assert condition:found and expected----"
						+ hp_info.get("hp_segment").trim()
						+ " "
						+ offer_details.get("offer_cover").trim().split("\\.")[0]);
				Assert.assertTrue(hp_info
						.get("hp_segment")
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").trim()
										.split("\\.")[0]));
			}
			if (dashboard_offer.contains(MIP_CustomerManagementPage.LIFE_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						db_cust_details.get("cust_id"),
						MIP_CustomerManagementPage.LIFE_2017);
				Map<String, String> life_info = regpage.getLifeInformation();
				log.info("Assert condition:found and expected----"
						+ life_info.get("life_altnum").trim() + " "
						+ offer_details.get("alt_msisdn").trim());

				Assert.assertTrue(life_info.get("life_altnum".trim()).equals(
						offer_details.get("alt_msisdn").trim()));

				log.info("Assert condition:found and expected----"
						+ life_info.get("life_segment").trim() + " "
						+ offer_details.get("offer_cover").split("\\.")[0]);
				Assert.assertTrue(life_info
						.get("life_segment")
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));

				log.info("Assert condition:found and expected----"
						+ life_info.get("nominee_name").trim() + " "
						+ db_cust_details.get("nominee_name").trim());

				Assert.assertTrue(life_info.get("nominee_name").trim()
						.equals(db_cust_details.get("nominee_name").trim()));

				log.info("Assert condition:found and expected----"
						+ life_info.get("nominee_msisdn").trim() + " "
						+ db_cust_details.get("nominee_msisdn").trim());

				Assert.assertTrue(life_info.get("nominee_msisdn").trim()
						.equals(db_cust_details.get("nominee_msisdn").trim()));

				log.info("Assert condition:found and expected----"
						+ life_info.get("life_relation").trim() + " "
						+ db_cust_details.get("relationship").trim());

				Assert.assertTrue(life_info.get("life_relation").trim()
						.equals(db_cust_details.get("relationship").trim()));
			}
			if (dashboard_offer.contains(MIP_CustomerManagementPage.PA_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						db_cust_details.get("cust_id"),
						MIP_CustomerManagementPage.PA_2017);
				Map<String, String> pa_info = regpage.getPAInformation();
				log.info("Assert condition:found and expected----"
						+ pa_info.get("pa_altnum").trim() + " "
						+ offer_details.get("alt_msisdn").trim());

				Assert.assertTrue(pa_info.get("life_altnum".trim()).equals(
						offer_details.get("alt_msisdn").trim()));

				log.info("Assert condition:found and expected----"
						+ pa_info.get("pa_segment").trim() + " "
						+ offer_details.get("offer_cover").split("\\.")[0]);
				Assert.assertTrue(pa_info
						.get("pa_segment")
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));

				log.info("Assert condition:found and expected----"
						+ pa_info.get("nominee_name").trim() + " "
						+ db_cust_details.get("nominee_name").trim());

				Assert.assertTrue(pa_info.get("nominee_name").trim()
						.equals(db_cust_details.get("nominee_name").trim()));

				log.info("Assert condition:found and expected----"
						+ pa_info.get("nominee_msisdn").trim() + " "
						+ db_cust_details.get("nominee_msisdn").trim());

				Assert.assertTrue(pa_info.get("nominee_msisdn").trim()
						.equals(db_cust_details.get("nominee_msisdn").trim()));

			}
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("UpdateExistingCustomer-GSM-Prepaid--" + testcase
					+ " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}
//need to test
	@Test(testName = "register_different_product_existing_customer_GSM_Prepaid", dataProvider = "register_different_product_existing_customer_GSM_Prepaid", dataProviderClass = MIP_RegCust_TestData.class)
	public void register_different_product_existing_customer_GSM_Prepaid(
			String testcase, String nic, String product_to_reg,
			String hp_altmsisdn, String life_altmsisdn, String life_cover,
			String nominee_name, String nominee_contact, String relationship,
			String pa_altmsisdn, String update_nominee_info_or_not)
			throws Throwable {
		try {
			logger = report
					.startTest("register_different_product_existing_customer_GSM_Prepaid---"
							+ testcase);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);

			regpage.waitForElementToVisible(By.id("nic"));
			Thread.sleep(1000);
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(MIP_CustomerManagementPage.GSM);
			regpage.clickOnSeachButton();
			regpage.selectAvailableOffers(product_to_reg);
			if (product_to_reg.trim().equalsIgnoreCase(
					MIP_CustomerManagementPage.HOSPITALIZATION)) {
				regpage.enterHPAlternateContactNum(hp_altmsisdn.trim());
			}
			if (product_to_reg.trim().equalsIgnoreCase(
					MIP_CustomerManagementPage.LIFE_2017)) {
				regpage.enterLife2017AlternateContactNum(life_altmsisdn)
						.selectLifeCoverSegment_Life2017(life_cover.trim());
				if (update_nominee_info_or_not.equals("1")) {
					regpage.editNomineeInformation(nominee_name,
							nominee_contact, relationship.trim());
				} else {
					regpage.enterNomineeInformation(nominee_name,
							nominee_contact, relationship.trim());
				}

			}
			if (product_to_reg.trim().equalsIgnoreCase(
					MIP_CustomerManagementPage.PA_2017)) {
				regpage.enterPA2017AlternateContactNum(pa_altmsisdn);
				if (update_nominee_info_or_not.equals("1")) {
					regpage.editNomineeInformation(nominee_name,
							nominee_contact, relationship.trim());
				} else {
					regpage.enterNomineeInformation(nominee_name,
							nominee_contact, "");
				}

			}

			regpage.clickOnSave();
			regpage.confirmCustManagementPopup("yes");

			Assert.assertTrue(regpage.getSuccessMessage().equalsIgnoreCase(
					MIP_RegisterCustomerPage.SUCCESS_MSG)
					|| regpage.getSuccessMessage().equalsIgnoreCase(
							MIP_RegisterCustomerPage.UPDATE_MSG));
			Map<String, String> cust_details = MIP_RegisterCustomer_Queries
					.getCustomerDetails(nic, MIP_CustomerManagementPage.GSM);
			/*
			 * Assert.assertEquals(username.trim(),
			 * cust_details.get("modified_by") .trim());
			 */
			if (product_to_reg.trim().contains(
					MIP_CustomerManagementPage.HOSPITALIZATION)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.HOSPITALIZATION);
				if (!hp_altmsisdn.equals("")) {
					log.info("Assert expected and Found:" + hp_altmsisdn
							+ "and" + offer_details.get("alt_msisdn").trim());
					Assert.assertTrue(hp_altmsisdn.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}
				Assert.assertNotNull(offer_details.get("premium_charges"));
				Assert.assertTrue(MIP_RegCust_TestData.DEFAULT_OFFER_LEVEL_PREPAID_GSM
						.equalsIgnoreCase(offer_details.get("offer_cover")
								.trim()));
				Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
						.equals(offer_details.get("is_confirmed").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));

			}
			if (product_to_reg.trim().contains(
					MIP_CustomerManagementPage.LIFE_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.LIFE_2017);
				if (!life_altmsisdn.equals("")) {
					log.info("Assert expected and Found:" + life_altmsisdn
							+ "and" + offer_details.get("alt_msisdn").trim());
					Assert.assertTrue(life_altmsisdn.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}
				if (update_nominee_info_or_not.equals("1")) {
					log.info("Assert expected and Found:" + nominee_name
							+ " and " + cust_details.get("nominee_name").trim());
					Assert.assertTrue(nominee_name.trim().equals(
							cust_details.get("nominee_name").trim()));

					if (!nominee_contact.equals("")) {
						log.info("Assert expected and Found:" + nominee_contact
								+ "and"
								+ cust_details.get("nominee_msisdn").trim());
						Assert.assertTrue(nominee_contact.trim().equals(
								cust_details.get("nominee_msisdn").trim()));

					}
					log.info("Assert condition:found and expected----"
							+ relationship.trim() + " "
							+ cust_details.get("relationship").trim());
					Assert.assertTrue(relationship.trim().equals(
							cust_details.get("relationship").trim()));
				}
				log.info("Assert condition:found and expected----"
						+ life_cover.trim() + " "
						+ offer_details.get("offer_cover").trim());
				Assert.assertTrue(life_cover.replaceAll("\\,", "").contains(
						offer_details.get("offer_cover").split("\\.")[0]));
				Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
						.equals(offer_details.get("is_confirmed").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
			}
			if (product_to_reg.trim().contains(
					MIP_CustomerManagementPage.PA_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.PA_2017);

				if (!pa_altmsisdn.equals("")) {
					log.info("Assert condition:found and expected----"
							+ pa_altmsisdn + " "
							+ offer_details.get("alt_msisdn").trim());
					Assert.assertTrue(pa_altmsisdn.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}
				if (update_nominee_info_or_not.equals("1")) {
					log.info("Assert condition:found and expected----"
							+ nominee_name.trim() + " "
							+ cust_details.get("nominee_name").trim());
					Assert.assertTrue(nominee_name.trim().equals(
							cust_details.get("nominee_name").trim()));

					if (!nominee_contact.equals("")) {
						log.info("Assert condition:found and expected----"
								+ nominee_contact.trim() + " "
								+ cust_details.get("nominee_msisdn").trim());
						Assert.assertTrue(nominee_contact.trim().equals(
								cust_details.get("nominee_msisdn").trim()));
					}
				}
				Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
						.equals(offer_details.get("is_confirmed").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
			}
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("UpdateExistingCustomer-GSM-Prepaid--" + testcase
					+ " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "register_cust_GSM_Prepaid_negative_scenarios", dataProvider = "register_cust_GSM_Prepaid_negative_scenarios", dataProviderClass = MIP_RegCust_TestData.class)
	public void register_cust_GSM_Prepaid_negative_scenarios(String testcase,
			String validate_field, String nic, String channel,
			String cust_name, String language, String ref_num,
			String mobile_num, String err_msg) throws Throwable {
		try {
			logger = report
					.startTest("register_different_product_existing_customer_GSM_Prepaid---");
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);
			Thread.sleep(1000);
			regpage.waitForElementToVisible(By.id("nic"));
			if (validate_field.trim().equalsIgnoreCase("NIC_GSM")) {
				regpage.enterNic(nic);
				regpage.selectSubscriberChannelType(channel);
				regpage.clickOnSeachButton();
				Assert.assertTrue(regpage.getValidationMessage().trim()
						.contains(err_msg.trim()));
			} else if (validate_field.trim().equalsIgnoreCase("CUST_INFO")) {
				regpage.enterNic(nic);
				regpage.selectSubscriberChannelType(channel);
				regpage.clickOnSeachButton();
				regpage.clickOnSave();
				Assert.assertEquals(regpage.getValidationMessage().trim(),
						err_msg.trim());
			} else if (validate_field.trim().equalsIgnoreCase("PRO_INFO")) {
				regpage.enterNic(nic);
				regpage.selectSubscriberChannelType(channel);
				regpage.clickOnSeachButton();
				regpage.enterCustomerInfo(cust_name, language, ref_num,
						mobile_num);
				regpage.selectAvailableOffers(MIP_CustomerManagementPage.HOSPITALIZATION);
				regpage.selectAvailableOffers(MIP_CustomerManagementPage.LIFE_2017);
				regpage.selectAvailableOffers(MIP_CustomerManagementPage.PA_2017);
				regpage.clickOnSave();
				Assert.assertEquals(regpage.getValidationMessage().trim(),
						err_msg.trim());
			} else if (validate_field.trim().equalsIgnoreCase("REF_NUM")) {
				regpage.enterNic(nic);
				regpage.selectSubscriberChannelType(channel);
				regpage.clickOnSeachButton();
				regpage.enterCustomerInfo(cust_name, language, ref_num,
						mobile_num);
				regpage.selectAvailableOffers(MIP_CustomerManagementPage.HOSPITALIZATION);
				Assert.assertEquals(regpage.getValidationMessage().trim(),
						err_msg.trim());
			} else if (validate_field.trim().equalsIgnoreCase("MOBILE_NUM")) {
				regpage.enterNic(nic);
				regpage.selectSubscriberChannelType(channel);
				regpage.clickOnSeachButton();
				regpage.enterCustomerInfo(cust_name, language, ref_num,
						mobile_num);
				regpage.selectAvailableOffers(MIP_CustomerManagementPage.HOSPITALIZATION);
				Assert.assertEquals(regpage.getValidationMessage().trim(),
						err_msg.trim());
			} else if (validate_field.trim().equalsIgnoreCase("CUST_NAME")) {
				regpage.enterNic(nic);
				regpage.selectSubscriberChannelType(channel);
				regpage.clickOnSeachButton();
				regpage.enterCustomerInfo(cust_name, language, ref_num,
						mobile_num);
				regpage.selectAvailableOffers(MIP_CustomerManagementPage.HOSPITALIZATION);
				regpage.clickOnSave();
				Assert.assertEquals(regpage.getValidationMessage().trim(),
						err_msg.trim());
			} else if (validate_field.trim().equalsIgnoreCase("NOMINEE_INFO")) {
				regpage.enterNic(nic);
				regpage.selectSubscriberChannelType(channel);
				regpage.clickOnSeachButton();
				regpage.enterCustomerInfo(cust_name, language, ref_num,
						mobile_num);
				regpage.selectAvailableOffers(MIP_CustomerManagementPage.HOSPITALIZATION);
				regpage.selectAvailableOffers(MIP_CustomerManagementPage.LIFE_2017);
				regpage.selectAvailableOffers(MIP_CustomerManagementPage.PA_2017);
				regpage.enterNomineeInformation(ref_num + mobile_num,
						cust_name, "Parent")
						.enterHPAlternateContactNum(cust_name)
						.enterLife2017AlternateContactNum(cust_name)
						.enterPA2017AlternateContactNum(cust_name);
				regpage.clickOnSave();
				Assert.assertEquals(regpage.getValidationMessage().trim(),
						err_msg.trim());
			}
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			Thread.sleep(1000);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("UpdateExistingCustomer-GSM-Prepaid--" + " Test Failed");
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
