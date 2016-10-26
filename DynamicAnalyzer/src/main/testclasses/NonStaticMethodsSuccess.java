package main.testclasses;

public class NonStaticMethodsSuccess {

	public static void main(String[] args) {
		NonStaticMethodsSuccess nsm = new NonStaticMethodsSuccess();
		nsm.nonStatic();
	}
	
	public void nonStatic() {
		System.out.println("Called a non-static method");
	}

}
