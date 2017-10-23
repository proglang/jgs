package util.logging;

import java.util.logging.*;

/**
 * This Formatter is used for detailed CSV Log-Messages,
 * such that it could be opened with some Table Calculation Program,
 * that is then able to sort or filter out things,
 * that might be helpful for debugging things.
 *
 * @author Karsten Fix, 23.10.17
 */
public class CSVFormatter extends DateFormatter {

    /** Defines the Separator for CSV Column Separation */
    private final char SEP = ';';

    /**
     * This Method is called once, when the CSVHandler (or a
     * other Handler using this CSVFormatter) is initialized.
     * @param h The Handler, that uses this Formatter.
     * @return The HeadString for this CSVFormatter. <br>
     *     Means: The Headings for the LogRecords Rows.
     */
    @Override
    public String getHead(Handler h) {
        StringBuilder sb = new StringBuilder();
        sb.append("Time").append(SEP);
        sb.append("Class").append(SEP);
        sb.append("Method").append(SEP);
        sb.append("Level").append(SEP);
        sb.append("Message").append(SEP);
        return sb.toString();
    }

    /**
     * Specifies how one {@link LogRecord} should look like.
     * @param logRecord The Simple Message, without any location things.
     * @return A String that has all formatted.
     */
    @Override
    public String format(LogRecord logRecord) {
        StringBuilder sb = new StringBuilder();
        // First the Time Stamp
        sb.append(formatTime(logRecord.getMillis())).append(SEP);
        // Then the Source Class Name.
        sb.append(logRecord.getSourceClassName()).append(SEP);
        // Then the Method Name, causing the log
        sb.append(logRecord.getSourceMethodName()).append(SEP);
        // The Level of the Log Message
        sb.append(logRecord.getLevel()).append(SEP);

        // In the end The Message Content.
        sb.append(logRecord.getMessage());
        // Record End
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}
