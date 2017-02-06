package utils.staticResults.superfluousInstrumentation;

import utils.exceptions.SuperfluousInstrumentationException;

/**
 * Created by Nicolas Müller on 06.02.17.
 */
public class ActiveController extends PassivController {
    @Override
    /**
     * If called, aborts execution with {@link SuperfluousInstrumentationException}. Overwrites method in {@link PassivController}.
     */
    public void abortIfActive() {
        throw new SuperfluousInstrumentationException("Superfluous instrumentation detected");
    }
}
