package main.testclasses;

import main.testclasses.utils.C;
import utils.analyzer.HelperClass;

public class NSU_FieldAccessStatic {
		static int f = 0;
		public static void main(String[] args) {

			C b = new C();
			C c = HelperClass.makeHigh(b);
			
	        f = 1; // ok
	        if (c.equals(b)){
	          f = 2; // NSU Error
	        }
		
		}
	}