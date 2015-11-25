package classfiletests;


import org.junit.Test;
import utils.logging.L1Logger;

import java.util.logging.Logger;


public class MulArrayTest {

  Logger logger = L1Logger.getLogger();

  @Test
  public void test() {
    logger.info("Start of executing main.testclasses.MulArray");

    ClassRunner.runClass2("MulArray");

    logger.info("Finished executing main.testclasses.MulArray");
  }
}
