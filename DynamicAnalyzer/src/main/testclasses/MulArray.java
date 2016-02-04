package main.testclasses;



public class MulArray {
   
    public static void main(String[] args) {
    	m(2);
    }
    
    @SuppressWarnings("unused")
	static void m(int x){
    	String[][] twoD = new String[][] {{"e"},{"f"},{"g"}};
    	System.out.println("Old val: " + twoD[1][0]);
    	twoD[1][0] = "newVal";
    	String val = twoD[0][0];
    	System.out.println("Value of MulArray: " + val);
    }
}
