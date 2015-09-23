package tests.testClasses;

import static org.junit.Assert.assertEquals;
import analyzer.level2.HandleStmt;
import analyzer.level2.storage.ObjectMap;

public class TestSubClass {
	
	int field;
	public int pField;
	public static int sField;
	
	public TestSubClass() {
		HandleStmt hs = new HandleStmt();
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
		
		hs.returnConstant();
		hs.close();
		return 2;
	}
	
	public void writeToCallerField(TestClass ts) {
		ts.intField = 4;
	}
	
	public int methodWithLowLocalReturn() {
		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_result");
		
		hs.setLevelOfLocal("int_result");
		int result = 0;
		
		hs.returnLocal("int_result");
		hs.close();
		return result;
	}
	
	public int methodWithParams(int a, int b, int c) {
		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_a1");
		hs.addLocal("int_b1");
		hs.addLocal("int_c1");
		hs.addLocal("int_tmp");
		hs.addLocal("int_tmp2");
		
		hs.assignArgumentToLocal(0, "int_a1");
		hs.assignArgumentToLocal(1, "int_b1");
		hs.assignArgumentToLocal(2, "int_c1");
		hs.addLevelOfLocal("int_b1");
		hs.addLevelOfLocal("int_c1");
		hs.setLevelOfLocal("int_tmp");
		hs.addLevelOfLocal("int_tmp");
		hs.addLevelOfLocal("int_a1");
		hs.setLevelOfLocal("int_tmp2");
		
		hs.returnLocal("int_tmp2");
		hs.close();
		return a + b + c;
	}
	
	public int methodWithHighLocalReturn() {
		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_result");
		
		hs.setLevelOfLocal("int_result");
		int result = 0;
		hs.makeLocalHigh("int_result");
		
		hs.returnLocal("int_result");
		hs.close();
		return result;
	}
	
	public void methodWithArgs(int i1, int i2, int i3) {
		
	}
}