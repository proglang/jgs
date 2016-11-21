package main.testclasses;

import utils.analyzer.HelperClass;
import utils.test.C;

public class NSUPolicy4 {
	static int f = 0;
	public static void main(String[] args) {

		C b = new C();
		C c = HelperClass.makeHigh(b);
		c.f = true; // should throw an error, since we access access f through
					// high-sec c and PC = LOW
        f = 1; // ok
        if (c == b){
          f = 2; // NSU Error
        }
	
	}
}
