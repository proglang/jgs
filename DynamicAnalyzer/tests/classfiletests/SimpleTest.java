package classfiletests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.exceptions.IllegalFlowException;
import utils.logging.L1Logger;

import java.util.logging.Logger;



public class SimpleTest{

  Logger logger = L1Logger.getLogger();

  @Test
  public void test() {
    logger.info("Start of executing main.testclasses.Simple");
    try {
      ClassRunner.runClass2("Simple");
    } catch (Exception e) {
      assertEquals(e.getCause().getClass(), IllegalFlowException.class);
      logger.info("Successfully found an illegal flow");
    }
    logger.info("Finished executing main.testclasses.Simple");
  }
}
