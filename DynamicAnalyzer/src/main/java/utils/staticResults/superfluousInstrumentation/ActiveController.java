package utils.staticResults.superfluousInstrumentation;

import utils.exceptions.NSUCheckCalledException;

/**
 * Created by Nicolas Müller on 06.02.17.
 */
public class ActiveController extends PassivController {
    @Override
    /**
     * If called, aborts execution with {@link NSUCheckCalledException}. Overwrites method in {@link PassivController}.
     */
    // braucht noch argumente, methode, stmt
    public void abortIfActive() {
        throw new NSUCheckCalledException("Somewhere, checkGlobalPC or checkLocalPC was called!");
    }
}
