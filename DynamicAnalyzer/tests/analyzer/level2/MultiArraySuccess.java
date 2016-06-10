package analyzer.level2;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import utils.logging.L2Logger;

public class MultiArraySuccess {

	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmtForTests.init();
	}
	
	@Test
	public void createArray() {
		
		logger.info("createArray success test started");
		
		HandleStmtForTests hs = new HandleStmtForTests();

		String[][] twoD = new String[2][2];
		hs.addArrayToObjectMap(twoD);

		assertEquals(1, hs.getNumberOfElements());
		assertEquals(2, hs.getNumberOfFields(twoD));
		
		/*
		 * The following is the Jimple representation of:
	     * String[][][] threeD = new String[][][] {{{"e"}},{{"f"}},{{"g"}}};
		 */
		String[][][] threeD = new String[3][][]; 
		String[][] tmp1 = new String[1][];
		String[][] tmp2 = new String[1][];
		String[][] tmp3 = new String[1][];
		String[] tmp4 = new String[1];
		String[] tmp5 = new String[1];
		String[] tmp6 = new String[1];
		tmp4[0] = "e";
		tmp1[0] = tmp4;
		threeD[0] = tmp1;
		tmp5[0] = "f";
		tmp2[0] = tmp5;
		threeD[1] = tmp2;
		tmp6[0] = "g";
		tmp3[0] = tmp6;
		threeD[2] = tmp3;
		
		
		// TODO
		
		hs.close();
		
		logger.info("createArray success test finished");
	}
	
	@Test
	public void findNewInstancesOfElements() {
		logger.info("createArray success test started");
		
		HandleStmtForTests hs = new HandleStmtForTests();
		
		String[][] twoD = new String[2][2];
		hs.addArrayToObjectMap(twoD);
		hs.addLocal("String[][]_twoD");

		assertEquals(1, hs.getNumberOfElements());
		assertEquals(2, hs.getNumberOfFields(twoD));
		
		/*
		 * The following is the Jimple representation of:
		 * twoD[0][0] = "first element";		
		 * twoD[0][1] = "second element";	
		 */
		
		
		hs.addLocal("String[]_tmp1");
		String[] tmp1 = new String[1];
		hs.addArrayToObjectMap(tmp1);
		
		hs.setLevelOfArrayField(tmp1, Integer.toString(0), "String[]_tmp1");
		tmp1[0] = "first element";
		
		hs.addLocal("String[]_tmp2");
		String[] tmp2 = new String[1];
		hs.addArrayToObjectMap(tmp2);
		
		hs.setLevelOfArrayField(tmp1, Integer.toString(0), "String[]_tmp2");
		tmp2[0] = "second element";
		
		hs.addLevelOfLocal("String[]_tmp1");
		hs.setLevelOfArrayField(twoD, Integer.toString(0),  "String[][]_twoD" );
		twoD[0] = tmp1;
		
		hs.addLevelOfLocal("String[]_tmp2");
		hs.setLevelOfArrayField(twoD, Integer.toString(1), "String[][]_twoD");
		twoD[1] = tmp2;

		assertEquals(SecurityLevel.LOW, hs.getLocalLevel("String[][]_twoD"));
		
		hs.close();
		
		logger.info("createArray success test finished");
	}

	@Test
	public void readArray() {
		
		logger.info("readArray success test started");
		
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
		hs.setLevelOfArrayField(arr, Integer.toString(0), "String[][]_arr");
		arr[0] = inner1;
		
		hs.addLevelOfLocal("String[]_inner2");
		hs.setLevelOfArrayField(arr, Integer.toString(1), "String[][]_arr");
		arr[1] = inner2;
		
		hs.addLocal("String[]_tmp");
		hs.addLevelOfArrayField(arr, Integer.toString(1));
		hs.setLevelOfLocal("String[]_tmp");
		String[] tmp = arr[1];
		
		hs.addLocal("String_x");
		hs.addLevelOfArrayField(tmp, Integer.toString(0));
		hs.setLevelOfLocal("String_x");
		@SuppressWarnings("unused")
		String x = tmp[0];
		
		hs.close();
		
		logger.info("readArray success test finished");
	}
	
	@Test
	public void writeArray() {

		logger.info("writeArray success test started");

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
		hs.addLocal("String[]_inner1");
		hs.addArrayToObjectMap(inner1);
		
		String[] inner2 = new String[1];
		hs.addLocal("String[]_inner2");
		hs.addArrayToObjectMap(inner2);
		
		hs.setLevelOfArrayField(inner1, Integer.toString(0), "String[]_inner1");
		inner1[0] = "a";
		
		hs.setLevelOfArrayField(inner2, Integer.toString(0), "String[]_inner2");
		inner2[0] = "b";
		
		hs.addLevelOfLocal("String[]_inner1");
		hs.setLevelOfArrayField(arr, Integer.toString(0), "String[][]_arr");
		arr[0] = inner1;
		
		hs.addLevelOfLocal("String[]_inner2");
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
		
		logger.info("writeArray success test finished");
	}
}
