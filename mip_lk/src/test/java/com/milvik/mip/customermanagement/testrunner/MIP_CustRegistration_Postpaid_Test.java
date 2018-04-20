package com.milvik.mip.customermanagement.testrunner;

import java.util.HashMap;
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

public class MIP_CustRegistration_Postpaid_Test {
	ExtentReports report;
	ExtentTest logger;
	static Logger log;
	MIP_LoginPage loginpage;
	MIP_HomePage homepage;
	MIP_RegisterCustomerPage regpage = null;
	String username = "";
	String testcaseName = "";

	@BeforeTest
	@Parameters({ "flag", "browser" })
	public void test_setup(@Optional("firefox") String browser,
			@Optional("0") String flag) {
		log = MIP_Logging.logDetails("MIP_CustRegistration_Postpaid_Test");
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
			/*try {
				if (!homepage.waitForElementToVisible(
						By.linkText(MIP_Menu_Constants.REGISTER_CUSTOMER))
						.isDisplayed()) {*/
					homepage.clickOnMenu(MIP_Menu_Constants.CUSTOMER);
					homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			/*
			 * } } catch (Exception e) {
			 * homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER); }
			 */
		}
	}

	@Test(enabled = false, testName = "RegisterCustomer-Postpaid", dataProvider = "RegisterCustomer-Postpaid", dataProviderClass = MIP_RegCust_TestData.class)
	public void registration_Postpaid(String testcase, String nic,
			String channel, String cust_name, String pref_lang,
			String account_id, String ref_num, String mobile_num,
			String product_reg, String hp_alt_contactnum, String hp_ref_num,
			String hp_cover_seg, String spouse_name, String child1,
			String child2, String child3, String life_contact_num,
			String life_ref_num, String life_cov_seg, String nominee_name,
			String nominee_contact, String nominee_relation,
			String pa_contact_num, String pa_ref_num, String pa_cover_seg,
			String hmp_ref_num, String hmp_alt_msisdn, String hmp_cov_seg,
			String address1, String address2, String postal_code)
			throws Throwable {
		testcaseName = testcase;
		try {
			logger = report.startTest("RegisterCustomer-Postpaid--" + testcase);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);

			regpage.waitForElementToVisible(By.id("nic"));
			regpage.waitForElementToVisible(By
					.xpath("//input[@id='subscriberChannelId']"));
			Thread.sleep(1000);
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(channel.trim());
			regpage.clickOnSeachButton();

			if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
				regpage.enterCustomerInfoForNonGSMChannel(cust_name.trim(),
						pref_lang).selectAvailableOffers(product_reg.trim());
				regpage.enterAccoundId(account_id);
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.HOSPITALIZATION)) {
					regpage.enterHPInfoForPostpaid(hp_alt_contactnum,
							hp_ref_num, hp_cover_seg, spouse_name, child1,
							child2, child3);

				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.LIFE_2017)) {
					regpage.enterLifeInfoForPostpaid(life_ref_num,
							life_contact_num, life_cov_seg.trim())
							.enterNomineeInformation(nominee_name,
									nominee_contact, nominee_relation.trim());
				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.PA_2017)) {
					regpage.enterPAInfoForPostpaid(pa_ref_num, pa_contact_num,
							pa_cover_seg.trim()).enterNomineeInformation(
							nominee_name, nominee_contact, "");
				}
				if (product_reg.trim().contains(MIP_CustomerManagementPage.HMP)) {
					regpage.enterHMPInfoForPostpaid(hmp_ref_num,
							hmp_alt_msisdn, hmp_cov_seg, address1, address2,
							postal_code);
				}
			} else {
				regpage.enterCustomerInfo(cust_name.trim(), pref_lang, ref_num,
						mobile_num).selectAvailableOffers(product_reg);
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.HOSPITALIZATION)) {
					regpage.enterHPInfoForPostpaid(hp_alt_contactnum, "",
							hp_cover_seg, spouse_name, child1, child2, child3);

				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.LIFE_2017)) {
					regpage.enterLife2017AlternateContactNum(life_contact_num)
							.selectLifeCoverSegment_Life2017(
									life_cov_seg.trim())
							.enterNomineeInformation(nominee_name,
									nominee_contact, nominee_relation.trim());
				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.PA_2017)) {
					regpage.enterPA2017AlternateContactNum(pa_contact_num)
							.enterNomineeInformation(nominee_name,
									nominee_contact, "");
				}
			}

			regpage.clickOnSave();
			regpage.confirmCustManagementPopup("yes");

			Assert.assertTrue(regpage.getSuccessMessage().equalsIgnoreCase(
					MIP_RegisterCustomerPage.SUCCESS_MSG));

			Map<String, String> cust_details = MIP_RegisterCustomer_Queries
					.getCustomerDetails(nic, channel.trim());
			log.info("Assert expected and Found:" + cust_name.trim() + " and "
					+ cust_details.get("customer_name").trim());
			Assert.assertEquals(cust_name.trim(),
					cust_details.get("customer_name").trim());
			log.info("Assert expected and Found:" + pref_lang.charAt(0) + ""
					+ " and " + cust_details.get("preferred_lang").trim());
			Assert.assertTrue((pref_lang.charAt(0) + "")
					.equalsIgnoreCase(cust_details.get("preferred_lang").trim()));
			log.info("Assert expected and Found:" + username.trim() + ""
					+ " and " + cust_details.get("user"));
			Assert.assertEquals(username.trim(), cust_details.get("user")
					.trim());
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(channel);
			regpage.clickOnSeachButton();

			Assert.assertTrue(regpage
					.checkProductselectiondisabledAfterRegistration(MIP_RegisterCustomer_Queries
							.getRegisteredProduct(nic, channel)));
			if (product_reg.trim().contains(
					MIP_CustomerManagementPage.HOSPITALIZATION)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.HOSPITALIZATION);
				if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
					if (!hp_ref_num.equals(""))
						log.info("Assert expected and Found:" + ref_num.trim()
								+ "" + " and " + cust_details.get("ref_no"));
					Assert.assertTrue(hp_ref_num.trim().equals(
							offer_details.get("ref_no").trim()));
					Assert.assertTrue(MIP_RegCust_TestData.NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));
					Assert.assertTrue(regpage.hpProductInfoReadOnlyCheck(
							hp_ref_num, hp_alt_contactnum, hp_cover_seg,
							spouse_name, child1, child2, child3));
				} else {
					if (!ref_num.equals(""))
						log.info("Assert expected and Found:" + ref_num.trim()
								+ "" + " and " + offer_details.get("ref_no"));
					Assert.assertTrue(ref_num.trim().equals(
							offer_details.get("ref_no").trim()));
					if (!mobile_num.equals(""))
						log.info("Assert expected and Found:"
								+ mobile_num.trim() + "" + " and "
								+ offer_details.get("msisdn"));
					Assert.assertTrue(mobile_num.trim().equals(
							offer_details.get("msisdn").trim()));
					Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));
					Assert.assertTrue(regpage.hpProductInfoReadOnlyCheck("",
							hp_alt_contactnum, hp_cover_seg, spouse_name,
							child1, child2, child3));
				}

				if (!hp_alt_contactnum.equals("")) {
					log.info("Assert expected and Found:"
							+ hp_alt_contactnum.trim() + "" + " and "
							+ offer_details.get("alt_msisdn"));
					Assert.assertTrue(hp_alt_contactnum.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}
				Assert.assertNotNull(offer_details.get("premium_charges"));
				log.info("Assert expected and Found:" + hp_cover_seg.trim()
						+ "" + " and " + offer_details.get("offer_cover"));
				Assert.assertTrue(hp_cover_seg.replaceAll("\\,", "").contains(
						offer_details.get("offer_cover").split("\\.")[0]));

				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
				log.info("Assert expected and Found:" + spouse_name.trim() + ""
						+ " and " + offer_details.get("spouse_name"));
				Assert.assertTrue(spouse_name.trim().equalsIgnoreCase(
						offer_details.get("spouse_name").trim()));
				log.info("Assert expected and Found:" + child1.trim() + ""
						+ " and " + offer_details.get("child1_name"));
				Assert.assertTrue(child1.trim().equalsIgnoreCase(
						offer_details.get("child1_name").trim()));
				log.info("Assert expected and Found:" + child2.trim() + ""
						+ " and " + offer_details.get("child2_name"));
				Assert.assertTrue(child2.trim().equalsIgnoreCase(
						offer_details.get("child2_name").trim()));
				log.info("Assert expected and Found:" + child3.trim() + ""
						+ " and " + offer_details.get("child3_name"));
				Assert.assertTrue(child3.trim().equalsIgnoreCase(
						offer_details.get("child3_name").trim()));

			}

			if (product_reg.trim().contains(MIP_CustomerManagementPage.PA_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.PA_2017);
				if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
					if (!pa_ref_num.trim().equals("")) {
						log.info("Assert expected and Found:" + pa_ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(pa_ref_num.trim().equalsIgnoreCase(
								offer_details.get("ref_no").trim()));
					}

					Assert.assertTrue(MIP_RegCust_TestData.NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));
					Assert.assertTrue(regpage.paInfoReadOnlyCheck(pa_ref_num,
							pa_contact_num, pa_cover_seg, nominee_name,
							nominee_contact));
				} else {
					if (!ref_num.equals("")) {
						log.info("Assert expected and Found:" + ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(ref_num.trim().equals(
								offer_details.get("ref_no").trim()));
					}
					if (!mobile_num.equals("")) {
						log.info("Assert expected and Found:" + mobile_num
								+ " and " + offer_details.get("msisdn").trim());
						Assert.assertTrue(mobile_num.trim().equals(
								offer_details.get("msisdn").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));
					Assert.assertTrue(regpage.paInfoReadOnlyCheck("",
							pa_contact_num, pa_cover_seg, nominee_name,
							nominee_contact));
				}

				if (!pa_contact_num.equals("")) {
					log.info("Assert expected and Found:" + pa_contact_num
							+ " and " + offer_details.get("alt_msisdn").trim());
					Assert.assertTrue(pa_contact_num.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}
				log.info("Assert expected and Found:" + nominee_name + " and "
						+ cust_details.get("nominee_name").trim());
				Assert.assertTrue(nominee_name.trim().equals(
						cust_details.get("nominee_name").trim()));

				if (!nominee_contact.equals("")) {
					log.info("Assert expected and Found:" + nominee_contact
							+ " and "
							+ cust_details.get("nominee_msisdn").trim());
					Assert.assertTrue(nominee_contact.trim().equals(
							cust_details.get("nominee_msisdn").trim()));
				}
				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
				log.info("Assert expected and Found:" + pa_cover_seg + " and "
						+ offer_details.get("offer_cover").trim());
				Assert.assertTrue(pa_cover_seg
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));

			}
			if (product_reg.trim().contains(
					MIP_CustomerManagementPage.LIFE_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.LIFE_2017);
				if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
					if (!life_ref_num.equals("")) {
						log.info("Assert expected and Found:" + life_ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(life_ref_num.trim().equals(
								offer_details.get("ref_no").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));
					Assert.assertTrue(regpage.lifeInfoReadOnlyCheck(
							life_ref_num, life_contact_num.trim(),
							life_cov_seg.trim(), nominee_name, nominee_contact,
							nominee_relation.trim()));
				} else {
					if (!ref_num.equals("")) {
						log.info("Assert expected and Found:" + ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(ref_num.trim().equals(
								offer_details.get("ref_no").trim()));
					}
					if (!mobile_num.equals("")) {
						log.info("Assert expected and Found:" + mobile_num
								+ " and " + offer_details.get("msisdn").trim());
						Assert.assertTrue(mobile_num.trim().equals(
								offer_details.get("msisdn").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));
					Assert.assertTrue(regpage.lifeInfoReadOnlyCheck("",
							life_contact_num.trim(), life_cov_seg.trim(),
							nominee_name, nominee_contact,
							nominee_relation.trim()));
				}

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
				log.info("Assert expected and Found:" + nominee_relation
						+ " and " + cust_details.get("relationship").trim());
				Assert.assertTrue(nominee_relation.trim().equals(
						cust_details.get("relationship").trim()));
				log.info("Assert expected and Found:" + life_cov_seg + " and "
						+ offer_details.get("offer_cover").trim());
				Assert.assertTrue(life_cov_seg
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));

				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
			}
			if (product_reg.trim().contains(MIP_CustomerManagementPage.HMP)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.HMP);
				if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
					if (!hmp_ref_num.trim().equals("")) {
						log.info("Assert expected and Found:" + hmp_ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(hmp_ref_num.trim().equalsIgnoreCase(
								offer_details.get("ref_no").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));
					Assert.assertTrue(regpage.hmpProductInfoReadOnlyCheck(
							hmp_ref_num, hmp_alt_msisdn, hmp_cov_seg, address1,
							address2, postal_code));
				} else {
					if (!ref_num.equals("")) {
						log.info("Assert expected and Found:" + ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(ref_num.trim().equals(
								offer_details.get("ref_no").trim()));
					}
					if (!mobile_num.equals("")) {
						log.info("Assert expected and Found:" + mobile_num
								+ " and " + offer_details.get("msisdn").trim());
						Assert.assertTrue(mobile_num.trim().equals(
								offer_details.get("msisdn").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));
					Assert.assertTrue(regpage.hmpProductInfoReadOnlyCheck("",
							hmp_alt_msisdn, hmp_cov_seg, address1, address2,
							postal_code));

				}

				if (!hmp_alt_msisdn.equals("")) {
					log.info("Assert expected and Found:" + hmp_alt_msisdn
							+ " and " + offer_details.get("alt_msisdn").trim());
					Assert.assertTrue(hmp_alt_msisdn.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}
				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
				log.info("Assert expected and Found:" + hmp_cov_seg + " and "
						+ offer_details.get("offer_cover").trim());
				Assert.assertTrue(hmp_cov_seg
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));
				log.info("Assert expected and Found:" + address1 + " and "
						+ offer_details.get("address_line_1").trim());
				Assert.assertTrue(address1.trim().equals(
						offer_details.get("address_line_1").trim()));
				log.info("Assert expected and Found:" + address2 + " and "
						+ offer_details.get("address_line_2").trim());
				Assert.assertTrue(address2.trim().equals(
						offer_details.get("address_line_2").trim()));
				log.info("Assert expected and Found:" + postal_code + " and "
						+ offer_details.get("postal_code").trim());
				Assert.assertTrue(postal_code.trim().equals(
						offer_details.get("postal_code").trim()));
			}
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("RegisterCustomer-GSM-Prepaid--" + testcase
					+ " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	/**
	 * register a customer with HMP and Life with any channel and update the
	 * address, postal code,noiminee information and test the updated
	 * information is reflecting or not.
	 */
	@Test(enabled = false, testName = "update_hmp_life_pa_info_Test-Postpaid", dataProvider = "update_hmp_life_pa_info-postpaid", dataProviderClass = MIP_RegCust_TestData.class)
	public void update_hmp_life_pa_info_Test(String testcase, String nic,
			String channel, String cust_name, String pref_lang,
			String account_id, String ref_num, String mobile_num,
			String product_reg, String life_contact_num, String life_ref_num,
			String life_cov_seg, String nominee_name, String nominee_contact,
			String nominee_relation, String pa_contact_num, String pa_ref_num,
			String pa_cover_seg, String hmp_ref_num, String hmp_alt_msisdn,
			String hmp_cov_seg, String address1, String address2,
			String postal_code, String update_cust_name,
			String update_language, String update_address1,
			String update_address2, String update_postal_code,
			String update_nominee_name, String update_nominee_msisdn,
			String update_nominee_relation) throws Throwable {
		testcaseName = testcase;
		try {
			logger = report.startTest("update_hmp_life_pa_info_Test-Postpaid--"
					+ testcase);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);

			regpage.waitForElementToVisible(By.id("nic"));
			Thread.sleep(1000);
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(channel.trim());
			regpage.clickOnSeachButton();

			if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
				regpage.enterCustomerInfoForNonGSMChannel(cust_name, pref_lang)
						.selectAvailableOffers(product_reg.trim());
				regpage.enterAccoundId(account_id);

				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.LIFE_2017)) {
					regpage.enterLifeInfoForPostpaid(life_ref_num,
							life_contact_num, life_cov_seg.trim())
							.enterNomineeInformation(nominee_name,
									nominee_contact, nominee_relation.trim());
				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.PA_2017)) {
					regpage.enterPAInfoForPostpaid(pa_ref_num, pa_contact_num,
							pa_cover_seg.trim()).enterNomineeInformation(
							nominee_name, nominee_contact, "");
				}
				if (product_reg.trim().contains(MIP_CustomerManagementPage.HMP)) {
					regpage.enterHMPInfoForPostpaid(hmp_ref_num,
							hmp_alt_msisdn, hmp_cov_seg, address1, address2,
							postal_code);
				}
			} else {
				regpage.enterCustomerInfo(cust_name.trim(), pref_lang, ref_num,
						mobile_num).selectAvailableOffers(product_reg);
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.LIFE_2017)) {
					regpage.enterLife2017AlternateContactNum(life_contact_num)
							.selectLifeCoverSegment_Life2017(
									life_cov_seg.trim())
							.enterNomineeInformation(nominee_name,
									nominee_contact, nominee_relation.trim());
				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.PA_2017)) {
					regpage.enterPAInfoForPostpaid("", pa_contact_num,
							pa_cover_seg.trim()).enterNomineeInformation(
							nominee_name, nominee_contact, "");
				}
			}

			regpage.clickOnSave();
			regpage.confirmCustManagementPopup("yes");

			Assert.assertTrue(regpage.getSuccessMessage().equalsIgnoreCase(
					MIP_RegisterCustomerPage.SUCCESS_MSG));

			Map<String, String> cust_details = MIP_RegisterCustomer_Queries
					.getCustomerDetails(nic, channel.trim());
			log.info("Assert expected and Found:"
					+ cust_name.trim().replaceAll("\\s", "")
					+ " and "
					+ cust_details.get("customer_name").trim()
							.replaceAll("\\s", ""));
			Assert.assertEquals(
					cust_name.trim().replaceAll("\\s", ""),
					cust_details.get("customer_name").trim()
							.replaceAll("\\s", ""));
			log.info("Assert expected and Found:" + pref_lang.charAt(0) + ""
					+ " and " + cust_details.get("preferred_lang").trim());
			Assert.assertTrue((pref_lang.charAt(0) + "")
					.equalsIgnoreCase(cust_details.get("preferred_lang").trim()));
			log.info("Assert expected and Found:" + username.trim() + ""
					+ " and " + cust_details.get("user"));
			Assert.assertEquals(username.trim(), cust_details.get("user")
					.trim());
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(channel);
			regpage.clickOnSeachButton();

			Assert.assertTrue(regpage
					.checkProductselectiondisabledAfterRegistration(MIP_RegisterCustomer_Queries
							.getRegisteredProduct(nic, channel)));
			if (product_reg.trim().contains(MIP_CustomerManagementPage.PA_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.PA_2017);
				if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
					if (!pa_ref_num.trim().equals("")) {
						log.info("Assert expected and Found:" + pa_ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(pa_ref_num.trim().equalsIgnoreCase(
								offer_details.get("ref_no").trim()));
					}

					Assert.assertTrue(MIP_RegCust_TestData.NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));
				} else {
					if (!ref_num.equals("")) {
						log.info("Assert expected and Found:" + ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(ref_num.trim().equals(
								offer_details.get("ref_no").trim()));
					}
					if (!mobile_num.equals("")) {
						log.info("Assert expected and Found:" + mobile_num
								+ " and " + offer_details.get("msisdn").trim());
						Assert.assertTrue(mobile_num.trim().equals(
								offer_details.get("msisdn").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));
				}

				if (!pa_contact_num.equals("")) {
					log.info("Assert expected and Found:" + pa_contact_num
							+ " and " + offer_details.get("alt_msisdn").trim());
					Assert.assertTrue(pa_contact_num.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}
				log.info("Assert expected and Found:" + nominee_name + " and "
						+ cust_details.get("nominee_name").trim());
				Assert.assertTrue(nominee_name.trim().equals(
						cust_details.get("nominee_name").trim()));

				if (!nominee_contact.equals("")) {
					log.info("Assert expected and Found:" + nominee_contact
							+ " and "
							+ cust_details.get("nominee_msisdn").trim());
					Assert.assertTrue(nominee_contact.trim().equals(
							cust_details.get("nominee_msisdn").trim()));
				}
				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
				log.info("Assert expected and Found:" + pa_cover_seg + " and "
						+ offer_details.get("offer_cover").trim());
				Assert.assertTrue(pa_cover_seg
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));

			}
			if (product_reg.trim().contains(
					MIP_CustomerManagementPage.LIFE_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.LIFE_2017);
				if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
					if (!life_ref_num.equals("")) {
						log.info("Assert expected and Found:" + life_ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(life_ref_num.trim().equals(
								offer_details.get("ref_no").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));

				} else {
					if (!ref_num.equals("")) {
						log.info("Assert expected and Found:" + ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(ref_num.trim().equals(
								offer_details.get("ref_no").trim()));
					}
					if (!mobile_num.equals("")) {
						log.info("Assert expected and Found:" + mobile_num
								+ " and " + offer_details.get("msisdn").trim());
						Assert.assertTrue(mobile_num.trim().equals(
								offer_details.get("msisdn").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));

				}

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
				log.info("Assert expected and Found:" + nominee_relation
						+ " and " + cust_details.get("relationship").trim());
				Assert.assertTrue(nominee_relation.trim().equals(
						cust_details.get("relationship").trim()));
				log.info("Assert expected and Found:" + life_cov_seg + " and "
						+ offer_details.get("offer_cover").trim());
				Assert.assertTrue(life_cov_seg
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));

				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
			}
			if (product_reg.trim().contains(MIP_CustomerManagementPage.HMP)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.HMP);
				if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
					if (!hmp_ref_num.trim().equals("")) {
						log.info("Assert expected and Found:" + hmp_ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(hmp_ref_num.trim().equalsIgnoreCase(
								offer_details.get("ref_no").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));
					Assert.assertTrue(regpage.hmpProductInfoReadOnlyCheck(
							hmp_ref_num, hmp_alt_msisdn, hmp_cov_seg, address1,
							address2, postal_code));
				} else {
					if (!ref_num.equals("")) {
						log.info("Assert expected and Found:" + ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(ref_num.trim().equals(
								offer_details.get("ref_no").trim()));
					}
					if (!mobile_num.equals("")) {
						log.info("Assert expected and Found:" + mobile_num
								+ " and " + offer_details.get("msisdn").trim());
						Assert.assertTrue(mobile_num.trim().equals(
								offer_details.get("msisdn").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));

				}

				if (!hmp_alt_msisdn.equals("")) {
					log.info("Assert expected and Found:" + hmp_alt_msisdn
							+ " and " + offer_details.get("alt_msisdn").trim());
					Assert.assertTrue(hmp_alt_msisdn.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}
				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
				log.info("Assert expected and Found:" + hmp_cov_seg + " and "
						+ offer_details.get("offer_cover").trim());
				Assert.assertTrue(hmp_cov_seg
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));
				log.info("Assert expected and Found:" + address1 + " and "
						+ offer_details.get("address_line_1").trim());
				Assert.assertTrue(address1.trim().equals(
						offer_details.get("address_line_1").trim()));
				log.info("Assert expected and Found:" + address2 + " and "
						+ offer_details.get("address_line_2").trim());
				Assert.assertTrue(address2.trim().equals(
						offer_details.get("address_line_2").trim()));
				log.info("Assert expected and Found:" + postal_code + " and "
						+ offer_details.get("postal_code").trim());
				Assert.assertTrue(postal_code.trim().equals(
						offer_details.get("postal_code").trim()));
			}

			regpage.editCustomerInfo("", update_cust_name, update_language);

			if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {

				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.LIFE_2017)) {
					regpage.editNomineeInformation(update_nominee_name,
							update_nominee_msisdn, update_nominee_relation);
				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.PA_2017)) {
					regpage.editNomineeInformation(update_nominee_name,
							update_nominee_msisdn, update_nominee_relation);
				}
				if (product_reg.trim().contains(MIP_CustomerManagementPage.HMP)) {
					regpage.editHMPInfoForPostpaid(update_address1,
							update_address2, update_postal_code);
				}
			} else {
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.LIFE_2017)) {
					regpage.editNomineeInformation(update_nominee_name,
							update_nominee_msisdn, update_nominee_relation);
				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.PA_2017)) {
					regpage.editNomineeInformation(update_nominee_name,
							update_nominee_msisdn, update_nominee_relation);
				}
			}
			regpage.clickOnSave();
			regpage.confirmCustManagementPopup("yes");

			Assert.assertTrue(regpage.getSuccessMessage().equalsIgnoreCase(
					MIP_RegisterCustomerPage.SUCCESS_MSG)
					|| regpage.getSuccessMessage().equalsIgnoreCase(
							MIP_RegisterCustomerPage.UPDATE_MSG));

			cust_details = MIP_RegisterCustomer_Queries.getCustomerDetails(nic,
					channel.trim());
			if (!update_cust_name.equals("")) {
				log.info("Assert expected and Found:" + update_cust_name.trim()
						+ " and " + cust_details.get("customer_name").trim());
				Assert.assertEquals(update_cust_name.trim(),
						cust_details.get("customer_name").trim());
			}
			if (!update_language.equals("")) {
				log.info("Assert expected and Found:"
						+ update_language.charAt(0) + "" + " and "
						+ cust_details.get("preferred_lang").trim());
				Assert.assertTrue((update_language.charAt(0) + "")
						.equalsIgnoreCase(cust_details.get("preferred_lang")
								.trim()));
			}
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(channel);
			regpage.clickOnSeachButton();

			Assert.assertTrue(regpage
					.checkProductselectiondisabledAfterRegistration(MIP_RegisterCustomer_Queries
							.getRegisteredProduct(nic, channel)));

			if (product_reg.trim().contains(MIP_CustomerManagementPage.PA_2017)) {
				if (!update_nominee_name.equals("")) {
					log.info("Assert expected and Found:" + update_nominee_name
							+ " and " + cust_details.get("nominee_name").trim());
					Assert.assertTrue(update_nominee_name.trim().equals(
							cust_details.get("nominee_name").trim()));
				}
				if (!update_nominee_msisdn.equals("")) {
					log.info("Assert expected and Found:"
							+ update_nominee_msisdn + " and "
							+ cust_details.get("nominee_msisdn").trim());
					Assert.assertTrue(update_nominee_msisdn.trim().equals(
							cust_details.get("nominee_msisdn").trim()));
				}

			}
			if (product_reg.trim().contains(
					MIP_CustomerManagementPage.LIFE_2017)) {

				if (!update_nominee_name.equals("")) {
					log.info("Assert expected and Found:" + update_nominee_name
							+ " and " + cust_details.get("nominee_name").trim());
					Assert.assertTrue(update_nominee_name.trim().equals(
							cust_details.get("nominee_name").trim()));
				}
				if (!update_nominee_msisdn.equals("")) {
					log.info("Assert expected and Found:"
							+ update_nominee_msisdn + "and"
							+ cust_details.get("nominee_msisdn").trim());
					Assert.assertTrue(update_nominee_msisdn.trim().equals(
							cust_details.get("nominee_msisdn").trim()));

				}
				if (!update_nominee_relation.equals("")) {
					log.info("Assert expected and Found:"
							+ update_nominee_relation + " and "
							+ cust_details.get("relationship").trim());
					Assert.assertTrue(update_nominee_relation.trim().equals(
							cust_details.get("relationship").trim()));
				}

			}
			if (product_reg.trim().contains(MIP_CustomerManagementPage.HMP)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.HMP);

				if (!update_address1.equals("")) {
					log.info("Assert expected and Found:" + update_address1
							+ " and "
							+ offer_details.get("address_line_1").trim());
					Assert.assertTrue(update_address1.trim().equals(
							offer_details.get("address_line_1").trim()));
				}
				if (!update_address2.equals("")) {
					log.info("Assert expected and Found:" + update_address2
							+ " and "
							+ offer_details.get("address_line_2").trim());
					Assert.assertTrue(update_address2.trim().equals(
							offer_details.get("address_line_2").trim()));
				}
				if (!update_postal_code.equals("")) {
					log.info("Assert expected and Found:" + update_postal_code
							+ " and " + offer_details.get("postal_code").trim());
					Assert.assertTrue(update_postal_code.trim().equals(
							offer_details.get("postal_code").trim()));
				}
			}
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("RegisterCustomer-GSM-Prepaid--" + testcase
					+ " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(enabled = false, testName = "Register_DifferentProdct_ForExisting_Customer-Postpaid", dataProvider = "RegisterDifferent_ExistingCustomer-Postpaid", dataProviderClass = MIP_RegCust_TestData.class)
	public void differentProduct_registration_ExistingCustomer_Postpaid(
			String testcase, String nic, String channel, String product_reg,
			String hp_alt_contactnum, String hp_ref_num, String hp_cover_seg,
			String spouse_name, String child1, String child2, String child3,
			String life_contact_num, String life_ref_num, String life_cov_seg,
			String nominee_name, String nominee_contact,
			String nominee_relation, String pa_contact_num, String pa_ref_num,
			String pa_cover_seg, String hmp_ref_num, String hmp_alt_msisdn,
			String hmp_cov_seg, String address1, String address2,
			String postal_code) throws Throwable {
		testcaseName = testcase;
		try {
			logger = report
					.startTest("Register_DifferentProdct_ForExisting_Customer--"
							+ testcase);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);

			regpage.waitForElementToVisible(By.id("nic"));
			Thread.sleep(1000);
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(channel.trim());
			regpage.clickOnSeachButton();

			if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
				regpage.selectAvailableOffers(product_reg.trim());
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.HOSPITALIZATION)) {
					regpage.enterHPInfoForPostpaid(hp_alt_contactnum,
							hp_ref_num, hp_cover_seg, spouse_name, child1,
							child2, child3);

				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.LIFE_2017)) {

					regpage.enterLifeInfoForPostpaid(life_ref_num,
							life_contact_num, life_cov_seg.trim())
							.editNomineeInformation(nominee_name,
									nominee_contact, nominee_relation.trim());

				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.PA_2017)) {
					regpage.enterPAInfoForPostpaid(pa_ref_num, pa_contact_num,
							pa_cover_seg.trim()).editNomineeInformation(
							nominee_name, nominee_contact, "");
				}
				if (product_reg.trim().contains(MIP_CustomerManagementPage.HMP)) {

					regpage.enterHMPInfoForPostpaid(hmp_ref_num,
							hmp_alt_msisdn, hmp_cov_seg, address1, address2,
							postal_code);
				}
			} else {
				regpage.selectAvailableOffers(product_reg);
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.HOSPITALIZATION)) {
					regpage.enterHPInfoForPostpaid(hp_alt_contactnum, "",
							hp_cover_seg, spouse_name, child1, child2, child3);

				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.LIFE_2017)) {
					regpage.enterLife2017AlternateContactNum(life_contact_num)
							.selectLifeCoverSegment_Life2017(
									life_cov_seg.trim());

					regpage.enterNomineeInformation(nominee_name,
							nominee_contact, nominee_relation.trim());

				}
				if (product_reg.trim().contains(
						MIP_CustomerManagementPage.PA_2017)) {
					regpage.enterPAInfoForPostpaid("", pa_contact_num,
							pa_cover_seg.trim());
					regpage.enterNomineeInformation(nominee_name,
							nominee_contact, nominee_relation.trim());

				}
			}

			regpage.clickOnSave();
			regpage.confirmCustManagementPopup("yes");

			Assert.assertTrue(regpage.getSuccessMessage().equalsIgnoreCase(
					MIP_RegisterCustomerPage.SUCCESS_MSG)
					|| regpage.getSuccessMessage().equalsIgnoreCase(
							MIP_RegisterCustomerPage.UPDATE_MSG));

			Map<String, String> cust_details = MIP_RegisterCustomer_Queries
					.getCustomerDetails(nic, channel.trim());
			Assert.assertEquals(username.trim(), cust_details
					.get("modified_by").trim());
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(channel);
			regpage.clickOnSeachButton();

			Assert.assertTrue(regpage
					.checkProductselectiondisabledAfterRegistration(MIP_RegisterCustomer_Queries
							.getRegisteredProduct(nic, channel)));
			if (product_reg.trim().contains(
					MIP_CustomerManagementPage.HOSPITALIZATION)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.HOSPITALIZATION);
				if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
					if (!hp_ref_num.equals(""))
						Assert.assertTrue(hp_ref_num.trim().equals(
								offer_details.get("ref_no").trim()));
					Assert.assertTrue(MIP_RegCust_TestData.NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));

				} else {

					Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));

				}

				if (!hp_alt_contactnum.equals("")) {
					log.info("Assert expected and Found:"
							+ hp_alt_contactnum.trim() + "" + " and "
							+ offer_details.get("alt_msisdn"));
					Assert.assertTrue(hp_alt_contactnum.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}
				Assert.assertNotNull(offer_details.get("premium_charges"));
				log.info("Assert expected and Found:" + hp_cover_seg.trim()
						+ "" + " and " + offer_details.get("offer_cover"));
				Assert.assertTrue(hp_cover_seg.replaceAll("\\,", "").contains(
						offer_details.get("offer_cover").split("\\.")[0]));

				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
				log.info("Assert expected and Found:" + spouse_name.trim() + ""
						+ " and " + offer_details.get("spouse_name"));
				Assert.assertTrue(spouse_name.trim().equalsIgnoreCase(
						offer_details.get("spouse_name").trim()));
				log.info("Assert expected and Found:" + child1.trim() + ""
						+ " and " + offer_details.get("child1_name"));
				Assert.assertTrue(child1.trim().equalsIgnoreCase(
						offer_details.get("child1_name").trim()));
				log.info("Assert expected and Found:" + child2.trim() + ""
						+ " and " + offer_details.get("child2_name"));
				Assert.assertTrue(child2.trim().equalsIgnoreCase(
						offer_details.get("child2_name").trim()));
				log.info("Assert expected and Found:" + child3.trim() + ""
						+ " and " + offer_details.get("child3_name"));
				Assert.assertTrue(child3.trim().equalsIgnoreCase(
						offer_details.get("child3_name").trim()));

			}

			if (product_reg.trim().contains(MIP_CustomerManagementPage.PA_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.PA_2017);
				if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
					if (!pa_ref_num.trim().equals("")) {
						log.info("Assert expected and Found:" + pa_ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(pa_ref_num.trim().equalsIgnoreCase(
								offer_details.get("ref_no").trim()));
					}

					Assert.assertTrue(MIP_RegCust_TestData.NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));

				} else {

					Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));

				}

				if (!pa_contact_num.equals("")) {
					log.info("Assert expected and Found:" + pa_contact_num
							+ " and " + offer_details.get("alt_msisdn").trim());
					Assert.assertTrue(pa_contact_num.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}

				log.info("Assert expected and Found:" + nominee_name + " and "
						+ cust_details.get("nominee_name").trim());
				Assert.assertTrue(nominee_name.trim().equals(
						cust_details.get("nominee_name").trim()));
				if (!nominee_contact.equals("")) {

					log.info("Assert expected and Found:" + nominee_contact
							+ " and "
							+ cust_details.get("nominee_msisdn").trim());
					Assert.assertTrue(nominee_contact.trim().equals(
							cust_details.get("nominee_msisdn").trim()));
				}

				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
				log.info("Assert expected and Found:" + pa_cover_seg + " and "
						+ offer_details.get("offer_cover").trim());
				Assert.assertTrue(pa_cover_seg
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));

			}
			if (product_reg.trim().contains(
					MIP_CustomerManagementPage.LIFE_2017)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.LIFE_2017);
				if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
					if (!life_ref_num.equals("")) {
						log.info("Assert expected and Found:" + life_ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(life_ref_num.trim().equals(
								offer_details.get("ref_no").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));

				} else {

					Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));

				}

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
				log.info("Assert expected and Found:" + nominee_relation
						+ " and " + cust_details.get("relationship").trim());
				Assert.assertTrue(nominee_relation.trim().equals(
						cust_details.get("relationship").trim()));

				log.info("Assert expected and Found:" + life_cov_seg + " and "
						+ offer_details.get("offer_cover").trim());
				Assert.assertTrue(life_cov_seg
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));

				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
			}
			if (product_reg.trim().contains(MIP_CustomerManagementPage.HMP)) {
				Map<String, String> offer_details = new HashMap<String, String>();
				offer_details = MIP_RegisterCustomer_Queries.getOfferDetails(
						cust_details.get("cust_id"),
						MIP_CustomerManagementPage.HMP);
				if (!channel.equalsIgnoreCase(MIP_CustomerManagementPage.GSM)) {
					if (!hmp_ref_num.trim().equals("")) {
						log.info("Assert expected and Found:" + hmp_ref_num
								+ " and " + offer_details.get("ref_no").trim());
						Assert.assertTrue(hmp_ref_num.trim().equalsIgnoreCase(
								offer_details.get("ref_no").trim()));
					}
					Assert.assertTrue(MIP_RegCust_TestData.NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));

				} else {
					Assert.assertTrue(MIP_RegCust_TestData.GSM_IS_CONFIRMED_AFTER_REGISTRATION
							.equals(offer_details.get("is_confirmed").trim()));

				}

				if (!hmp_alt_msisdn.equals("")) {
					log.info("Assert expected and Found:" + hmp_alt_msisdn
							+ " and " + offer_details.get("alt_msisdn").trim());
					Assert.assertTrue(hmp_alt_msisdn.trim().equals(
							offer_details.get("alt_msisdn").trim()));
				}
				Assert.assertTrue(MIP_RegCust_TestData.PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION
						.equals(offer_details.get("is_product_attached").trim()));
				Assert.assertTrue(MIP_RegCust_TestData.SUBSCRIPTION_STATUS_AFTER_REGISTRATION
						.equalsIgnoreCase(offer_details.get(
								"subscription_state").trim()));
				log.info("Assert expected and Found:" + hmp_cov_seg + " and "
						+ offer_details.get("offer_cover").trim());
				Assert.assertTrue(hmp_cov_seg
						.trim()
						.replaceAll("\\,", "")
						.contains(
								offer_details.get("offer_cover").split("\\.")[0]));
				log.info("Assert expected and Found:" + address1 + " and "
						+ offer_details.get("address_line_1").trim());
				Assert.assertTrue(address1.trim().equals(
						offer_details.get("address_line_1").trim()));
				log.info("Assert expected and Found:" + address2 + " and "
						+ offer_details.get("address_line_2").trim());
				Assert.assertTrue(address2.trim().equals(
						offer_details.get("address_line_2").trim()));
				log.info("Assert expected and Found:" + postal_code + " and "
						+ offer_details.get("postal_code").trim());
				Assert.assertTrue(postal_code.trim().equals(
						offer_details.get("postal_code").trim()));
			}
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("RegisterCustomer-GSM-Prepaid--" + testcase
					+ " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "Mandatory_Field_Validation_Postpaid", dataProvider = "mandatoryFieldValidationin_Postpaid_Registration", dataProviderClass = MIP_RegCust_TestData.class)
	public void mandatoryFieldValidationInregistration_Postpaid(
			String testcase, String nic, String channel, String err_msg)
			throws Throwable {
		testcaseName = testcase;
		try {
			logger = report.startTest("Mandatory_Field_Validation_Postpaid--"
					+ testcase);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);

			regpage.waitForElementToVisible(By.id("nic"));
			Thread.sleep(1000);
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(channel.trim());
			regpage.clickOnSeachButton();
			regpage.selectAvailableOffers(MIP_CustomerManagementPage.HOSPITALIZATION);
			regpage.selectAvailableOffers(MIP_CustomerManagementPage.LIFE_2017);
			regpage.selectAvailableOffers(MIP_CustomerManagementPage.PA_2017);
			if (channel.trim()
					.equalsIgnoreCase(MIP_CustomerManagementPage.CDMA)
					|| channel.trim().equalsIgnoreCase(
							MIP_CustomerManagementPage.LTE)) {
				regpage.selectAvailableOffers(MIP_CustomerManagementPage.HMP);
			}
			regpage.clickOnSave();
			String err = regpage.getValidationMsg().trim();
			log.info("Actual Error message:" + err_msg
					+ " Error message found:" + err);
			Assert.assertTrue(err.trim().replaceAll("\\s", "")
					.equalsIgnoreCase(err_msg.trim().replaceAll("\\s", "")));
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);

		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("RegisterCustomer-GSM-Prepaid--" + testcase
					+ " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "Field_Validation_negativeData_Postpaid", dataProvider = "Field_Validation_With_NegativeData-Postpaid", dataProviderClass = MIP_RegCust_TestData.class)
	public void fieldValidationWithNegativeData_Postpaid(String testcase,
			String nic, String channel, String data, String err_msg)
			throws Throwable {
		testcaseName = testcase;
		try {
			logger = report
					.startTest("Field_Validation_negativeData_Postpaid--"
							+ testcase);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);

			regpage.waitForElementToVisible(By.id("nic"));
			Thread.sleep(1000);
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(channel.trim());
			regpage.clickOnSeachButton();
			regpage.selectAvailableOffers(MIP_CustomerManagementPage.HOSPITALIZATION);
			regpage.selectAvailableOffers(MIP_CustomerManagementPage.LIFE_2017);
			regpage.selectAvailableOffers(MIP_CustomerManagementPage.PA_2017);
			if (channel.trim()
					.equalsIgnoreCase(MIP_CustomerManagementPage.CDMA)
					|| channel.trim().equalsIgnoreCase(
							MIP_CustomerManagementPage.LTE)) {
				regpage.selectAvailableOffers(MIP_CustomerManagementPage.HMP);
			}
			regpage.enterCustomerInfoForNonGSMChannel(data.trim(), "English");
			regpage.enterAccoundId(data);
			regpage.enterHPInfoForPostpaid(data, data, "", data, data, data,
					data);
			regpage.enterLifeInfoForPostpaid(data, data, "");
			regpage.enterPAInfoForPostpaid(data, data, "");
			regpage.enterHMPInfoForPostpaid(data, data, "", data, data, data);
			regpage.enterNomineeInformation(data, data, "Parent");
			regpage.clickOnSave();
			String err = regpage.getValidationMsg().trim();
			log.info("Actual Error message:" + err_msg
					+ " Error message found:" + err);
			Assert.assertTrue(err.trim().replaceAll("\\s", "")
					.equalsIgnoreCase(err_msg.trim().replaceAll("\\s", "")));
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);

		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("RegisterCustomer-GSM-Prepaid--" + testcase
					+ " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@Test(testName = "duplicateAccountIdValidation_Postpaid", dataProvider = "duplicateAccountIdValidation-Postpaid", dataProviderClass = MIP_RegCust_TestData.class)
	public void duplicateAccountIdValidation_Postpaid(String testcase,
			String nic, String channel, String accountId, String err_msg)
			throws Throwable {
		testcaseName = testcase;
		try {
			logger = report
					.startTest("Field_Validation_negativeData_Postpaid--"
							+ testcase);
			regpage = PageFactory.initElements(MIP_Test_Configuration.driver,
					MIP_RegisterCustomerPage.class);

			regpage.waitForElementToVisible(By.id("nic"));
			Thread.sleep(1000);
			regpage.enterNic(nic);
			regpage.selectSubscriberChannelType(channel.trim());
			regpage.clickOnSeachButton();
			regpage.enterAccoundId(accountId);
			regpage.enterCustomerInfoForNonGSMChannel("", "English");

			String err = regpage.getValidationMsg().trim();
			log.info("Actual Error message:" + err_msg
					+ " Error message found:" + err);
			Assert.assertTrue(err.trim().replaceAll("\\s", "")
					.equalsIgnoreCase(err_msg.trim().replaceAll("\\s", "")));
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);

		} catch (Throwable t) {
			homepage.clickOnMenu(MIP_Menu_Constants.REGISTER_CUSTOMER);
			log.info("RegisterCustomer-GSM-Prepaid--" + testcase
					+ " Test Failed");
			log.info("Error occured in the test case", t);
			throw t;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void after_test(ITestResult res) {

		if (res.getStatus() == ITestResult.FAILURE) {
			MIP_ScreenShots.takeScreenShot(MIP_Test_Configuration.driver,
					res.getName() + "_" + testcaseName);
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