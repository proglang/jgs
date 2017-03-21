package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * This should normally throw an NSU exception. However, because of compiler optimisations,
 * it does not. Since this does not pose a security issue, we say that this is desired behavoir.
 * @author Nicolas Müller
 *
 */
public class NSU_SwitchStmtSpecialCase {

		public static void main(String[] args) {
			simpleLookupSwitchVoid(2); 
		}
		
		/**
		 * Simple lookup switch testcase.
		 * @param x input
		 */
		public static void simpleLookupSwitchVoid(int x) {
			int y = 0;
			x = DynamicLabel.makeHigh(x);
			switch (x) {
			  case 1: 
				  y = x; // should throw NSU!! but because of snd branch, it does not.
				  break;
			  default:			
				 y = 100; 
				 break;
			}
			@SuppressWarnings("unused")
			int z = y + 42;
		}

}
