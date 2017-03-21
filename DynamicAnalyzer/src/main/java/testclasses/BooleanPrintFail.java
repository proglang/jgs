package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * If explicit leak does not throw exception, 
 * check main.utils.externalClasses, line 35
 * for missing type
 * 
 * @author Nicolas Müller
 *
 */
public class BooleanPrintFail {
	public static void main(String[] args) {
		boolean b = true;
		b = DynamicLabel.makeHigh(b);
		System.out.println(b);
	}
}
