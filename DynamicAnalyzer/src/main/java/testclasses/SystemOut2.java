package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Must fail.
 * @author Nicolas Müller
 *
 */
public class SystemOut2 {
	
	public static void main(String[] args) {
		System.out.println(DynamicLabel.makeHigh(3));
	}
	
}
