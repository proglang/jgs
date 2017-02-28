package de.unifreiburg.cs.proglang.jgs.jimpleutils

import de.unifreiburg.cs.proglang.jgs.instrumentation.Var
import soot.jimple.{AbstractJimpleValueSwitch, ParameterRef, ThisRef}
import soot.{Local, Value, ValueBox}

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
  * Utilities for constructing and extracting Var[_]s (local, params, ...)
  */
object Vars {

  // TODO: remove those, as they just forward to Var.
  def fromLocal(l : Local) : Var[Local] = Var.fromLocal(l)
  def fromThis(tr : ThisRef) : Var[ThisRef] = Var.fromThis(tr)
  def fromParam(p : Int) : Var[java.lang.Integer] = Var.fromParam(p)

  def map[T,S](v : Var[T], f : T => S) : Var[S] = new Var(f(v.repr))

  /**
    * see getAll(Value)
    */
  def getAllFromValues(bs: java.util.Collection[Value]): Iterator[Var[_]] = {
    return bs.iterator().flatMap(Vars.getAll)
  }

  /**
    * see getAll(Value)
    */
  def getAllFromValueBoxes(bs: java.util.Collection[ValueBox]): Iterator[Var[_]] = {
    return bs.iterator().flatMap((b : ValueBox) => getAll(b))
  }

  /**
    * see getAll(Value)
    */
  def getAll(b: ValueBox): Iterator[Var[_]] = {
    return getAll(b.getValue)
  }

  /**
    * Collect all variables from a jimple Value
    */
  def getAll(value: Value): Iterator[Var[_]] = {
    val result: ListBuffer[Var[_]] = ListBuffer()
    value.apply(new AbstractJimpleValueSwitch() {
      override def caseThisRef(v: ThisRef) {
        result.add(Vars.fromThis(v))
      }

      override def caseParameterRef(v: ParameterRef) {
        result.add(Vars.fromParam((v.getIndex)))
      }

      override def caseLocal(l: Local) {
        result.add(Vars.fromLocal(l))
      }

      @SuppressWarnings(Array("unchecked")) override def defaultCase(`object`: AnyRef) {
        val v: Value = `object`.asInstanceOf[Value]
        v.getUseBoxes.foreach(o => {
          o.asInstanceOf[ValueBox].getValue().apply(this);
        })
      }
    })
    return result.iterator
  }
}