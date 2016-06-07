package de.unifreiburg.cs.proglang.jgs.constraints

/**
  * Created by fennell on 2/29/16.
  */
object TypeVarViews {

  sealed trait TypeVarView
  case class Param(pos : Int) extends TypeVarView
  case class Cx() extends TypeVarView
  case class Ret() extends TypeVarView
  case class Internal(description : String) extends TypeVarView

}
