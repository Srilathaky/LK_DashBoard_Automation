package com.milvik.mip.dataprovider;

import org.testng.annotations.DataProvider;

public class MIP_OfferManagement_TestData {
	/**
	 * Data in the order msisdn,error message
	 * 
	 * @return
	 */
	@DataProvider(name = "offerName")
	public static String[][] offerName() {
		String[][] data = { { "Dialog Accident Protect" },
				{ "Dialog Hospitalization" }, { "PA 2017" }, { "Life 2017" },
				{ "New Dialog Accident Protect" }, { "Home Protection" } };
		return data;
	}

}
