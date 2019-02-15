import end2endtest.AllFakeAnalysisTests;
import end2endtest.compileToJarTests;
import analyzer.level2.AssignFieldsFail;
import analyzer.level2.AssignFieldsSuccess;
import analyzer.level2.AssignLocalsFail;
import analyzer.level2.AssignLocalsSuccess;
import analyzer.level2.ForStmtFail;
import analyzer.level2.ForStmtSuccess;
import analyzer.level2.IfStmtFail;
import analyzer.level2.IfStmtSuccess;
import analyzer.level2.InvokeSuccess;
import analyzer.level2.MultiArrayFail;
import analyzer.level2.MultiArraySuccess;
import analyzer.level2.Nico;
import analyzer.level2.ReturnStmtFail;
import analyzer.level2.ReturnStmtSuccess;
import analyzer.level2.SimpleSuccess;
import analyzer.level2.StaticFieldsFail;
import analyzer.level2.StaticFieldsSuccess;
import analyzer.level2.SwitchStmtFail;
import analyzer.level2.SwitchStmtSuccess;
import analyzer.level2.WhileStmtFail;
import analyzer.level2.WhileStmtSuccess;
import analyzer.level2.storage.LocalMapTest;
import analyzer.level2.storage.LowMiddleHighTest;
import analyzer.level2.storage.ObjectMapTest;
import analyzer.level2.storage.SecurityOptionalTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import end2endtest.AllEndToEndTests;


@RunWith(Suite.class)
@SuiteClasses({ 
    // Tests for public HandleStmt methods
    AssignLocalsSuccess.class,
    AssignLocalsFail.class,
    AssignFieldsSuccess.class,
    AssignFieldsFail.class,
    ForStmtFail.class,
    ForStmtSuccess.class,
    InvokeSuccess.class,
    MultiArrayFail.class,
    MultiArraySuccess.class,
    Nico.class,
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
    SimpleSuccess.class,

    analyzer.level2.storage.ObjectMapTest.class, 
   

    // Tests for protected HandleStmt methods
    ObjectMapTest.class,
    LocalMapTest.class,
    
    // Test for SecurityOptinal Container
    SecurityOptionalTest.class,
    
    // Tests for LowMiddleHigh Lattice
    //LowMiddleHighTest.class,

    // Tests for valid bytecode of testclasses
    AllEndToEndTests.class,
    // SingleEndToEndTest.class		// SingleE2E is subset of AllE2E-Tests, so no need to rerun!

    // Test for commandline arguments and correct path output
    compileToJarTests.class,

        // Test with fake static analysis
        AllFakeAnalysisTests.class,
    })

public class RunAllTests {

}
