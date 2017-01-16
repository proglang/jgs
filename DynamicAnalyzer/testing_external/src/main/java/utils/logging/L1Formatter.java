package utils.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class L1Formatter extends Formatter{

	@Override
	public String format(LogRecord rec) {
		StringBuffer buf = new StringBuffer(1000);

		buf.append(formatMessage(rec));
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
