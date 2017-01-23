package testclasses;

public class ForLoopFail {

	/**
	 * Test various for loops.
	 * @param args Not used
	 */
	public static void main(String[] args) {
		simpleFor(5);
		arrayIterator(new String[]{"hg", "jh", "kj", "ef"});

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
