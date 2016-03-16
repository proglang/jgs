package main.testclasses;

public class ArithmeticExpressionsSuccess {

	int addExpr(int x, int y) {
		return x + y;
	}
	
	int subExpr(int x, int y) {
		return x - y;
	}
	
	int mulExpr(int x, int y) {
		return x * y;
	}
	
	int divExpr(int x, int y) {
		return x / y;
	}
	
	int orExpr(int x, int y) {
		return x | y;
	}
	
	int andExpr(int x, int y) {
		return x & y;
	}
	
	int xorExpr(int x, int y) {
		return x ^ y;
	}
	
	int shiftrExpr(int x, int y) {
		return x >> y;
	}
	
	int shiftlExpr(int x, int y) {
		return x << y;
	}
	
	int shiftrzerofillExpr(int x, int y) {
		return x >>> y;
	}
	
	int notExpr(int x) {
		return ~x;
	}
	
	int incrExpr(int x, int y) {
		return x++;
	}
	
	int decrExpr(int x, int y) {
		return x--;
	}
	
}
