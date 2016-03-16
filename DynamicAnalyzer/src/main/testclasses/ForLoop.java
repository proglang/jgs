package main.testclasses;

public class ForLoop {

	public static void main(String[] args) {
		simpleFor(5);

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
}
