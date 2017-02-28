package de.unifreiburg.cs.proglang.jgs.signatures

import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView


/**
  * JGS Symbols that occur in method signatures.
  * They correspond to Jimple's Parameters, @return, or literal security levels
  */
sealed trait Symbol[Level]
sealed case class Param[Level] (position : Int) extends Symbol[Level] {

  if (position < 0) {
    throw new IllegalArgumentException(s"Negative position for parameter: ${position}")
  }

  override def toString: String = s"@param${position}"
}
sealed case class Return[Level] () extends Symbol[Level] {
  override def toString : String = "@return"
}
sealed case class Literal[Level] (level : TypeView[Level]) extends Symbol[Level] {
  override def toString : String = level.toString
}

object Symbol {

  def param[Level](pos : Int) : Param[Level] = {
    Param(pos)
  }

  def ret[Level]() : Return[Level] = Return()

  def literal[Level](t : TypeView[Level]) = Literal(t)
}

