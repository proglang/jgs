package logging;

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

	  static public void setup() throws IOException {

	    // get the global logger to configure it
	    Logger logger = Logger.getLogger(LOGGER_NAME);
	    logger.setUseParentHandlers(false);

	    // remove standard handler
	    Handler[] handlers = logger.getHandlers();
	    if (handlers.length > 0) { 
	    	for (Handler h : handlers) {
	    		logger.removeHandler(h);
	    	}
	    }

	    logger.setLevel(Level.ALL);
	    handler = new ConsoleHandler();
	    handler.setLevel(Level.ALL);

	    // create a formatter
	    formatter = new L2Formatter();
	    handler.setFormatter(formatter);
	    logger.addHandler(handler);
	    

	  }
	  
	  static public Logger getLogger() {
		return Logger.getLogger(LOGGER_NAME);		  
	  }
}
