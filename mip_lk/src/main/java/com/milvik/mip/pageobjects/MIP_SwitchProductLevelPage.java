package com.milvik.mip.pageobjects;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_SwitchProductLevelPage extends MIP_CustomerManagementPage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_SwitchProductLevelPage");
	}

	public MIP_SwitchProductLevelPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public boolean ValidateSwitchProductLevel() {
		boolean value1 = this.waitForElementToVisible(By.id("offerId"))
				.isDisplayed();
		boolean value2 = driver.findElement(By.id("offerId"))
				.getAttribute("disabled").equalsIgnoreCase("true");
		boolean value3 = driver.findElement(By.id("oldOfferCoverId"))
				.getAttribute("readonly").equalsIgnoreCase("true");
		boolean value4 = driver.findElement(By.id("newOfferCoverIdLife"))
				.getTagName().equalsIgnoreCase("select");
		boolean value5 = driver.findElement(By.id("saveBtn")).isDisplayed();
		boolean value6 = driver.findElement(By.id("backBtn")).isDisplayed();
		if (value1 && value2 && value3 && value4 && value5 && value6)
			return true;
		else
			return false;
	}

	public String getCurrentCoverLevel() {
		return this.waitForElementToVisible(By.id("oldOfferCoverId"))
				.getAttribute("value");
	}

	public List<WebElement> getLifeBenefitLevelDropDownValue() {
		WebElement ele = this.waitForElementToVisible(By
				.id("newOfferCoverIdLife"));
		Select s = new Select(ele);
		return s.getOptions();

	}

	public MIP_SwitchProductLevelPage switchProductLevel(String benefitLevel) {
		this.selectDropDownbyText(
				this.waitForElementToVisible(By.id("newOfferCoverIdLife")),
				benefitLevel);
		return this;

	}

	public MIP_SwitchProductLevelPage clickOnSaveChanges() {
		this.waitForElementToVisible(By.id("saveBtn")).click();
		return this;
	}

	public MIP_SwitchProductLevelPage clickOnBackButton() {
		this.waitForElementToVisible(By.id("backBtn")).click();
		return this;
	}

	public String getSuccessMessage() {
		return this.waitForElementToVisible(
				By.xpath("//div[@class='error-div-body']/div[2]")).getText();
	}
}
