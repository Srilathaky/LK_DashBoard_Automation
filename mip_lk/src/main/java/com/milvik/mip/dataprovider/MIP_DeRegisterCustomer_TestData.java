package com.milvik.mip.dataprovider;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_DeRegisterCustomer_TestData {

	/**
	 * This method will read the data from the given XL
	 * 
	 * @return
	 */
	@DataProvider(name = "deRegisterCustomerPositive")
	public String[][] deRegisterCustomerPositive() {
		return MIP_RegCust_TestData
				.storeCellData("MIP_Customer_DeRegistration_Positive_Scenario");
	}

	/* De-Register Existing customer */
	/* msisdn,successmessage,product_to_deregsiter */
	@DataProvider(name = "deRegisterExistingCustomer")
	public String[][] deRegisterExistingCustomer() {
		String[][] data = {
				{
						"0266666666",
						"Your request to de-register mobile number: XXXXX is successfully processed. Click here to go back.",
						"Income Protection" },
				{
						"0570010146",
						"Your request to de-register mobile number: XXXXX is successfully processed. Click here to go back.",
						"Hospitalization" },
				{
						"0570010152",
						"Your request to de-register mobile number: XXXXX is successfully processed. Click here to go back.",
						"Xtra-life" }, };
		return data;
	}

	public static String[][] storeCellData(String filename) {
		Sheet s = MIP_XLOperation.loadXL(filename);
		int numRows = MIP_XLOperation.getNumRows();
		int numcell = MIP_XLOperation.getNumCell();
		int rowcount = 0;
		List<String> DOB_col = new ArrayList<String>();
		for (int i = 0; i < numcell; i++) {

			if (s.getRow(0).getCell(i).getStringCellValue().toUpperCase()
					.contains("DOB")) {
				DOB_col.add(i + "");
			}
		}
		String[][] data = new String[numRows - 1][numcell];
		for (int i = 1; i < numRows; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < numcell; j++) {
				if (r.getCell(j) == null) {
					data[rowcount][j] = "";
				} else {
					try {
						data[rowcount][j] = r.getCell(j).getStringCellValue();
					} catch (Exception ex) {
						if (DOB_col.contains(j + "")) {
							DataFormatter df = new DataFormatter();
							data[rowcount][j] = df
									.formatCellValue(r.getCell(j));
						} else {
							data[rowcount][j] = new Double(r.getCell(j)
									.getNumericCellValue()).longValue() + "";
						}

					}
				}

			}
			rowcount++;
		}
		return data;
	}

}
