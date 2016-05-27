package de.unifreiburg.cs.proglang.jgs.constraints

import TypeVars.TypeVar

/**
  * Created by fennell on 2/29/16.
  */
object CTypeViews {

  sealed trait CTypeView[Level]
  case class Lit[Level](t : TypeDomain.Type[Level]) extends CTypeView[Level]
  case class Variable[Level](v : TypeVar) extends CTypeView[Level]
}
