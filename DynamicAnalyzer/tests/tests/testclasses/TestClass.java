package tests.testclasses;

import analyzer.level2.HandleStmt;

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
        
        hs.addLevelOfLocal("int_High");
        hs.setLevelOfField(TestClass.class, "int_intField");
    	intField = High;
		hs.close();
        return High;
    }
    
    
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
    	hs.addLevelOfLocal("int_a1");
    	hs.addLevelOfLocal("int_a2");
    	hs.setLevelOfLocal("int_res");
    	int res = a1 + a2;
    	
    	hs.addLevelOfLocal("int_res");
    	hs.addLevelOfLocal("int_res");
    	hs.setLevelOfLocal("int_anotherRes");
    	int anotherRes = res + res;
    	
    	String s;
    	
    	String d = "jhjk";
    	
    	s = "ghj";
    	
    	hs.addLevelOfLocal("String_d");
    	hs.setLevelOfLocal("String_s");
    	s = d;
    	
    	ifReturnExpr();
    	
    	System.out.println("finish");
		hs.close();

    }

}