package tests.testClasses;

import analyzer.level2.HandleStmt;
import analyzer.level2.Level;
import analyzer.level2.storage.LocalMap;
import analyzer.level2.storage.ObjectMap;
import annotations.*;

public class TestClass {

	public static int intField;
	
	public TestClass() {
		HandleStmt hs = new HandleStmt();
		hs.addObjectToObjectMap(this);
		hs.addFieldToObjectMap(this, "int_intField");
		
		// TODO : ?????
		intField = 3;
		hs.close();
	}
	
    public static int ifReturnExpr() {
    	HandleStmt hs = new HandleStmt();
    	hs.addLocal("int_High");
    	
        int High = 42;
        
        TestSubClass newObj;
        newObj = new TestSubClass();
        
        hs.assignLocalsToField("int_intField", "int_High");
    	intField = High;
		hs.close();
        return High;
    }
    
    
    @MyAnnotation
    public static void main(String[] args) {
    	HandleStmt hs = new HandleStmt();
    	hs.addLocal("int_a1");
    	hs.addLocal("int_a2");
    	hs.addLocal("int_res");
    	hs.addLocal("int_anotherRes");
    	hs.addLocal("String_s");
    	hs.addLocal("String_d");
    	
// TODO: Was passiert hier?
    	int a1 = 22;
    	
    	int a2 = 23;
    	
    	hs.makeLocalHigh("int_a2");
    	hs.assignLocalsToLocal("int_res", "int_a1", "int_a2");
    	int res = a1 + a2;
    	
    	hs.assignLocalsToLocal("int_anotherRes", "int_res", "int_res");
    	int anotherRes = res + res;
    	
    	String s;
    	
    	String d = "jhjk";
    	
    	s = "ghj";
    	
    	hs.assignLocalsToLocal("String_s", "String_d");
    	s = d;
    	
    	ifReturnExpr();
    	
    	System.out.println("finish");
		hs.close();

    }

}
