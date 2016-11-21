package main.testclasses;

import utils.HelperClass.HelperClass;
/**
 * Working example from readme. Since two exceptions are to be thrown, also see NSUPolicy3
 * @author Nicolas Müller
 *
 */
public class NSUPolicy2 {

	public static void main(String[] args) {
		C o1 = new C();
		C o2 = new C();
		
		o1.f = true;
		o2.f = false;
		
		// o1, o2, o1.f and o2.f are all LOW
		
		boolean secret = HelperClass.makeHigh(true);
		C o;
		if (secret) {
			o = o1;
		} else {
			o = o2;
		}
		
		// o is high.
		// o1, o2, o1.f and o2.f are still LOW
		
		System.out.println(o1.f); // Okay
		System.out.println(o.f);  // Not okay! Leaks information!
		
		o1.f = false;			// Okay
		// o.f = true; 			// Not okay! NSU IllegalFlowException -> Test online in NSUPolicy3
		
	}
	
	
}

/**
 * Testclass 
 * @author Nicolas Müller
 *
 */
class C { 
	boolean f;
}


