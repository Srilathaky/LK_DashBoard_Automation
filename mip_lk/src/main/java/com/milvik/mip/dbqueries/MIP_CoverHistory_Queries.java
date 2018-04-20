package com.milvik.mip.dbqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.milvik.mip.pageutil.MIP_CustomerManagementPage;
import com.milvik.mip.utility.MIP_DataBaseConnection;
import com.milvik.mip.utility.MIP_Logging;
import com.milvik.mip.utility.MIP_ReadPropertyFile;

public class MIP_CoverHistory_Queries {
/*	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_CoverHistory_Queries");
	}

	public static List<String> getCoverHistoryDetails(String msisdn) {
		List<String> coverDetails = new ArrayList<String>();

		logger.info("Executing getCoverHistoryDetails query");
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT month,year,offer_cover_id as cover_level,offer_charges as Deducted_charges,offer_cover as Cover_Earned,rolled_out_amount "
							+ "FROM cover_history where msisdn=" + msisdn + ";");

			while (result.next()) {
				coverDetails.add(result.getString("month"));
				coverDetails.add(result.getString("year"));
				coverDetails.add(result.getString("cover_level"));
				coverDetails.add(result.getString("Deducted_charges"));
				coverDetails.add(result.getString("Cover_Earned"));
				coverDetails.add(result.getString("rolled_out_amount"));
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCoverHistoryDetails queries",
					e);
		}
		return coverDetails;
	}

	public static boolean setCoverHistoryDetails(String product, String msisdn,
			String prev_month_usage, String cover_free, String charges_xl,
			String cover_xl, String charges_hp, String cover_hp,
			String charges_ip, String cover_ip, String month, String year) {
		logger.info("Executing setCoverHistoryDetails query");
		try {
			if (product.equalsIgnoreCase(MIP_CustomerManagementPage.XTRALIFE)) {
				MIP_DataBaseConnection.st
						.executeUpdate("INSERT INTO coverhistory(cust_id,msisdn,prev_month_usage,cover_free,charges_xl,cover_xl,month,year)"
								+ " VALUES ((select cust_id from customer_details where msisdn="
								+ msisdn
								+ "),"
								+ msisdn
								+ ","
								+ prev_month_usage
								+ ","
								+ cover_free
								+ ","
								+ charges_xl
								+ ","
								+ cover_xl
								+ ",'"
								+ month
								+ "'," + year + ");");

				MIP_DataBaseConnection.st
						.executeUpdate("UPDATE coverhistory SET msisdn=lpad(msisdn,10,0);");
				result = MIP_DataBaseConnection.st
						.executeQuery("select cust_id from coverhistory where msisdn="
								+ msisdn + ";");
				while (result.next() == false) {
					return false;
				}
				MIP_DataBaseConnection.st
						.executeUpdate("update customer_subscription set is_confirmed=1 where cust_id=(select cust_id from customer_details where msisdn="
								+ msisdn
								+ " ) and product_id=(select product_id from product_details where product_name='"
								+ MIP_CustomerManagementPage.XTRALIFE + "');");
			} else if (product
					.equalsIgnoreCase(MIP_CustomerManagementPage.HOSPITAL)) {
				MIP_DataBaseConnection.st
						.executeUpdate("INSERT INTO coverhistory(cust_id,msisdn,charges_hp,cover_hp,month,year)"
								+ " VALUES ((select cust_id from customer_details where msisdn="
								+ msisdn
								+ "),"
								+ msisdn
								+ ","
								+ charges_hp
								+ ","
								+ cover_hp
								+ ",'"
								+ month
								+ "',"
								+ year
								+ ");");

				MIP_DataBaseConnection.st
						.executeUpdate("UPDATE coverhistory SET msisdn=lpad(msisdn,10,0);");
				result = MIP_DataBaseConnection.st
						.executeQuery("select cust_id from coverhistory where msisdn="
								+ msisdn + ";");
				while (result.next() == false) {
					return false;
				}
				MIP_DataBaseConnection.st
						.executeUpdate("update customer_subscription set is_confirmed=1 where cust_id=(select cust_id from customer_details where msisdn="
								+ msisdn
								+ " ) and product_id=(select product_id from product_details where product_name='"
								+ MIP_CustomerManagementPage.HOSPITAL + "');");
			} else if (product.equalsIgnoreCase(MIP_CustomerManagementPage.IP)) {
				MIP_DataBaseConnection.st
						.executeUpdate("INSERT INTO coverhistory(cust_id,msisdn,charges_ip,cover_ip,month,year)"
								+ " VALUES ((select cust_id from customer_details where msisdn="
								+ msisdn
								+ "),"
								+ msisdn
								+ ","
								+ charges_ip
								+ ","
								+ cover_ip
								+ ",'"
								+ month
								+ "',"
								+ year
								+ ");");

				MIP_DataBaseConnection.st
						.executeUpdate("UPDATE coverhistory SET msisdn=lpad(msisdn,10,0);");
				result = MIP_DataBaseConnection.st
						.executeQuery("select cust_id from coverhistory where msisdn="
								+ msisdn + ";");
				while (result.next() == false) {
					return false;
				}
				MIP_DataBaseConnection.st
						.executeUpdate("update customer_subscription set is_confirmed=1 where cust_id=(select cust_id from customer_details where msisdn="
								+ msisdn
								+ " ) and product_id=(select product_id from product_details where product_name='"
								+ MIP_CustomerManagementPage.IP + "');");
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCoverHistoryDetails queries",
					e);
		}
		return true;
	}

	public static void main(String[] args) {
		MIP_ReadPropertyFile.loadProperty("config");
		MIP_DataBaseConnection.connectToDatabase();
		setCoverHistoryDetails("xtra-life", "0570088803", "12.23", "50", "56",
				"85", "", "", "", "", "Jan", "2018");

	}*/
}
