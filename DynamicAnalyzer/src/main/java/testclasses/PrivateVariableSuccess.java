package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Test that must pass. Information flows from high x to y, but
 * since y cannot be read anywhere, no information leak is present.
 * @author Nicolas Müller
 *
 */
public class PrivateVariableSuccess {
	public static void main(String[] args) {
		int z = DynamicLabel.makeHigh(4);
		int x = DynamicLabel.makeHigh(3);
		int y = DynamicLabel.makeLow(2);
		
		// we test both the implicit flow inside main (below)
		if (x > 0) { 
			y =+ x;	//y wird hier high (fluss - sensitivität)
		} else {
			y =+ 42;
		}
		
		// assign a high-sec variable to a low-sec, private one multiple times
		// gets called multiple times: Check the output!
		int low = func(x,  y);
		low = func(x,  low);
		low = func(x,  z);
		
		
	}
	
	// and the implicit flow outside main, using func(int x, int y)
	public static int func(int x, int y) {
		if (x > 0) { 
			y = x;
		} else {
			y = 42;
		}
		System.out.println("Func called");
		return y;
	}
	
	// NullPointer if this method is not commented out?!
	
}
