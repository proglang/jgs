package logging;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * <h1>Console formatter for the SootLogger</h1>
 * 
 * The {@link SootLoggerConsoleFormatter} extends the {@link Formatter} and provides methods to
 * format logged message with the customized {@link SootLoggerLevel} to export them via console. The
 * formatting for the console provides that lines do not exceed the specified length
 * {@link SootLoggerConsoleFormatter#LINE_WIDTH} and all messages are displayed in boxes.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see SootLoggerUtils
 * @see SootLogger
 * @see SootLoggerLevel
 */
public class SootLoggerConsoleFormatter extends Formatter {

	/**
	 * Maximal character number of a single line. I.e. a printed line has to have at most the given
	 * number of characters.
	 */
	private static final int LINE_WIDTH = 100;

	/**
	 * Stores the given message formatted in the given {@link StringBuilder}. The given message will
	 * be divided in lines with maximal {@link SootLoggerConsoleFormatter#LINE_WIDTH} characters and
	 * in front of each line the given prefix will be placed as well as a suffix and a newline
	 * character will be appended to the end of each line. Thus a box will be placed around the
	 * message.
	 * 
	 * @param result
	 *            StringBuilder instance to which the formatted message will be appended.
	 * @param msg
	 *            Message which should be formatted.
	 * @param prefix
	 *            String which will be added to the message at the start of each new line.
	 * @see SootLoggerFileFormatter#format(LogRecord)
	 */
	private static void buildMsgBox(StringBuilder result, String msg, String prefix) {
		List<String> list = reduceToSuitableLineWidth(msg, prefix.length());
		int maxWidth = maxString(list);
		result.append(prefix + "+" + SootLoggerUtils.repeat("-", maxWidth + 2) + "+"
				+ SootLoggerUtils.TXT_LINE_SEPARATOR);
		for (String string : list) {
			result.append(prefix);
			string = "| " + string + SootLoggerUtils.repeat(" ", maxWidth - string.length()) + " |";
			result.append(string);
			result.append(SootLoggerUtils.TXT_LINE_SEPARATOR);
		}
		result.append(prefix + "+" + SootLoggerUtils.repeat("-", maxWidth + 2) + "+"
				+ SootLoggerUtils.TXT_LINE_SEPARATOR);
	}

	/**
	 * Returns the maximal character count of the Strings contained the given list.
	 * 
	 * @param list
	 *            List of strings for which the maximum character count of a string contained by the
	 *            list is returned.
	 * @return The maximal character number of the Strings which are contained by the given list.
	 */
	private static int maxString(List<String> list) {
		int max = 0;
		for (String string : list) {
			max = Math.max(max, string.length());
		}
		return max;
	}

	/**
	 * Method that divides the given message String into single lines Strings and for each single
	 * line including the given prefix will be checked whether it contains at most
	 * {@link SootLoggerConsoleFormatter#LINE_WIDTH} characters if yes, then the line will be stored
	 * in the returned list, if not, then the line will be reduced recursively.
	 * 
	 * @param msg
	 *            Message which should be divided in lines with maximal
	 *            {@link SootLoggerConsoleFormatter#LINE_WIDTH} characters.
	 * @param prefix
	 *            Prefix that will be placed in front of each list element.
	 * @return A list with the message divided into Strings, where each string has at most
	 *         {@link SootLoggerConsoleFormatter#LINE_WIDTH} characters.
	 */
	private static List<String> reduceToSuitableLineWidth(String msg, int prefix) {
		String[] preResult = msg.split(SootLoggerUtils.TXT_LINE_SEPARATOR);
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
	 * Format the given log record and return the formatted string. The resulting formatted String
	 * will include a localized and formatted version of the LogRecord's message field. The format
	 * of the message depends on the log record level. For the {@link SootLoggerLevel#HEADING} level
	 * a heading will be generated (see {@link SootLoggerUtils#generateLogHeading(String, Object[])}
	 * ), for the {@link SootLoggerLevel#STRUCTURE} level a structural message will be generated
	 * (see {@link SootLoggerUtils#generateStructureMessage(String)}) and for all other
	 * {@link SootLoggerLevel} a standard message will be generated (see
	 * {@link SootLoggerConsoleFormatter#buildMsgBox(StringBuilder, String, String)}).
	 * 
	 * @param record
	 *            The log record to be formatted.
	 * @return The formatted log record String.
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 * @see SootLoggerConsoleFormatter#buildMsgBox(StringBuilder, String, String)
	 */
	@Override
	public String format(LogRecord record) {
		StringBuilder result = new StringBuilder();
		if (record.getLevel().equals(SootLoggerLevel.HEADING)) {
			result.append(SootLoggerUtils.generateLogHeading(formatMessage(record),
					record.getParameters()));
		} else if (SootLoggerUtils.isStandardLoggableMessage(record.getLevel())) {
			buildMsgBox(result, formatMessage(record), SootLoggerUtils.TXT_TAB);
		} else if (record.getLevel().equals(SootLoggerLevel.STRUCTURE)) {
			result.append(SootLoggerUtils.generateStructureMessage(formatMessage(record)));
		} else {
			result.append(SootLoggerUtils.generateDefaultMessage(record.getLevel()
					.getLocalizedName(), formatMessage(record)));
		}
		result.append(SootLoggerUtils.handleThrownException(record.getThrown(),
				record.getThreadID()));
		return result.toString();
	}

}
