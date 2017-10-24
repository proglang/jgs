package util.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;

/**
 * This class just provides the method of
 * Formatting the Date from millis to String
 */
abstract class DateFormatter extends Formatter {

    /**
     * Formats the given Time in Millis into a formatted String.
     * @param millis The milliseconds since some when.
     * @return A Nice Formatted String.
     */
     String formatTime(Long millis) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date rd = new Date(millis);
        return df.format(rd);
    }
}
