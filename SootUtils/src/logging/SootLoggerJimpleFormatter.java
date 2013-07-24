package logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;


/**
 * 
 * @author Thomas Vogel
 *
 */
public class SootLoggerJimpleFormatter extends Formatter {
	
	private static final String CLOSE_TAG = " ] --|";
	private static final String OPEN_TAG = "|-- [ ";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String TAB = "   ";
	
	@Override
	public String format(LogRecord record) {
		StringBuilder result = new StringBuilder();
		if (record.getLevel().equals(SootLoggerLevel.JIMPLE)) {
			result.append(formatMessage(record) + LINE_SEPARATOR);
		} else {
			result.append(OPEN_TAG + record.getLevel().getLocalizedName() + ":" + TAB);
			result.append(formatMessage(record));
			result.append(CLOSE_TAG + LINE_SEPARATOR);
		}
		return result.toString();
	}

}
