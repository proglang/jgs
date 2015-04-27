package main;

import analyzer.level2.HandleStmt;
import annotations.*;

public class Test {

	int intField = 3;
	int intField2 = 0;
	
    public int ifReturnExpr() {
        int High = 42;
    	intField = 456;
    	High = intField + intField2;
        return High;
    }
    
    public int constReturn() {
    	int x = intField;
    	x = x + intField;
    	return 3;
    }
    
    public int returnField() {
    	intField2 = intField;
    	return intField;
    }
    
    public void methodWithParams(int i, int x, String c) {
    	
    }
    
    public void ifStmt() {
    	if (intField == intField2) {
    		HandleStmt hs = new HandleStmt();
    		int v = 0;
    	} else {
    		int v = 1;
    	}
    	HandleStmt hs = new HandleStmt();
    }
    
    @MyAnnotation
    public static void main(String[] args) {
    	int a1 = 22;
    	int a2 = 23;
    	int res = a1 + a2;
    	int anotherRes = res + res;
    	String s;
    	String d = "jhjk";
    	s = "ghj";
    	s = d;
    	//TODO: Wie kann man selber eine Konstante (Enum) erstellen, mit eigenem Typ?
    	
    	return;

    }

}
