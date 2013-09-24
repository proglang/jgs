package logging;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import model.ExtendedHeadingInformation;
import model.MinimalHeadingInformation;


/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class SootLoggerConsoleFormatter extends Formatter {
	
	/** */
	private static final String CLOSE_TAG = " ] --|";
	/** */
	private static final String OPEN_TAG = "|-- [ ";
	/** */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	/** */
	private static final String TAB = "   ";
	/** */
	private static final int TRACE_ELEMENTS_MAX = 15;
	/** */
	private static final int LINE_WIDTH = 100;
	
	/**
	 * 
	 * @param record
	 * @return
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord record) {
		StringBuilder result = new StringBuilder();
		if (record.getLevel().equals(SootLoggerLevel.HEADING)) {
			String prefix = "";
			String additionalInfo = "";
			if (record.getParameters() != null && record.getParameters().length == 1) {
				Object obj = record.getParameters()[0];
				if (obj instanceof ExtendedHeadingInformation) {
					ExtendedHeadingInformation info = (ExtendedHeadingInformation) obj;
					prefix = repeat(TAB, info.getTabs());
					additionalInfo = " (" + info.getFileName() + ".java:" + info.getSrcLn() + ")";
				} else if (obj instanceof MinimalHeadingInformation) {
					MinimalHeadingInformation info = (MinimalHeadingInformation) obj;
					prefix = repeat(TAB, info.getTabs());
				}
			}
			result.append(prefix + OPEN_TAG + formatMessage(record) + additionalInfo + CLOSE_TAG + LINE_SEPARATOR);
		} else if (record.getLevel().equals(SootLoggerLevel.EXCEPTION)) {
			buildMsgBox(result, formatMessage(record), TAB);
		} else if (record.getLevel().equals(SootLoggerLevel.ERROR)) {
			buildMsgBox(result, formatMessage(record), TAB);
		} else if (record.getLevel().equals(SootLoggerLevel.WARNING)) {
			buildMsgBox(result, formatMessage(record), TAB);
		} else if (record.getLevel().equals(SootLoggerLevel.INFORMATION)) {
			buildMsgBox(result, formatMessage(record), TAB);
		} else if (record.getLevel().equals(SootLoggerLevel.STRUCTURE)) {
			result.append(OPEN_TAG + formatMessage(record) + CLOSE_TAG + LINE_SEPARATOR);
		} else if (record.getLevel().equals(SootLoggerLevel.DEBUG)) {
			buildMsgBox(result, formatMessage(record), TAB);
		} else if (record.getLevel().equals(SootLoggerLevel.CONFIGURATION)) {
			buildMsgBox(result, formatMessage(record), TAB);
		} else if (record.getLevel().equals(SootLoggerLevel.SIDEEFFECT)) {
			buildMsgBox(result, formatMessage(record), TAB);
		} else if (record.getLevel().equals(SootLoggerLevel.SECURITY)) {
			buildMsgBox(result, formatMessage(record), TAB);
		} else if (record.getLevel().equals(SootLoggerLevel.SECURITYCHECKER)) {
			buildMsgBox(result, formatMessage(record), TAB);
		} else {
			result.append(record.getLevel().getLocalizedName() + ":" + TAB);
			result.append(formatMessage(record));
			result.append(LINE_SEPARATOR);
		}
		handleThrownException(result, record.getThrown(), record.getThreadID());
		return result.toString();
	}

	/**
	 * @param result
	 * @param thrown
	 * @param threadID
	 */
	private void handleThrownException(StringBuilder result, Throwable thrown, int threadID) {
		if (thrown != null) {
			String thread = "unknown";
			for (Thread t : Thread.getAllStackTraces().keySet()) {
				if (t.getId() == threadID) {
					thread = t.getName();
				}
			}
			result.append(TAB + "Exception in thread \"" + thread + "\" "+ thrown.getClass().getName());
			result.append(": " + thrown.getLocalizedMessage() + LINE_SEPARATOR);
			StackTraceElement[] stackTraceElements = thrown.getStackTrace();
			for(int i = 0; i < TRACE_ELEMENTS_MAX && i < stackTraceElements.length; i++) {
				StackTraceElement stackTraceElement = stackTraceElements[i];
				result.append(TAB + TAB +"at " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName());
				result.append("(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() +")");
				result.append(LINE_SEPARATOR);
			}
			if (TRACE_ELEMENTS_MAX <= stackTraceElements.length) {
				result.append(TAB + TAB + "..." + LINE_SEPARATOR);
			}
		}
	}
	
	/**
	 * 
	 * @param result
	 * @param msg
	 * @param prefix
	 */
	private static void buildMsgBox(StringBuilder result, String msg, String prefix) {
		List<String> list = reduceToSuitableLineWidth(msg, prefix.length());
		int maxWidth = maxString(list);
		result.append(prefix + "+" + repeat("-", maxWidth + 2) + "+" + LINE_SEPARATOR);
		for (String string : list) {
			result.append(prefix);
			string = "| " + string + repeat(" ", maxWidth - string.length()) + " |";
			result.append(string);
			result.append(LINE_SEPARATOR);
		}
		result.append(prefix + "+" + repeat("-", maxWidth + 2) + "+" + LINE_SEPARATOR);
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	private static int maxString(List<String> list) {
		int max = 0;
		for (String string : list) {
			max = Math.max(max, string.length());
		}
		return max;
	}
	
	/**
	 * 
	 * @param msg
	 * @param prefix
	 * @return
	 */
	private static List<String> reduceToSuitableLineWidth(String msg, int prefix) {
		String[] preResult = msg.split(LINE_SEPARATOR);
		List<String> result = new ArrayList<String>();
		for (String subMsg : preResult) {
			subMsg = subMsg.trim();
			if (subMsg.length() > LINE_WIDTH - prefix) {
				int indexLastSpace = subMsg.lastIndexOf(" ", LINE_WIDTH);
				String partOne = subMsg.substring(0, indexLastSpace);
				String partTwo = subMsg.substring(indexLastSpace);
				result.add(partOne);
				result.addAll(reduceToSuitableLineWidth(partTwo, prefix));
			} else {
				result.add(subMsg);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param str
	 * @param n
	 * @return
	 */
	private static String repeat(String str, int n) {
		if (n < 0)
			return "";
		return new String(new char[n]).replace("\0", str);
	}
}
