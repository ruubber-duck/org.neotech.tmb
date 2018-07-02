package org.neotech.tmb.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.neotech.tmb.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author
 *
 */
public class JdbcManager {

	/* Class */
	
	private static final Logger LOG = LoggerFactory.getLogger(JdbcManager.class); 

	public static final String JDBC_DRIVER = "org.neotech.tmb.jdbc.driver";
	public static final String JDBC_URL = "org.neotech.tmb.jdbc.url";
	public static final String JDBC_LOGIN = "org.neotech.tmb.jdbc.login";
	public static final String JDBC_PASSWORD = "org.neotech.tmb.jdbc.password";

	/* Instance */

	private final AppConfig appConfig;

	private String jdbcDriver;
	private String jdbcUrl;
	private String jdbcLogin;
	private String jdbcPassword;

	/**
	 * 
	 */
	public JdbcManager(AppConfig appConfig) {
		super();
		this.appConfig = appConfig;
		this.jdbcDriver = this.appConfig.getProperty(JDBC_DRIVER);
		this.jdbcUrl = this.appConfig.getProperty(JDBC_URL);
		this.jdbcLogin = this.appConfig.getProperty(JDBC_LOGIN);
		this.jdbcPassword = this.appConfig.getProperty(JDBC_PASSWORD);
	}

	public Connection getConnection() throws SQLException {
		Connection dbConnection = null;
		
		try {
			Class.forName(this.jdbcDriver);
			dbConnection =
					DriverManager.getConnection(this.jdbcUrl, this.jdbcLogin, this.jdbcPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			LOG.error("JDBC driver is not found in classpath", e);
			throw new IllegalStateException("JDBC driver is not found in classpath", e);			
		} 
		
		return dbConnection;
	}

}
