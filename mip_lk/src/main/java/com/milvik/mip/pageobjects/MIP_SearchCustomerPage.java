package com.milvik.mip.pageobjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_SearchCustomerPage extends MIP_CustomerManagementPage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_SearchCustomerPage");
	}

	public MIP_SearchCustomerPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public MIP_SearchCustomerPage enterMSISDN(String msisdn) {
		try {
			logger.info("Entering the customer MSISDN");
			if (!msisdn.equals(""))
				this.enterText(this.waitForElementToVisible(By.id("msisdn")),
						msisdn);
		} catch (Exception e) {
			logger.error("Error while entering the MSISDN", e);
		}
		return this;
	}

	public MIP_SearchCustomerPage enterCustomerName(String name) {
		try {
			logger.info("Entering the customer Name");
			if (!name.equals(""))
				this.enterText(this.waitForElementToVisible(By.id("custName")),
						name);
		} catch (Exception e) {
			logger.error("Error while entering the Customer Name", e);
		}
		return this;
	}

	public MIP_SearchCustomerPage enterRefNum(String ref_num) {
		try {
			logger.info("Entering the Reference Number");
			if (!ref_num.equals(""))
				this.enterText(
						this.waitForElementToVisible(By.id("referenceNumber")),
						ref_num);
		} catch (Exception e) {
			logger.error("Error while entering the Reference Number", e);
		}
		return this;
	}

	public MIP_SearchCustomerPage clickOnSearch() {
		try {
			logger.info("Clicking on search button");
			this.waitForElementToVisible(By.id("searchBtn")).click();
		} catch (Exception e) {
			logger.error("Error while clicking on Search button", e);
		}
		return this;
	}

	public MIP_SearchCustomerPage clickOnClear() {
		this.clickOnElement(By.id("clearBtn"));
		return this;
	}

	public MIP_SearchCustomerPage clickOnBack() {
		this.clickOnElement(By.id("backBtn"));
		return this;
	}

	public String getCustname() {
		return this.waitForElementToVisible(By.id("custName")).getAttribute(
				"value");
	}

	public String getmsisdn() {
		return this.waitForElementToVisible(By.id("msisdn")).getAttribute(
				"value");
	}

	public String getNIC() {
		return this.waitForElementToVisible(By.id("nic")).getAttribute("value");
	}

	public String getRefNum() {
		return this.waitForElementToVisible(By.id("referenceNumber"))
				.getAttribute("value");
	}

	public String getValidationMsg() {
		logger.info("Getting Validation message in Customer registration page");
		return this.waitForElementToVisible(By.id("validationMessages"))
				.getText();
	}

	public String getSuccessMsg() {
		return this.waitForElementToVisible(
				By.xpath("//div[@id='validationMessages']//li")).getText();
	}

	public boolean validateSearchCustObjects() {
		if (this.waitForElementToVisible(By.id("custName")).isDisplayed()
				&& this.waitForElementToVisible(By.id("msisdn")).isDisplayed()
				&& this.waitForElementToVisible(By.id("nic")).isDisplayed()
				&& this.waitForElementToVisible(By.id("referenceNumber"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//input[@id='subscriberChannelId'][@value='1']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//input[@id='subscriberChannelId'][@value='2']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//input[@id='subscriberChannelId'][@value='3']"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//input[@id='subscriberChannelId'][@value='4']"))
						.isDisplayed()
				&& this.waitForElementToVisible(By.id("searchBtn"))
						.isDisplayed()
				&& this.waitForElementToVisible(By.id("clearBtn"))
						.isDisplayed()) {
			return true;
		}
		return false;

	}

	public boolean validateSearchTableContent() {
		this.waitForElementToVisible(By.id("customerDetailsList"));
		String _xpath = "//table[@id='customerDetailsList']/thead/tr/th[contains(text(),";
		if (this.waitForElementToVisible(
				By.xpath("//table[@id='customerDetailsList']/thead/tr/th[contains(text(),'Name')]"))
				.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(_xpath + "'Mobile Number')]")).isDisplayed()
				&& this.waitForElementToVisible(By.xpath(_xpath + "'NIC')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(_xpath + "'Subscribed Products')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(_xpath + "'Subscriber Channel')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(_xpath + "'Registered Date')]")).isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(_xpath + "'Registered By')]")).isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath(_xpath + "'Confirmed Date')]")).isDisplayed()) {
			return true;
		}
		return false;
	}

	public int getTableHeadingIndex(String heading) {
		int count = -1;
		this.waitForElementToVisible(By.id("customerDetailsList"));
		List<WebElement> index = driver.findElements(By
				.xpath("//table[@id='customerDetailsList']/thead/tr/th"));
		for (int i = 0; i < index.size(); i++) {
			if (index.get(i).getText().equalsIgnoreCase(heading)) {
				return i + 1;

			}
		}
		return count;
	}

	public boolean ValidateCustomerName(String cus_name) {
		this.waitForElementToVisible(By.id("customerDetailsList"));
		int count = 0;
		List<WebElement> name = driver.findElements(By
				.xpath("//table[@id='customerDetailsList']/tbody/tr/td/a"));
		for (int i = 0; i < name.size(); i++) {
			if (name.get(i).getText().toUpperCase()
					.contains(cus_name.toUpperCase())) {
				count++;
			}
		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}

	}

	public int ValidateMSISDNSearch() {
		this.waitForElementToVisible(By.id("customerDetailsList"));
		int index = getTableHeadingIndex("Mobile Number");
		List<WebElement> product_count = driver.findElements(By
				.xpath("//table[@id='customerDetailsList']/tbody//tr//td["
						+ index + "]"));
		return product_count.size();

	}

	public int ValidateNicsearch() {
		this.waitForElementToVisible(By.id("customerDetailsList"));
		int index = getTableHeadingIndex("NIC");
		List<WebElement> product_count = driver.findElements(By
				.xpath("//table[@id='customerDetailsList']/tbody//tr//td["
						+ index + "]"));
		return product_count.size();

	}

	public int ValidateRefNumsearch() {
		this.waitForElementToVisible(By.id("customerDetailsList"));
		List<WebElement> product_count = driver.findElements(By
				.xpath("//table[@id='customerDetailsList']/tbody//tr"));
		return product_count.size();

	}

	public MIP_SearchCustomerPage clickOnCustomerNameLink(String searchcriteria) {
		this.waitForElementToVisible(By.id("customerDetailsList"));
		this.waitForElementToVisible(
				By.xpath("//table[@id='customerDetailsList']/tbody//tr//td[contains(text(),'"
						+ searchcriteria.trim() + "')]//preceding::td/a"))
				.click();

		return this;
	}

	public int getSearchCustomerTableRowCount() {
		this.waitForElementToVisible(By.id("customerDetailsList"));
		return driver.findElements(
				By.xpath("//table[@id='customerDetailsList']/tbody//tr"))
				.size();

	}

	public Map<String, String> getCustomerDeailsFromSearchTable(int rownum) {
		int index = 0;
		Map<String, String> cust_details = new HashMap<String, String>();
		this.waitForElementToVisible(By.id("customerDetailsList"));
		cust_details.put(
				"Name",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody/tr["
								+ rownum + "]//td/a")).getText());
		index = this.getTableHeadingIndex("Mobile Number");
		cust_details.put(
				"Mobile Number",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody/tr["
								+ rownum + "]//td[" + index + "]")).getText());
		index = this.getTableHeadingIndex("NIC");
		cust_details.put(
				"NIC",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody/tr["
								+ rownum + "]//td[" + index + "]")).getText());
		index = this.getTableHeadingIndex("Subscribed Products");
		cust_details.put(
				"subs_prdct",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody/tr["
								+ rownum + "]//td[" + index + "]")).getText());
		index = this.getTableHeadingIndex("Subscriber Channel");
		cust_details.put(
				"subs_chnl",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody/tr["
								+ rownum + "]//td[" + index + "]")).getText());
		index = this.getTableHeadingIndex("Registered By");
		cust_details.put(
				"reg_by",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody/tr["
								+ rownum + "]//td[" + index + "]")).getText());
		index = this.getTableHeadingIndex("Registered Date");
		cust_details.put(
				"reg_date",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody/tr["
								+ rownum + "]//td[" + index + "]")).getText());
		index = this.getTableHeadingIndex("Confirmed Date");
		cust_details.put(
				"conf_date",
				this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/tbody/tr["
								+ rownum + "]//td[" + index + "]")).getText());
		return cust_details;
	}

	public List<WebElement> getCustomer_Name(String searchcriteria) {
		this.waitForElementToVisible(By.id("customerDetailsList"));
		waitForElementToVisible(By
				.xpath("//table[@id='customerDetailsList']/tbody//tr//td[contains(text(),'"
						+ searchcriteria.trim() + "')]//preceding::td/a"));
		List<WebElement> res = driver
				.findElements(By
						.xpath("//table[@id='customerDetailsList']/tbody//tr//td[contains(text(),'"
								+ searchcriteria.trim()
								+ "')]//preceding::td/a"));

		return res;
	}

	public Map<String, String> getCustInfo() {
		Map<String, String> cust_info = new HashMap<String, String>();
		cust_info
				.put("Cust_Name",
						this.waitForElementToVisible(
								By.xpath(("//input[@id='custName'][@readonly='readonly']")))
								.getAttribute("value"));
		WebElement ele = this.waitForElementToVisible(By
				.xpath("//select[@id='preferredLang'][@disabled='disabled']"));
		Select s = new Select(ele);
		cust_info.put("Language", s.getFirstSelectedOption().getText()
				.charAt(0)
				+ "");
		return cust_info;
	}

	public Map<String, String> getHPInfo() {
		WebDriverWait w = new WebDriverWait(driver, 2);

		Map<String, String> cust_info = new HashMap<String, String>();
		cust_info
				.put("hp_seg",
						this.waitForElementToVisible(
								By.xpath(("//input[@id='offerCoverIdHP'][@readonly='readonly']")))
								.getAttribute("value"));
		try {
			cust_info.put("msisdn", driver.findElement(By

			.xpath("//input[@id='msisdn'][@readonly='readonly']"))
					.getAttribute("value"));
		} catch (Exception e) {
			cust_info.put("msisdn", "");
		}
		try {
			cust_info
					.put("ref_num",
							driver.findElement(
									By.xpath("//input[@id='referenceNoHP'][@readonly='readonly']"))
									.getAttribute("value"));
		} catch (Exception e) {
			cust_info.put("ref_num", "");
		}
		try {
			cust_info
					.put("account_id",
							w.until(ExpectedConditions.visibilityOfElementLocated(By
									.xpath("//input[@id='accountNoHP'][@readonly='readonly']")))
									.getAttribute("value"));
		} catch (Exception e) {
			cust_info.put("account_id", "");

		}
		try {
			cust_info
					.put("spouse_name",
							driver.findElement(
									By.xpath("//input[@id='spouseName'][@readonly='readonly']"))
									.getAttribute("value"));
		} catch (Exception e) {
			cust_info.put("spouse_name", "");
		}
		try {
			cust_info
					.put("child1_name",
							driver.findElement(
									By.xpath("//input[@id='childOne'][@readonly='readonly']"))
									.getAttribute("value"));
		} catch (Exception e) {
			cust_info.put("child1_name", "");

		}
		try {
			cust_info
					.put("child2_name",
							driver.findElement(
									By.xpath("//input[@id='childTwo'][@readonly='readonly']"))
									.getAttribute("value"));
		} catch (Exception e) {
			cust_info.put("child2_name", "");
		}
		try {
			cust_info
					.put("child3_name",
							driver.findElement(
									By.xpath("//input[@id='childThree'][@readonly='readonly']"))
									.getAttribute("value"));
		} catch (Exception e) {
			cust_info.put("child3_name", "");
		}
		try {
			cust_info
					.put("alt_msisdn",
							driver.findElement(
									By.xpath("//input[@id='alternativeNoHP'][@readonly='readonly']"))
									.getAttribute("value"));
		} catch (Exception e) {
			cust_info.put("alt_msisdn", "");
		}

		return cust_info;

	}

	public Map<String, String> getHMPInfo() {
		Map<String, String> cust_info = new HashMap<String, String>();
		cust_info
				.put("hmp_seg",
						this.waitForElementToVisible(
								By.xpath(("//input[@id='homeProLevelId'][@readonly='readonly']")))
								.getAttribute("value"));
		cust_info.put("msisdn",
				this.waitForElementToVisible(By.id(("homeProMsisdn")))
						.getAttribute("value"));

		List<WebElement> ref_num = driver
				.findElements(By
						.xpath("//input[@id='homeProReferenceNumber'][@readonly='readonly']"));
		if (ref_num.size() != 0)
			cust_info.put("ref_num", ref_num.get(0).getAttribute("value"));
		List<WebElement> addressLine1 = driver.findElements(By
				.xpath("//textarea[@id='addressLine1'][@readonly='readonly']"));
		if (addressLine1.size() != 0)
			cust_info.put("addressLine1", addressLine1.get(0).getText());
		else
			cust_info.put("addressLine1", "");
		List<WebElement> addressLine2 = driver.findElements(By
				.xpath("//div[@id='div_id_13']/textarea[@id='addressLine1']"));
		if (addressLine2.size() != 0)
			cust_info.put("addressLine2",
					addressLine2.get(0).getText());
		else
			cust_info.put("addressLine2", "");
		List<WebElement> homeProAccountId = driver
				.findElements(By
						.xpath("//input[@id='homeProAccountId'][@readonly='readonly']"));
		if (homeProAccountId.size() != 0)
			cust_info.put("account_id",
					homeProAccountId.get(0).getAttribute("value"));
		List<WebElement> postalCode = driver.findElements(By
				.xpath("//input[@id='postalCode'][@readonly='readonly']"));
		if (postalCode.size() != 0)
			cust_info
					.put("postalCode", postalCode.get(0).getAttribute("value"));
		else
			cust_info.put("postalCode", "");

		List<WebElement> alt_msisdn = driver.findElements(By
				.xpath("//input[@id='alternativeNoHP'][@readonly='readonly']"));
		if (alt_msisdn.size() != 0)
			cust_info
					.put("alt_msisdn", alt_msisdn.get(0).getAttribute("value"));
		return cust_info;

	}

	public Map<String, String> getLife2017Info() {
		Map<String, String> cust_info = new HashMap<String, String>();
		cust_info
				.put("life_seg",
						this.waitForElementToVisible(
								By.xpath(("//div[@id='displayLife2017Details_div']//input[@id='offerCoverId'][@readonly='readonly']")))
								.getAttribute("value"));
		List<WebElement> ref_num = driver
				.findElements(By
						.xpath("//div[@id='displayOfferLife2017_div']//input[@id='referenceNo'][@readonly='readonly']"));
		if (ref_num.size() != 0)
			cust_info.put("ref_num", ref_num.get(0).getAttribute("value"));
		List<WebElement> accountNo = driver
				.findElements(By
						.xpath("//div[@id='displayOfferLife2017_div']//input[@id='accountNo'][@readonly='readonly']"));
		if (accountNo.size() != 0)
			cust_info.put("account_id", accountNo.get(0).getAttribute("value"));
		else
			cust_info.put("account_id", "");
		List<WebElement> mobile_num = driver
				.findElements(By
						.xpath("//div[@id='displayOfferLife2017_div']//input[@id='msisdn'][@readonly='readonly']"));
		if (mobile_num.size() != 0)
			cust_info.put("msisdn", mobile_num.get(0).getAttribute("value"));

		List<WebElement> alt_msisdn = driver
				.findElements(By
						.xpath("//input[@id='life2017AlterContactNumber'][@readonly='readonly']"));
		if (alt_msisdn.size() != 0)
			cust_info
					.put("alt_msisdn", alt_msisdn.get(0).getAttribute("value"));
		else
			cust_info.put("alt_msisdn", "");
		return cust_info;

	}

	public Map<String, String> getPA2017Info() {
		Map<String, String> cust_info = new HashMap<String, String>();
		cust_info
				.put("pa_seg",
						this.waitForElementToVisible(
								By.xpath(("//div[@id='displayOfferPA2017_div']//input[@id='offerCoverId'][@readonly='readonly']")))
								.getAttribute("value"));
		List<WebElement> ref_num = driver
				.findElements(By
						.xpath("//div[@id='displayOfferPA2017_div']//input[@id='referenceNo'][@readonly='readonly']"));
		if (ref_num.size() != 0)
			cust_info.put("ref_num", ref_num.get(0).getAttribute("value"));
		List<WebElement> accountNo = driver
				.findElements(By
						.xpath("//div[@id='displayOfferPA2017_div']//input[@id='accountNo'][@readonly='readonly']"));
		if (accountNo.size() != 0)
			cust_info.put("account_id", accountNo.get(0).getAttribute("value"));
		else
			cust_info.put("account_id", "");
		List<WebElement> mobile_num = driver
				.findElements(By
						.xpath("//div[@id='displayOfferPA2017_div']//input[@id='msisdn'][@readonly='readonly']"));
		if (mobile_num.size() != 0)
			cust_info
					.put("mobile_num", mobile_num.get(0).getAttribute("value"));

		List<WebElement> alt_msisdn = driver
				.findElements(By
						.xpath("//input[@id='life2017AlterContactNumber'][@readonly='readonly']"));
		if (alt_msisdn.size() != 0)
			cust_info
					.put("alt_msisdn", alt_msisdn.get(0).getAttribute("value"));
		else
			cust_info.put("alt_msisdn", "");
		return cust_info;

	}

	public Map<String, String> getNomineeInfo() {
		Map<String, String> cust_info = new HashMap<String, String>();
		cust_info
				.put("nominee_name",
						this.waitForElementToVisible(
								By.xpath(("//input[@id='nomineeName'][@readonly='readonly']")))
								.getAttribute("value"));
		List<WebElement> nomineeMsisdn = driver.findElements(By
				.xpath("//input[@id='nomineeMsisdn'][@readonly='readonly']"));
		if (nomineeMsisdn.size() != 0)
			cust_info.put("nominee_msisdn",
					nomineeMsisdn.get(0).getAttribute("value"));
		else
			cust_info.put("nominee_msisdn", "");
		List<WebElement> relation = driver.findElements(By
				.xpath("//input[@id='insRelation'][@readonly='readonly']"));
		if (relation.size() != 0)
			cust_info.put("nominee_relationship",
					relation.get(0).getAttribute("value"));
		else
			cust_info.put("nominee_relationship", "");

		return cust_info;

	}
}
