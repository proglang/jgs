package logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * <h1>File formatter for the SootLogger</h1>
 * 
 * The {@link SootLoggerFileFormatter} extends the {@link Formatter} and provides methods to
 * format logged message with the customized {@link SootLoggerLevel} to export them via file.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see SootLoggerUtils
 * @see SootLogger
 * @see SootLoggerLevel
 */
public class SootLoggerFileFormatter extends Formatter {

	/**
	 * Stores the given message formatted in the given {@link StringBuilder}. The given message will
	 * be divided at each newline character and in front of each line the given prefix will be
	 * placed as well as a newline character will be appended to the end of each line.
	 * 
	 * @param result
	 *            StringBuilder instance to which the formatted message will be appended.
	 * @param msg
	 *            Message which should be formatted.
	 * @param prefix
	 *            String which will be added to the message at the start of each new line.
	 * @see SootLoggerFileFormatter#format(LogRecord)
	 */
	private static void createReadableMsg(StringBuilder result, String msg, String prefix) {
		List<String> list = new ArrayList<String>(Arrays.asList(msg
				.split(SootLoggerUtils.TXT_LINE_SEPARATOR)));
		for (String string : list) {
			result.append(prefix);
			result.append(string);
			result.append(SootLoggerUtils.TXT_LINE_SEPARATOR);
		}
	}

	/**
	 * Format the given log record and return the formatted string. The resulting formatted String
	 * will include a localized and formatted version of the LogRecord's message field. The format
	 * of the message depends on the log record level. For the {@link SootLoggerLevel#HEADING} level
	 * a heading will be generated (see {@link SootLoggerUtils#generateLogHeading(String, Object[])}
	 * ), for the {@link SootLoggerLevel#STRUCTURE} level a structural message will be generated
	 * (see {@link SootLoggerUtils#generateStructureMessage(String)}) and for all other
	 * {@link SootLoggerLevel} a standard message will be generated (see
	 * {@link SootLoggerFileFormatter#createReadableMsg(StringBuilder, String, String)}).
	 * 
	 * @param record
	 *            The log record to be formatted.
	 * @return The formatted log record String.
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 * @see SootLoggerFileFormatter#createReadableMsg(StringBuilder, String, String)
	 */
	@Override
	public String format(LogRecord record) {
		StringBuilder result = new StringBuilder();
		if (record.getLevel().equals(SootLoggerLevel.HEADING)) {
			result.append(SootLoggerUtils.generateLogHeading(formatMessage(record),
					record.getParameters()));
		} else if (SootLoggerUtils.isStandardLoggableMessage(record.getLevel())) {
			createReadableMsg(result, formatMessage(record), SootLoggerUtils.TXT_TAB);
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
