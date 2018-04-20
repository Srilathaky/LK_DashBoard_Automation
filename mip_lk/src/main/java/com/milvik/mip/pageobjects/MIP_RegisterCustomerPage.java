package com.milvik.mip.pageobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_RegisterCustomerPage extends MIP_CustomerManagementPage {
	private WebDriver driver;
	static Logger logger;
	public static final String SUCCESS_MSG = "The customer has been registered successfully.";
	public static final String UPDATE_MSG = "The customer details have been saved successfully.";
	static {
		logger = MIP_Logging.logDetails("MIP_RegisterCustomerPage");
	}

	public MIP_RegisterCustomerPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "nic")
	WebElement msisdn_;
	@FindBy(id = "subscriberChannelId")
	WebElement channel_Type;

	public MIP_RegisterCustomerPage enterCustomerInfo(String name,
			String language, String ref_num, String mobile_num) {
		logger.info("Entering the customer information");
		this.waitForElementToVisible(By.id("custName")).sendKeys(name);
		this.selectDropDownbyText(
				this.waitForElementToVisible(By.id("preferredLang")), language);
		if (!ref_num.equals("")) {
			this.waitForElementToVisible(By.id("referenceNumber")).sendKeys(
					ref_num);
			for (int i = 0; i < 3; i++) {
				if (this.waitForElementToInvisible(By.id("msisdn")))
					break;
			}
		} else if (!mobile_num.equals("")) {
			this.waitForElementToVisible(By.id("msisdn")).sendKeys(mobile_num);
			for (int i = 0; i < 3; i++) {
				if (this.waitForElementToInvisible(By.id("referenceNumber")))
					break;
			}
		}
		return this;
	}

	public MIP_RegisterCustomerPage enterCustomerInfoForNonGSMChannel(
			String name, String language) {
		logger.info("Entering the customer information");
		WebElement ele = this.waitForElementToVisible(By.id("custName"));
		ele.clear();
		ele.sendKeys(name);
		this.selectDropDownbyText(
				this.waitForElementToVisible(By.id("preferredLang")), language);
		return this;
	}

	public MIP_RegisterCustomerPage editCustomerInfo(String nic, String name,
			String language) {
		logger.info("Editing the customer information");
		if (!name.equals("")) {

			WebElement ele = this.waitForElementToVisible(By.id("custName"));
			ele.clear();
			ele.sendKeys(name);
		}
		if (!language.equals(""))
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("preferredLang")),
					language);
		if (!nic.equals("")) {
			WebElement ele = this.waitForElementToVisible(By.id("nic"));
			ele.clear();
			ele.sendKeys(nic);
		}
		return this;
	}

	public List<String> getRegisteredOffer() {
		List<String> offer = new ArrayList<String>();
		logger.info("Getting Registered offer of the customer");
		WebElement ele = this.waitForElementToVisible(By
				.xpath("//*[contains(text(),'"
						+ MIP_CustomerManagementPage.HOSPITALIZATION
						+ "')]//preceding-sibling::input[1]"));
		if (ele.isSelected()) {
			offer.add(MIP_CustomerManagementPage.HOSPITALIZATION);
		}

		ele = this.waitForElementToVisible(By.xpath("//*[contains(text(),'"
				+ MIP_CustomerManagementPage.LIFE_2017
				+ "')]//preceding-sibling::input[1]"));
		if (ele.isSelected()) {
			offer.add(MIP_CustomerManagementPage.LIFE_2017);
		}

		ele = this.waitForElementToVisible(By.xpath("//*[contains(text(),'"
				+ MIP_CustomerManagementPage.PA_2017
				+ "')]//preceding-sibling::input[1]"));
		if (ele.isSelected()) {

			offer.add(MIP_CustomerManagementPage.PA_2017);
		}
		return offer;
	}

	public Map<String, String> getHPProductInformation() {
		Map<String, String> hp_info = new HashMap<String, String>();
		logger.info("Getting Hp product Information");
		hp_info.put(
				"hp_segment",
				this.waitForElementToVisible(By.id("offerCover"))
						.getAttribute("value").trim());
		List<WebElement> ele = driver.findElements(By
				.id("hpAlterContactNumber"));
		if (ele.size() != 0) {
			hp_info.put("hp_altnum", ele.get(0).getAttribute("value"));
		} else {
			hp_info.put("hp_altnum", "");
		}
		return hp_info;
	}

	public Map<String, String> getLifeInformation() {
		Map<String, String> life_info = new HashMap<String, String>();
		logger.info("Getting Life product Information");
		WebElement ele = this.waitForElementToVisible(By
				.id("offerCoverIdLife2017"));
		Select s = new Select(ele);

		life_info.put("life_segment", s.getFirstSelectedOption().getText()
				.trim());
		List<WebElement> ele_ = driver.findElements(By
				.id("life2017AlterContactNumber"));
		if (ele_.size() != 0) {
			life_info.put("life_altnum", ele_.get(0).getAttribute("value"));
		} else {
			life_info.put("life_altnum", "");
		}
		life_info.put("nominee_name",
				this.waitForElementToVisible(By.id("nomineeName"))
						.getAttribute("value"));
		ele_ = driver.findElements(By.id("nomineeMsisdn"));
		if (ele_.size() != 0) {
			life_info.put("nominee_msisdn", ele_.get(0).getAttribute("value"));
		} else {
			life_info.put("nominee_msisdn", "");
		}
		ele = this.waitForElementToVisible(By.id("insRelation"));
		s = new Select(ele);
		life_info.put("life_relation", s.getFirstSelectedOption().getText()
				.trim());
		return life_info;

	}

	public Map<String, String> getPAInformation() {
		Map<String, String> pa_info = new HashMap<String, String>();
		logger.info("Getting Life product Information");
		WebElement ele = this.waitForElementToVisible(By
				.id("offerCoverIdPA2017"));
		Select s = new Select(ele);

		pa_info.put("pa_segment", s.getFirstSelectedOption().getText().trim());
		List<WebElement> ele_ = driver.findElements(By
				.id("pa2017AlterContactNumber"));
		if (ele_.size() != 0) {
			pa_info.put("pa_altnum", ele_.get(0).getAttribute("value"));
		} else {
			pa_info.put("pa_altnum", "");
		}
		pa_info.put("nominee_name",
				this.waitForElementToVisible(By.id("nomineeName"))
						.getAttribute("value"));
		ele_ = driver.findElements(By.id("nomineeMsisdn"));
		if (ele_.size() != 0) {
			pa_info.put("nominee_msisdn", ele_.get(0).getAttribute("value"));
		} else {
			pa_info.put("nominee_msisdn", "");
		}

		return pa_info;

	}

	public Map<String, String> getCustomerInfo() {
		Map<String, String> cust_details = new HashMap<String, String>();
		logger.info("Getting the customer information");
		cust_details.put(
				"cust_name",
				this.waitForElementToVisible(By.id("custName")).getAttribute(
						"value"));
		WebElement ele = this.waitForElementToVisible(By.id("preferredLang"));
		Select s = new Select(ele);
		cust_details.put("language", s.getFirstSelectedOption().getText());
		cust_details.put("nic", this.waitForElementToVisible(By.id("nic"))
				.getAttribute("value"));

		return cust_details;
	}

	public MIP_RegisterCustomerPage editNomineeInformation(String nomineeName,
			String contact_num, String relation) {
		logger.info("Editing Nominee Information");
		if (!nomineeName.equals("")) {
			WebElement ele = this.waitForElementToVisible(By.id("nomineeName"));
			ele.clear();
			ele.sendKeys(nomineeName);
		}
		if (!contact_num.equals("")) {
			WebElement ele = this.waitForElementToVisible(By
					.id("nomineeMsisdn"));
			ele.clear();
			ele.sendKeys(contact_num);
		}
		if (!relation.equals(""))
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("insRelation")),
					relation);
		logger.info("edited Nominee Information");
		return this;
	}

	public MIP_RegisterCustomerPage selectAvailableOffers(String product) {
		logger.info("Selecting available offers");
		if (product.trim().contains(MIP_CustomerManagementPage.HOSPITALIZATION))
			this.waitForElementToVisible(
					By.xpath("//*[contains(text(),'"
							+ MIP_CustomerManagementPage.HOSPITALIZATION
							+ "')]//preceding-sibling::input[1]")).click();
		if (product.trim().contains(MIP_CustomerManagementPage.LIFE_2017))
			this.waitForElementToVisible(
					By.xpath("//*[contains(text(),'"
							+ MIP_CustomerManagementPage.LIFE_2017
							+ "')]//preceding-sibling::input[1]")).click();
		if (product.trim().contains(MIP_CustomerManagementPage.PA_2017))
			this.waitForElementToVisible(
					By.xpath("//*[contains(text(),'"
							+ MIP_CustomerManagementPage.PA_2017
							+ "')]//preceding-sibling::input[1]")).click();
		if (product.trim().contains(MIP_CustomerManagementPage.HMP))
			this.waitForElementToVisible(
					By.xpath("//*[contains(text(),'"
							+ MIP_CustomerManagementPage.HMP
							+ "')]//preceding-sibling::input[1]")).click();
		logger.info("Selected available offers");
		return this;
	}

	public MIP_RegisterCustomerPage enterHPAlternateContactNum(
			String contact_num) {
		logger.info("Entering HP Alternative contact number");
		this.waitForElementToVisible(By.id("hpAlterContactNumber")).sendKeys(
				contact_num);
		logger.info("entered HP Alternate ContactNum");
		return this;
	}

	public MIP_RegisterCustomerPage enterPA2017AlternateContactNum(
			String contact_num) {
		logger.info("Entering PA 2017 Alternative contact number");
		this.waitForElementToVisible(By.id("pa2017AlterContactNumber"))
				.sendKeys(contact_num);
		logger.info("entered PA Alternate ContactNum");
		return this;
	}

	public MIP_RegisterCustomerPage enterLife2017AlternateContactNum(
			String contact_num) {
		logger.info("Entering Life 2017 Alternative contact number");
		this.waitForElementToVisible(By.id("life2017AlterContactNumber"))
				.sendKeys(contact_num);
		logger.info("entered Alternate ContactNum");
		return this;
	}

	public MIP_RegisterCustomerPage selectLifeCoverSegment_Life2017(
			String life_cover) {
		logger.info("Selecting Life cover segment for Life 2017");
		WebElement ele = this.waitForElementToVisible(By
				.id("offerCoverIdLife2017"));
		this.selectDropDownbyText(ele, life_cover);
		logger.info("Selected Life cover segment");
		return this;
	}

	public MIP_RegisterCustomerPage enterNomineeInformation(String nomineeName,
			String contact_num, String relation) {
		logger.info("Entering Nominee Information");
		if (waitForElementToVisible(By.id("nomineeName")).getAttribute("value")
				.equals(""))
			this.waitForElementToVisible(By.id("nomineeName")).sendKeys(
					nomineeName);
		if (waitForElementToVisible(By.id("nomineeMsisdn")).getAttribute(
				"value").equals(""))
			this.waitForElementToVisible(By.id("nomineeMsisdn")).sendKeys(
					contact_num);
		if (!relation.equals(""))
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("insRelation")),
					relation);
		logger.info("entered Nominee Information");
		return this;
	}

	public MIP_RegisterCustomerPage enterMSISDN(String msisdn) {
		try {
			logger.info("Entering the customer MSISDN");
			for (int i = 0; i < 3; i++) {
				try {

					WebElement ele = this.waitForElementToVisible(By
							.id("msisdn"));
					this.enterText(ele, msisdn);
					if (ele.getAttribute("value").equalsIgnoreCase(msisdn)) {
						break;

					}
				} catch (Exception e) {
					logger.info("Entering msisdn again after exception");
				}
			}

		} catch (Exception e) {
			logger.error("Error while entering the MSISDN", e);
		}
		return this;
	}

	public MIP_RegisterCustomerPage clickOnSave() {
		logger.info("Clicking on Save button in Customer Registration page");
		this.waitForElementTobeClickable(By.id("saveBtn"));
		this.clickOnElement(By.id("saveBtn"));
		return this;
	}

	public void clickOnClear() {
		this.clickOnElement(By.id("clearBtn"));
	}

	public void clickOnBack() {
		this.clickOnElement(By.id("backBtn"));
	}

	public String getSuccessMsg() {
		WebDriverWait w = new WebDriverWait(driver, 50);
		return w.until(
				ExpectedConditions.visibilityOfElementLocated(By
						.id("message_div"))).getText();
	}

	public String getErrorMsg() {
		return this.waitForElementToPresent(By.id("errormessage_div"))
				.getText();
	}

	public String getValidationMsg() {
		logger.info("Getting Validation message in Customer registration page");
		return this.waitForElementToVisible(By.id("validationMessages"))
				.getText();
	}

	public boolean verifyBackButton() {
		return this.waitForElementToPresent(By.id("backBtn")).isDisplayed();
	}

	public void clickOnSeachButton() {
		logger.info("clicking on search button");
		this.waitForElementTobeClickable(By.id("searchBtn")).click();
		logger.info("Clicked on Search button");
	}

	public MIP_RegisterCustomerPage enterHPInfoForPostpaid(String contact_num,
			String hp_ref_num, String hosp_segments, String spouse_name,
			String child1, String child2, String child3) {
		logger.info("Entering HP product Information for PostPaid Registration----"
				+ contact_num
				+ ","
				+ hp_ref_num
				+ ","
				+ hosp_segments
				+ ","
				+ spouse_name + "," + child1 + "," + child2 + "," + child3);
		this.waitForElementToVisible(By.id("hpAlterContactNumber")).sendKeys(
				contact_num);
		if (!hp_ref_num.equals(""))
			this.waitForElementToVisible(By.id("hpReferenceNo")).sendKeys(
					hp_ref_num);
		if (!hosp_segments.equals(""))
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("offerCoverIdHP")),
					hosp_segments.trim());
		if (!spouse_name.equals(""))
			this.waitForElementToVisible(By.id("spouseName")).sendKeys(
					spouse_name);
		if (!child1.equals("")) {
			this.waitForElementToVisible(By.id("childOne")).sendKeys(child1);
			if (!child2.equals("")) {
				this.waitForElementToVisible(By.id("add-second-icon")).click();
				this.waitForElementToVisible(By.id("childTwo"))
						.sendKeys(child2);
				if (!child3.equals("")) {
					this.waitForElementToVisible(By.id("add-third-icon"))
							.click();
					this.waitForElementToVisible(By.id("childThree")).sendKeys(
							child3);
				}
			}
		}

		logger.info("entered HP Product Information for postpaid");
		return this;
	}

	public MIP_RegisterCustomerPage enterLifeInfoForPostpaid(String ref_num,
			String alt_msisdn, String life_cover_seg) {
		logger.info("Entering Life product Information for PostPaid Registration----"
				+ ref_num + "," + alt_msisdn + "," + life_cover_seg);
		this.waitForElementToVisible(By.id("life2017ReferenceNumber"))
				.sendKeys(ref_num);
		if (!life_cover_seg.equals(""))
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("offerCoverIdLife2017")),
					life_cover_seg.trim());
		if (!alt_msisdn.equals(""))
			this.waitForElementToVisible(By.id("life2017AlterContactNumber"))
					.sendKeys(alt_msisdn);

		logger.info("entered Life Product Information for postpaid");
		return this;
	}

	public MIP_RegisterCustomerPage enterPAInfoForPostpaid(String ref_num,
			String alt_msisdn, String pa_cover_seg) {
		logger.info("Entering PA product Information for PostPaid Registration----"
				+ ref_num + "," + alt_msisdn + "," + pa_cover_seg);
		if (!ref_num.equals(""))
			this.waitForElementToVisible(By.id("pa2017ReferenceNumber"))
					.sendKeys(ref_num);
		if (!pa_cover_seg.equals(""))
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("offerCoverIdPA2017")),
					pa_cover_seg.trim());
		if (!alt_msisdn.equals(""))
			this.waitForElementToVisible(By.id("pa2017AlterContactNumber"))
					.sendKeys(alt_msisdn);

		logger.info("entered Life Product Information for postpaid");
		return this;
	}

	public MIP_RegisterCustomerPage enterHMPInfoForPostpaid(String ref_num,
			String alt_msisdn, String hmp_cover_seg, String address1,
			String address2, String postal_code) {
		logger.info("Entering HMP product Information for PostPaid Registration----"
				+ ref_num
				+ ","
				+ alt_msisdn
				+ ","
				+ hmp_cover_seg
				+ ","
				+ address1 + "," + address2 + "," + postal_code);
		this.waitForElementToVisible(By.id("hmpReferenceNo")).sendKeys(ref_num);
		if (!hmp_cover_seg.equals(""))
			this.selectDropDownbyText(
					this.waitForElementToVisible(By.id("offerCoverIdHmp")),
					hmp_cover_seg.trim());
		if (!alt_msisdn.equals(""))
			this.waitForElementToVisible(By.id("hmpAlterContactNumber"))
					.sendKeys(alt_msisdn);
		if (!address1.equals(""))
			this.waitForElementToVisible(By.id("addressLine1")).sendKeys(
					address1);
		if (!address2.equals(""))
			this.waitForElementToVisible(By.id("addressLine2")).sendKeys(
					address2);
		if (!postal_code.equals(""))
			this.waitForElementToVisible(By.id("postalCode")).sendKeys(
					postal_code);

		logger.info("entered HMP Product Information for postpaid");
		return this;
	}

	public MIP_RegisterCustomerPage editHMPInfoForPostpaid(String address1,
			String address2, String postal_code) {
		logger.info("Edit HMP address and postal code information for PostPaid Registration----"
				+ address1 + "," + address2 + "," + postal_code);

		if (!address1.equals("")) {
			WebElement ele = this
					.waitForElementToVisible(By.id("addressLine1"));
			ele.clear();
			ele.sendKeys(address1);
		}
		if (!address2.equals("")) {
			WebElement ele = this
					.waitForElementToVisible(By.id("addressLine2"));
			ele.clear();
			ele.sendKeys(address2);
		}
		if (!postal_code.equals("")) {
			WebElement ele = this.waitForElementToVisible(By.id("postalCode"));
			ele.clear();
			ele.sendKeys(postal_code);
		}
		logger.info("Edited HMP Product Information for postpaid");
		return this;
	}

	public void enterAccoundId(String accountId) {
		logger.info("Entering Account ID  Information for PostPaid Registration----"
				+ accountId);
		this.waitForElementToVisible(By.id("accountId")).sendKeys(accountId);
		logger.info("Acount ID entered");
	}

	public boolean checkProductselectiondisabledAfterRegistration(
			List<String> reg_product) {
		String hp = "";
		String life = "";
		String pa = "";
		String hmp = "";
		logger.info("checking registered product check box is disabled after registration");
		if (reg_product.toString().trim()
				.contains(MIP_CustomerManagementPage.HOSPITALIZATION)) {
			hp = this
					.waitForElementToVisible(
							By.xpath("//input[@type='checkbox'][@id='offerId'][@value='2']"))
					.getAttribute("disabled");
		} else
			hp = "true";
		if (reg_product.toString().trim()
				.contains(MIP_CustomerManagementPage.LIFE_2017)) {
			life = this
					.waitForElementToVisible(
							By.xpath("//input[@type='checkbox'][@id='offerId'][@value='6']"))
					.getAttribute("disabled");
		} else
			life = "true";
		if (reg_product.toString().trim()
				.contains(MIP_CustomerManagementPage.HMP)) {
			hmp = this
					.waitForElementToVisible(
							By.xpath("//input[@type='checkbox'][@id='offerId'][@value='4']"))
					.getAttribute("disabled");
		} else
			hmp = "true";
		if (reg_product.toString().trim()
				.contains(MIP_CustomerManagementPage.PA_2017)) {
			pa = this
					.waitForElementToVisible(
							By.xpath("//input[@type='checkbox'][@id='offerId'][@value='7']"))
					.getAttribute("disabled");
		} else
			pa = "true";
		if ((!hp.equals("") || hp.equalsIgnoreCase("true")
				&& (!life.equals("") || life.equalsIgnoreCase("true"))
				&& (!pa.equals("") || pa.equalsIgnoreCase("true"))
				&& (!hmp.equals("") || hmp.equalsIgnoreCase("true")))) {
			return true;
		}
		return false;
	}

	public boolean lifeInfoReadOnlyCheck(String ref_num, String alt_msisdn,
			String life_cover, String nominee_name, String nominee_contact,
			String nominee_relation) {
		String altmsisdn = "";
		String refnum = "";
		String lifecover = "";
		String nomineename = "";
		String nomineeMsisdn = "";
		String nomineerelation = "";
		boolean value1 = false;
		boolean value2 = false;
		logger.info("checking Life product info is disabled after registration");
		if (!ref_num.equals(""))
			refnum = this.waitForElementToVisible(
					By.xpath("//input[@id='life2017ReferenceNumber'][@value='"
							+ ref_num.trim() + "']")).getAttribute("disabled");
		else
			refnum = "true";
		if (!alt_msisdn.equals("")) {
			altmsisdn = this
					.waitForElementToVisible(
							By.xpath("//input[@id='life2017AlterContactNumber'][@value='"
									+ alt_msisdn.trim() + "']")).getAttribute(
							"disabled");
		} else {
			altmsisdn = "true";
		}
		if (!life_cover.equals(""))
			lifecover = this.waitForElementToVisible(
					By.xpath("//select[@id='offerCoverIdLife2017']"))
					.getAttribute("disabled");
		if (!nominee_name.equals(""))
			nomineename = this.waitForElementToVisible(
					By.xpath("//input[@id='nomineeName'][@value='"
							+ nominee_name.trim() + "']")).getAttribute(
					"disabled");
		if (!nominee_contact.equals("")) {
			nomineeMsisdn = this.waitForElementToVisible(
					By.xpath("//input[@id='nomineeMsisdn'][@value='"
							+ nominee_contact.trim() + "']")).getAttribute(
					"disabled");
		} else {
			nomineeMsisdn = "true";
		}
		if (!nominee_relation.equals(""))
			nomineerelation = this.waitForElementToVisible(
					By.xpath("//select[@id='insRelation']")).getAttribute(
					"disabled");
		try {
			if ((!refnum.equals("") || refnum.equalsIgnoreCase("true"))
					&& (!altmsisdn.equals("") || altmsisdn
							.equalsIgnoreCase("true"))
					&& (!lifecover.equals("") || lifecover
							.equalsIgnoreCase("true"))) {
				value1 = true;
			}
			try {
				if ((!nomineename.equals("") || nomineename
						.equalsIgnoreCase("true"))
						&& (!nomineeMsisdn.equals("") || nomineeMsisdn
								.equalsIgnoreCase("true"))
						&& (!nomineerelation.equals("") || nomineerelation
								.equalsIgnoreCase("true"))) {
					value2 = false;
				}
			} catch (NullPointerException e) {
				Select s = new Select(this.waitForElementToVisible(By
						.xpath("//select[@id='offerCoverIdLife2017']")));
				if (s.getFirstSelectedOption().getText().trim()
						.equalsIgnoreCase(life_cover.trim())) {
					if (new Select(this.waitForElementToVisible(By
							.xpath("//select[@id='insRelation']")))
							.getFirstSelectedOption().getText().trim()
							.equalsIgnoreCase(nominee_relation.trim()))
						value2 = true;
				}

			}
		} catch (NullPointerException e) {
			value1 = false;
		}
		if (value1 && value2) {
			return true;
		}
		return false;
	}

	public boolean paInfoReadOnlyCheck(String ref_num, String alt_msisdn,
			String pa_cover, String nominee_name, String nominee_contact) {
		String altmsisdn = "";
		String refnum = "";
		String pacover = "";
		String nomineename = "";
		String nomineeMsisdn = "";
		boolean value1 = false;
		boolean value2 = false;
		logger.info("checking Life product info is disabled after registration");
		if (!ref_num.equals(""))
			refnum = this.waitForElementToVisible(
					By.xpath("//input[@id='pa2017ReferenceNumber'][@value='"
							+ ref_num.trim() + "']")).getAttribute("disabled");
		else
			refnum = "true";
		if (!alt_msisdn.equals("")) {
			altmsisdn = this.waitForElementToVisible(
					By.xpath("//input[@id='pa2017AlterContactNumber'][@value='"
							+ alt_msisdn.trim() + "']")).getAttribute(
					"disabled");
		} else {
			altmsisdn = "true";
		}
		if (!pa_cover.equals(""))
			pacover = this.waitForElementToVisible(
					By.xpath("//select[@id='offerCoverIdPA2017']"))
					.getAttribute("disabled");
		if (!nominee_name.equals(""))
			nomineename = this.waitForElementToVisible(
					By.xpath("//input[@id='nomineeName'][@value='"
							+ nominee_name.trim() + "']")).getAttribute(
					"disabled");
		if (!nominee_contact.equals("")) {
			nomineeMsisdn = this.waitForElementToVisible(
					By.xpath("//input[@id='nomineeMsisdn'][@value='"
							+ nominee_contact.trim() + "']")).getAttribute(
					"disabled");
		} else {
			nomineeMsisdn = "true";
		}
		try {
			if ((!refnum.equals("") || refnum.equalsIgnoreCase("true"))
					&& (!altmsisdn.equals("") || altmsisdn
							.equalsIgnoreCase("true"))
					&& (pacover.equals("") || pacover.equalsIgnoreCase("true"))) {
				value1 = true;
			}
			try {
				if ((!nomineename.equals("") || nomineename
						.equalsIgnoreCase("true"))
						&& (!nomineeMsisdn.equals("") || nomineeMsisdn
								.equalsIgnoreCase("true"))) {
					value2 = false;
				}
			} catch (NullPointerException e) {
				Select s = new Select(this.waitForElementToVisible(By
						.xpath("//select[@id='offerCoverIdPA2017']")));
				if (s.getFirstSelectedOption().getText().trim()
						.equalsIgnoreCase(pa_cover.trim()))
					value2 = true;
			}
		} catch (NullPointerException e) {
			value1 = false;
		}
		if (value1 && value2) {
			return true;
		}
		return false;
	}

	public boolean hmpProductInfoReadOnlyCheck(String ref_num,
			String alt_msisdn, String hmp_cover, String address1,
			String address2, String postal_code) {
		String altmsisdn = "";
		String refnum = "";
		String hmpcover = "";
		String add1 = "";
		String add2 = "";
		String postal = "";
		boolean value1 = false;
		boolean value2 = false;
		WebElement address1_ = null;
		WebElement address2_ = null;
		WebElement postalCode = null;
		logger.info("checking HP product info is disabled after registration");
		if (!ref_num.equals(""))
			refnum = this.waitForElementToVisible(
					By.xpath("//input[@id='hmpReferenceNo'][@value='"
							+ ref_num.trim() + "']")).getAttribute("disabled");
		if (!alt_msisdn.equals("")) {
			altmsisdn = this.waitForElementToVisible(
					By.xpath("//input[@id='hmpAlterContactNumber'][@value='"
							+ alt_msisdn.trim() + "']")).getAttribute(
					"disabled");
		} else {
			altmsisdn = "true";
		}
		if (!hmp_cover.equals(""))
			hmpcover = this.waitForElementToVisible(
					By.xpath("//input[@id='offerCoverHmp'][@value='"
							+ hmp_cover.trim() + "']"))
					.getAttribute("disabled");
		if (!address1.equals("")) {
			address1_ = this.waitForElementToVisible(By.id("addressLine1"));
			add1 = address1_.getAttribute("disabled");
		}
		if (!address2.equals("")) {
			address2_ = this.waitForElementToVisible(By.id("addressLine2"));
			add2 = address2_.getAttribute("disabled");
		}
		if (!postal_code.equals(""))
			postalCode = this.waitForElementToVisible(By
					.xpath("//input[@id='postalCode'][@value='"
							+ postal_code.trim() + "']"));
		postal = postalCode.getAttribute("disabled");

		try {
			if ((!refnum.equals("") || refnum.equalsIgnoreCase("true"))
					&& (!altmsisdn.equals("") || altmsisdn
							.equalsIgnoreCase("true"))
					&& (!hmpcover.equals("") || hmpcover
							.equalsIgnoreCase("true"))) {
				value1 = true;
			}
			try {
				if ((!add1.equals("") || add1.equalsIgnoreCase("true"))
						&& (!add2.equals("") || add2.equalsIgnoreCase("true"))
						&& (!postal.equals("") || postal
								.equalsIgnoreCase("true"))) {
					value2 = false;
				}
			} catch (NullPointerException e) {
				if (address1_.getText().trim().equals(address1.trim())
						&& address2_.getText().trim().equals(address2.trim()))
					value2 = true;
			}
		} catch (NullPointerException e) {
			value1 = false;
		}
		if (value1 && value2) {
			return true;
		}
		return false;
	}

	public boolean hpProductInfoReadOnlyCheck(String ref_num,
			String alt_msisdn, String hp_cover, String spouse_name,
			String child1, String child2, String child3) {
		String altmsisdn = "";
		String refnum = "";
		String hpcover = "";
		String spousename = "";
		String childone = "";
		String childtwo = "";
		String childthree = "";

		logger.info("checking HP product info is disabled after registration");
		if (!ref_num.equals("")) {
			refnum = this.waitForElementToVisible(
					By.xpath("//input[@id='hpReferenceNo'][@value='"
							+ ref_num.trim() + "']")).getAttribute("disabled");
		} else {
			refnum = "true";
		}
		if (!alt_msisdn.equals("")) {
			altmsisdn = this.waitForElementToVisible(
					By.xpath("//input[@id='hpAlterContactNumber'][@value='"
							+ alt_msisdn.trim() + "']")).getAttribute(
					"disabled");
		} else {
			altmsisdn = "true";
		}
		if (!hp_cover.equals(""))
			hpcover = this.waitForElementToVisible(
					By.xpath("//input[@id='offerCover'][@value='"
							+ hp_cover.trim() + "']")).getAttribute("disabled");
		if (!spouse_name.equals("")) {
			spousename = this.waitForElementToVisible(
					By.xpath("//input[@id='spouseName'][@value='"
							+ spouse_name.trim() + "']")).getAttribute(
					"disabled");
		} else {
			spousename = "true";
		}
		if (!child1.equals("")) {
			childone = this.waitForElementToVisible(
					By.xpath("//input[@id='childOne'][@value='" + child1.trim()
							+ "']")).getAttribute("disabled");
			if (!child2.equals("")) {
				childtwo = this.waitForElementToVisible(
						By.xpath("//input[@id='childTwo'][@value='"
								+ child2.trim() + "']")).getAttribute(
						"disabled");
				if (!child3.equals("")) {
					childthree = this.waitForElementToVisible(
							By.xpath("//input[@id='childThree'][@value='"
									+ child3.trim() + "']")).getAttribute(
							"disabled");
				} else {
					childthree = "true";
				}

			} else {
				childtwo = "true";
				childthree = "true";
			}
		} else {
			childone = "true";
			childtwo = "true";
			childthree = "true";
		}
		try {
			if ((!refnum.equals("") || refnum.equalsIgnoreCase("true"))
					&& (!altmsisdn.equals("") || altmsisdn
							.equalsIgnoreCase("true"))
					&& (!hpcover.equals("") || hpcover.equalsIgnoreCase("true"))
					&& (!spousename.equals("") || spousename
							.equalsIgnoreCase("true"))
					&& (!childone.equals("") || childone
							.equalsIgnoreCase("true"))
					&& (!childtwo.equals("") || childtwo
							.equalsIgnoreCase("true"))
					&& (!childthree.equals("") || childthree
							.equalsIgnoreCase("true"))) {
				return true;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return false;
	}

}
