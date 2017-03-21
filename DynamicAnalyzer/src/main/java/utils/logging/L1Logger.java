package utils.logging;

import analyzer.level2.HandleStmt;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class L1Logger {
	private static ConsoleHandler handler;
	private static L1Formatter formatter;
	private static final String LOGGER_NAME = HandleStmt.class.getName();
	private static Logger logger;

	/**
	 * Setup the logger.
	 * @param loggerlevel Define maximum level.
	 */
	public static void setup(Level loggerlevel) throws IOException {

		// get the global logger to configure it
		logger = Logger.getLogger(LOGGER_NAME);
		logger.setUseParentHandlers(false);

		// remove standard handler
		Handler[] handlers = logger.getHandlers();
		if (handlers.length > 0) { 
			for (Handler h : handlers) {
				logger.removeHandler(h);
			}
		}

		logger.setLevel(loggerlevel);
		handler = new ConsoleHandler();
		handler.setLevel(loggerlevel);

		// create a formatter
		formatter = new L1Formatter();
		handler.setFormatter(formatter);
		logger.addHandler(handler);    
	}
	  
	public static Logger getLogger() {
		return logger;
	}
}
