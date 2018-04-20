package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;
import com.milvik.mip.utility.MIP_XLOperation;

public class MIP_AdminConfiguration_TestData {
	/**
	 * data in the order default password,login id prefix,max pwd history,max
	 * login attem,idle state,Offer Subscription Last Day,Commission
	 * Percentage,announcement,exchange rate
	 */
	@DataProvider(name = "adminconfigdata")
	public String[][] editadminconfig() {
		String[][] data = { {
				"Dialog123!",// Tigo123!
				"DA",
				"5",
				"3",
				"4",
				"For \"October\" 16th to November 15th, the target registration for Mobile and CSC is  460 and 625 for Call Centre Agents. Targets were based on 23 and 25 working days respectively.",
				"The Admin Configuration details have been saved successfully. Click here to go back" } };
		return data;
	}

	/**
	 * This method will read the data from the given XL
	 * 
	 * @return
	 */
	@DataProvider(name = "adminconfignegativecases")
	public static String[][] negativescenario() {
		Sheet s = MIP_XLOperation.loadXL("MIP_AdminConfig");
		int rowcount = MIP_XLOperation.getNumRows();
		int cellcount = MIP_XLOperation.getNumCell();

		String[][] data = new String[rowcount - 1][cellcount];
		int count = 0;
		for (int i = 1; i < rowcount; i++) {
			Row r = s.getRow(i);
			for (int j = 0; j < r.getLastCellNum(); j++) {
				if (r.getCell(j) == null) {
					data[count][j] = "";
				} else {
					try {
						data[count][j] = r.getCell(j).getStringCellValue();
					} catch (Exception e) {
						data[count][j] = r.getCell(j).getNumericCellValue()
								+ "";
					}

				}

			}
			count++;
		}
		return data;

	}
}
