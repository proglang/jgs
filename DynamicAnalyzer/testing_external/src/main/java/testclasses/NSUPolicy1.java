package testclasses;

import org.junit.Ignore;

import util.analyzer.HelperClass;

/**
 * Class to test the basics of NSU policy
 * @author Nicolas Müller
 *
 */
public class NSUPolicy1 {
	
	public static void main(String[] args) {
		int y = 5;
		int secret = 42;
		
		y = HelperClass.makeLow(5);		// just for clarity
		secret = HelperClass.makeHigh(5);
		
		if (secret > 0) {
			y += 1;						// NSU IFCError
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
