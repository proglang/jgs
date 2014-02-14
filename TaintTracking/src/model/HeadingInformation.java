package model;

import logging.SecurityLogger;
import logging.SootLogger;
import logging.SootLoggerConsoleFormatter;
import logging.SootLoggerFileFormatter;

/**
 * <h1>Heading information for a Logger heading</h1>
 * 
 * The class {@link HeadingInformation} provides the information how many tabs should be
 * inserted in front of an heading. This information is necessary for all kinds of
 * {@link SootLogger} and also for the corresponding LoggerFormatter.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see SecurityLogger
 * @see SootLogger
 * @see SootLoggerConsoleFormatter
 * @see SootLoggerFileFormatter
 */
public class HeadingInformation {

	/** The number of tabs that will be inserted before the heading. */
	private int tabs;

	/**
	 * Constructor of the HeadingInformation which stores the information how many tabs
	 * should be inserted in front of a heading.
	 * 
	 * @param tabs
	 *            Number of tabs that will be inserted before the heading.
	 */
	public HeadingInformation(int tabs) {
		this.tabs = tabs;
	}

	/**
	 * Method return the number of tabs that will be inserted before the heading.
	 * 
	 * @return The number of tabs that will be inserted before the heading.
	 */
	public int getTabs() {
		return this.tabs;
	}
}