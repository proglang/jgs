package util.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

/***
 * This Handler logs everything into the debugLog.csv file,
 * and uses the {@link CSVFormatter} to format the outcome.
 *
 * @author Karsten Fix, 23.10.17
 */
public class DebugCSVHandler extends FileHandler {

    /**
     * Creates a new DebugCSVHandler, that writes into the debugLog.csv.
     * It implicits adds a CSVFormatter and sets its Level to all,
     * because that is what is wanted here.
     *
     * @throws IOException if there are IO problems opening the files.
     * @throws SecurityException if a security manager exists and if the caller
     *         does not have LoggingPermission("control")
     */
    public DebugCSVHandler() throws IOException, SecurityException {
        super("%h/jgs/debugLog.csv");

       this.setLevel(Level.ALL);
       this.setFormatter(new CSVFormatter());
    }
}
