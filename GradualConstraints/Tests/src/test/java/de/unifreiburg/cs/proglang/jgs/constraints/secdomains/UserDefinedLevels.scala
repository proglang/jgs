package de.unifreiburg.cs.proglang.jgs.constraints.secdomains


/**
  * Created by fennell on 5/10/16.
  */
object UserDefinedLevels {
  // the extremes
  val levPublic = "public"
  val levTop = "top"

  // the corporate domain
  val levEmployee = "employee"
  val levManager = "manager"

  // the personal domain
  val levAlice = "Alice"
  val levBob = "Bob"
  val levCharlie = "Charlie"
  val personalPrincipals = Set(levAlice, levBob, levCharlie)

}
