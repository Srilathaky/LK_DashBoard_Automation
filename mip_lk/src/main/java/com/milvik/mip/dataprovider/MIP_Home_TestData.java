package com.milvik.mip.dataprovider;

import org.testng.annotations.DataProvider;

public class MIP_Home_TestData {

	/**
	 * Dashboard Menu
	 * 
	 * @return
	 */
	@DataProvider(name = "homeobjects")
	public static String[][] homepageObjetcts() {
		String[][] data = { { "Home", "Branch Management", "User Management",
				"Leave Management", "Customer Management", "SMS Management",
				"Role Management", "Report Management", "Admin Configuration",
				"Offer Management", "Change Password", "Logout",
				"Announcements", "Customer Statistics", "User Statistics" } };

		return data;
	}

	/**
	 * Customer Statistics information
	 * 
	 * @return
	 */
	@DataProvider(name = "CustStatics")
	public static String[][] CustomerStatistcs() {
		String[][] data = { { "Total Confirmed Life Insurance Customers :",
				"Total Unconfirmed Life Insurance Customers :",
				"Total Self-Registered Life Insurance Customers  :",
				"Total Life Insurance Customers Registered by Agent  :",
				"Total Life Insurance Customers Confirmed by Agent  :" } };

		return data;
	}

	/**
	 * User Name,Password,Customer Statistics information
	 * 
	 * @return
	 */
	@DataProvider(name = "CustStaticsDiffRole")
	public static String[][] CustomerStatistcsForDiffRole() {
		String[][] data = { { "TA11", "Tigo123!",
				"Confirmed Life Insurance registrations by you this month :",
				"Pending Life Insurance registrations by you this month :" } };

		return data;
	}

	/**
	 * User NAme,Password
	 * 
	 * @return
	 */
	@DataProvider(name = "UserLogin")
	public static String[][] UserLoginDetails() {
		String[][] data = { { "DA1486", "Dialog123@" } };

		return data;
	}
}
