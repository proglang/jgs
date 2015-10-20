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

		assertEquals(1, hs.getNumberOfElements());
		assertEquals(2, hs.getNumberOfFields(twoD));
		
		/*
		 * The following is the Jimple representation of:
		 * twoD[0][0] = "first element";		
		 * twoD[0][1] = "second element";	
		 */
		
		String[] tmp1 = new String[1];
		tmp1[0] = "first element";
		String[] tmp2 = new String[1];
		tmp1[0] = "second element";
		twoD[0] = tmp1;
		twoD[1] = tmp2;
		// TODO
		
		/*
		 * The following is the Jimple representation of:
		 *twoD[1] = new String[] {"third element", "fourth element"};		
		 */
		String[] tmp = new String[2];
		tmp[0] = "third element";
		tmp[1] = "fourth element";
		twoD[1] = tmp;
		// TODO
		
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
		String [] inner1 = new String[1];
		String[] inner2 = new String[1];
		inner1[0] = "a";
		inner2[0] = "b";
		arr[0] = inner1;
		arr[1] = inner2;
		String[] tmp = arr[1];
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
		String [] inner1 = new String[1];
		String[] inner2 = new String[1];
		inner1[0] = "a";
		inner2[0] = "b";
		arr[0] = inner1;
		arr[1] = inner2;
		String[] tmp = arr[0];
		tmp[0] = "a";
		
		assertEquals("a", arr[0][0]);
		
		hs.close();
		
		logger.info("writeArray success test finished");
	}
}
