package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_RegisterCustomer_Queries {
	private static ResultSet result;
	private static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_RegisterCustomer_Queries");
	}
	private static Map<String, String> custDetails = new HashMap<String, String>();
	private static Map<String, String> offerDetails = new HashMap<String, String>();
	private static String sms;

	public static Map<String, String> getCustomerDetails(String nic,
			String channel_Type) {
		logger.info("Executing getCustomerDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT cd.cust_id,ud.user_uid as user,ud1.user_uid as modified_by, cd.customer_name,cd.nominee_name,cd.preferred_lang,cd.spouse_name,cd.child1_name,cd.child2_name,cd.child3_name,cd.nominee_msisdn,cr.cust_relationship as relationship FROM customer_details cd "
							+ " left join customer_relationship_table cr on cd.nominee_relationship=cr.cr_id"
							+ " join user_details ud on ud.user_id=cd.created_by join user_details ud1 on ud1.user_id=cd.modified_by"
							+ " join subscriber_channel sc on sc.chn_id=cd.subscriber_channel_id and  sc.chn_name='"
							+ channel_Type + "'" + " where cd.nic=" + nic + ";");
			result.next();
			custDetails.put("cust_id", result.getString("cust_id"));
			custDetails.put("customer_name", result.getString("customer_name"));
			if (result.getString("nominee_name") == null)
				custDetails.put("nominee_name", "");
			else
				custDetails.put("nominee_name",
						result.getString("nominee_name"));
			if (result.getString("preferred_lang") == null)
				custDetails.put("preferred_lang", "");
			else
				custDetails.put("preferred_lang",
						result.getString("preferred_lang"));
			if (result.getString("user") == null)
				custDetails.put("user", "");
			else
				custDetails.put("user", result.getString("user"));
			if (result.getString("spouse_name") == null)
				custDetails.put("spouse_name", "");
			else
				custDetails.put("spouse_name", result.getString("spouse_name"));
			if (result.getString("child1_name") == null)
				custDetails.put("child1_name", "");
			else
				custDetails.put("child1_name", result.getString("child1_name"));
			if (result.getString("child2_name") == null)
				custDetails.put("child2_name", "");
			else
				custDetails.put("child2_name", result.getString("child2_name"));
			if (result.getString("child3_name") == null)
				custDetails.put("child3_name", "");
			else
				custDetails.put("child3_name", result.getString("child3_name"));
			if (result.getString("nominee_msisdn") == null)
				custDetails.put("nominee_msisdn", "");
			else
				custDetails.put("nominee_msisdn",
						result.getString("nominee_msisdn"));
			if (result.getString("relationship") == null)
				custDetails.put("relationship", "");
			else
				custDetails.put("relationship",
						result.getString("relationship"));
			if (result.getString("modified_by") == null)
				custDetails.put("modified_by", "");
			else
				custDetails.put("modified_by", result.getString("modified_by"));

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCustomerDetails queries", e);
		}
		return custDetails;
	}

	public static Map<String, String> getOfferDetails(String cust_id,
			String offer_name) {
		logger.info("Executing getOfferDetails query");
		try {
			System.out.println();
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT  os.is_confirmed,os.is_product_attached,os.subscription_state,os.premium_charges,ocd.offer_cover,os.alt_msisdn,cc.chn_name,"
							+ "os.subscription_state,os.msisdn,os.ref_no,os.is_prepaid,cd.spouse_name,cd.child1_name,cd.child2_name,cd.child3_name,cd.address_line_1,cd.address_line_2,cd.postal_code FROM offer_subscription os "
							+ " join communication_channel cc on cc.chn_id=os.registration_channel_id join offer_cover_details ocd on os.offer_cover_id=ocd.offer_cover_id join  customer_details cd on cd.cust_id=os.cust_id  where"
							+ " os.cust_id="
							+ cust_id
							+ " and os.offer_id=(select offer_id from offer_details where offer_name='"
							+ offer_name.trim() + "');");
			result.next();
			if (result.getString("chn_name") == null)
				offerDetails.put("chn_name", "");
			else
				offerDetails.put("chn_name", result.getString("chn_name"));
			if (result.getString("is_confirmed") == null)
				offerDetails.put("is_confirmed", "");
			else
				offerDetails.put("is_confirmed",
						result.getString("is_confirmed"));
			if (result.getString("is_product_attached") == null)
				offerDetails.put("is_product_attached", "");
			else
				offerDetails.put("is_product_attached",
						result.getString("is_product_attached"));
			if (result.getString("premium_charges") == null)
				offerDetails.put("premium_charges", "");
			else
				offerDetails.put("premium_charges",
						result.getString("premium_charges"));
			if (result.getString("subscription_state") == null)
				offerDetails.put("subscription_state", "");
			else
				offerDetails.put("subscription_state",
						result.getString("subscription_state"));
			if (result.getString("msisdn") == null)
				offerDetails.put("msisdn", "");
			else
				offerDetails.put("msisdn", result.getString("msisdn"));
			if (result.getString("ref_no") == null)
				offerDetails.put("ref_no", "");
			else
				offerDetails.put("ref_no", result.getString("ref_no"));
			if (result.getString("is_prepaid") == null)
				offerDetails.put("is_prepaid", "");
			else
				offerDetails.put("is_prepaid", result.getString("is_prepaid"));
			if (result.getString("alt_msisdn") == null)
				offerDetails.put("alt_msisdn", "");
			else
				offerDetails.put("alt_msisdn", result.getString("alt_msisdn"));
			if (result.getString("subscription_state") == null)
				offerDetails.put("subscription_state", "");
			else
				offerDetails.put("subscription_state",
						result.getString("subscription_state"));
			if (result.getString("offer_cover") == null)
				offerDetails.put("offer_cover", "");
			else
				offerDetails
						.put("offer_cover", result.getString("offer_cover"));
			if (result.getString("spouse_name") == null)
				offerDetails.put("spouse_name", "");
			else
				offerDetails
						.put("spouse_name", result.getString("spouse_name"));
			if (result.getString("child1_name") == null)
				offerDetails.put("child1_name", "");
			else
				offerDetails
						.put("child1_name", result.getString("child1_name"));
			if (result.getString("child2_name") == null)
				offerDetails.put("child2_name", "");
			else
				offerDetails
						.put("child2_name", result.getString("child2_name"));
			if (result.getString("child3_name") == null)
				offerDetails.put("child3_name", "");
			else
				offerDetails
						.put("child3_name", result.getString("child3_name"));
			if (result.getString("address_line_1") == null)
				offerDetails.put("address_line_1", "");
			else
				offerDetails.put("address_line_1",
						result.getString("address_line_1"));
			if (result.getString("address_line_2") == null)
				offerDetails.put("address_line_2", "");
			else
				offerDetails.put("address_line_2",
						result.getString("address_line_2"));
			if (result.getString("postal_code") == null)
				offerDetails.put("postal_code", "");
			else
				offerDetails
						.put("postal_code", result.getString("postal_code"));
			System.out.println();
		} catch (SQLException e) {
			logger.error("Error while executing the getOfferDetails queries", e);
		}
		return offerDetails;
	}

	public static String getSMSText(String msisdn, String product_name) {
		logger.info("Executing getSMSText query");

		return sms;
	}

	public static List<String> getRegisteredProduct(String nic, String channel) {
		List<String> offer = new ArrayList<String>();
		logger.info("Executing getRegisteredProduct query");
		try {

			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT od.offer_name from offer_details od join  offer_subscription os "
							+ " on os.offer_id=od.offer_id join customer_details cd on cd.cust_id=os.cust_id"
							+ " where cd.nic="
							+ nic
							+ " and cd.subscriber_channel_id=(select chn_id from subscriber_channel where chn_name='"
							+ channel + "');");
			while (result.next()) {
				offer.add(result.getString("offer_name").trim());
			}

		} catch (Exception e) {
			logger.error(
					"Error while executing the getRegisteredProduct queries", e);
		}
		return offer;
	}
}
