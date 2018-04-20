package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_SearchCustomer_TestData {
	/**
	 * This method will read the data from specified XL
	 * 
	 * @return
	 */
	@DataProvider(name = "SearchCustomerNegative")
	public static String[][] searchCustomerNegative() {
		Sheet s = MIP_XLOperation
				.loadXL("MIP_Search_Customer_Negative_Scenarios");
		int num_row = MIP_XLOperation.getNumRows();
		int num_cell = MIP_XLOperation.getNumCell();
		int rowcount = 0;
		String[][] data = new String[num_row - 1][num_cell];
		for (int i = 1; i < num_row; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < r.getLastCellNum(); j++) {
				if (r.getCell(j) == null) {
					data[rowcount][j] = "";
				} else {
					try {
						data[rowcount][j] = r.getCell(j).getStringCellValue();
					} catch (Exception e) {
						data[rowcount][j] = new Double(r.getCell(j)
								.getNumericCellValue()).longValue() + "";
					}
				}
			}
			rowcount++;
		}
		return data;
	}

	/**
	 * testcase name,name,mobile number,NIC,Reference number,channel
	 * 
	 * @return
	 */
	@DataProvider(name = "searchCriteria")
	public static String[][] searchData() {
		String[][] data = {
				{ "Search By Name", "test", "", "", "", "GSM" },
				{ "Search By Mobile Number", "", "987654678", "", "", "" },
				{ "Search By NIC", "", "", "197678900802", "", "" },
				{ "Search By Reference Number", "", "", "", "JBQ209", "" },
				{ "Search By All", "ashish", "912121212", "197578900678", "",
						"GSM" } };
		return data;
	}

	@DataProvider(name = "searchAndValidateCust")
	public static String[][] searchAndValidateCust() {
		String[][] data = {
				{ "Search By All", "latestcustomer", "773659661",
						"197678900802", "", "GSM" },
				{ "Search By All", "cdma", "773659661", "197678900802", "",
						"CDMA" },
				{ "Search By All", "ltenic", "773659661", "197678900802", "",
						"LTE" },
				{ "Search By All", "dsdsds", "773659661", "197678900802", "",
						"DTV" } };
		return data;
	}

	/**
	 * msisdn,product,fname,sname,DOB,Gender,Insured Relative
	 * Information:(Relationship,Fname,sname,DOB,inform beneficiary,mobile
	 * number,deduction mode.)
	 */
	@DataProvider(name = "registerAndSearch")
	public static String[][] registerAndSearchCust() {
		String[][] data = { { "0277778885", "Xtra-Life", "search", "one",
				"19/05/1986", "Male", "Father", "Relative", "one",
				"10/10/1984", "Yes", "0278545852", "Daily Deduction" } };
		return data;
	}

}
