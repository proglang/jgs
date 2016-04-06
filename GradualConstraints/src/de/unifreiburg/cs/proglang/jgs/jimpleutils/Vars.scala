package de.unifreiburg.cs.proglang.jgs.jimpleutils

import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol
import soot.Local
import soot.Value
import soot.ValueBox
import soot.jimple.AbstractJimpleValueSwitch
import soot.jimple.ParameterRef
import soot.jimple.ThisRef

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
  * Created by fennell on 3/31/16.
  */
object Vars {
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
        result.add(Var.fromThis(v))
      }

      override def caseParameterRef(v: ParameterRef) {
        result.add(Var.fromParam(Symbol.param(v.getIndex)))
      }

      override def caseLocal(l: Local) {
        result.add(Var.fromLocal(l))
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