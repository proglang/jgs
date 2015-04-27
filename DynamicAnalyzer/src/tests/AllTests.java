package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ LocalMapTest.class, 
				tests.handleStmtTests.assignLocalsSuccess.class,
				tests.handleStmtTests.assignLocalsFail.class,
				tests.handleStmtTests.internalMethods.class,
				tests.handleStmtTests.assignFieldsSuccess.class,
				tests.handleStmtTests.assignFieldsFail.class,
				tests.handleStmtTests.invokeSuccess.class,
				tests.handleStmtTests.invokeFail.class,
				tests.handleStmtTests.returnStmtSuccess.class,
				tests.handleStmtTests.returnStmtFail.class,
				tests.handleStmtTests.ifStmtSuccess.class,
				tests.handleStmtTests.ifStmtFail.class,
				tests.handleStmtTests.whileStmtSuccess.class,
				tests.handleStmtTests.whileStmtFail.class,
				tests.handleStmtTests.switchStmtSuccess.class,
				tests.handleStmtTests.switchStmtFail.class,
				ObjectMapTest.class, 
				JimpleInjectorTest.class })

public class AllTests {

}
