package de.unifreiburg.cs.proglang.jgs

import de.unifreiburg.cs.proglang.jgs.typing.MethodTyping
import de.unifreiburg.cs.proglang.jgs.typing.MethodTyping.Result
import soot.SootMethod

import scala.collection.mutable.ArrayBuffer

/**
  * Collect unexpected results from type checking methods
  */
class TestCollector[Level] {

  import TestCollector._

  private val observed : ArrayBuffer[SootMethod] = ArrayBuffer()
  private val errors : ArrayBuffer[Exceptional] = ArrayBuffer()
  private val failures : ArrayBuffer[Fail] = ArrayBuffer()

  def observe(method : SootMethod, result : MethodTyping.Result[Level]) : Unit = {
    observed += method
    if (shouldFail(method)) {
       if (result.isSuccess)
         failures += UnexpectedSuccess(method)
    } else if (!result.isSuccess){
      failures += UnexpectedFailure(method, result)
    }
  }

  def observeError(method : SootMethod, exception : Throwable) : Unit = {
    observed += method
    errors += Exceptional(method, exception)
  }

  def getObserved = observed.toList
  def getErrors = errors.toList
  def getFailures = failures.toList

  private def shouldFail(method : SootMethod) : Boolean =
    method.getName.contains("ERROR_")

}

object TestCollector {
  sealed abstract class Fail(val method : SootMethod)
  sealed case class UnexpectedSuccess(override val method : SootMethod) extends Fail(method)
  sealed case class UnexpectedFailure[Level](override val method : SootMethod, result : MethodTyping.Result[Level]) extends Fail(method)

  sealed case class Exceptional(method : SootMethod, exception : Throwable)

  def apply[Level]() : TestCollector[Level] = new TestCollector()

}
