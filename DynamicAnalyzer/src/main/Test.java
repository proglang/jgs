package main;

import tests.testClasses.TestSubClass;


public class Test {
	int field1 = 1;
	static int field2 = 2;
   
    public static void main(String[] args) {
		
    }
    
    void m() {
    	field1 = field2;
    	TestSubClass tsc = new TestSubClass();
    	tsc.pField = 2;
    }
}
