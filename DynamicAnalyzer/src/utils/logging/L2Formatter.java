package logging;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class L2Formatter extends Formatter{

	@Override
	public String format(LogRecord rec) {
	    StringBuffer buf = new StringBuffer(1000);

	    // TODO
	    
	    buf.append(rec.getLevel());
	    buf.append(": ");
	    buf.append(formatMessage(rec));
	    buf.append("\n");

	    
	    return buf.toString();
	}

}
