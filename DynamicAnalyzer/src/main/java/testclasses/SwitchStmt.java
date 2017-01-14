package testclasses;

public class SwitchStmt {

	public static void main(String[] args) {
		simpleSwitch(3);
	}
	
	/**
	 * Simple switch-testcase.
	 * @param x input
	 * @return output
	 */
	public static int simpleSwitch(int x) {
		switch (x) {
		  case 1: 
			  x++; 			
			  break;
		  case 2: 
			  x--; 			
			  break;
		  default: 
			  x = 100; 
			  break;
		}
		return x;
	}

}
