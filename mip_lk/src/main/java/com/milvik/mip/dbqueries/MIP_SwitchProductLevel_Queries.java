package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;

public class MIP_SwitchProductLevel_Queries {
	static ResultSet result;
	static Logger logger;

	static {
		logger = MIP_Logging.logDetails("MIP_SwitchProductLevel_Queries");
	}

	public static List<String> getLifeBenefitLevels(String mobilenum) {
		List<String> benefitlevel = new ArrayList<String>();
		try {
			logger.info("Executing the getLifeBenefitLevel queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT offer_cover FROM offer_cover_details where offer_id=1 "
							+ "and is_prepaid=(select is_prepaid from customer_details where msisdn='"
							+ mobilenum + "');");
			while (result.next()) {
				benefitlevel.add(result.getString("offer_cover"));
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getLifeBenefitLevel queries", e);
		}
		return benefitlevel;
	}

	public static String getCurrentCoverLevel(String mobilenum) {
		String coverlevel = "";
		try {
			logger.info("Executing the getCurrentCoverLevel queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT ocd.offer_cover FROM offer_cover_details ocd join  offer_subscription os on "
							+ "ocd.offer_cover_id=os.offer_cover_id where "
							+ "os.cust_id=(select cust_id from customer_details where msisdn='"
							+ mobilenum + "');");
			result.next();
			coverlevel = result.getString("offer_cover");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCurrentCoverLevel queries", e);
		}
		return coverlevel;
	}

	public static String getSwitchCoverLevelSMS(String mobilenum) {
		String sms_text = "";
		try {
			logger.info("Executing the getSwitchCoverLevelSMS queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT sms_text FROM sms_in_queue where sms_msisdn ="
							+ mobilenum
							+ " order by  sms_queue_id desc limit 1;");
			result.next();
			sms_text = result.getString("sms_text");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getSwitchCoverLevelSMS queries",
					e);
		}
		return sms_text;
	}

	public static int getConfirmationStatus(String mobilenum) {
		int is_confirmed = -1;
		try {
			logger.info("Executing the getConfirmationStatus queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("	SELECT is_confirmed FROM mip_ni_tf.offer_subscription where cust_id=(select cust_id from customer_details where msisdn="
							+ mobilenum + ");");
			result.next();
			is_confirmed = result.getInt("is_confirmed");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getConfirmationStatus queries",
					e);
		}
		return is_confirmed;
	}

	public static String getProductLevelforConfirmedCustomer(String mobilenum) {
		String cover_level = "";
		try {
			logger.info("Executing the getProductLevelorConfirmedCustomer queries");
			result = MIP_DataBaseConnection.st
					.executeQuery("	SELECT count(*) as count FROM mip_ni_tf.updated_cover_level "
							+ "where cust_id=(select cust_id from customer_details where msisdn='"
							+ mobilenum + "') ;");
			result.next();
			if (result.getInt("count") ==1) {
				ResultSet result1 = MIP_DataBaseConnection.st
						.executeQuery("select ocd.offer_cover from offer_cover_details ocd join updated_cover_level ucl "
								+ "on ocd.offer_cover_id=ucl.new_offer_cover_id  where ucl.cust_id=(select cust_id from customer_details where msisdn='"
								+ mobilenum + "');");
				result1.next();
				cover_level = result1.getString("offer_cover");
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getProductLevelorConfirmedCustomer queries",
					e);
		}
		return cover_level;
	}

}
