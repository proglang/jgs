package main.level2;

import analyzer.level2.HandleStmt;

public class TestSubClass {
	
	int field;
	
	public TestSubClass() {
		HandleStmt hs = new HandleStmt();
		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this	,"int_field");
	}
	
	public void method() {
		HandleStmt hs = new HandleStmt();
		
	}
	
	public int methodWithConstReturn() {
		HandleStmt hs = new HandleStmt();
		
		hs.returnConstant();
		return 2;
	}
	
	public int methodWithLowLocalReturn() {
		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_result");
		
		hs.assignLocalsToLocal("int_result");
		int result = 0;
		
		hs.returnLocal("int_result");
		return result;
	}
	
	public int methodWithHighLocalReturn() {
		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_result");
		
		hs.assignLocalsToLocal("int_result");
		int result = 0;
		hs.makeLocalHigh("int_result");
		
		hs.returnLocal("int_result");
		return result;
	}
}