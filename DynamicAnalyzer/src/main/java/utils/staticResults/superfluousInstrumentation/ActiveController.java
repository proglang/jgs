package utils.staticResults.superfluousInstrumentation;

import utils.exceptions.NSUCheckCalledException;

/**
 * Created by Nicolas Müller on 06.02.17.
 */
public class ActiveController extends PassivController {
    private int kindOfException;
    public ActiveController(int kindOfException) {
        this.kindOfException = kindOfException;
    }

    @Override
    /**
     * If called, aborts execution with {@link NSUCheckCalledException}. Overwrites method in {@link PassivController}.
     */
    // braucht noch argumente, methode, stmt
    public void abortIfActiveAndExceptionIsType(int expectedException) {
        if (expectedException == this.kindOfException) {
            throw new NSUCheckCalledException("Somewhere, checkGlobalPC or checkLocalPC was called!");
        }
    }
}
