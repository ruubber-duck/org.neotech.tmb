package org.neotech.tmb.domain;

import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neotech.tmb.AppContext;

public class TmRcrdServiceImplIT {

	private AppContext appContext;
	
	@Before
	public void setUp() throws Exception {
		this.appContext = AppContext.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws InterruptedException {
		TmRcrdService tmRcrdService = this.appContext.getTmRcrdService();
		
		Long count = 0L;
		
		TmRcrd tm = new TmRcrd();
		tm.setTm(Calendar.getInstance().getTime());
		System.out.println("tm = " + tm);
		
		boolean inserted = tmRcrdService.add(tm);
		System.out.println("inserted = " + inserted);
		
		count = tmRcrdService.count();
		System.out.println("count = " + count);
		
		boolean deleted = tmRcrdService.del(tm);
		System.out.println("deleted = " + deleted);
		
		count = tmRcrdService.count();
		System.out.println("count = " + count);
		
		List<TmRcrd> select = tmRcrdService.readAll();
		System.out.println("select = " + select);
	}

}
