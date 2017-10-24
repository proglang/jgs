package util.logging;

import analyzer.level1.BodyAnalyzer;

import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * This Class allows access to a Logger, that is configured, as
 * wanted.
 * @author Karsten Fix, 23.10.17
 * @version 2.0
 */
public class L1Logger {

	/** Defines the Logger for the Level 1, that is the instrumentation */
	public static final Logger logger = Logger.getLogger("analyzer");

}
