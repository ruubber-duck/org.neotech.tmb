package org.neotech.tmb.domain;

import java.util.List;

import org.neotech.tmb.domain.exception.RepositoryException;
import org.neotech.tmb.domain.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author
 *
 */
public class TmRcrdServiceImpl implements TmRcrdService {

	/* Class */
	
	private static final Logger LOG = LoggerFactory.getLogger(TmRcrdServiceImpl.class); 
	
	/* Instance */
	
	private TmRcrdRepository tmRepository;

	/**
	 * Default
	 * 
	 * @param tmReposotory
	 */
	public TmRcrdServiceImpl(TmRcrdRepository tmRepository) {
		super();
		this.tmRepository = tmRepository;

	}

	/* ***** Implementation ***** */

	@Override
	public boolean add(TmRcrd o) throws ServiceException {
		LOG.debug(">> add(TmRcrd o = {})", o);
		
		boolean result = false;
		
		try {
			int rs = this.tmRepository.insert(o);
			result = (rs != 0);
		} catch (RepositoryException e) {
			LOG.warn("Exception in service. ", e);
			throw new ServiceException(e);
		}		
		
		LOG.debug(">> add(TmRcrd o): {}", result);
		return result;
	}

	@Override
	public boolean del(TmRcrd o) throws ServiceException {
		LOG.debug(">> del(TmRcrd o = {})", o);
		
		boolean result = false;
		
		try {
			int rs = this.tmRepository.delete(o);
			result = (rs != 0);
		} catch (RepositoryException e) {
			LOG.warn("Exception in service. ", e);
			throw new ServiceException(e);
		} 
		
		LOG.debug(">> del(TmRcrd o): {}", result);
		return result;		
	}

	@Override
	public Long count() throws ServiceException {
		LOG.debug(">> count()");
		
		Long result = null;
		
		try {
			result = this.tmRepository.count();
		} catch (RepositoryException e) {
			LOG.warn("Exception in service. ", e);
			throw new ServiceException(e);
		} 
		
		LOG.debug("<< count(): {}", result);
		return result;		
	}

	@Override
	public List<TmRcrd> readAll() throws ServiceException {	
		LOG.debug(">> readAll()");
		
		List<TmRcrd> result = null;
		
		try {
			result = this.tmRepository.select();
		} catch (RepositoryException e) {
			LOG.warn("Exception in service. ", e);
			throw new ServiceException(e);
		} 		
		
		LOG.debug(">> readAll(): {}", result);
		return result;
	}

}
