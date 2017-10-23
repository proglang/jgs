package util.logging;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * This Formatter tries to visualise the Console Log Messages
 * in a more elegant way.
 *
 * @author Karsten Fix, 23.10.17
 */
public class ASCIFormatter extends DateFormatter {

	@Override
	public String format(LogRecord rec) {
		// Characters, Codes that are used more often.
		final String ESC = "\u001B[";
		final String end = "m";
		final String SEP = "\t";

		StringBuilder sb = new StringBuilder();

		// Selecting Color of output due to Level
		if ( rec.getLevel() == Level.SEVERE)
			sb.append(ESC).append(41).append(end)
		      .append(ESC).append(30).append(end);
		else if ( rec.getLevel() == Level.WARNING)
			sb.append(ESC).append(31).append(end);

		// Beginning with the time
		sb.append(formatTime(rec.getMillis())).append(SEP);
		sb.append(rec.getLevel()).append(SEP);
		sb.append(rec.getMessage());

		sb.append(System.lineSeparator());
		if (rec.getLevel() == Level.SEVERE || rec.getLevel() == Level.WARNING)
			sb.append(ESC).append(0).append(end);
		return sb.toString();
	}
}
