package main.testclasses;

import utils.test.C;

/**
 * Test throwa a java.lang.VerifyError: (class: main/testclasses/EqualObjectsVerifySuccess, 
 * method: main signature: ([Ljava/lang/String;)V) Incompatible argument to function
 * @author Nicolas Müller
 *
 */
public class EqualObjectsVerifySuccess {

		public static void main(String[] args) {
			C a = new C();
			C b = new C();
			if (a == b) {
				System.out.println("this will never print");
			}
		}
}
