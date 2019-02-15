package testclasses;

import testclasses.utils.C;

/**
 * Test throw a java.lang.VerifyError: (class: main/testclasses/EqualObjectsVerifySuccess,
 * method: main signature: ([Ljava/lang/String;)V) Incompatible argument to function
 * @author Nicolas Müller
 *
 */
public class EqualObjectsVerifySuccess {

		public static void main(String[] args) {
			C a = new C();
			C b = new C();
			if (a == b) { // if (3 == retrun3()) 		works fine!
				System.out.println("this will never print");
			}
		}
}
