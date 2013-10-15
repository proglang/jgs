package model;

import logging.SecurityLogger;
import logging.SootLogger;
import logging.SootLoggerConsoleFormatter;
import logging.SootLoggerFileFormatter;

/**
 * <h1>Extended heading information for a Logger heading</h1>
 * 
 * The class {@link ExtendedHeadingInformation} provides the informations how many tabs should be
 * inserted in front of an heading as well as the file name and the source line number which are
 * related to the heading. The class {@link ExtendedHeadingInformation} extends the normal heading
 * information class {@link HeadingInformation}, to store all additional required information. These
 * informations are necessary for all kinds of {@link SootLogger} and also for the corresponding
 * LoggerFormatter.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see HeadingInformation
 * @see SecurityLogger
 * @see SootLogger
 * @see SootLoggerConsoleFormatter
 * @see SootLoggerFileFormatter
 */
public class ExtendedHeadingInformation extends HeadingInformation {

	/** The file name, which is related to the heading. */
	private String fileName = "unknown";
	/** The source line number, which is related to the heading. */
	private long srcLn = 0;

	/**
	 * Constructor of the ExtendedHeadingInformation which stores the informations how many tabs
	 * should be inserted in front of a heading as well as the source line number and the file name
	 * which are related to the heading.
	 * 
	 * @param tabs
	 *            Number of tabs that will be inserted before the heading.
	 * @param srcLn
	 *            Source line number, which is related to the heading.
	 * @param fileName
	 *            File name, which is related to the heading.
	 */
	public ExtendedHeadingInformation(int tabs, long srcLn, String fileName) {
		super(tabs);
		this.srcLn = srcLn;
		this.fileName = fileName;
	}

	/**
	 * Method returns the file name which is related to the heading.
	 * 
	 * @return File name, which is related to the heading.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Method returns the source line number which is related to the heading.
	 * 
	 * @return Source line number, which is related to the heading.
	 */
	public long getSrcLn() {
		return srcLn;
	}

}
