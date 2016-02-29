package de.unifreiburg.cs.proglang.jgs.constraints

import de.unifreiburg.cs.proglang.jgs.jimpleutils.CastsFromMapping.Conversion
import soot.{SootMethod, SootField}

/**
  * Created by fennell on 2/29/16.
  */
object TypeVarTags {

  /**
    * Additional information that may be attached to a type variables, e.g. for denoting it's origin or destination.
    */
  sealed abstract trait TypeVarTag
  case class Null() extends TypeVarTag
  case class Symbol(symbol: Symbol) extends TypeVarTag
  case class Field(field : SootField) extends TypeVarTag
  case class Cast(conversion: Conversion[_]) extends TypeVarTag
  case class MethodReturn(method : SootMethod) extends TypeVarTag
  case class MethodArg(method : SootMethod, pos : Int) extends TypeVarTag
}
