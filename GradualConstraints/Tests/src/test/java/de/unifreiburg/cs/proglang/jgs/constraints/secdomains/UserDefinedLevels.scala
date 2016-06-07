package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.Level

/**
  * Created by fennell on 5/10/16.
  */
object UserDefinedLevels {
  // the extremes
  val levPublic = Level("public")
  val levTop = Level("top")

  // the corporate domain
  val levEmployee = Level("employee")
  val levManager = Level("manager")

  // the personal domain
  val levAlice = Level("Alice")
  val levBob = Level("Bob")
  val levCharlie = Level("Charlie")
  val personalPrincipals = Set(levAlice, levBob, levCharlie)

}
