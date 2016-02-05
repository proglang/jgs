package analyzer.level1;

import soot.Body;
import soot.ByteType;
import soot.IntType;
import soot.RefType;
import soot.jimple.Jimple;

public class BodyForTests {

	protected static enum BodyType {DEFAULT, IFSTMT}
	
	static Body body;
  
	protected static Body createBody(BodyType bodytype) {
		body  = Jimple.v().newBody();
		switch (bodytype) {
		  case DEFAULT: 
			  break;
		  case IFSTMT: 
			  createIfStmtBody();
			  break;
		  default: break;
		}
		return body;
	}
	
	/*
    r0 := @this: main.testclasses.IfStmt;
    i0 := @parameter0: int;
    if i0 >= 0 goto label0;

    b1 = 0;
    goto label3;

 label0:
    if i0 >= 2 goto label1;

    b1 = 2;
    goto label3;

 label1:
    if i0 >= 4 goto label2;

    b1 = 4;
    goto label3;

 label2:
    b1 = 6;

 label3:
    return b1;
    */
	private static void createIfStmtBody() {
		
		body.getLocals().add(Jimple.v().newLocal("i0", IntType.v()));
		body.getLocals().add(Jimple.v().newLocal("b1", ByteType.v()));
		body.getLocals().add(Jimple.v().newLocal("r0", 
				RefType.v("main.testclasses.IfStmt")));
		
		
		//printBody();
		//body.validate();
	}
	
	private static void printBody() {
		System.out.println(body.toString());
	}

}
