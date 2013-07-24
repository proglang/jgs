package logging;


import java.util.logging.Level;

/**
 * Class that provides a level hierarchy which matches the requirements of the Soot Analysis.
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class SootLoggerLevel extends Level {

	
	private static final long serialVersionUID = 4614063859303032668L;
	
	/**
	 * Level which is used to print exceptions. The SootLogger provides therefore a method that
	 * takes a Throwable.
	 */
	public static final Level EXCEPTION = new SootLoggerLevel("EXCEPTION", Level.SEVERE.intValue());
	/** Level which is used to print errors. E.g. if the analysis detects a (fatal) error. */
	public static final Level ERROR = new SootLoggerLevel("ERROR", Level.WARNING.intValue());
	/** Level which is used to print warnings. */
	public static final Level WARNING = new SootLoggerLevel("WARNING", Level.INFO.intValue());
	/** Level which is used to print informations. */
	public static final Level INFORMATION = new SootLoggerLevel("INFORMATION",
			Level.CONFIG.intValue());
	/**
	 * Level which is used to print structure elements such us a heading for the analysis of a new
	 * method.
	 */
	public static final Level STRUCTURE = new SootLoggerLevel("STRUCTURE",
			Level.CONFIG.intValue() - 100);
	/** Level which is used to print configuration information. */
	public static final Level CONFIGURATION = new SootLoggerLevel("CONFIGURATION",
			Level.FINE.intValue());
	/** Level which is used to print debug information. */
	public static final Level DEBUG = new SootLoggerLevel("DEBUG", Level.FINER.intValue());
	/** Level which is used to print the jimple source code. */
	public static final Level JIMPLE = new SootLoggerLevel("JIMPLE", Level.FINEST.intValue());
	
	public static final Level SIDEEFFECT = new SootLoggerLevel("SIDEEFFECT", 200);
	
	public static final Level SECURITY = new SootLoggerLevel("SECURITY", 100);
	
	public static final Level SECURITYCHECKER = new SootLoggerLevel("SECURITYCHECKER", 99);
	
	/** Level which should be used only internal for headings. */
	protected static final Level HEADING = new SootLoggerLevel("HEADING", 42);
	/**
	 * Constructor of a SootLoggerLevel.
	 * 
	 * @param name
	 *            Name of the Level.
	 * @param value
	 *            Integer value of the Level.
	 */
	protected SootLoggerLevel(String name, int value) {
		super(name, value);
	}

}
