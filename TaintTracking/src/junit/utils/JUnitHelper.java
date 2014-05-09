package junit.utils;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import main.AnalysisType;

/**
 * DOC
 * 
 * @author Thomas Vogel
 * @version 0.3
 */
public class JUnitHelper {

	private final static String PATH = "./../Testcases/src";

	/**
	 * DOC
	 */
	private final static String[] ARG_CLASSPATH = new String[] { "-cp",
			"/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/rt.jar" };

	/**
	 * DOC
	 */
	private final static String[] ARG_DEF_CLASSPATH = new String[] { "-def-classpath", "./../Testcases/bin" };

	/**
	 * DOC
	 */
	private final static String[] ARG_PROGRAM_CLASSPATH = new String[] { "-program-classpath", "./bin" };

	/**
	 * DOC
	 */
	private final static String[] ARG_SOURCE_PATH = new String[] { "-source-path", PATH };

	/**
	 * DOC
	 */
	private final static String[] ARG_LOG_LEVELS = new String[] { "-log-levels", "off" };

	private static final String[] ARG_CONSTRAINTS = new String[] {"-constraints"};

	/**
	 * DOC
	 * 
	 * @param className
	 * @param type
	 * @return
	 */
	public static String[] generateAnalysisArgumentsForAnalyzedFile(String className, AnalysisType type) {
		
		return concat(ARG_CLASSPATH, ARG_DEF_CLASSPATH, ARG_PROGRAM_CLASSPATH, ARG_SOURCE_PATH, ARG_LOG_LEVELS, (type.equals(AnalysisType.CONSTRAINTS)) ? ARG_CONSTRAINTS : new String[] {} ,new String[] { "-main-class",
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

	public static String getSourcePath() {
		Path basePath = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
		Path absolutePath = basePath.resolve(PATH).normalize();
		return absolutePath.toString();
	}

	public static <U> boolean equalContentOfLists(Collection<U> list1, Collection<U> list2) {
		if (list1.size() != list2.size()) return false;
		for (U e : list1) {
			if (!list2.contains(e)) return false;
		}
		for (U e : list2) {
			if (!list1.contains(e)) return false;
		}
		return true;
	}

	public static <U> Set<U> mkList(U[] array) {
		return new HashSet<U>(Arrays.asList(array));
	}

}
