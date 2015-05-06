package main;

import tests.testClasses.TestSubClass;
import analyzer.level2.HandleStmt;
import annotations.*;

public class Test {

	public void m(TestSubClass o) {
		TestSubClass i = o;
		i.pField = 22;
	}
    
    
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
