package utils.logging;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class L1Formatter extends Formatter{
	// TODO: ansi colors are just a demo hack, here
public static final String ANSI_RESET = "\u001B[0m";
public static final String ANSI_BLACK = "\u001B[30m";
public static final String ANSI_RED = "\u001B[31m";
	@Override
	public String format(LogRecord rec) {
		StringBuffer buf = new StringBuffer(1000);

		if ( rec.getLevel() == Level.SEVERE) {
			buf.append(ANSI_RED);
		}

		buf.append(formatMessage(rec));
		if ( rec.getLevel() == Level.SEVERE) {
			buf.append(ANSI_RESET);
		}
		buf.append("\n\t\t\t\t\t (");
		buf.append(rec.getLevel());
		buf.append(" - ");
		buf.append(rec.getSourceClassName() + "#");
		buf.append(rec.getSourceMethodName());
		buf.append(")");
		buf.append("\n");

		return buf.toString();
	}
}
