package de.unifreiburg.cs.proglang.jgs.jimpleassumptions

import org.scalatest.{FlatSpec, Matchers}
import soot.{IntType, Local}
import soot.jimple.{IntConstant, Jimple, Stmt}

/**
  * Test various assumptions I make about Jimple
  */
class JimpleAssumptions extends FlatSpec with Matchers {
  val j : Jimple = Jimple.v()


  "If statements" should "not change their identity when their target is set" in {
    val nullStmt : Stmt = j.newNopStmt()
    val assStmt : Stmt = j.newAssignStmt(j.newLocal("x", IntType.v()), IntConstant.v(0))
    val sIf1 = j.newIfStmt(j.newEqExpr(IntConstant.v(0), IntConstant.v(0)), nullStmt)

    // now we store the if stmt and set the target afterwards
    val someSet : Set[Stmt] = Set(sIf1)
    sIf1.setTarget(assStmt)

    someSet should contain(sIf1)

  }
}
