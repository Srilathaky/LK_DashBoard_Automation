package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_DeRegisterCustomer_Queries {
/*	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_DeRegisterCustomer_Queries");
	}
	static Map<String, String> custDetails = new HashMap<String, String>();
	static Map<String, String> deregcustDetails = new HashMap<String, String>();

	public static List<String> getRegisteredProduct(String msisdn) {
		List<String> product_name = new ArrayList<String>();
		logger.info("Executing getRegisteredProduct query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select pd.product_name from product_details pd join customer_subscription cs on pd.product_id=cs.product_id where cs.cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn + ") and cs.is_deactivated=0;");
			while (result.next()) {
				product_name.add(result.getString("product_name"));
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getRegisteredProduct queries", e);
		}
		return product_name;
	}

	public static int getActiveProductCount(String msisdn) {
		logger.info("Executing getActiveProductCount query");
		int count = 0;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM customer_subscription where cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn + ") and is_deactivated=0;");
			result.next();
			count = result.getInt("count");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getActiveProductCount queries",
					e);
		}
		return count;
	}

	public static HashMap<String, String> getCustomerDetails(String msisdn,
			String product_name) {
		logger.info("Executing getCustomerDetails query");
		HashMap<String, String> cust_details = new HashMap<String, String>();
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select concat(cd.fname,' ',cd.sname) as customer_name,cs.is_confirmed,cs.deducted_offer_amount,cs.earned_cover,cs.prev_month_usage  from customer_details cd join customer_subscription cs on"
							+ " cd.cust_id=cs.cust_id  where cs.cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn
							+ ") and cs.product_id=(select product_id from product_details where product_name='"
							+ product_name + "')");
			result.next();
			cust_details
					.put("customer_name", result.getString("customer_name"));
			if (result.getInt("is_confirmed") == 0) {
				cust_details.put("confirmed_stats", "Not confirmed");
			} else {
				cust_details.put("confirmed_stats", "Confirmed");
			}
			cust_details.put("deducted_offer_amount",
					result.getString("deducted_offer_amount"));
			cust_details.put("earned_cover", result.getString("earned_cover"));
			cust_details.put("prev_month_usage",
					result.getString("prev_month_usage"));

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCustomerDetails queries", e);
		}
		return cust_details;
	}

	public static boolean getCustomerStatus(String msisdn, String productname) {
		logger.info("Executing getCustomerStatus query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select is_deactivated from customer_subscription where cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn
							+ " )and product_id=(select product_id from product_details where product_name='"
							+ productname + "')");
			if (!result.next() == false) {

				if (result.getInt("is_deactivated") == 1) {
					return true;
				} else {
					return false;
				}
			} else {
				result = MIP_DataBaseConnection.st
						.executeQuery("SELECT count(*) as count FROM bima_cancellations where msisdn="
								+ msisdn + ";");
				result.next();
				if (result.getInt("count") == 1) {
					return true;
				} else {
					return false;
				}

			}

		} catch (SQLException e) {
			logger.error("Error while executing the getCustomerStatus queries",
					e);
		}
		return false;
	}

	public static boolean validateProductCancelation(String msisdn,
			String product_name) {
		logger.info("Executing validateProductCancelation query");
		List<String> bima_cancel = new ArrayList<String>();
		List<String> product_cancel = new ArrayList<String>();
		boolean status1 = false;
		boolean status2 = false;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select product_level_id,prior_deduction_date,prev_month_usage,next_deduction_amount,max_deduction_per_day,is_prior_deduction_successful,is_prev_month_purchase_successful,is_deduction_completed,is_confirmed,earned_cover,deducted_offer_amount,cover_charges,amount_deducted_today from product_cancellations where cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn
							+ ") and product_id=(select product_id from product_details where product_name='"
							+ product_name + "');");

			if (result.next() == false) {
				status1 = true;
			} else {
				// result.next();
				if (result.getString("product_level_id") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result.getString("product_level_id"));
				if (result.getString("prior_deduction_date") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result.getString("prior_deduction_date"));
				if (result.getString("prev_month_usage") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result.getString("prev_month_usage"));
				if (result.getString("next_deduction_amount") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result.getString("next_deduction_amount"));
				if (result.getString("max_deduction_per_day") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result.getString("max_deduction_per_day"));
				if (result.getString("is_prior_deduction_successful") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result
							.getString("is_prior_deduction_successful"));
				if (result.getString("is_prev_month_purchase_successful") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result
							.getString("is_prev_month_purchase_successful"));
				if (result.getString("is_deduction_completed") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result.getString("is_deduction_completed"));
				if (result.getString("is_confirmed") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result.getString("is_confirmed"));
				if (result.getString("earned_cover") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result.getString("earned_cover"));
				if (result.getString("deducted_offer_amount") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result.getString("deducted_offer_amount"));
				if (result.getString("cover_charges") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result.getString("cover_charges"));
				if (result.getString("amount_deducted_today") == null)
					bima_cancel.add("");
				else
					bima_cancel.add(result.getString("amount_deducted_today"));
			}
			result = MIP_DataBaseConnection.st
					.executeQuery("select product_level_id,prior_deduction_date,prev_month_usage,next_deduction_amount,max_deduction_per_day,is_prior_deduction_successful,is_prev_month_purchase_successful,is_deduction_completed,is_confirmed,earned_cover,deducted_offer_amount,cover_charges,amount_deducted_today from customer_subscription where cust_id=(select cust_id from customer_details where msisdn="
							+ msisdn
							+ ") and product_id=(select product_id from product_details where product_name='"
							+ product_name + "');");
			if (result.next() == false) {
				status2 = true;
			} else {
				if (result.getString("product_level_id") == null)
					product_cancel.add("");
				else
					product_cancel.add(result.getString("product_level_id"));
				if (result.getString("prior_deduction_date") == null)
					product_cancel.add("");
				else
					product_cancel
							.add(result.getString("prior_deduction_date"));
				if (result.getString("prev_month_usage") == null)
					product_cancel.add("");
				else
					product_cancel.add(result.getString("prev_month_usage"));
				if (result.getString("next_deduction_amount") == null)
					product_cancel.add("");
				else
					product_cancel.add(result
							.getString("next_deduction_amount"));
				if (result.getString("max_deduction_per_day") == null)
					product_cancel.add("");
				else
					product_cancel.add(result
							.getString("max_deduction_per_day"));
				if (result.getString("is_prior_deduction_successful") == null)
					product_cancel.add("");
				else
					product_cancel.add(result
							.getString("is_prior_deduction_successful"));
				if (result.getString("is_prev_month_purchase_successful") == null)
					product_cancel.add("");
				else
					product_cancel.add(result
							.getString("is_prev_month_purchase_successful"));
				if (result.getString("is_deduction_completed") == null)
					product_cancel.add("");
				else
					product_cancel.add(result
							.getString("is_deduction_completed"));
				if (result.getString("is_confirmed") == null)
					product_cancel.add("");
				else
					product_cancel.add(result.getString("is_confirmed"));
				if (result.getString("earned_cover") == null)
					product_cancel.add("");
				else
					product_cancel.add(result.getString("earned_cover"));
				if (result.getString("deducted_offer_amount") == null)
					product_cancel.add("");
				else
					product_cancel.add(result
							.getString("deducted_offer_amount"));
				if (result.getString("cover_charges") == null)
					product_cancel.add("");
				else
					product_cancel.add(result.getString("cover_charges"));
				if (result.getString("amount_deducted_today") == null)
					product_cancel.add("");
				else
					product_cancel.add(result
							.getString("amount_deducted_today"));
			}
			if ((status1 && status2) == true) {
				return true;
			} else {
				if (bima_cancel.equals(product_cancel)) {
					return true;
				} else {
					return false;
				}
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the validateProductCancelation queries",
					e);
		}
		return false;
	}

	public static boolean getDeRegisteredSMS(String msisdn, String product) {
		logger.info("Executing getDeRegisteredSMS query");
		String sms_text = "";
		String sms = "";
		String product_code = "";
		if (product.equalsIgnoreCase(MIP_CustomerManagementPage.XTRALIFE)) {
			product_code = "xl";
			sms_text = "Dear customer, you have been cancelled from Xtra-Life as per your request. If you wish to register again call 550 or dial *550#";
		} else if (product
				.equalsIgnoreCase(MIP_CustomerManagementPage.HOSPITAL)) {
			product_code = "hp";
			sms_text = "Your Tigo Hospital support has been cancelled as per your request. If you wish to register again call 550 or dial *550#";
		} else if (product.equalsIgnoreCase(MIP_CustomerManagementPage.IP)) {
			product_code = "ip";
			sms_text = "Dear customer, you have been cancelled from Tigo Income Protection as per your request. If you wish to register again call 550 or dial *550#";
		} else if (product.trim().toUpperCase()
				.contains(MIP_CustomerManagementPage.XTRALIFE.toUpperCase())
				&& product.trim().toUpperCase()
						.contains(MIP_CustomerManagementPage.IP.toUpperCase())) {
			product_code = "xl_Ip";
			sms_text = "Dear customer, you have been cancelled from Xtra-Life and Income Protection as per your request. If you wish to register again call 550 or dial *550#";
		} else if (product.trim().toUpperCase()
				.contains(MIP_CustomerManagementPage.XTRALIFE.toUpperCase())
				&& product
						.trim()
						.toUpperCase()
						.contains(
								MIP_CustomerManagementPage.HOSPITAL
										.toUpperCase())) {
			product_code = "xl_hp";
			sms_text = "Dear customer, you have been cancelled from Xtra-Life and Hospitalization as per your request. If you wish to register again call 550 or dial *550#";
		}
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT sms_text FROM sms_in_queue where sms_template_name like 'dashboard_%"
							+ product_code
							+ "%_dereg_success' and  sms_msisdn="
							+ msisdn
							+ ";");
			while (result.next()) {
				sms = result.getString("sms_text");
			}
			if (sms.contains(sms_text))
				return true;
			else {
				return false;
			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getRegisteredProduct queries", e);
			return false;
		}
	}

	public static Map<String, String> getDeRegisterInfo(String msisdn,
			String product) {
		logger.info("Executing getDeRegisterInfo query");
		Map<String, String> details = new HashMap<String, String>();
		String product_name = "";
		if (product.equalsIgnoreCase(MIP_CustomerManagementPage.XTRALIFE)) {
			product_name = "irfname";
		} else if (product
				.equalsIgnoreCase(MIP_CustomerManagementPage.HOSPITAL)) {
			product_name = "hpirfname";
		} else if (product.equalsIgnoreCase(MIP_CustomerManagementPage.IP)) {
			product_name = "kinfname";
		} else if (product.trim().toUpperCase()
				.contains(MIP_CustomerManagementPage.XTRALIFE.toUpperCase())
				&& product.trim().toUpperCase()
						.contains(MIP_CustomerManagementPage.IP.toUpperCase())) {
			product_name = "irfname and kinfname";
		} else if (product.trim().toUpperCase()
				.contains(MIP_CustomerManagementPage.XTRALIFE.toUpperCase())
				&& product
						.trim()
						.toUpperCase()
						.contains(
								MIP_CustomerManagementPage.HOSPITAL
										.toUpperCase())) {
			product_name = "irfname and hpirfname";
		}
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select concat(ud.user_uid ,' - ',ud.fname,' ',ud.sname) as De_registered_by,bc.bc_date as De_registered_Date,"
							+ " bc.record_deletion_date as Date_of_customer_removal from user_details ud join bima_cancellations bc on  ud.user_id= bc.bc_by"
							+ " where bc.msisdn="
							+ msisdn
							+ " and bc."
							+ product_name + " is not null;");
			while (result.next()) {
				details.put("De_registered_by",
						result.getString("De_registered_by"));
				details.put("De_registered_Date",
						result.getString("De_registered_Date"));
				details.put("Date_of_customer_removal",
						result.getString("Date_of_customer_removal"));
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getRegisteredProduct queries", e);

		}
		return details;
	}*/
}
