package analyzer.level2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import util.exceptions.IFCError;
import util.exceptions.InternalAnalyzerException;
import util.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ArraysFail {
	
	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}
	
	@Test(expected = InternalAnalyzerException.class)
	public void readArray() {
		
		logger.log(Level.INFO, "READ ARRAY FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);

		hs.addLocal("int_i");
		hs.addLocal("String[]_a");
		hs.addLocal("String_x");

		String[] a = new String[] {"asd", "", ""};
		hs.addArrayToObjectMap(a);
		
		int i = 2;
		hs.pushLocalPC(CurrentSecurityDomain.top(), 12);
		
		
		assertTrue(hs.containsObjectInObjectMap(a));
		
		/*
		 * x = a[i]
		 * check x >= lpc
		 * level(x) = (i, a, gpc, a_i)
		 */
		hs.joinLevelOfArrayFieldAndAssignmentLevel(a, Integer.toString(i));
		hs.joinLevelOfLocalAndAssignmentLevel("int_i");
		hs.joinLevelOfLocalAndAssignmentLevel("String[]_a");
		hs.setLocalToCurrentAssignmentLevel("String_x");
		@SuppressWarnings("unused")
		String x = a[i];
		
		
		hs.close();
		
		logger.log(Level.INFO, "READ ARRAY FAIL TEST FINISHED");
		
	}
	
	@Test //(expected = InternalAnalyzerException.class)
	public void writeArray() {

		logger.log(Level.INFO, "WRITE ARRAY FAIL TEST STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		
		String[] a = new String[] {"asd", "v", "v"};
		assertEquals(3, a.length);
		hs.addArrayToObjectMap(a);
		assertTrue(hs.containsObjectInObjectMap(a));
		assertEquals(3, hs.getNumberOfFieldsInObjectMap(a));
		hs.setLocal("String[]_a", CurrentSecurityDomain.top());
		
		
		/*
		 * check(a_i >= join(gpc, a, i))
		 * level(a_i) = join(gpc,local, i)
		 */
		hs.setLevelOfArrayField(a, Integer.toString(2), "String[]_a");
		a[2] = "3";
		
		
		
		hs.close();
		
		logger.log(Level.INFO, "WRITE ARRAY FAIL TEST FINISHED");
		
	}

}
