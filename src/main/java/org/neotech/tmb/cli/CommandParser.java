package org.neotech.tmb.cli;

import java.text.SimpleDateFormat;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.neotech.tmb.AppContext;
import org.neotech.tmb.domain.TmRcrd;
import org.neotech.tmb.domain.TmRcrdService;
import org.neotech.tmb.domain.TmRcrdTask;
import org.neotech.tmb.domain.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author
 *
 */
public class CommandParser {

	/* Class */

	private static final Logger LOG =
			LoggerFactory.getLogger(CommandParser.class);

	public static long DELAY = 500L;
	
	private static final Option OPTION_P;
	private static final Option OPTION_H;
	private static final SimpleDateFormat TIMESTAMP_FORMAT;

	static {
		OPTION_P = new Option("p", "present", false, "Show database content. ");
		OPTION_H = new Option("h", "help", false, "Show help. ");
		TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	/* Instance */

	private final CommandLineParser parser;
	private final HelpFormatter helpFormatter;
	private final Options options;

	private boolean up = true;
	private TmRcrdTask task = null;

	/**
	 * Default
	 */
	public CommandParser() {
		super();
		this.parser = new DefaultParser();
		this.helpFormatter = new HelpFormatter();

		this.options = new Options();
		this.options.addOption(OPTION_H);
		this.options.addOption(OPTION_P);
	}

	/**
	 * Destructor
	 */
	public void _CommandParser() {
		this.up = false;
		if (this.task != null && this.task.isWriting()) {
			this.task.writeEnd();
		}
	}

	/* ***** Routines ***** */

	private void printf(String msg, Object... args) {
		System.out.printf(msg + "\r\n", args);
	}

	private void doPrintHelp() {
		LOG.debug(">> doPrintHelp()");

		this.helpFormatter.printHelp(80,
				"tmb",
				"This application writes and reads timestamp. ",
				this.options,
				"Press <q + Enter> to stop.");

		LOG.debug("<< doPrintHelp()");
	}

	private void doPrintData() {
		LOG.debug(">> doPrintData()");

		TmRcrdService service = AppContext.getInstance().getTmRcrdService();

		try {
			List<TmRcrd> tmList = service.readAll();

			if (tmList == null || tmList.size() == 0) {
				this.printf("Database is empty. ");
			} else {
				this.printf("Database content: ");

				int i = 0;
				for (TmRcrd tm : tmList) {
					this.printf("|\t [%d] \t| %s |",
							++i,
							TIMESTAMP_FORMAT.format(tm.getTm()));
				}
			}

		} catch (ServiceException e) {
			LOG.error("Can't read data. ", e);
			this.printf("Can't read data. Error %s. \r\n", e);
		}

		LOG.debug("<< doPrintData()");
	}

	private void waitForInput(Function<String, Boolean> onInput) {
		Thread inputThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Scanner scan = new Scanner(System.in);

				while (up && scan.hasNextLine()) {
					String input = scan.nextLine();

					if (input != null) {
						CommandParser.this.up = onInput.apply(input);
					}

					try {
						Thread.sleep(DELAY);
					} catch (InterruptedException e) {
						LOG.error("Console input thread is interrapted. ", e);
					}
				}

				scan.close();
			}
		});

		inputThread.start();

		try {
			inputThread.join();
		} catch (InterruptedException e) {
			LOG.error("Console input thread is interrapted. ", e);
		}
	}

	private void doWrite() {
		LOG.debug(">> doWrite()");

		this.printf("Start to writing timestamps to the database. ");

		this.task = AppContext.getInstance().getTmRcrdTask();
		this.task.setOnNextTm((Deque<TmRcrd> q) -> {
			if (q != null && q.size() != 0) {
				TmRcrd rc = q.peek();
				this.printf("Next TM [%s] done: queue.size = %d. ",
						TIMESTAMP_FORMAT.format(rc.getTm()),
						q.size());
			}

		});
		this.task.setOnPersistTmDone((Deque<TmRcrd> q) -> {
			if (q != null && q.size() != 0) {
				TmRcrd rc = q.peek();
				this.printf("Save TM [%s] done: queue.size = %d. ",
						TIMESTAMP_FORMAT.format(rc.getTm()),
						q.size());
			}
		});
		this.task.setOnPersistTmError((Deque<TmRcrd> q) -> {
			if (q != null && q.size() != 0) {
				TmRcrd rc = q.peek();
				this.printf("Save TM [%s] error: queue.size = %d. ",
						TIMESTAMP_FORMAT.format(rc.getTm()),
						q.size());
			}
		});

		this.task.writeBegin();

		this.waitForInput((String input) -> {
			switch (input) {
			case "q":
				if (this.task != null && this.task.isWriting()) {
					this.task.writeEnd();
				}
				return false;
			default:
				return true;
			}
		});

		LOG.debug("<< doWrite()");
	}

	/* ***** Implementation ***** */

	/**
	 * 
	 * @param args
	 */
	public void execute(String[] args) {
		try {
			CommandLine cli = parser.parse(options, args);

			if (cli.hasOption("h")) {
				this.doPrintHelp();
			} else if (cli.hasOption("p")) {
				this.printf("-------------------------------------------------------------------------------");
				this.doPrintHelp();
				this.printf("-------------------------------------------------------------------------------");
				this.doPrintData();
			} else {
				this.printf("-------------------------------------------------------------------------------");
				this.doPrintHelp();
				this.printf("-------------------------------------------------------------------------------");
				this.doWrite();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			this.doPrintHelp();
		}

	}

}
