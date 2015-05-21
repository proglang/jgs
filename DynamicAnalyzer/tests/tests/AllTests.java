package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.handleStmtTests.AssignFieldsFail;
import tests.handleStmtTests.AssignFieldsSuccess;
import tests.handleStmtTests.AssignLocalsFail;
import tests.handleStmtTests.AssignLocalsSuccess;
import tests.handleStmtTests.IfStmtFail;
import tests.handleStmtTests.IfStmtSuccess;
import tests.handleStmtTests.InvokeFail;
import tests.handleStmtTests.InvokeSuccess;
import tests.handleStmtTests.ReturnStmtFail;
import tests.handleStmtTests.ReturnStmtSuccess;
import tests.handleStmtTests.StaticFieldsFail;
import tests.handleStmtTests.StaticFieldsSuccess;
import tests.handleStmtTests.SwitchStmtFail;
import tests.handleStmtTests.SwitchStmtSuccess;
import tests.handleStmtTests.WhileStmtFail;
import tests.handleStmtTests.WhileStmtSuccess;

import analyzer.level1.JimpleInjectorTest;
import analyzer.level2.LocalMapTest;
import analyzer.level2.ObjectMapTest;

@RunWith(Suite.class)
@SuiteClasses({ LocalMapTest.class, 
				AssignLocalsSuccess.class,
				AssignLocalsFail.class,
				AssignFieldsSuccess.class,
				AssignFieldsFail.class,
				InvokeSuccess.class,
				InvokeFail.class,
				ReturnStmtSuccess.class,
				ReturnStmtFail.class,
				IfStmtSuccess.class,
				IfStmtFail.class,
				WhileStmtSuccess.class,
				WhileStmtFail.class,
				SwitchStmtSuccess.class,
				SwitchStmtFail.class,
				StaticFieldsSuccess.class,
				StaticFieldsFail.class,
				
				ExampleFailScenarios.class,
				ObjectMapTest.class, 
				JimpleInjectorTest.class, 
				
				analyzer.level2.InternalMethods.class


})

public class AllTests {

}
