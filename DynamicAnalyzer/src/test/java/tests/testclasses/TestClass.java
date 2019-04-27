package tests.testclasses;

import analyzer.level2.HandleStmt;

/**
 * TestClass used by TestSubClass.writeToCallerField method, which is currently not used anywhere in the whole program.
 * TODO clean this up? 
 * @author Nicolas Müller
 *
 */
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
        
        hs.joinLevelOfLocalAndAssignmentLevel("int_High");
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
    	
    	hs.setLocalFromString("int_a2", null);
    	hs.joinLevelOfLocalAndAssignmentLevel("int_a1");
    	hs.joinLevelOfLocalAndAssignmentLevel("int_a2");
    	hs.setLocalToCurrentAssignmentLevel("int_res");
    	int res = a1 + a2;
    	
    	hs.joinLevelOfLocalAndAssignmentLevel("int_res");
    	hs.joinLevelOfLocalAndAssignmentLevel("int_res");
    	hs.setLocalToCurrentAssignmentLevel("int_anotherRes");
    	int anotherRes = res + res;
    	
    	String s;
    	
    	String d = "jhjk";
    	
    	s = "ghj";
    	
    	hs.joinLevelOfLocalAndAssignmentLevel("String_d");
    	hs.setLocalToCurrentAssignmentLevel("String_s");
    	s = d;
    	
    	ifReturnExpr();
    	
    	System.out.println("finish");
		hs.close();

    }

}
