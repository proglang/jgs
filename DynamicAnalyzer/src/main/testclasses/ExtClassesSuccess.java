package main.testclasses;

import utils.test.simpleClassForTests;

public class ExtClassesSuccess {
	public static void main(String[] args) {
		simpleClassForTests s = new simpleClassForTests();
	    String sec = s.provideSecretString();
	    System.out.println(sec);
	}
}
