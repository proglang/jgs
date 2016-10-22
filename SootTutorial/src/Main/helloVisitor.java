package Main;

/*
 * @author: Nicolas Müller
 * 
 * This is a testfile for the visitor pattern. The bottom line is:
 * 
 * visitable accepts visitor visits visitable.
 */

interface Visitor {
	public double visit(Visitable visitable);
}

interface Visitable {
	public double price();

	public double accept(Visitor visitor);
}


// ---------- implementation ----------------
class Taxman implements Visitor {
	public double visit(Visitable visitable) {
		return visitable.price() * 0.1;
	}
}

class AssholeTaxman implements Visitor {
	public double visit (Visitable visitable) {
		return visitable.price() * 0.2;
	}
}

// --------- impl. visitable ----------------

class Milk implements Visitable {
	
	@Override
	public double price() {return 10;}
	
	@Override
	public double accept(Visitor visitor) {
		return visitor.visit(this);
	}
}

class iPhone implements Visitable {

	@Override
	public double price() { return 799;	}

	@Override
	public double accept(Visitor visitor) {
		return visitor.visit(this);
	}
	
}


public class helloVisitor {
	public static void main(String[] args) {
		// visitable accepts visitor visits visitable
		Milk milk = new Milk();
		System.out.println(milk.accept(new Taxman()));
		System.out.println(milk.accept(new AssholeTaxman()));
		
		iPhone iphone = new iPhone();
		System.out.println(iphone.accept(new Taxman()));
		System.out.println(iphone.accept(new AssholeTaxman()));
		
	}
}
