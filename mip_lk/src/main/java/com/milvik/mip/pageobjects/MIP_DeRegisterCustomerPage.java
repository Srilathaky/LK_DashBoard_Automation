package com.milvik.mip.pageobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_DeRegisterCustomerPage extends MIP_CustomerManagementPage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_DeRegisterCustomerPage");
	}

	public MIP_DeRegisterCustomerPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "msisdn")
	WebElement mobilenum;
	@FindBy(id = "search-icon")
	WebElement searchicon;

	public boolean validateDeRegisterBtn() {
		logger.info("Validating De-Register button");

		return this.waitForElementToVisible(By.id("deRegisterBtn"))
				.isDisplayed();
	}

/*	public String getConfirmationStatus() {
		logger.info("Confirming the pop-up");

		this.waitForElementToVisible(By.id("customerDetailsList"));
		return driver.findElement(
				By.xpath("//table[@id='customerDetailsList']/tbody/tr//td["
						+ getIndex("Confirmation Status") + "]")).getText();
	}

	public MIP_DeRegisterCustomerPage clickOnDeRegisterBtn() {
		logger.info("clicking on de-Register Button");
		this.waitForElementToVisible(By.id("saveBtn")).click();
		return this;
	}

	public MIP_DeRegisterCustomerPage selectProductToDeRegister(String product) {
		logger.info("selecting the product to de-register");
		product = product.toUpperCase();
		if (product.equalsIgnoreCase(MIP_CustomerManagementPage.XTRALIFE))
			this.waitForElementToVisible(
					By.xpath("//input[@id='productId'][@value='2']")).click();
		else if (product.equalsIgnoreCase(MIP_CustomerManagementPage.HOSPITAL))
			this.waitForElementToVisible(
					By.xpath("//input[@id='productId'][@value='3']")).click();
		else if (product.equalsIgnoreCase(MIP_CustomerManagementPage.IP)) {
			this.waitForElementToVisible(
					By.xpath("//input[@id='productId'][@value='4']")).click();
		} else if (product.trim().contains(
				MIP_CustomerManagementPage.XTRALIFE.toUpperCase())
				&& product.trim().contains(
						MIP_CustomerManagementPage.HOSPITAL.toUpperCase())) {
			this.waitForElementToVisible(
					By.xpath("//input[@id='productId'][@value='2']")).click();
			this.waitForElementToVisible(
					By.xpath("//input[@id='productId'][@value='3']")).click();
		} else if (product.trim().contains(
				MIP_CustomerManagementPage.XTRALIFE.toUpperCase())
				&& product.trim().contains(
						MIP_CustomerManagementPage.IP.toUpperCase())) {
			this.waitForElementToVisible(
					By.xpath("//input[@id='productId'][@value='2']")).click();
			this.waitForElementToVisible(
					By.xpath("//input[@id='productId'][@value='4']")).click();
		}
		return this;
	}

	public List<String> getCustomerProducts() {
		String[] prosuct_list = new String[3];
		this.waitForElementToVisible(By.id("productId"));
		List<WebElement> products = driver.findElements(By.id("div_id_2"));
		List<String> product_name = new ArrayList<String>();
		for (WebElement e : products) {
			prosuct_list = e.getText().trim().split("\\s");
			for (int i = 0; i < prosuct_list.length; i++) {
				if (!prosuct_list[i].trim().equals(""))
					if (prosuct_list[i].trim().equalsIgnoreCase("Income")) {
						product_name.add(prosuct_list[i] + " "
								+ prosuct_list[i + 1]);
						i = i + 1;
					} else
						product_name.add(prosuct_list[i]);
			}

		}
		return product_name;
	}

	public boolean validateTableHeading() {
		try {
			this.waitForElementToVisible(By.id("customerDetailsList"));
		} catch (TimeoutException e) {
			this.clickOnSearchButton();
		}
		logger.info("validating de-register cutomer table");
		if (this.waitForElementToVisible(
				By.xpath("//table[@id='customerDetailsList']/thead/tr/th[contains(text(),'Customer Name')]"))
				.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr/th[contains(text(),'Mobile Number')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr/th[contains(text(),'Product')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr/th[contains(text(),'Confirmation Status')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr/th[contains(text(),'Deducted Amount(as on Date)')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr/th[contains(text(),'Cover Earned in the current month')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDetailsList']/thead/tr/th[contains(text(),'Previous Month Usage')]"))
						.isDisplayed()) {
			return true;
		}
		return false;
	}

	public int getProductCount() {
		this.waitForElementToVisible(By.id("customerDetailsList"));
		return driver.findElements(
				By.xpath("//table[@id='customerDetailsList']/tbody//tr"))
				.size();
	}

	public int getIndex(String heading) {
		this.waitForElementToVisible(By.id("customerDetailsList"));
		List<WebElement> details = driver.findElements(By
				.xpath("//table[@id='customerDetailsList']/thead//tr//th"));
		for (int i = 0; i < details.size(); i++) {
			if (details.get(i).getText().equalsIgnoreCase(heading)) {
				return i + 1;
			}
		}
		return 0;
	}

	public Map<String, String> getCustomerInformation(String product_name) {
		Map<String, String> cust_details = new HashMap<String, String>();
		this.waitForElementToVisible(By.id("customerDetailsList"));
		int index = getIndex("Customer Name") + 1;
		int product_index = getIndex("Product");
		cust_details
				.put("customer_name",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//preceding-sibling::td["
										+ index
										+ "]")).getText());

		cust_details
				.put("confirmed_stats",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//following-sibling::td["
										+ (getIndex("Confirmation Status") - product_index)
										+ "]")).getText());
		cust_details
				.put("deducted_offer_amount",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//following-sibling::td["
										+ (getIndex("Deducted Amount(as on Date)") - product_index)
										+ "]")).getText());
		cust_details
				.put("earned_cover",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//following-sibling::td["
										+ (getIndex("Cover Earned in the current month") - product_index)
										+ "]")).getText());
		cust_details
				.put("prev_month_usage",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//following-sibling::td["
										+ (getIndex("Previous Month Usage") - product_index)
										+ "]")).getText());
		return cust_details;
	}

	public String getSuccessMessage() {
		try {

			String text = this.waitForElementToVisible(
					By.xpath("//div[@class='error-div-body']//div[2]"))
					.getText();
			return text;
		} catch (Exception e) {
			logger.info("Error while getting success message");
		}
		return "";
	}

	public MIP_DeRegisterCustomerPage clickOnHereLink() {
		this.waitForElementToVisible(By.linkText("here")).click();
		return this;
	}

	public boolean validateTableHeadingAfterDeRegister() {
		try {
			this.waitForElementToVisible(By.id("customerDeregDetailsList"));
		} catch (TimeoutException e) {
			this.clickOnSearchButton();
		}
		logger.info("validating de-register cutomer table after deregistration");
		if (this.waitForElementToVisible(
				By.xpath("//table[@id='customerDeregDetailsList']/thead/tr/th[contains(text(),'Customer Name')]"))
				.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDeregDetailsList']/thead/tr/th[contains(text(),'Mobile Number')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDeregDetailsList']/thead/tr/th[contains(text(),'Product')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDeregDetailsList']/thead/tr/th[contains(text(),'Deducted Amount')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDeregDetailsList']/thead/tr/th[contains(text(),'Cover Earned')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDeregDetailsList']/thead/tr/th[contains(text(),'Previous Month Usage')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDeregDetailsList']/thead/tr/th[contains(text(),'De-registered by')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDeregDetailsList']/thead/tr/th[contains(text(),'De-registered Date')]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//table[@id='customerDeregDetailsList']/thead/tr/th[contains(text(),'Date of customer removal ')]"))
						.isDisplayed()) {
			return true;
		}
		return false;
	}

	public int getIndexInDeRegisterTable(String heading) {
		this.waitForElementToVisible(By.id("customerDeregDetailsList"));
		List<WebElement> details = driver
				.findElements(By
						.xpath("//table[@id='customerDeregDetailsList']/thead//tr//th"));
		for (int i = 0; i < details.size(); i++) {
			if (details.get(i).getText().equalsIgnoreCase(heading)) {
				return i + 1;
			}
		}
		return 0;
	}

	public Map<String, String> getCustomerInformationAfterDeRegistration(
			String product_name) {
		Map<String, String> cust_details = new HashMap<String, String>();
		this.waitForElementToVisible(By.id("customerDeregDetailsList"));
		int index = getIndexInDeRegisterTable("Customer Name") + 1;
		int product_index = getIndexInDeRegisterTable("Product");
		if (product_name.equalsIgnoreCase(MIP_CustomerManagementPage.XTRALIFE)) {
			product_name = MIP_CustomerManagementPage.XTRALIFE;
		}
		cust_details
				.put("customer_name",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDeregDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//preceding-sibling::td["
										+ index
										+ "]")).getText());

		cust_details
				.put("Deducted Amount",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDeregDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//following-sibling::td["
										+ (getIndexInDeRegisterTable("Deducted Amount") - product_index)
										+ "]")).getText());
		cust_details
				.put("Cover Earned",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDeregDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//following-sibling::td["
										+ (getIndexInDeRegisterTable("Cover Earned") - product_index)
										+ "]")).getText());
		cust_details
				.put("Previous Month Usage",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDeregDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//following-sibling::td["
										+ (getIndexInDeRegisterTable("Previous Month Usage") - product_index)
										+ "]")).getText());
		cust_details
				.put("De-registered by",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDeregDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//following-sibling::td["
										+ (getIndexInDeRegisterTable("De-registered by") - product_index)
										+ "]")).getText());
		cust_details
				.put("De-registered Date",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDeregDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//following-sibling::td["
										+ (getIndexInDeRegisterTable("De-registered Date") - product_index)
										+ "]")).getText());
		cust_details
				.put("Date of customer removal ",
						this.waitForElementToVisible(
								By.xpath("//table[@id='customerDeregDetailsList']/tbody//tr/td[contains(text(),'"
										+ product_name
										+ "')]//following-sibling::td["
										+ (getIndexInDeRegisterTable("Date of customer removal") - product_index)
										+ "]")).getText());
		return cust_details;
	}
*/
}
