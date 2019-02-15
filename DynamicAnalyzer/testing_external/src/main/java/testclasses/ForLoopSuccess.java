package testclasses;

import util.analyzer.HelperClass;

public class ForLoopSuccess {

	/**
	 * Test various for loops.
	 * @param args Not used
	 */
	public static void main(String[] args) {
		simpleFor(5);
		arrayIterator(new String[]{"hg", "jh", "kj", "ef"});
		
		int secret = HelperClass.makeHigh(42);
		simpleFor(secret);
		arrayIterator(new String[] {HelperClass.makeHigh("secret1"), HelperClass.makeHigh("secret2")});

	}

	/**
	 * Simple method with just one for-loop.
	 * @param x input
	 * @return output
	 */
	public static int simpleFor(int x) {
		for (int i = 0; i < x; i++) {
			x--;
		}
		return x;
	}
	
	/**
	 * Test array iterator.
	 * @param x input
	 * @return output
	 */
	public static int arrayIterator(String[] x) {
		int z = 0;
		for (@SuppressWarnings("unused") String y : x) {
			z++;
		}
		return z;
	}
}

/* should fail as String[] x has HIGH strings and so z will also get updated - NSU failure */
/* running this code however gives not implemented - 'newarray (java.lang.String)[4] not supported as a RHS-expression yet
' error  */