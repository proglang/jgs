package classfiletests;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.exceptions.IllegalFlowException;
import utils.logging.L1Logger;

import java.util.logging.Logger;


public class ArrayRefTest {

  Logger logger = L1Logger.getLogger();

  @Test
  public void test() {
    logger.info("Start of executing main.testclasses.ArrayRef");
    Exception catchedException = null;
    try {
      ClassRunner.runClass2("ArrayRef");
    } catch (Exception e) {
      catchedException = e;
      logger.info("Successfully found an illegal flow");
    }
    assertEquals(catchedException.getCause().getClass(),
        IllegalFlowException.class);
    logger.info("Finished executing main.testclasses.ArrayRef");
  }
}