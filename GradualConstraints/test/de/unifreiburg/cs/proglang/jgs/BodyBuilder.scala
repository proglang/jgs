package de.unifreiburg.cs.proglang.jgs

import soot.{Body, SootMethod, VoidType, PatchingChain}

import scala.collection.JavaConverters._

import soot.jimple.{Expr, Jimple, Stmt, JimpleBody}


class BodyBuilder private  {

  private val body : Body = Jimple.v().newBody(new SootMethod("dummy", List().asJava, VoidType.v()))
  // TODO: check if we need to keep locals consistent
  // for (l <- locals) { body.getLocals.addLast(l) }

  private val chain : PatchingChain[soot.Unit]  = body.getUnits

  private var finalized = false

  def build() : Body = { finalized = true; body }

  def seq(s : Stmt) : BodyBuilder = {
    addToChain(s)
    return this
  }

  def ite(cond : Expr, thn : Stmt, els : Stmt) : BodyBuilder = {
    return ite(cond, BodyBuilder.begin().seq(thn), BodyBuilder.begin().seq(els))
  }

  def ite(cond : Expr, thn : BodyBuilder, els : BodyBuilder) : BodyBuilder = {
    val thnChain = thn.chain
    val thnStmt = thn.chain.getFirst
    val head = Jimple.v().newIfStmt(cond, thnStmt)
    val end = Jimple.v().newNopStmt()
    val gotoEnd = Jimple.v().newGotoStmt(end)
    addToChain(head)
    addToChain(els)
    addToChain(gotoEnd)
    addToChain(thn)
    addToChain(end)


    return this
  }

  def whileLoop(cond : Expr, body : Stmt) : BodyBuilder = {
    return whileLoop(cond, BodyBuilder.begin().seq(body))
  }

  def whileLoop(cond : Expr, body : BodyBuilder) : BodyBuilder = {

    val bodyHead = body.chain.getFirst
    val end = Jimple.v().newNopStmt()
    val gotoEnd = Jimple.v().newGotoStmt(end)
    val head = Jimple.v().newIfStmt(cond, bodyHead)
    val gotoHead = Jimple.v().newGotoStmt(head)

    addToChain(head)
    addToChain(gotoEnd)
    addToChain(body)
    addToChain(gotoHead)
    addToChain(end)

    return this
  }

  private def addToChain(s : Stmt) : Unit = {
    if (finalized) {
      throw new IllegalStateException("Attempt to modify a finalyzed BodyBuilder")
    }
    val result = chain.add(s)
    if (!result) {
      throw new IllegalArgumentException(s"Failed adding statement `${s}' to chain ${chain}")
    }
  }

  private def addToChain(body: BodyBuilder) : Unit = {
    for (s <- body.chain.asScala) addToChain(s.asInstanceOf[Stmt])
    // finalize the body builder
    body.build()
  }

}

object BodyBuilder {
  def apply() : BodyBuilder = new BodyBuilder()
  def begin() : BodyBuilder = new BodyBuilder()
  def begin(b : Body) : BodyBuilder = {
    val result = new BodyBuilder()
    for (s <- b.getUnits.asScala) result.addToChain(s.asInstanceOf[Stmt])
    result
  }
}
