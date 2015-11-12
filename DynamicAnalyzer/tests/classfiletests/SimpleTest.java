package classfiletests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.junit.Test;

import utils.logging.L1Logger;

public class SimpleTest {

	Logger logger = L1Logger.getLogger();
	
	@Test
	public void test() {
		logger.info("Start of executing main.testclasses.Simple");
		
		ClassRunner.runClass("main.testclasses.Simple");

		logger.info("Finished executing main.testclasses.Simple");
	}

}
