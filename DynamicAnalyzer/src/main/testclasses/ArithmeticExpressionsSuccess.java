package main.testclasses;

public class ArithmeticExpressionsSuccess {
	
	/**
	 * Simple test of all arithmetic functions.
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		System.out.println("2 + 3 = " + addExpr(2,3));
		System.out.println("2 - 3 = " + subExpr(2,3));
		System.out.println("2 * 3 = " + mulExpr(2,3));
		System.out.println("2 / 3 = " + divExpr(2,3));
		System.out.println("2 | 3 = " + orExpr(2,3));
		System.out.println("2 & 3 = " + andExpr(2,3));
		System.out.println("2 ^ 3 = " + xorExpr(2,3));
		System.out.println("2 >> 3 = " + shiftrExpr(2,3));
		System.out.println("2 << 3 = " + shiftlExpr(2,3));
		System.out.println("2 >>> 3 = " + shiftrzerofillExpr(2,3));
		System.out.println("~2 = " + notExpr(2));
		System.out.println("++2 = " + incrExpr(2));
		System.out.println("--2 = " + decrExpr(2));
	}

	static int addExpr(int x, int y) {
		return x + y;
	}
	
	static int subExpr(int x, int y) {
		return x - y;
	}
	
	static int mulExpr(int x, int y) {
		return x * y;
	}
	
	static int divExpr(int x, int y) {
		return x / y;
	}
	
	static int orExpr(int x, int y) {
		return x | y;
	}
	
	static int andExpr(int x, int y) {
		return x & y;
	}
	
	static int xorExpr(int x, int y) {
		return x ^ y;
	}
	
	static int shiftrExpr(int x, int y) {
		return x >> y;
	}
	
	static int shiftlExpr(int x, int y) {
		return x << y;
	}
	
	static int shiftrzerofillExpr(int x, int y) {
		return x >>> y;
	}
	
	static int notExpr(int x) {
		return ~x;
	}
	
	static int incrExpr(int x) {
		return ++x;
	}
	
	static int decrExpr(int x) {
		return --x;
	}
	
}
