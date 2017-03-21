package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Test using a for loop as an if statement.
 * @author Nicolas Müller
 *
 */
public class NSU_ForLoopFail {

	public static void main(String[] args) {
		int doUpdate = DynamicLabel.makeHigh(1);
		int res = ForActingAsIf(doUpdate);
		
		// to make sure compiler doesn't optimize away
		@SuppressWarnings("unused")
		int forCompiler = res * 2;
	}

	private static int ForActingAsIf(int x) {
		int y = 0;
		for (int i = 0; i < x; i++) {
			y = 1;						// NSU!! 
		}
		return y;
	}
}
