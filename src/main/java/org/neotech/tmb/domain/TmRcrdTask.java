package org.neotech.tmb.domain;

import java.util.Calendar;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

import org.neotech.tmb.domain.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author
 *
 */
public class TmRcrdTask {

	/* Class */

	private static final long DELAY_PRODUSER = 5000L;
	private static final long DELAY_CONSUMER = 1000L;	
	private static final Logger LOG = LoggerFactory.getLogger(TmRcrdTask.class); 

	/* Inject */

	private TmRcrdService tmRcrdService;

	/* Instance */

	private final ConcurrentLinkedDeque<TmRcrd> queue;
	private final Thread threadProducer;
	private final Thread threadConsumer;
	private boolean writing = true;

	private Consumer<Deque<TmRcrd>> onNextTm;
	private Consumer<Deque<TmRcrd>> onPersistTmDone;
	private Consumer<Deque<TmRcrd>> onPersistTmError;
	
	/**
	 * 
	 * @param tmRcrdService
	 */
	public TmRcrdTask(TmRcrdService tmRcrdService) {
		super();		
		this.tmRcrdService = tmRcrdService;
		this.queue = new ConcurrentLinkedDeque<TmRcrd>();

		this.threadProducer = new Thread(new Runnable() {
			@Override
			public void run() {
				TmRcrdTask.this.taskNextTm();
			}
		});
		this.threadProducer.setName("ThreadProducer");

		this.threadConsumer = new Thread(new Runnable() {
			@Override
			public void run() {
				TmRcrdTask.this.taskPersistTm();
			}
		});
		this.threadConsumer.setName("ThreadConsumer");

		this.threadProducer.setDaemon(true);
		this.threadConsumer.setDaemon(true);
	}

	/* ***** Routines ***** */

	private void taskNextTm() {
		LOG.debug(">> taskNextTm()");
		
		while (this.writing) {
			LOG.debug("Generate next TmRcrd object. ");
			
			TmRcrd tmRcrd = new TmRcrd(Calendar.getInstance().getTime());
			this.queue.offer(tmRcrd);
			
			if (this.onNextTm != null) {
				this.onNextTm.accept(this.queue);
			}
			
			LOG.debug("TmRcrd: {}.", tmRcrd);
			LOG.debug("Queue.size: {}.", this.queue.size());
			LOG.debug("Queue: {}.", this.queue);
			
			try {
				LOG.debug("Producer is waiting for {} ms.", DELAY_PRODUSER);
				Thread.sleep(DELAY_PRODUSER);
			} catch (InterruptedException e) {
				LOG.error("Producer thread is interapted. ", e);
				e.printStackTrace();
			}
		}

		LOG.debug("<< taskNextTm()");
	}

	private void taskPersistTm() {
		LOG.debug(">> taskPersistTm()");
		
		while (this.writing) {
			LOG.debug("Persist next TmRcrd object. ");
			LOG.debug("taskPersistTm: Queue.size = {}.", this.queue.size());
			LOG.debug("taskPersistTm: Queue = {}.", this.queue);
			
			TmRcrd tmRcrd = this.queue.peek();
			LOG.debug("taskPersistTm: TmRcrd   {}.", tmRcrd);
			
			try {
				if (this.tmRcrdService != null && tmRcrd != null && this.tmRcrdService.add(tmRcrd)) {
					if (this.onPersistTmDone != null) {
						this.onPersistTmDone.accept(this.queue);
					}
					this.queue.remove();
				}				
			} catch (ServiceException e) {
				LOG.warn("Failed to persist next TmRcrd. ", e);
				
				if (this.onPersistTmError != null) {
					this.onPersistTmError.accept(this.queue);
				}
			}
			
			try {
				LOG.debug("Consumer is waiting for {} ms.", DELAY_CONSUMER);
				Thread.sleep(DELAY_CONSUMER);
			} catch (InterruptedException e) {
				LOG.error("Consumer thread is interapted. ", e);
				e.printStackTrace();
			}
		}

		LOG.debug("<< taskPersistTm()");
	}

	/* ***** Implementation ***** */

	public boolean writeBegin() {
		LOG.debug(">> writeBegin()");
		
		LOG.info("Starting timestamp writing thread... ");
		this.writing = true;
		this.threadConsumer.start();
		this.threadProducer.start();
		LOG.info("Timestamp writing thread is stated. ");
		
		LOG.debug("<< writeBegin()");
		return false;
	}

	public boolean writeEnd() {
		LOG.debug(">> writeEnd()");
		
		LOG.info("Stopping timestamp task thread... ");
		this.writing = false;
		try {
			this.threadProducer.join(DELAY_PRODUSER);
			this.threadConsumer.join(DELAY_CONSUMER);
		} catch (InterruptedException e) {			
			e.printStackTrace();
			LOG.error("Timestamp writing thread is interrupted. ", e);
		}
		LOG.info("Timestamp writing task is stoped. ");
		
		LOG.debug("<< writeEnd()");
		return false;
	}

	/* ***** Get & Set ***** */

	public boolean isWriting() {
		return writing;
	}

	public Consumer<Deque<TmRcrd>> getOnNextTm() {
		return onNextTm;
	}

	public void setOnNextTm(Consumer<Deque<TmRcrd>> onNextTm) {
		this.onNextTm = onNextTm;
	}

	public Consumer<Deque<TmRcrd>> getOnPersistTmDone() {
		return onPersistTmDone;
	}

	public void setOnPersistTmDone(Consumer<Deque<TmRcrd>> onPersistTmDone) {
		this.onPersistTmDone = onPersistTmDone;
	}

	public Consumer<Deque<TmRcrd>> getOnPersistTmError() {
		return onPersistTmError;
	}

	public void setOnPersistTmError(Consumer<Deque<TmRcrd>> onPersistTmError) {
		this.onPersistTmError = onPersistTmError;
	}
	
	
	
}
