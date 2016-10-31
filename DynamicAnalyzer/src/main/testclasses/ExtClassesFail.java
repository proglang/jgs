package main.testclasses;

import utils.test.*; 
/**
 * Test using an external class (from utils.test)
 * @author NicolasM
 */
public class ExtClassesFail {

	public static void main(String[] args) {
		simpleClassForTests s = new simpleClassForTests();
	    String sec = s.provideSecretString();
	    System.out.println(sec);
	}
}


