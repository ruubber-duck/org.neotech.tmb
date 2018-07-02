package org.neotech.tmb.domain;

import java.util.List;

import org.neotech.tmb.domain.exception.ServiceException;

/**
 * 
 * @author 
 *
 */
public interface TmRcrdService {
	
	public boolean add(TmRcrd o) throws ServiceException;
	
	public boolean del(TmRcrd o) throws ServiceException;
	
	public Long count() throws ServiceException;
	
	public List<TmRcrd> readAll() throws ServiceException;
	
}
