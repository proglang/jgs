package analyzer.level2;

import static org.junit.Assert.assertEquals;

import analyzer.level2.HandleStmtForTests;
import analyzer.level2.SecurityLevel;

import org.junit.Before;
import org.junit.Test;
import tests.testClasses.TestSubClass;
import utils.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;


public class InternalMethods {

  Logger logger = L2Logger.getLogger();
  HandleStmtUtils hsu = new HandleStmtUtils();

  @Before
  public void init() {
    HandleStmtForTests.init();
  }

  @Test
  public void checkLocalPcTest() {
    logger.log(Level.INFO, "LOCAL PC TEST STARTED");

    HandleStmtForTests hs = new HandleStmtForTests();

    hs.addLocal("int_x");

    // Level(x) = LOW, Level(lpc) = LOW
    assertEquals(true, hsu.checkLocalPC("int_x"));

    // Level(x) = HIGH, Level(lpc) = LOW
    hs.makeLocalHigh("int_x");
    assertEquals(true, hsu.checkLocalPC("int_x"));

    // Level(x) = HIGH, Level(lpc) = HIGH
    hs.setLocalPC(SecurityLevel.HIGH);
    assertEquals(true, hsu.checkLocalPC("int_x"));

    // Level(x) = LOW, Level(lpc) = HIGH
    hs.makeLocalLow("int_x");
    assertEquals(false, hsu.checkLocalPC("int_x"));

    hs.close();
 
 
    logger.log(Level.INFO, "LOCAL PC TEST FINISHED");
  }

  @Test
  public void joinLocalsTest() {
    logger.log(Level.INFO, "JOIN LOCALS TEST STARTED");

    HandleStmtForTests hs = new HandleStmtForTests();
    hs.addLocal("int_x", SecurityLevel.LOW);
    hs.addLocal("int_y", SecurityLevel.HIGH);
    hs.addLocal("int_z", SecurityLevel.LOW);
    assertEquals(SecurityLevel.LOW, hsu.joinLocals("int_x"));
    assertEquals(SecurityLevel.HIGH, hsu.joinLocals("int_x", "int_y"));
    assertEquals(SecurityLevel.HIGH, hsu.joinLocals("int_x", "int_y", "int_z"));


    hs.close();

    logger.log(Level.INFO, "JOIN LOCALS TEST FINISHED");
  }

  @Test
  public void argumentsListTest() {
    logger.log(Level.INFO, "ARGUMENTS LIST TEST STARTED");

    HandleStmtForTests hs = new HandleStmtForTests();

    hs.addLocal("TestSubClass_xy");

    hs.addLocal("int_i1");
    hs.addLocal("int_i2");
    hs.addLocal("int_i3");
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;

    TestSubClass xy = new TestSubClass();
    
    hs.storeArgumentLevels("int_i1", "int_i2", "int_i3");
    xy.methodWithArgs(i1, i2, i3);

    hs.close();

    logger.log(Level.INFO, "ARGUMENTS LIST TEST FINISHED");
  }
}
