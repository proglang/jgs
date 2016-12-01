package main.testclasses;

public class NSU_ForLoopFail {

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
		int i = 0;
		for (int j = 0; j < x; j++) {
			i--;
		}
		return i;
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
