package de.unifreiburg.cs.proglang.jgs

import soot.{Body, SootMethod, VoidType, PatchingChain}

import scala.collection.JavaConverters._

import soot.jimple.{Jimple, Stmt, JimpleBody}


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

  private def addToChain(s : Stmt) : Unit = {
    if (finalized) {
      throw new IllegalStateException("Attempt to modify a finalyzed BodyBuilder")
    }
    val result = chain.add(s)
    if (!result) {
      throw new IllegalArgumentException(s"Failed adding statement `${s}' to chain ${chain}")
    }
  }

}

object BodyBuilder {
  def apply() : BodyBuilder = new BodyBuilder()
  def begin() : BodyBuilder = new BodyBuilder()
}
