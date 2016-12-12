package main.testclasses;

import main.testclasses.utils.simpleClassForTests;

public class ExternalFail1 {
	public static void main(String[] args) {
		simpleClassForTests s = new simpleClassForTests();
	    String sec = s.provideSecretString();
	    System.out.println(sec);
	}
}
