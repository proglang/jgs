package classfiletests;


import org.junit.Test;
import utils.logging.L1Logger;

import java.util.logging.Logger;


public class IfStmtTest {

  Logger logger = L1Logger.getLogger();

  @Test
  public void test() {
    logger.info("Start of executing main.testclasses.IfStmt");

    ClassRunner.runClass2("IfStmt");

    logger.info("Finished executing main.testclasses.IfStmt");
  }
}