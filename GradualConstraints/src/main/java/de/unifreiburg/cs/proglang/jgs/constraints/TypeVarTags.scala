package de.unifreiburg.cs.proglang.jgs.constraints

import de.unifreiburg.cs.proglang.jgs.jimpleutils.CastUtils.Conversion
import soot.{SootField, SootMethod}

import scala.language.existentials

/**
  * Created by fennell on 2/29/16.
  */
object TypeVarTags {

  /**
    * Additional information that may be attached to a type variables, e.g. for denoting it's origin or destination.
    */
  sealed trait TypeVarTag
  sealed case class Symbol(symbol: Symbol) extends TypeVarTag
  sealed case class Field(field : SootField) extends TypeVarTag
  sealed case class Cast(conversion: Conversion[_]) extends TypeVarTag
  sealed case class MethodReturn(method : SootMethod) extends TypeVarTag
  sealed case class MethodArg(method : SootMethod, pos : Int) extends TypeVarTag
  sealed case class CxCast(conversion : Conversion[_]) extends TypeVarTag
  sealed case class Join(condition: String) extends TypeVarTag
}
