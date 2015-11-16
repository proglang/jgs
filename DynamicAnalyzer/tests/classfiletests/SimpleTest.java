package classfiletests;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.junit.Test;

import utils.exceptions.IllegalFlowException;
import utils.logging.L1Logger;

public class SimpleTest{
	


	Logger logger = L1Logger.getLogger();
	
	@Test
	public void test() {
		logger.info("Start of executing main.testclasses.Simple");
		try {
		  ClassRunner.runClass2("Simple.class");
	/*	} catch(VerifyError e) {
			ClassRunner.printByteCode();
			e.printStackTrace();
			System.out.println("ERROR");*/
		} finally {
			
		}
		logger.info("Finished executing main.testclasses.Simple");
	}
	}
