package com.milvik.mip.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class MIP_DataBaseConnection {
	static Connection con;
	public static Statement st;
	static Logger logger;
	static {
		logger = MIP_Logging.logDetails("MIP_DataBaseConnection");
	}

	public static Statement connectToDatabase() {
		try {
			logger.info("Connecting to Database");
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

			con = DriverManager.getConnection("jdbc:mysql:"
					+ MIP_ReadPropertyFile.getPropertyValue("dburl"),
					MIP_ReadPropertyFile.getPropertyValue("dbusername"),
					MIP_ReadPropertyFile.getPropertyValue("dbpassword"));

			if (con != null) {
				logger.info("Connecting to Database is successfull");
			}
		} catch (ClassNotFoundException e) {
			logger.error("Error while connecting to DataBase", e);
		} catch (SQLException e) {
			logger.error("Error while connecting to DataBase", e);
		} catch (InstantiationException e) {
			logger.error("Error while connecting to DataBase", e);
		} catch (IllegalAccessException e) {
			logger.error("Error while connecting to DataBase", e);
		}
		try {
			st = con.createStatement();
		} catch (SQLException e) {
			logger.error("Error while creating statement", e);
		}
		return st;
	}

	public static void closeDatabase() {
		if (con != null) {
			try {
				logger.info("Closing the database");
				con.close();
			} catch (SQLException e) {
				logger.error("Error while closing the DataBase", e);
			}
		}
	}
}
