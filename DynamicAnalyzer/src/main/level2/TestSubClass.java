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
		
		return 2;
	}
	
	public int methodWithLocalReturn() {
		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_result");
		
		hs.assignLocal("int_result");
		int result = 0;
		
		hs.returnLocal("int_result");
		return result;
	}
	
	public int methodWithFieldReturn() {
		HandleStmt hs = new HandleStmt();
		hs.returnField(this, "int_field");
		return field;
	}
}