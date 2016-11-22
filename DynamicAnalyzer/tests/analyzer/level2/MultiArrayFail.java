package analyzer.level2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import utils.exceptions.IllegalFlowException;
import utils.logging.L2Logger;

import java.util.logging.Logger;

public class MultiArrayFail {

	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}
	

	/**
	 * NSU IFExept. Test with arrays.
	 */
	@Test(expected = IllegalFlowException.class)
	public void readArray() {
		
		logger.info("readArray fail test started");
		
		HandleStmt hs = new HandleStmt();
		/*
		 * x = a[i]
		 * check x >= lpc
		 * level(x) = (i, a, gpc, a_i)
		 * 
		 * The following is the Jimple representation of: 
		 * String[][] arr = new String[][]{{"a"},{"b"}};
		 * String x = arr[1][0];
		 */ 
		hs.pushLocalPC(SecurityLevel.top(), 123);
		
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
		
		hs.addLocal("String[]_arr");
		hs.joinLevelOfLocalAndAssignmentLevel("String[]_inner1");
		hs.setLevelOfArrayField(arr, Integer.toString(0), "String[]_arr");
		arr[0] = inner1;
		
		hs.joinLevelOfLocalAndAssignmentLevel("String[]_inner2");
		hs.setLevelOfArrayField(arr, Integer.toString(1), "String[]_arr");
		arr[1] = inner2;
		
		hs.addLocal("String[]_tmp");
		hs.joinLevelOfArrayFieldAndAssignmentLevel(arr, Integer.toString(1));
		hs.checkLocalPC("String[]_tmp");
		hs.setLevelOfLocal("String[]_tmp");
		String[] tmp = arr[1];
		
		hs.addLocal("String_x");
		hs.joinLevelOfArrayFieldAndAssignmentLevel(tmp, Integer.toString(0));
		
		@SuppressWarnings("unused")
		String x = tmp[0];
		hs.initializeVariable("String_x");		// Comment out to remove NSU IFExept
		hs.checkLocalPC("String_x");
		hs.setLevelOfLocal("String_x");			// IllegalFlowException thrown here, NSU
		
		hs.popLocalPC(123);
		hs.close();
		
		logger.info("readArray fail test finished");
	}
	
	@Test(expected = IllegalFlowException.class)
	public void writeArray() {

		logger.info("writeArray fail test started");
		
		HandleStmt hs = new HandleStmt();

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
		hs.addLocal("String[]_inner1");
		hs.addArrayToObjectMap(inner1);
		
		String[] inner2 = new String[1];
		hs.addLocal("String[]_inner2");
		hs.addArrayToObjectMap(inner2);
		
		hs.setLevelOfArrayField(inner1, Integer.toString(0), "String[]_inner1");
		inner1[0] = "a";
		
		hs.setLevelOfArrayField(inner2, Integer.toString(0), "String[]_inner2");
		inner2[0] = "b";
		
		hs.joinLevelOfLocalAndAssignmentLevel("String[]_inner1");
		hs.setLevelOfArrayField(arr, Integer.toString(0), "String[][]_arr");
		arr[0] = inner1;
		
		hs.joinLevelOfLocalAndAssignmentLevel("String[]_inner2");
		hs.setLevelOfLocal("String[][]_arr", SecurityLevel.top());
		hs.setLevelOfArrayField(arr, Integer.toString(1), "String[][]_arr");
		arr[1] = inner2;
		
		hs.joinLevelOfArrayFieldAndAssignmentLevel(arr, Integer.toString(0));
		hs.addLocal("String[]_tmp");
		hs.checkLocalPC("String[]_tmp");
		hs.setLevelOfLocal("String[]_tmp");
		String[] tmp = arr[0];
		
		hs.setLevelOfArrayField(tmp, Integer.toString(0), "String[]_tmp");
		tmp[0] = "a";
		
		assertEquals("a", arr[0][0]);
		
		hs.close();
		
		logger.info("writeArray fail test finished");
	}

}
