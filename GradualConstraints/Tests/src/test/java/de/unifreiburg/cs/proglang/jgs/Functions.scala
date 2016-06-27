package de.unifreiburg.cs.proglang.jgs

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType
import de.unifreiburg.cs.proglang.jgs.constraints.Constraint
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level

import de.unifreiburg.cs.proglang.jgs.TestDomain._

/**
  * Java friendly construction of functions needed to interact w/ Scala
  */
object Functions {

  /**
    * leC(_, ct)
    */
  def isLe(ct : CType[Level]) : CType[Level] => Constraint[Level] = leC(_, ct)

  /**
    * leC(ct, _)
    */
  def isGe(ct : CType[Level]) : CType[Level] => Constraint[Level] = leC(ct, _)

}
