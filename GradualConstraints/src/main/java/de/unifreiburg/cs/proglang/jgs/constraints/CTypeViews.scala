package de.unifreiburg.cs.proglang.jgs.constraints

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView

/**
  * Created by fennell on 2/29/16.
  */
object CTypeViews {

  sealed trait CTypeView[Level] //like enums in java
  case class Lit[Level](t : TypeView[Level]) extends CTypeView[Level]
  case class Variable[Level](v : TypeVar) extends CTypeView[Level]
}
