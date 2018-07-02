package org.neotech.tmb.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.neotech.tmb.domain.exception.RepositoryException;
import org.neotech.tmb.jdbc.JdbcManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author
 *
 */
public class TmRcrdRepositoryImpl implements TmRcrdRepository {

	/* Class */
	
	private static final Logger LOG = LoggerFactory.getLogger(TmRcrdRepositoryImpl.class); 
	
	public static final String NAME_DB;
	public static final String NAME_TABLE;
	public static final String SQL_INSERT;
	public static final String SQL_DELETE;
	public static final String SQL_SELECT;
	public static final String SQL_COUNT;
	public static final String[] COLUMNS = {TmRcrdRepository.COLUMN_TM};
	
	static {
		NAME_DB = "`tmb`";
		NAME_TABLE = "`TmRcrd`";
		SQL_INSERT =
				"INSERT INTO " + NAME_DB + "." + NAME_TABLE
						+ " (`tm`) VALUES (?)";
		SQL_DELETE =
				"DELETE FROM " + NAME_DB + "." + NAME_TABLE
						+ " WHERE `tm` = ?;";
		SQL_SELECT =
				"SELECT `TmRcrd`.`tm` FROM " + NAME_DB + "." + NAME_TABLE + ";";
		SQL_COUNT = "SELECT count(*) FROM " + NAME_DB + "." + NAME_TABLE + ";";
	}

	/* Inject */

	private JdbcManager jdbcManager;

	/**
	 * Default
	 */
	public TmRcrdRepositoryImpl() {
		super();
	}

	/**
	 * 
	 * @param jdbcManager
	 */
	public TmRcrdRepositoryImpl(JdbcManager jdbcManager) {
		super();
		this.jdbcManager = jdbcManager;
	}

	/* ***** Implementation ***** */

	@Override
	public int insert(TmRcrd o) throws RepositoryException {
		LOG.debug(">> insert(TmRcrd o = {})", o);
		
		if (o == null) {
			throw new IllegalArgumentException("NULL argument is not allowed. ");
		}
		
		int result = 0;

		try {			
			Connection conn = this.jdbcManager.getConnection();
			PreparedStatement st =
					(PreparedStatement) conn.prepareStatement(SQL_INSERT);

			st.setTimestamp(1, new Timestamp(o.getTm().getTime()));
			
			result = st.executeUpdate();	
		} catch (SQLException e) {
			LOG.warn("Exception in JDBC operation. ", e);
			throw new RepositoryException("Exception in JDBC operation. ", e);
		}

		LOG.debug("<< insert(TmRcrd o): {}", result);
		return result;
	}

	@Override
	public int delete(TmRcrd o) throws RepositoryException {
		LOG.debug(">> delete(TmRcrd o = {})", o);
		
		if (o == null) {
			throw new IllegalArgumentException("NULL argument is not allowed. ");
		}
		
		int result = 0;
		
		try {
			Connection conn = this.jdbcManager.getConnection();
			PreparedStatement st =
					(PreparedStatement) conn.prepareStatement(SQL_DELETE);

			st.setTimestamp(1, new Timestamp(o.getTm().getTime()) );

			result = st.executeUpdate();

		} catch (SQLException e) {			
			LOG.warn("Exception in JDBC operation. ", e);
			throw new RepositoryException("Exception in JDBC operation. ", e);			
		}

		LOG.debug("<< delete(TmRcrd o): {}", result);
		return result;
	}

	@Override
	public Long count() throws RepositoryException {
		LOG.debug(">> count()");
		
		Long result = null;

		try {
			Connection conn = this.jdbcManager.getConnection();
			PreparedStatement st =
					(PreparedStatement) conn.prepareStatement(SQL_COUNT);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result = rs.getLong(1);
			}
		} catch (SQLException e) {
			LOG.warn("Exception in JDBC operation. ", e);
			throw new RepositoryException("Exception in JDBC operation. ", e);
		}

		LOG.debug("<< count(): {}", result);
		return result;
	}

	@Override
	public List<TmRcrd> select() throws RepositoryException {
		LOG.debug(">> select()");

		List<TmRcrd> result = new ArrayList<TmRcrd>();

		try {
			Connection conn = this.jdbcManager.getConnection();
			PreparedStatement st =
					(PreparedStatement) conn.prepareStatement(SQL_SELECT);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				TmRcrd tmRcrd = new TmRcrd(rs.getTimestamp(1));
				result.add(tmRcrd);
			}		
		} catch (SQLException e) {
			LOG.warn("Exception in JDBC operation. ", e);
			throw new RepositoryException("Exception in JDBC operation. ", e);
		}

		LOG.debug("<< select(): {}", result);
		return result;
	}

	/* ***** Get & Set ***** */

	public JdbcManager getJdbcManager() {
		return jdbcManager;
	}

	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}

	/* ***** Override ***** */

	@Override
	public String toString() {
		return "TmRcrdRepositoryImpl [jdbcManager=" + jdbcManager + "]";
	}

}
