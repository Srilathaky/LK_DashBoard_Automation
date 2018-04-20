package com.milvik.mip.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_LoyaltyPage extends MIP_CustomerManagementPage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_LoyaltyPage");
	}

	public MIP_LoyaltyPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public String getErrorMessage() {
		return this.waitForElementToVisible(By.id("message_div")).getText();
	}

	public List<String> getLoyaltyInformation() {
		List<String> details = new ArrayList<String>();
		this.waitForElementToVisible(By.id("div_id_2"));
		List<WebElement> XL_package = driver.findElements(By.id("pakageXL"));
		for (WebElement ele : XL_package) {
			if (ele.isDisplayed()) {
				Select s = new Select(ele);
				details.add(s.getFirstSelectedOption().getText());
			}
		}
		List<WebElement> HP_package = driver.findElements(By.id("pakageHP"));
		for (WebElement ele : HP_package) {
			if (ele.isDisplayed()) {
				Select s = new Select(ele);
				details.add(s.getFirstSelectedOption().getText());
			}
		}
		List<WebElement> cust_detalis = driver.findElements(By
				.xpath("//table[@id='loyalCustomers']/tbody//tr//td"));
		for (int i = 0; i < cust_detalis.size(); i++) {
			if (i == 3 || i == 10) {
				details.add(cust_detalis.get(i).getText()
						.replaceAll("\\.0", ""));
			} else {
				details.add(cust_detalis.get(i).getText());
			}

		}
		return details;
	}

	public MIP_LoyaltyPage clickOnCreditLoyaltyPackage() {
		this.waitForElementToVisible(By.id("saveCreditBtn")).click();
		return this;
	}

	public MIP_LoyaltyPage clickOnXtraLife() {
		WebElement ele = this.waitForElementToVisible(By
				.xpath("//div[@id='div_id_2']/input[@value='2']"));
		if (!ele.isSelected()) {
			ele.click();
		}
		return this;
	}

	public MIP_LoyaltyPage clickOnHospitalization() {
		WebElement ele = this.waitForElementToVisible(By
				.xpath("//div[@id='div_id_2']/input[@value='3']"));
		if (!ele.isSelected()) {
			ele.click();
		}
		return this;
	}

	public MIP_LoyaltyPage selectXLPackage(String pack) {
		WebElement ele = this.waitForElementToVisible(By.id("pakageXL"));
		this.selectDropDownbyText(ele, pack);
		return this;

	}

	public MIP_LoyaltyPage selectHPPackage(String pack) {
		WebElement ele = this.waitForElementToVisible(By.id("pakageHP"));
		this.selectDropDownbyText(ele, pack);
		return this;

	}

}
