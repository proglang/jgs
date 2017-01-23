package testclasses;



public class MulArray {
   
	public static void main(String[] args) {
		method(2);
	}
    
	static void method(int x) {
		String[][] twoD = new String[][] {{"e"},{"f"},{"g"}};
		System.out.println("Old val: " + twoD[1][0]);
		twoD[1][0] = "newVal";
		String val = twoD[0][0];
		System.out.println("Value of MulArray: " + val);
	}
}
