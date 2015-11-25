package classfiletests;


import org.junit.Test;
import utils.logging.L1Logger;

import java.util.logging.Logger;


public class GotoStmtTest {

  Logger logger = L1Logger.getLogger();

  @Test
  public void test() {
    logger.info("Start of executing main.testclasses.GotoStmt");

    ClassRunner.runClass2("GotoStmt");

    logger.info("Finished executing main.testclasses.GotoStmt");
  }
}