package org.neotech.tmb.domain;

import java.util.List;
import org.neotech.tmb.domain.exception.RepositoryException;

/**
 * 
 * @author 
 *
 */
public interface TmRcrdRepository {
	
	public static final String COLUMN_TM = "tm";

	public int insert(TmRcrd o) throws RepositoryException;	
	
	public int delete(TmRcrd o) throws RepositoryException;
	
	public List<TmRcrd> select() throws RepositoryException;
	
	public Long count() throws RepositoryException;
	
}
