package tests.testclasses;

import analyzer.level2.HandleStmt;

/**
 * MethodTypings which are used by the JUnit tests in test.analyzer.level2
 * @author Regina Koenig, NicolasM
 *
 */
public class TestSubClass {
	
	int field;
	public int pField;
	public static int sField;
	
	public TestSubClass() {
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this	,"int_field");
		hs.addFieldToObjectMap(this	,"int_pField");
		hs.addObjectToObjectMap(this.getClass());
		hs.addFieldToObjectMap(this.getClass(), "int_sField");
		hs.close();
	}
	
	public void method() {
		HandleStmt hs = new HandleStmt();
		hs.close();
	}
	
	public int methodWithConstReturn() {
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		
		hs.returnConstant();
		hs.close();
		return 2;
	}
	
	/**
	 * Weird method, never used anywhere
	 * @param ts 	Argument TestClass used ever only by this method in whole project
	 * TODO clean this up? not needed method?!
	 */
	@SuppressWarnings("static-access")
	public void writeToCallerField(TestClass ts) {
		ts.intField = 4;
	}
	
	public int methodWithLowLocalReturn() {
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("int_result");
		
		hs.setLocalToCurrentAssignmentLevel("int_result");
		int result = 0;
		
		hs.returnLocal("int_result");
		hs.close();
		return result;
	}
	
	public int methodWithParams(int a, int b, int c) {
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("int_a1");
		hs.addLocal("int_b1");
		hs.addLocal("int_c1");
		hs.addLocal("int_tmp");
		hs.addLocal("int_tmp2");
		
		hs.assignArgumentToLocal(0, "int_a1");
		hs.assignArgumentToLocal(1, "int_b1");
		hs.assignArgumentToLocal(2, "int_c1");
		hs.joinLevelOfLocalAndAssignmentLevel("int_b1");
		hs.joinLevelOfLocalAndAssignmentLevel("int_c1");
		hs.setLocalToCurrentAssignmentLevel("int_tmp");
		hs.joinLevelOfLocalAndAssignmentLevel("int_tmp");
		hs.joinLevelOfLocalAndAssignmentLevel("int_a1");
		hs.setLocalToCurrentAssignmentLevel("int_tmp2");
		
		hs.returnLocal("int_tmp2");
		hs.close();
		return a + b + c;
	}
	
	public int methodWithHighLocalReturn() {
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("int_result");
		
		hs.setLocalToCurrentAssignmentLevel("int_result");
		int result = 0;
		hs.setLocalFromString("int_result", "HIGH");
		
		hs.returnLocal("int_result");
		hs.close();
		return result;
	}
	
	public void methodWithArgs(int i1, int i2, int i3) {
		
	}
}