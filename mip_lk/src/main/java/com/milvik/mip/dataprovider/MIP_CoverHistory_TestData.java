package com.milvik.mip.dataprovider;

import org.testng.annotations.DataProvider;

public class MIP_CoverHistory_TestData {
	@DataProvider(name = "coverHistoryPositive")
	public String[][] deRegisterCustomerPositive() {
		return MIP_RegCust_TestData
				.storeCellData("MIP_Customer_CoverHistory_Positive_Scenario");
	}
}
