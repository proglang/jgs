package testclasses;

import testclasses.utils.C;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class NSU_FieldAccessStatic {
		static int f = 0;
		public static void main(String[] args) {

			C b = new C();
			C c = DynamicLabel.makeHigh(b);
			
	        f = 1; // ok
	        if (c.equals(b)){
	          f = 2; // NSU Error
	        }
		
		}
	}