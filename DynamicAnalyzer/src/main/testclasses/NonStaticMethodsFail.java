package main.testclasses;

public class NonStaticMethodsFail {

	public static void main(String[] args) {
		NonStaticMethods nsm = new NonStaticMethods();
		nsm.nonStatic();
	}
	
	public void nonStatic() {
		System.out.println("Called a non-static method");
	}

}
