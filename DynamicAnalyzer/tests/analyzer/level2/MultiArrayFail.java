package analyzer.level2;


import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import utils.exceptions.IllegalFlowException;
import utils.logging.L2Logger;

public class MultiArrayFail {

	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}
	


	@Test(expected = IllegalFlowException.class)
	public void readArray() {
		
		logger.info("readArray fail test started");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		/*
		 * x = a[i]
		 * check x >= lpc
		 * level(x) = (i, a, gpc, a_i)
		 * 
		 * The following is the Jimple representation of: 
		 * String[][] arr = new String[][]{{"a"},{"b"}};
		 * String x = arr[1][0];
		 */ 
		hs.pushLocalPC(SecurityLevel.HIGH);
		
		String[][] arr = new String[2][];
		hs.addArrayToObjectMap(arr);
		hs.addLocal("String[][]_arr");
		
		
		String[] inner1 = new String[1];
		hs.addArrayToObjectMap(inner1);
		hs.addLocal("String[]_inner1");
		
		String[] inner2 = new String[1];
		hs.addArrayToObjectMap(inner2);
		hs.addLocal("String[]_inner2");
		
		hs.setLevelOfArrayField(inner1, Integer.toString(0), "String[]_inner1");
		inner1[0] = "a";
		
		hs.setLevelOfArrayField(inner2, Integer.toString(0), "String[]_inner2");
		inner2[0] = "b";
		
		hs.addLevelOfLocal("String[]_inner1");
		hs.setLevelOfArrayField(arr, Integer.toString(0), "String[]_arr");
		arr[0] = inner1;
		
		hs.addLevelOfLocal("String[]_inner2");
		hs.setLevelOfArrayField(arr, Integer.toString(1), "String[]_arr");
		arr[1] = inner2;
		
		hs.addLevelOfArrayField(arr, Integer.toString(1));
		hs.setLevelOfLocal("String[]_tmp");
		String[] tmp = arr[1];
		
		hs.addLevelOfArrayField(tmp, Integer.toString(0));
		hs.setLevelOfLocal("String_x");
		@SuppressWarnings("unused")
		String x = tmp[0];
		
		hs.popLocalPC();
		hs.close();
		
		logger.info("readArray fail test finished");
	}
	
	@Test(expected = IllegalFlowException.class)
	public void writeArray() {

		logger.info("writeArray fail test started");
		
		HandleStmtForTests hs = new HandleStmtForTests();

		/*
		 * a[i] = x;
		 * check a_i >= join(gpc, a, i)
		 * level(a[i]) = join(a,i,x)
		 * 
		 * The following is the Jimple representation of: 
		 * String[][] arr = new String[][]{{"a"},{"b"}};
		 * String x = arr[1][0];
		 */ 
		
		
		
		String[][] arr = new String[2][];
		hs.addLocal("String[][]_arr");
		hs.addArrayToObjectMap(arr);
		
		String[] inner1 = new String[1];
		hs.addLocal("String[][]_inner1");
		hs.addArrayToObjectMap(inner1);
		
		String[] inner2 = new String[1];
		hs.addLocal("String[][]_inner2");
		hs.addArrayToObjectMap(inner2);
		
		hs.setLevelOfArrayField(inner1, Integer.toString(0), "String[]_inner1");
		inner1[0] = "a";
		
		hs.setLevelOfArrayField(inner2, Integer.toString(0), "String[]_inner2");
		inner2[0] = "b";
		
		hs.addLevelOfLocal("String[]_inner1");
		hs.setLevelOfArrayField(arr, Integer.toString(0), "String[][]_arr");
		arr[0] = inner1;
		
		hs.addLevelOfLocal("String[]_inner2");
		hs.setLevelOfLocal("String[][]_arr", SecurityLevel.HIGH);
		hs.setLevelOfArrayField(arr, Integer.toString(1), "String[][]_arr");
		arr[1] = inner2;
		
		hs.addLevelOfArrayField(arr, Integer.toString(0));
		hs.addLocal("String[]_tmp");
		hs.setLevelOfLocal("String[]_tmp");
		String[] tmp = arr[0];
		
		hs.setLevelOfArrayField(tmp, Integer.toString(0), "String[]_tmp");
		tmp[0] = "a";
		
		assertEquals("a", arr[0][0]);
		
		hs.close();
		
		logger.info("writeArray fail test finished");
	}

}
