package com.milvik.mip.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_CoverHistoryPage extends MIP_CustomerManagementPage {
	WebDriver driver;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_CoverHistoryPage");
	}

	public MIP_CoverHistoryPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public String getResultText() {
		return this.waitForElementToVisible(By.id("message_div")).getText();
	}

	public boolean validateTableHeadingXtraLife() {
		logger.info("Validating Cover History TableHeading for Xtra-Life");
		this.waitForElementToVisible(By.id("VO_CUSTOMER"));
		String xpath = "//table[@id='VO_CUSTOMER']/thead/tr//th[contains(text(),";
		if ((driver.findElement(By.xpath(xpath + "'Month')]")).isDisplayed())
				&& (driver.findElement(By.xpath(xpath + "'Cover')]"))
						.isDisplayed())
				&& (driver.findElement(By.xpath(xpath
						+ "'Prior Month Charges')]")).isDisplayed())
				&& (driver.findElement(By.xpath(xpath
						+ "'Prior Month Airtime')]")).isDisplayed())) {
			return true;
		}
		return false;
	}

	public List<String> getCoverHistoryDetailsXtraLife() {
		logger.info("Getting Cover History details for Xtra-Life");

		List<String> result = new ArrayList<String>();
		this.waitForElementToVisible(By.id("VO_CUSTOMER"));
		List<WebElement> coverele = driver.findElements(By
				.xpath("//table[@id='VO_CUSTOMER']/tbody//tr//td"));
		for (int i = 0; i < coverele.size(); i++) {
			result.add(coverele.get(i).getText());
		}
		return result;
	}

	public boolean validateTableHeadingHP() {
		logger.info("Validating Cover History TableHeading for HP");
		this.waitForElementToVisible(By.id("VO_CUSTOMER_HP"));
		String xpath = "//table[@id='VO_CUSTOMER_HP']/thead/tr//th[contains(text(),";
		if ((driver.findElement(By.xpath(xpath + "'Month')]")).isDisplayed())
				&& (driver.findElement(By.xpath(xpath
						+ "'Cover per Night in Hospital')]")).isDisplayed())
				&& (driver.findElement(By.xpath(xpath
						+ "'Prior Month Charges')]")).isDisplayed())) {
			return true;
		}
		return false;
	}

	public List<String> getCoverHistoryDetailsHP() {
		logger.info("Getting Cover History details for HP");

		List<String> result = new ArrayList<String>();
		this.waitForElementToVisible(By.id("VO_CUSTOMER_HP"));
		List<WebElement> coverele = driver.findElements(By
				.xpath("//table[@id='VO_CUSTOMER_HP']/tbody//tr//td"));
		for (int i = 0; i < coverele.size(); i++) {
			result.add(coverele.get(i).getText());
		}
		return result;
	}

	public boolean validateTableHeadingIP() {
		logger.info("Validating Cover History TableHeading for IP");
		this.waitForElementToVisible(By.id("VO_CUSTOMER_IP"));
		String xpath = "//table[@id='VO_CUSTOMER_IP']/thead/tr//th[contains(text(),";
		if ((driver.findElement(By.xpath(xpath + "'Month')]")).isDisplayed())
				&& (driver.findElement(By.xpath(xpath + "'Cover')]"))
						.isDisplayed())
				&& (driver.findElement(By.xpath(xpath
						+ "'Prior Month Charges')]")).isDisplayed())) {
			return true;
		}
		return false;
	}

	public List<String> getCoverHistoryDetailsIP() {
		logger.info("Getting Cover History details for IP");

		List<String> result = new ArrayList<String>();
		this.waitForElementToVisible(By.id("VO_CUSTOMER_IP"));
		List<WebElement> coverele = driver.findElements(By
				.xpath("//table[@id='VO_CUSTOMER_IP']/tbody//tr//td"));
		for (int i = 0; i < coverele.size(); i++) {
			result.add(coverele.get(i).getText());
		}
		return result;
	}

	public boolean validateEarlierMonthButton() {
		return this.waitForElementToVisible(By.id("moreBtn")).isDisplayed();
	}
}
