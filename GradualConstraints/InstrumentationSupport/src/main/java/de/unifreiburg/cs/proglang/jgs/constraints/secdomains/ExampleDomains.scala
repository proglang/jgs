package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.Edge


/**
  * Some hard-coded `UserDefinedDomain`s to serve as example and for testing.
  */
object ExampleDomains {

  val top = "top"
  val bottom = "bottom"

  val aliceBobCharlie : UserDefined = {
    val alice = "alice"
    val bob = "bob"
    val charlie = "charlie"
    val principals = Set(alice, bob, charlie)
    UserDefinedUtils.makeSecDomain(
      Set(bottom, top) ++ principals,
      (for (p <- principals) yield new Edge(bottom, p)) ++ (for (p <- principals) yield new Edge(p, top))
    )
  }

}
