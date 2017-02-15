package utils.staticResults.superfluousInstrumentation;

/**
 * Created by Nicolas Müller on 06.02.17.
 */
public class ControllerFactory {
    public static PassivController returnSuperfluousInstrumentationController(boolean isActive, int expectedException) {
        if (isActive) {
            return new ActiveController(expectedException);
        } else {
            return new PassivController();
        }
    }
}
