package main.testclasses;

/**
 * Test using a for loop as an if statement.
 * @author Nicolas Müller
 *
 */
public class NSU_ForLoopSuccess {

	/**
	 * Test various for loops.
	 * @param args Not used
	 */
	public static void main(String[] args) {
		int doUpdate = 1;
		int res = ForActingAsIf(doUpdate);
		
		// to make sure compiler doesn't optimize away
		@SuppressWarnings("unused")
		int forCompiler = res * 2;
	}

	private static int ForActingAsIf(int x) {
		int y = 0;
		for (int i = 0; i < x; i++) {
			y = 1;						// would throw NSU if x were HIGH
		}
		return y;
	}
}
