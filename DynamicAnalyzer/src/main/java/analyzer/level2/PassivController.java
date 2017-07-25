package analyzer.level2;

/**
 * Created by Nicolas Müller on 06.02.17.
 * Base class for the {@link ActiveController} superfluous instrumentation controller class.
 */
public class PassivController {

    /**
     * Do nothing, since this is the passiv catcher. Overwritten by {@link ActiveController}.
     */
    public void abortIfActiveAndExceptionIsType(int expectedException) {

    }

}
