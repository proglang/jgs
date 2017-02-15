package utils.staticResults.superfluousInstrumentation;

import utils.exceptions.AssignArgumentToLocalExcpetion;
import utils.exceptions.InternalAnalyzerException;
import utils.exceptions.LocalPcCalledException;

/**
 * A Controller to check if superfluous instrumentation is present. Extends {@link PassivController}, which is its
 * passive counterpart that does nothing.
 */
public class ActiveController extends PassivController {
    private int kindOfException;
    public ActiveController(int kindOfException) {
        this.kindOfException = kindOfException;
    }

    @Override
    /**
     * If called, aborts execution with {@link LocalPcCalledException}. Overwrites method in {@link PassivController}.
     * expectedException is where the caller specifies which exception we expect, see {@link ExpectedException}.
     * Exception is only thrown if expectation of caller matches the kind of expection this controller is looking for
     * If controller is not expecting any exception at all (e.g. this.kindOfexception == 0),
     * it throws exceptions allways.
     */
    public void abortIfActiveAndExceptionIsType(int expectedException) {
        if (expectedException == this.kindOfException || this.kindOfException == 0) {
            switch (expectedException) {
                case 0:
                    throw new InternalAnalyzerException("Some method in the HandleStmt requested to" +
                            " throw an exception of type 0 - which is no exception!!");
                case 1:
                    throw new InternalAnalyzerException("Some method in the HandleStmt requested to" +
                            "throw an exception of type 1 - which is IllegalFlowexception, which" +
                            "should not be thrown by this controller!");
                case 2:
                    throw new LocalPcCalledException("Check local PC was called somewhere with an active controller");
                case 3:
                    throw new AssignArgumentToLocalExcpetion("A sec value of an argument was assigned needlessly" +
                            "to a local while the controller was active");
                default:
                    throw new InternalAnalyzerException("Unknown type of exception" + expectedException);
            }
        }
    }
}
