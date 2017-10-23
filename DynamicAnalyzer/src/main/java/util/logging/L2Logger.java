package util.logging;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import analyzer.level2.HandleStmt;

public class L2Logger {
	  static private ConsoleHandler handler;
	  static private L2Formatter formatter;
	  private static final String LOGGER_NAME = HandleStmt.class.getName();
	  private static final Logger logger;
      private static final Level logLevel;
	static {
		logger = Logger.getLogger(LOGGER_NAME);
		String wantVerbose = System.getenv("JGS_VERBOSE_LOGGING");
        logLevel = (wantVerbose == null
					|| wantVerbose.isEmpty()
					|| wantVerbose.equals("0")) ?
				   Level.WARNING :
				   Level.ALL;
		logger.setLevel(logLevel);
	}

	  static public void setup() throws IOException {

	    // get the global logger to configure it
	    logger.setUseParentHandlers(false);

	    // remove standard handler
	    Handler[] handlers = logger.getHandlers();
	    if (handlers.length > 0) { 
	    	for (Handler h : handlers) {
	    		logger.removeHandler(h);
	    	}
	    }

	    handler = new ConsoleHandler();
	    handler.setLevel(logLevel);

	    // create a formatter
	    formatter = new L2Formatter();
	    handler.setFormatter(formatter);
	    logger.addHandler(handler);
	    

	  }
	  
	  static public Logger getLogger() {
		return logger;
	  }
}
