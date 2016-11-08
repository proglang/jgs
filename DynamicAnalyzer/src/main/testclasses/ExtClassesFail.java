package main.testclasses;

import utils.test.simpleClassForTests;

public class ExtClassesFail {
	public static void main(String[] args) {
		simpleClassForTests s = new simpleClassForTests();
	    String sec = s.provideSecretString();
	    System.out.println(sec);
	}
}
