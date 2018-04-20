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
import com.milvik.mip.utility.MIP_ReadPropertyFile;

public class MIP_SearchCustomer_Queries {
	static ResultSet result;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_SearchCustomer_Queries");
	}

	public static int countCustomerRecordByMSISDN(String msisdn) {
		logger.info("Executing countCustomerRecordByMSISDN query");
		int count = 0;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(distinct(cust_id)) as count FROM offer_subscription where msisdn="
							+ msisdn + " ;");
			result.next();

			return result.getInt("count");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the countCustomerRecordByMSISDN queries",
					e);
		}
		return count;
	}

	public static int countCustomerRecordByNic(String Nic) {
		logger.info("Executing countCustomerRecordByNic query");
		int count = 0;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(*) as count FROM customer_details where nic="
							+ Nic + ";");
			result.next();
			return count = result.getInt("count");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the countCustomerRecordByNic queries",
					e);
		}
		return count;
	}

	public static int countCustomerRecordByRefNum(String ref_num) {
		logger.info("Executing countCustomerRecordByRefNum query");
		int count = 0;
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT count(distinct(cust_id)) as count FROM mip_lk_dg.offer_subscription where ref_no='"
							+ ref_num + "' ;");
			result.next();
			return count = result.getInt("count");

		} catch (SQLException e) {
			logger.error(
					"Error while executing the countCustomerRecordByRefNum queries",
					e);
		}
		return count;
	}

	public static Map<String, String> getCustomerSearchDataWithNic(String Nic,
			String name, String chn_name) {
		logger.info("Executing getCustomerSearchData query");
		String product = "";
		String reg_by = "";
		String reg_date = "";
		String conf_date = "";
		String msisdn = "";
		Map<String, String> details = new HashMap<String, String>();
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select concat(od.offer_name,'(XXXXX - LKR ',ocd.offer_cover,')') as sub_prd,os.is_confirmed,ud.user_uid,DATE_FORMAT(cd.created_date,'%d/%m/%Y') as  created_date,DATE_FORMAT(os.confirmed_date,'%d/%m/%Y') as confirmed_date,os.msisdn  from customer_details cd join offer_subscription os"
							+ " on cd.cust_id=os.cust_id join offer_details od on od.offer_id=os.offer_id join offer_cover_details ocd"
							+ " on ocd.offer_cover_id=os.offer_cover_id join user_details ud on ud.user_id=cd.created_by join subscriber_channel sc on sc.chn_id=cd.subscriber_channel_id "
							+ " where cd.nic="
							+ Nic
							+ " and cd.customer_name='"
							+ name.trim()
							+ "' and sc.chn_name='" + chn_name.trim() + "';	");
			while (result.next()) {
				if (result.getString("sub_prd") != null) {
					if (result.getInt("is_confirmed") == 1)

						product = product
								+ result.getString("sub_prd").replaceAll(
										"XXXXX", "CONFIRMED") + ",";
					else

						product = product
								+ result.getString("sub_prd").replaceAll(
										"XXXXX", "UNCONFIRMED") + ",";
				}

				if (result.getString("user_uid") != null) {
					reg_by = reg_by + result.getString("user_uid") + ",";
				}
				if (result.getString("created_date") != null) {
					reg_date = reg_date + result.getString("created_date")
							+ ",";
				}
				if (result.getString("confirmed_date") != null) {
					conf_date = conf_date + result.getString("confirmed_date")
							+ "," + ",";
				}
				if (result.getString("msisdn") != null) {
					msisdn = result.getString("msisdn");

				}
			}
			details.put("subs_prdct", product);
			details.put("reg_by", reg_by);
			details.put("reg_date", reg_date);
			details.put("conf_date", conf_date);
			details.put("Mobile Number", msisdn);

		} catch (SQLException e) {
			logger.error(
					"Error while executing the countCustomerRecordByCustId queries",
					e);
		}
		return details;
	}

	public static Map<String, String> getCustNomineeDetails(String nic,
			String channel) {
		logger.info("Executing getCustNomineeDetails query");
		Map<String, String> details = new HashMap<String, String>();
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT cd.customer_name,cd.preferred_lang,cd.nominee_name,cd.nominee_msisdn,cr.cust_relationship  FROM customer_details cd join customer_relationship_table cr"
							+ " on cd.nominee_relationship=cr.cr_id join subscriber_channel sc on"
							+ " cd.subscriber_channel_id=sc.chn_id where"
							+ " nic="
							+ nic
							+ " and sc.chn_name='"
							+ channel
							+ "';");
			while (result.next()) {
				details.put("Cust_Name", result.getString("customer_name"));
				details.put("Language", result.getString("preferred_lang")
						.charAt(0) + "");
				if (result.getString("nominee_name") == null)
					details.put("nominee_name", "");
				else
					details.put("nominee_name",
							result.getString("nominee_name"));
				if (result.getString("nominee_msisdn") == null)
					details.put("nominee_msisdn", "");
				else
					details.put("nominee_msisdn",
							result.getString("nominee_msisdn"));
				if (result.getString("cust_relationship") == null)
					details.put("nominee_relationship", "");
				else
					details.put("nominee_relationship",
							result.getString("cust_relationship"));

			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getCustNomineeDetails queries",
					e);
		}
		return details;
	}

	public static Map<String, String> getHPProductDetails(String nic,
			String channel) {
		logger.info("Executing getHPProductDetails query");
		Map<String, String> details = new HashMap<String, String>();
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select os.msisdn,os.ref_no,os.account_id,os.alt_msisdn,cd.spouse_name,cd.child1_name,cd.child2_name,cd.child3_name,ocd.offer_cover from offer_subscription os join"
							+ " customer_details cd on cd.cust_id=os.cust_id join subscriber_channel sc on"
							+ " cd.subscriber_channel_id=sc.chn_id join offer_cover_details ocd on  ocd.offer_cover_id=os.offer_cover_id where"
							+ " nic="
							+ nic
							+ " and sc.chn_name='"
							+ channel
							+ "'"
							+ " and os.offer_id=(select offer_id from offer_details where offer_name='Dialog Hospitalization') ;");
			while (result.next()) {
				if (result.getString("msisdn") == null)
					details.put("msisdn", "");
				else
					details.put("msisdn", result.getString("msisdn"));
				if (result.getString("ref_no") == null)
					details.put("ref_num", "");
				else
					details.put("ref_num", result.getString("ref_no"));
				if (result.getString("account_id") == null)
					details.put("account_id", "");
				else
					details.put("account_id", result.getString("account_id"));
				if (result.getString("alt_msisdn") == null)
					details.put("alt_msisdn", "");
				else
					details.put("alt_msisdn", result.getString("alt_msisdn"));
				if (result.getString("spouse_name") == null)
					details.put("spouse_name", "");
				else
					details.put("spouse_name", result.getString("spouse_name"));
				if (result.getString("child1_name") == null)
					details.put("child1_name", "");
				else
					details.put("child1_name", result.getString("child1_name"));
				if (result.getString("child2_name") == null)
					details.put("child2_name", "");
				else
					details.put("child2_name", result.getString("child2_name"));
				if (result.getString("child3_name") == null)
					details.put("child3_name", "");
				else
					details.put("child3_name", result.getString("child3_name"));
				if (result.getString("offer_cover") == null)
					details.put("offer_cover", "");
				else
					details.put("offer_cover", result.getString("offer_cover"));

			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getHPProductDetails queries", e);
		}
		return details;
	}

	public static Map<String, String> getHMPProductDetails(String nic,
			String channel) {
		logger.info("Executing getHMPProductDetails query");
		Map<String, String> details = new HashMap<String, String>();
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT cd.address_line_1,cd.address_line_2,cd.postal_code,os.msisdn,os.ref_no,os.account_id,os.alt_msisdn,ocd.offer_cover  FROM customer_details cd join offer_subscription os"
							+ " on cd.cust_id=os.cust_id join subscriber_channel sc on "
							+ " cd.subscriber_channel_id=sc.chn_id join offer_cover_details ocd on  ocd.offer_cover_id=os.offer_cover_id where"
							+ " nic="
							+ nic
							+ " and sc.chn_name='"
							+ channel
							+ "' and os.offer_id=(select offer_id from offer_details where offer_name='Home Protection');");
			while (result.next()) {
				if (result.getString("msisdn") == null)
					details.put("msisdn", "");
				else
					details.put("msisdn", result.getString("msisdn"));
				if (result.getString("ref_no") == null)
					details.put("ref_num", "");
				else
					details.put("ref_num", result.getString("ref_no"));
				if (result.getString("account_id") == null)
					details.put("account_id", "");
				else
					details.put("account_id", result.getString("account_id"));
				if (result.getString("address_line_1") == null)
					details.put("addressLine1", "");
				else
					details.put("addressLine1",
							result.getString("address_line_1"));
				if (result.getString("alt_msisdn") == null)
					details.put("alt_msisdn", "");
				else
					details.put("alt_msisdn", result.getString("alt_msisdn"));
				if (result.getString("address_line_2") == null)
					details.put("addressLine2", "");
				else
					details.put("addressLine2",
							result.getString("address_line_2"));
				if (result.getString("postal_code") == null)
					details.put("postalCode", "");
				else
					details.put("postalCode", result.getString("postal_code"));
				if (result.getString("offer_cover") == null)
					details.put("offer_cover", "");
				else
					details.put("offer_cover", result.getString("offer_cover"));
			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getHMPProductDetails queries", e);
		}
		return details;
	}

	public static Map<String, String> getLife2017ProductDetails(String nic,
			String channel) {
		logger.info("Executing getLife2017ProductDetails query");
		Map<String, String> details = new HashMap<String, String>();
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT os.msisdn,os.ref_no,os.account_id,ocd.offer_cover,os.alt_msisdn  FROM customer_details cd join offer_subscription os"
							+ " on cd.cust_id=os.cust_id join subscriber_channel sc on"
							+ " cd.subscriber_channel_id=sc.chn_id join offer_cover_details ocd on  ocd.offer_cover_id=os.offer_cover_id where"
							+ " nic="
							+ nic
							+ " and sc.chn_name='"
							+ channel
							+ "' and os.offer_id=(select offer_id from offer_details where offer_name='Life 2017');");
			while (result.next()) {
				if (result.getString("msisdn") == null)
					details.put("msisdn", "");
				else
					details.put("msisdn", result.getString("msisdn"));
				if (result.getString("ref_no") == null)
					details.put("ref_num", "");
				else
					details.put("ref_num", result.getString("ref_no"));
				if (result.getString("account_id") == null)
					details.put("account_id", "");
				else
					details.put("account_id", result.getString("account_id"));
				if (result.getString("offer_cover") == null)
					details.put("offer_cover", "");
				else
					details.put("offer_cover", result.getString("offer_cover"));
				if (result.getString("alt_msisdn") == null)
					details.put("alt_msisdn", "");
				else
					details.put("alt_msisdn", result.getString("alt_msisdn"));

			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getLife2017ProductDetails queries",
					e);
		}
		return details;
	}

	public static Map<String, String> getPA2017ProductDetails(String nic,
			String channel) {
		logger.info("Executing getPA2017ProductDetails query");
		Map<String, String> details = new HashMap<String, String>();
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("SELECT os.msisdn,os.ref_no,os.account_id,ocd.offer_cover,os.alt_msisdn  FROM customer_details cd join offer_subscription os"
							+ " on cd.cust_id=os.cust_id join subscriber_channel sc on"
							+ " cd.subscriber_channel_id=sc.chn_id join offer_cover_details ocd on  ocd.offer_cover_id=os.offer_cover_id where"
							+ " nic="
							+ nic
							+ " and sc.chn_name='"
							+ channel
							+ "' and os.offer_id=(select offer_id from offer_details where offer_name='PA 2017');");
			while (result.next()) {
				if (result.getString("msisdn") == null)
					details.put("mobile_num", "");
				else
					details.put("mobile_num", result.getString("msisdn"));
				if (result.getString("ref_no") == null)
					details.put("ref_num", "");
				else
					details.put("ref_num", result.getString("ref_no"));
				if (result.getString("account_id") == null)
					details.put("account_id", "");
				else
					details.put("account_id", result.getString("account_id"));
				if (result.getString("offer_cover") == null)
					details.put("offer_cover", "");
				else
					details.put("offer_cover", result.getString("offer_cover"));
				if (result.getString("alt_msisdn") == null)
					details.put("alt_msisdn", "");
				else
					details.put("alt_msisdn", result.getString("alt_msisdn"));

			}

		} catch (SQLException e) {
			logger.error(
					"Error while executing the getPA2017ProductDetails queries",
					e);
		}
		return details;
	}

	public static List<String> getSubscribedChannel(String nic, String channel) {
		logger.info("Executing getSubscribedChannel query");
		List<String> details = new ArrayList<String>();
		try {
			result = MIP_DataBaseConnection.st
					.executeQuery("select od.offer_name from offer_details od left join offer_subscription os on os.offer_id=od.offer_id"
							+ " left join customer_details cd on os.cust_id=cd.cust_id "
							+ "where cd.nic="
							+ nic
							+ " and cd.subscriber_channel_id=(select chn_id from subscriber_channel where chn_name='"
							+ channel + "')");
			while (result.next()) {
				if (result.getString("offer_name") == null) {
					details.add("");
				} else {
					details.add(result.getString("offer_name"));
				}

			}
		} catch (SQLException e) {
			logger.error(
					"Error while executing the getSubscribedChannel queries", e);
		}
		return details;

	}

	public static void main(String[] args) {
		MIP_ReadPropertyFile.loadProperty("config");
		MIP_DataBaseConnection.connectToDatabase();
		MIP_SearchCustomer_Queries.getCustomerSearchDataWithNic("197678900802",
				"latestcustomer", "GSM");

	}
}
