package main;

import annotations.*;

public class Test {

	int intField = 3;
	int intField2 = 0;
	
    public int ifReturnExpr() {
        int High = 42;
    	intField = 456;
        return High;
    }
    
    public int constReturn() {
    	int x = intField;
    	return 3;
    }
    
    public int returnField() {
    	intField2 = intField;
    	return intField;
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
