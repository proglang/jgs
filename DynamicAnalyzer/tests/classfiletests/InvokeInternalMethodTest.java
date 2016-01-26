package classfiletests;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.exceptions.IllegalFlowException;
import utils.logging.L1Logger;

import java.util.logging.Logger;


public class InvokeInternalMethodTest {

  Logger logger = L1Logger.getLogger();

  @Test
  public void test() {
    logger.info("Start of executing main.testclasses.InvokeInternalMethod");
    try {
      ClassRunner.runClass2("InvokeInternalMethod");
    } catch (Exception e) {
        assertEquals(e.getCause().getClass(), IllegalFlowException.class);
        logger.info("Successfully found an illegal flow");
    }
    logger.info("Finished executing main.testclasses.InvokeInternalMethod");
  }
}