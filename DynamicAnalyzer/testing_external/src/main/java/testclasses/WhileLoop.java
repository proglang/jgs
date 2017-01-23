package testclasses;

public class WhileLoop {

	public static void main(String[] args) {
		simpleWhile(2);
	}
	
	/**
	 * Simple while-loop.
	 * @param x input
	 * @return output
	 */
	public static int simpleWhile(int x) {
		while (x < 10) {
			x++;
		}
		return x;
	}

}
