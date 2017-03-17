package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import utils.analyzer.HelperClass;

/**
 * Class to test the basics of NSU policy
 * @author Nicolas Müller
 *
 */
public class NSUPolicy {

	@Constraints({"LOW <= @0 "})
	public static void main(String[] args) {
		int y = 5;
		int secret = 42;
		
		//y = HelperClass.makeLow(5);		// just for clarity
		secret = HelperClass.makeHigh(5);
		
		if (secret > 0) {
			y += 1;						// NSU IFCError, Illegal
										// flow to int_i0
		}
		
		/**
		 * This is necessary, because otherwise the compiler will just
		 * throw away the updates of y inside the if, which will circumvent the
		 * IFCError
		 */
		@SuppressWarnings("unused")
		int x = 3 + y;

	}

}
