package de.unifreiburg.cs.proglang.jgs.signatures

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type

object SymbolViews {

  abstract sealed trait SymbolView[Level]
  sealed case class Param[Level](val position : Int) extends SymbolView[Level]
  sealed case class Return[Level]() extends SymbolView[Level]
  sealed case class Literal[Level](val level : Type[Level]) extends SymbolView[Level]

}
