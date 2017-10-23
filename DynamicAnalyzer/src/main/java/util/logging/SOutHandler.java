package util.logging;

import java.util.logging.StreamHandler;

/**
 * This handler is for the End-User.
 * It prints on the StdOut, <b>not</b> in the Sys.err, as {@link java.util.logging.ConsoleHandler}
 */
public class SOutHandler extends StreamHandler {

    /**
     * Creates a new SOutHandler for the System.out Stream.
     * Using the {@link ASCIFormatter}.
     */
    public SOutHandler () {
        super(System.out, new ASCIFormatter());
    }
}
