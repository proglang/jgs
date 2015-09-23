package main;

import tests.testClasses.TestSubClass;


public class Test {
   
    public static void main(String[] args) {
		String[][][] t = new String[2][2][2];
		t[1][1][1] = "ha";
		int i = 0;
		t[i][i][i] = "ha";
		String s = t[1][1][1];
		s = t[i][i][i];
    }
    
    public void m() {
		String[] t = new String[2];
		t[1] = "ha";
		int i = 0;
		t[i] = "ha";
		String s = t[1];
		s = t[i];
    }
}
