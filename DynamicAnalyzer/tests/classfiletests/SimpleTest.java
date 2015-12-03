package classfiletests;


import org.junit.Test;

import utils.logging.L1Logger;

import java.util.logging.Logger;



public class SimpleTest{

  Logger logger = L1Logger.getLogger();

  @Test
  public void test() {
    logger.info("Start of executing main.testclasses.Simple");

    ClassRunner.runClass2("Simple");

    logger.info("Finished executing main.testclasses.Simple");
  }
}
