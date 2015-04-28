package main.level2;

import static org.junit.Assert.assertEquals;
import analyzer.level2.HandleStmt;
import analyzer.level2.storage.ObjectMap;

public class TestSubClass {
	
	int field;
	
	public TestSubClass() {
		HandleStmt hs = new HandleStmt();
		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this	,"int_field");

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
	
	public int methodWithLowLocalReturn() {
		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_result");
		
		hs.assignLocalsToLocal("int_result");
		int result = 0;
		
		hs.returnLocal("int_result");
		hs.close();
		return result;
	}
	
	public int methodWithParams(int a, int b, int c) {
		return a + b + c;
	}
	
	public int methodWithHighLocalReturn() {
		HandleStmt hs = new HandleStmt();
		hs.addLocal("int_result");
		
		hs.assignLocalsToLocal("int_result");
		int result = 0;
		hs.makeLocalHigh("int_result");
		
		hs.returnLocal("int_result");
		hs.close();
		return result;
	}
}