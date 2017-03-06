import utils.parser.ArgParser;
import utils.parser.ArgumentContainer;

/**
 * This is the main entry point for jgs.
 * Launches the static analyzer, then the dynamic analyzer.
 */
public class Main {
    public static void main(String[] args) {
        ArgumentContainer sootOptionsContainer = ArgParser.getSootOptions(args);
        // run static

        // run dynamic
    }
}
