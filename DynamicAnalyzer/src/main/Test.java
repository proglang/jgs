package main;

import analyzer.level2.HandleStmt;


public class Test {
	int field;
   
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
    
    /*// TODO note that every instanceMethod has a this reference at the beginning
    public void d() {
    	
    }
    
    public void m2() {
    	
    	this.field = 2;
    	this.field = 3;
    	main.Test a = this;
    	
    }*/
    
    
}
