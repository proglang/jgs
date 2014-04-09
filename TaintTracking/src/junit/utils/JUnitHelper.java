package junit.utils;

/**
 * DOC
 * 
 * @author Thomas Vogel
 * @version 0.3
 */
public class JUnitHelper {

	/**
	 * DOC
	 */
	private final static String[] CLASSPATH = new String[] { "-cp",
			".:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/rt.jar" };

	/**
	 * DOC
	 */
	private final static String[] INPUT = new String[] { "-src-prec", "c" };

	/**
	 * DOC
	 */
	private final static String[] LINE_NUMBERS = new String[] { "-keep-line-number" };

	/**
	 * DOC
	 */
	private final static String[] LOG_LEVELS = new String[] { "-log-levels", "off" };

	/**
	 * DOC
	 */
	private final static String[] NO_BODIES = new String[] { "-no-bodies-for-excluded" };

	/**
	 * DOC
	 */
	private final static String[] OUTPUT = new String[] { "-f", "jimple", "-d", "./../sootOutput" };

	/**
	 * DOC
	 */
	private final static String[] PREPEND = new String[] { "-pp" };

	/**
	 * DOC
	 */
	private final static String[] WHOLE_PROGRAM = new String[] { "-w" };
//	
//	/**
//	 * DOC
//	 */
//	private final static String[] VERBOSE = new String[] { "-v" };

	/**
	 * DOC
	 * 
	 * @param className
	 * @return
	 */
	public static String[] generateAnalysisArgumentsForAnalyzedFile(String className) {
		return concat(CLASSPATH, PREPEND, INPUT, OUTPUT, NO_BODIES, LOG_LEVELS, LINE_NUMBERS, WHOLE_PROGRAM, new String[] { "-main-class",
				className }, new String[] { className });
	}

	/**
	 * DOC
	 * 
	 * @param arrays
	 * @return
	 */
	private static String[] concat(String[]... arrays) {
		int lengh = 0;
		for (String[] array : arrays) {
			lengh += array.length;
		}
		String[] result = new String[lengh];
		int pos = 0;
		for (String[] array : arrays) {
			for (String element : array) {
				result[pos] = element;
				pos++;
			}
		}
		return result;
	}

}
