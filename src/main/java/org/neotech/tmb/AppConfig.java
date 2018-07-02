package org.neotech.tmb;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author
 *
 */
public class AppConfig {

	private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);

	public static final String OPTION_CONFIG_APP = "org.neotech.config.app";
	public static final String OPTION_CONFIG_PSWD = "org.neotech.config.passwd";
	public static final String DEFAULT_CONFIG_APP = "tmb-app.properties";
	public static final String DEFAULT_CONFIG_PSWD = "tmb-pswd.properties";

	/* Instance */

	private Properties propertiesApp;
	private Properties propertiesPasswd;

	/**
	 * Default
	 */
	public AppConfig() {
		super();
		LOG.debug("AppConfig...");

		this.propertiesApp = new Properties();
		this.propertiesPasswd = new Properties();

		String sysOptionApp = System.getProperty(OPTION_CONFIG_APP);
		String sysOptionPswd = System.getProperty(OPTION_CONFIG_PSWD);

		if (sysOptionApp != null) {
			LOG.debug("Read app properties from external file [{}].",
					sysOptionApp);
			this.readFromFile(this.propertiesApp, sysOptionApp);
		} else {
			LOG.debug("Read default app properties from classpath [{}].",
					DEFAULT_CONFIG_APP);
			this.readFromClassPath(this.propertiesApp, DEFAULT_CONFIG_APP);
		}

		if (sysOptionPswd != null) {
			LOG.debug("Read pswd properties from external file [{}].",
					sysOptionPswd);
			this.readFromFile(this.propertiesPasswd, sysOptionPswd);
		} else {
			LOG.debug("Read default pswd properties from classpath [{}].",
					DEFAULT_CONFIG_PSWD);
			this.readFromClassPath(this.propertiesPasswd, DEFAULT_CONFIG_PSWD);
		}

		LOG.debug("AppConfig done.");
	}

	/* ***** Routines ***** */

	private boolean readFromFile(Properties props, String path) {
		boolean result = false;

		try {
			File fileConfigPasswd = new File(path);
			FileReader readerConfigPasswd = new FileReader(fileConfigPasswd);
			this.propertiesPasswd.load(readerConfigPasswd);
			result = true;
		} catch (IOException e) {
			LOG.error("Can't read propertis file. ", e);
			e.printStackTrace();
			throw new IllegalStateException("Can't read propertis file. ", e);
		}

		return result;
	}

	private boolean readFromClassPath(Properties props, String path) {
		boolean result = false;

		try {
			InputStream stConfigApp =
					this.getClass().getClassLoader().getResourceAsStream(path);
			this.propertiesApp.load(stConfigApp);
			result = true;
		} catch (IOException e) {
			LOG.error("Can't read propertis from classpath. ", e);
			e.printStackTrace();
			throw new IllegalStateException(
					"Can't read properties from classpath. ",
					e);
		}

		return result;
	}

	/* ***** Implementation ***** */

	public String getProperty(String key) {
		if (this.propertiesApp.containsKey(key)) {
			return this.propertiesApp.getProperty(key);
		} else if (this.propertiesPasswd.containsKey(key)) {
			return this.propertiesPasswd.getProperty(key);
		} else {
			return null;
		}
	}

}
