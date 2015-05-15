package main;

import analyzer.level2.HandleStmt;


public class Test {
   
    public static void main(String[] args) {
    	int a1 = 22;
    	m();
    }
    
    public static int m() {
    	int a = 2;
    	int b = a;
    	int c = a + b;
    	return c;
    }

}
