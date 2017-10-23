package testclasses;

import util.exceptions.IllegalFlowException;

/**
 * Currently, modulus % is not supported. Must throw exception to remind us.
 * 
 * @author Nicolas Müller
 *
 */
public class NotSupported {
	public static void main(String[] args) {
		int i = 3;
		if (i % 2 == 1) {
			System.out.println("mod 1");
		}
	}
}
