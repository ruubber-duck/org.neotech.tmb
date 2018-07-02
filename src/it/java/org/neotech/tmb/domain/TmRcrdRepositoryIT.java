package org.neotech.tmb.domain;

import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neotech.tmb.AppContext;

public class TmRcrdRepositoryIT {
	
	private AppContext appContext;

	@Before
	public void setUp() throws Exception {
		this.appContext = AppContext.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		TmRcrdRepository tmRcrdReposotory = this.appContext.getTmRcrdRepository();
		
		Long count = 0L;
		
		TmRcrd tm = new TmRcrd();
		tm.setTm(Calendar.getInstance().getTime());
		System.out.println("tm = " + tm);
		
		int inserted = tmRcrdReposotory.insert(tm);
		System.out.println("inserted = " + inserted);
		
		count = tmRcrdReposotory.count();
		System.out.println("count = " + count);
		
		int deleted = tmRcrdReposotory.delete(tm);
		System.out.println("deleted = " + deleted);
		
		count = tmRcrdReposotory.count();
		System.out.println("count = " + count);
		
		List<TmRcrd> select = tmRcrdReposotory.select();
		System.out.println("select = " + select);
	}

}
