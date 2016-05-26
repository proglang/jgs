package classfiletests;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import classfiletests.utils.ClassRunner;
import utils.exceptions.IllegalFlowException;
import utils.logging.L1Logger;

import java.util.logging.Logger;


/**
 * 
 * @author koenigr
 */
public class FieldsSuccessTest {

  Logger logger = L1Logger.getLogger();

  @Test
  public void test() {
    logger.info("Start of executing main.testclasses.FieldsSuccess");

      ClassRunner.testClass("FieldsSuccess", IllegalFlowException.class);

}