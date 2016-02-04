package testmain;

import analyzer.level1.JimpleInjectorTest;
import analyzer.level2.AssignFieldsFail;
import analyzer.level2.AssignFieldsSuccess;
import analyzer.level2.AssignLocalsFail;
import analyzer.level2.AssignLocalsSuccess;
import analyzer.level2.ForStmtFail;
import analyzer.level2.ForStmtSuccess;
import analyzer.level2.IfStmtFail;
import analyzer.level2.IfStmtSuccess;
import analyzer.level2.InternalMethods;
import analyzer.level2.InvokeSuccess;
import analyzer.level2.MultiArrayFail;
import analyzer.level2.MultiArraySuccess;
import analyzer.level2.ReturnStmtFail;
import analyzer.level2.ReturnStmtSuccess;
import analyzer.level2.StaticFieldsFail;
import analyzer.level2.StaticFieldsSuccess;
import analyzer.level2.SwitchStmtFail;
import analyzer.level2.SwitchStmtSuccess;
import analyzer.level2.WhileStmtFail;
import analyzer.level2.WhileStmtSuccess;
import analyzer.level2.storage.LocalMapTest;
import analyzer.level2.storage.ObjectMapTest;
import classfiletests.ArrayRefTest;
import classfiletests.ExtClassesTest;
import classfiletests.FieldsSuccessTest;
import classfiletests.GotoStmtTest;
import classfiletests.IfStmtTest;
import classfiletests.InvokeInternalMethodTest;
import classfiletests.InvokeLibMethodTest;
import classfiletests.MulArrayTest;
import classfiletests.SimpleTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.ExampleFailScenarios;


@RunWith(Suite.class)
@SuiteClasses({ 
    LocalMapTest.class, 
    AssignLocalsSuccess.class,
    AssignLocalsFail.class,
    AssignFieldsSuccess.class,
    AssignFieldsFail.class,
    ForStmtFail.class,
    ForStmtSuccess.class,
    InvokeSuccess.class,
    MultiArrayFail.class,
    MultiArraySuccess.class,
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
    analyzer.level2.storage.ObjectMapTest.class, 
    JimpleInjectorTest.class, 

    InternalMethods.class,
    ObjectMapTest.class,
    LocalMapTest.class,

    ArrayRefTest.class,
    ExtClassesTest.class,
    GotoStmtTest.class,
    IfStmtTest.class,
    InvokeInternalMethodTest.class,
    InvokeLibMethodTest.class,
    MulArrayTest.class,
    SimpleTest.class,
    FieldsSuccessTest.class
    })

public class AllTests {

}
