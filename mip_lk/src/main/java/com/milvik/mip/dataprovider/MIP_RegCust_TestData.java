package com.milvik.mip.dataprovider;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

import com.milvik.mip.utility.MIP_XLOperation;

/**
 * @author srilatha_yajnanaraya
 *
 */
public class MIP_RegCust_TestData {
	// confirmation status just after registration before confirmation.
	public static final String GSM_IS_CONFIRMED_AFTER_REGISTRATION = "0";
	public static final String NON_GSM_IS_CONFIRMED_AFTER_REGISTRATION = "1";
	// product attached status just after registration
	public static final String PRODUCT_ATTACHED_STATUS_AFTER_REGISTRATION = "0";
	public static final String SUBSCRIPTION_STATUS_AFTER_REGISTRATION = "ACTIVATED";
	public static final String DEFAULT_OFFER_LEVEL_PREPAID_GSM = "1000.0000";

	@DataProvider(name = "RegisterCustomer-GSM-Prepaid")
	public String[][] registerCustomer_GSM_Prepaid() {
		return storeCellData("Customer_management\\GSM_Prepaid\\MIP_Register_GSM_PrePaid");
	}

	@DataProvider(name = "RegisterCustomer-Postpaid")
	public String[][] registerCustomer_Postpaid() {
		return storeCellData("Customer_management\\PostPaid\\MIP_Register_PostPaid");
	}

	@DataProvider(name = "register_different_product_existing_customer_GSM_Prepaid")
	public String[][] register_different_product_existing_customer_GSM_Prepaid() {
		return storeCellData("Customer_management\\GSM_Prepaid\\MIP_UpdateExisting_GSM_PrePaid");
	}

	/**
	 * testcase name,existing nic,nic to update,name to update,language to
	 * update,nominee name to update,nominee contact num to update,nominee
	 * relationship to update.pa contact num,success_mag
	 * 
	 * @return
	 */

	@DataProvider(name = "updateExistingCustomer-GSM-Prepaid")
	public String[][] updateExistingCustomer_GSM_Prepaid() {
		String[][] data = { { "update cust_info_TC01", "197878900426",
				"197878900427", "test update", "English", "nominee nam",
				"475890322", "Children",
				"The customer details have been saved successfully." } };
		return data;
	}

	/**
	 * testcase name,nic,channel,cust_name,
	 * 
	 * @return
	 */
	@DataProvider(name = "update_hmp_life_pa_info-postpaid")
	public String[][] update_hmp_life_pa_info() {
		return storeCellData("Customer_management\\PostPaid\\MIP_Register_HmpLifePa_Update_HmpLifePa_PostPaid");
	}

	/**
	 * testname,Nic(provide nic which is registered for all the product),channel
	 * 
	 * 
	 */

	@DataProvider(name = "ReadOnly_Test_after_Registration")
	public String[][] readOnly_After_POstpaid_Registration() {
		String[][] data = { { "ReadOnly_Test_LTE", "197880555586", "CDMA" } };
		return data;
	}

	/**
	 * error message,
	 * 
	 * @return
	 */
	@DataProvider(name = "register_cust_GSM_Prepaid_negative_scenarios")
	public String[][] register_cust_GSM_Prepaid_negative_scenarios() {
		return storeCellData("Customer_management\\GSM_Prepaid\\MIP_Register_GSM_PrePaid_Negative_scenarios");
	}

	@DataProvider(name = "RegisterDifferent_ExistingCustomer-Postpaid")
	public String[][] registerDifferent_ExistingCustomer_Postpaid_scenarios() {
		return storeCellData("Customer_management\\PostPaid\\MIP_RegisterDifferentProduct_ExistingCustomer_PostPaid");
	}

	/**
	 * Pass invalid data data to all the fields. testcase name,channel, invalid
	 * data,error message
	 * 
	 * @return
	 */
	@DataProvider(name = "Field_Validation_With_NegativeData-Postpaid")
	public String[][] fieldValidationNegativeScenarios() {
		String[][] data = { {
				"Field Validation Scenario",
				"197880550586",
				"CDMA",
				"GFD%^$^D",
				"Customer Name  :  Names can contain only alphabets, one single quote and space."
						+ "Spouse Name  :  Names can contain only alphabets, one single quote and space."
						+ "Third Child Name  :  Names can contain only alphabets, one single quote and space."
						+ "Second Child Name  :  Names can contain only alphabets, one single quote and space."
						+ "First Child Name  :  Names can contain only alphabets, one single quote and space."
						+ "Nominee Name  :  Names can contain only alphabets, one single quote and space."
						+ "Contact Number  :  Mobile number can only contain numbers and the length must be of 9 digits."
						+ "Account ID  :  Please provide numeric value only."
						+ "Postal Code  :  Please provide numeric value only."
						+ "Reference Number  :  Please enter only alphanumeric character."
						+ "Alternative Contact Number  :  Please provide numeric value only."
						+ "Reference Number  :  Please enter only alphanumeric character."
						+ "Alternative Contact Number  :  Please provide numeric value only."
						+ "Reference Number  :  Please enter only alphanumeric character."
						+ "Alternative Contact Number  :  Please provide numeric value only."
						+ "Reference Number  :  Please enter only alphanumeric character."
						+ "Alternative Contact Number  :  Please provide numeric value only." } };
		return data;
	}

	/**
	 * @return
	 */
	@DataProvider(name = "duplicateAccountIdValidation-Postpaid")
	public String[][] duplicateAccountIdValidation_Postpaid() {
		String[][] data = { {
				"Duplicate Account ID",
				"197880550586",
				"CDMA",
				"2133233545",
				"Account ID  :  Entered Account ID value already exists. Please enter a new value" } };
		return data;
	}

	@DataProvider(name = "mandatoryFieldValidationin_Postpaid_Registration")
	public String[][] mandatoryFieldValidationin_Postpaid_Registration() {
		String cdma_lte_errmsg = " Customer Name  :  Field should not be empty."
				+ "Preferred Language  :  Please choose a value from the Drop down."
				+ "Nominee Name  :  Field should not be empty."
				+ "Relationship with Customer : Please choose a value from the Drop down."
				+ "Account ID  :  Field should not be empty."
				+ "Address Line 1  :  Field should not be empty."
				+ "Address Line 2  :  Field should not be empty."
				+ "Postal Code  :  Field should not be empty."
				+ "Reference Number  :  Field should not be empty."
				+ "Reference Number  :  Field should not be empty."
				+ "Reference Number  :  Field should not be empty."
				+ "Reference Number  :  Field should not be empty.";
		String[][] data = {
				{
						"Mandatory Field Validation_GSM",
						"197880550586",
						"GSM",
						" Customer Name  :  Field should not be empty."
								+ "Preferred Language  :  Please choose a value from the Drop down."
								+ "Nominee Name  :  Field should not be empty."
								+ "Relationship with Customer : Please choose a value from the Drop down."
								+ "Mobile Number  :  Field should not be empty." },
				{ "Mandatory Field Validation_CDMA", "197880550586", "CDMA",
						cdma_lte_errmsg },
				{ "Mandatory Field Validation_LTE", "197880550586", "LTE",
						cdma_lte_errmsg },
				{
						"Mandatory Field Validation_DTV",
						"197880550586",
						"DTV",
						"Customer Name  :  Field should not be empty."
								+ "Preferred Language  :  Please choose a value from the Drop down."
								+ "Nominee Name  :  Field should not be empty."
								+ "Relationship with Customer : Please choose a value from the Drop down."
								+ "Account ID  :  Field should not be empty.Reference Number  :  Field should not be empty."
								+ "Reference Number  :  Field should not be empty."
								+ "Reference Number  :  Field should not be empty." } };
		return data;
	}

	public static String[][] storeCellData(String filename) {
		Sheet s = MIP_XLOperation.loadXL(filename);
		int numRows = MIP_XLOperation.getNumRows();
		int numcell = MIP_XLOperation.getNumCell();
		int rowcount = 0;
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

						data[rowcount][j] = new Double(r.getCell(j)
								.getNumericCellValue()).longValue() + "";
					}

				}
			}
			rowcount++;
		}

		return data;
	}

}
