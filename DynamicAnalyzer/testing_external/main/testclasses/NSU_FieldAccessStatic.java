package main.testclasses;

import utils.analyzer.HelperClass;
import utils.test.C;

public class NSU_FieldAccessStatic {
		static int f = 0;
		public static void main(String[] args) {

			C b = new C();
			C c = HelperClass.makeHigh(b);
			
	        f = 1; // ok
	        if (c == b){
	          f = 2; // NSU Error
	        }
		
		}
	}