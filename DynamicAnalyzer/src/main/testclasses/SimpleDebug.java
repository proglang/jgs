package main.testclasses;

import utils.analyzer.HelperClass;

/**
 * Korrektes Vehalten: Wirft keinen NSU Fehler.
 * Erklären warum.
 * @author Nicolas Müller
 *
 */
public class SimpleDebug {
	public static void main(String[] args) {
		String y = "";
		int x = HelperClass.makeHigh(1);
		if (x == 1) {
			y = "Case 1";		// NSU
		} else {
			y = "Case Def";
		}
		
		/**
		 * This is necessary, because otherwise the compiler will just
		 * throw away the updates of y inside the if, which will circumvent the
		 * IllegalFlowException
		 */
		@SuppressWarnings("unused")
		String z = y + "i exist only so that the compiler wont optimize y away";
	}
}
