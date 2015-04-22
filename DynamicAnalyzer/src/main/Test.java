package main;

import annotations.*;

public class Test {

	int intField = 3;
	
    public int ifReturnExpr() {
        int High = 42;
    	intField = 456;
        return High;
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
