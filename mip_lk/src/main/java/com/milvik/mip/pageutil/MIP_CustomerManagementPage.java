package com.milvik.mip.pageutil;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_CustomerManagementPage extends MIP_BasePage {
	public static final String GSM = "GSM";
	public static final String CDMA = "CDMA";
	public static final String LTE = "LTE";
	public static final String DTV = "DTV";
	public static final String HOSPITALIZATION = "Dialog Hospitalization";
	public static final String LIFE_2017 = "Life 2017";
	public static final String PA_2017 = "PA 2017";
	public static final String HMP = "Home Protection";

	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_CustomerManagementPage");
	}

	public MIP_CustomerManagementPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(id = "msisdn")
	public WebElement mobileNum;
	@FindBy(id = "search-icon")
	public WebElement searchIcon;

	// ///////////////////////////////////////////////////////////////////
	public void enterNic(String nic) {
		logger.info("Entering NIC value " + nic);
		for (int i = 0; i < 5; i++) {
			WebElement ele = this.waitForElementToVisible(By.id("nic"));
			ele.sendKeys(nic);
			if (ele.getAttribute("value").equals(nic.trim()))
				break;
		}
		logger.info("Entered NIC value");
	}

	public void selectSubscriberChannelType(String channelType) {
		logger.info("Selecting subscriber channel type " + channelType);
		if (channelType.trim().equalsIgnoreCase(MIP_CustomerManagementPage.GSM))
			this.waitForElementToVisible(
					By.xpath("//input[@id='subscriberChannelId'][@value='1']"))
					.click();
		else if (channelType.trim().equalsIgnoreCase(
				MIP_CustomerManagementPage.CDMA))
			this.waitForElementToVisible(
					By.xpath("//input[@id='subscriberChannelId'][@value='2']"))
					.click();
		else if (channelType.trim().equalsIgnoreCase(
				MIP_CustomerManagementPage.LTE))
			this.waitForElementToVisible(
					By.xpath("//input[@id='subscriberChannelId'][@value='3']"))
					.click();
		else if (channelType.trim().equalsIgnoreCase(
				MIP_CustomerManagementPage.DTV))
			this.waitForElementToVisible(
					By.xpath("//input[@id='subscriberChannelId'][@value='4']"))
					.click();
		logger.info("Selected subscriber channel type");
	}

	public void clickOnSeachButton() {
		logger.info("clicking on search button");
		this.waitForElementToVisible(By.id("searchBtn")).click();
		logger.info("Clicked on Search button");
	}

	public boolean validateNicField() {
		logger.info("validating Nic Field");
		if (this.waitForElementToVisible(By.id("label_nic")).isDisplayed()
				&& this.waitForElementToVisible(By.id("nic")).isDisplayed()) {
			return true;
		}
		return false;
	}

	public boolean validatingChannelType() {
		logger.info("validating channel Type Field");
		if (this.waitForElementToVisible(
				By.xpath("//*[contains(text(),'GSM')]//preceding-sibling::input[1]"))
				.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//*[contains(text(),'CDMA')]//preceding-sibling::input[1]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//*[contains(text(),'LTE')]//preceding-sibling::input[1]"))
						.isDisplayed()
				&& this.waitForElementToVisible(
						By.xpath("//*[contains(text(),'DTV')]//preceding-sibling::input[1]"))
						.isDisplayed()) {
			return true;
		}
		return false;
	}

	public boolean validateSeachButton() {
		logger.info("validate search button");
		try {
			if (this.waitForElementToVisible(By.id("searchBtn")).isDisplayed())
				return true;

		} catch (Exception e) {
			logger.info("Search button is not displayed");
			return false;
		}
		return false;

	}

	public boolean validateBackBtn() {
		return this.waitForElementToVisible(By.id("backBtn")).isDisplayed();
	}

	public void clickOnBackBtn() {
		this.waitForElementToVisible(By.id("backBtn")).click();
	}

	public void enterMsisdn(String msisdn) {
		this.mobileNum.sendKeys(msisdn);
	}

	// ///////////////////////////////////////////////////
	public String getValidationMessage() {

		return this.waitForElementToVisible(By.id("validationMessages"))
				.getText();
	}

	public void confirmCustManagementPopup(String option) {
		this.confirmPopUp(option);
	}

	public String getSuccessMessage() {
		WebDriverWait w = new WebDriverWait(driver, 50);
		return w.until(
				ExpectedConditions.visibilityOfElementLocated(By
						.id("message_div"))).getText();
	}

}
