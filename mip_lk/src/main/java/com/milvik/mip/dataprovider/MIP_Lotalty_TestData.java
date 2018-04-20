package com.milvik.mip.dataprovider;

import org.testng.annotations.DataProvider;

public class MIP_Lotalty_TestData {
	/**
	 * Data in the order invalid msisdns and validation messages
	 * 
	 * @return
	 */
	@DataProvider(name = "msisdnValidation")
	public static String[][] validateMsisdn() {
		String[][] data = { { "dgfg", "gd575", "123456", "1234567894",
				"0575564546", "0261212121",
				"Mobile Number : Enter a valid number.",
				"This user is not eligible for loyalty package",
				"Mobile Number : Field should not be empty." } };
		return data;
	}

	/**
	 * Data in the order invalid msisdns
	 * 
	 * @return
	 */
	@DataProvider(name = "loyaltymsisdn")
	public static String[][] loyaltyCheck() {
		String[][] data = { { "0555464654" }, { "0578951212" },
				{ "0555464654" } };
		return data;
	}

	/**
	 * Data in the order invalid msisdn,products,package,product to
	 * select,success message,error messages
	 * (loyalty customer record  should be there in loyalty_customers table.)
	 * @return
	 */
	@DataProvider(name = "creditloyalty")
	public static String[][] creditLoyalty() {
		String sms_text = "Dear AAAAA, thanks for your loyalty! You received 100min and BBBBB free. Tigo Insurance supports you in times of need. Call 550 for Queries.";

		String[][] data = {
				{
						"0277186680",
						"Xtra-Life",
						"Data",
						"",
						"",
						"Loyalty package applied successfully for XXXXX product",
						"Xtra-Life", "Products : Please select an option.",
						"Package : Please choose a value from the Drop down.",
						sms_text },
				{
						"0554554544",
						"Xtra-Life",
						"Data",
						"Hospitalization",
						"SMS",
						"Loyalty package applied successfully for XXXXX product",
						"Hospitalization",
						"Products : Please select an option.",
						"Package : Please choose a value from the Drop down.",
						sms_text } };
		return data;
	}
}
