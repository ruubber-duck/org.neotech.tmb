package org.neotech.tmb;

import org.neotech.tmb.domain.TmRcrdRepositoryImpl;
import org.neotech.tmb.domain.TmRcrdRepository;
import org.neotech.tmb.domain.TmRcrdService;
import org.neotech.tmb.domain.TmRcrdServiceImpl;
import org.neotech.tmb.domain.TmRcrdTask;
import org.neotech.tmb.jdbc.JdbcManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author
 *
 */
public class AppContext {

	/* Class */

	private static final Logger LOG = LoggerFactory.getLogger(AppContext.class);

	private static volatile AppContext instance;

	public static AppContext getInstance() {
		if (instance == null) {
			instance = new AppContext();
		}
		return instance;
	}

	/* Instance */

	private final AppConfig appConfig;
	private final JdbcManager jdbcManager;
	private final TmRcrdRepository tmRcrdRepository;
	private final TmRcrdService tmRcrdService;
	private final TmRcrdTask tmRcrdTask;

	/**
	 * Default
	 */
	private AppContext() {
		super();
		LOG.debug("AppContext...");
		
		this.appConfig = new AppConfig();
		this.jdbcManager = new JdbcManager(this.appConfig);
		this.tmRcrdRepository = new TmRcrdRepositoryImpl(this.jdbcManager);
		this.tmRcrdService = new TmRcrdServiceImpl(this.tmRcrdRepository);
		this.tmRcrdTask = new TmRcrdTask(this.tmRcrdService);
		
		LOG.debug("AppContext done.");
	}

	/* ***** Get & Set ***** */

	public JdbcManager getJdbcManager() {
		return jdbcManager;
	}

	public TmRcrdRepository getTmRcrdRepository() {
		return tmRcrdRepository;
	}

	public TmRcrdService getTmRcrdService() {
		return tmRcrdService;
	}

	public TmRcrdTask getTmRcrdTask() {
		return tmRcrdTask;
	}

	/* ***** Override ***** */

	@Override
	public String toString() {
		return "AppContext [appConfig=" + appConfig + ", jdbcManager="
				+ jdbcManager + ", tmRcrdRepository=" + tmRcrdRepository
				+ ", tmRcrdService=" + tmRcrdService + ", tmRcrdTask="
				+ tmRcrdTask + "]";
	}

}
