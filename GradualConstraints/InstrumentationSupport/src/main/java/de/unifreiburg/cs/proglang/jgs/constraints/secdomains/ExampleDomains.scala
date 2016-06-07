package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.Level

/**
  * Some hard-coded `UserDefinedDomain`s to serve as example and for testing.
  */
object ExampleDomains {

  val top = Level("top")
  val bottom = Level("bottom")

  val aliceBobCharlie : UserDefined = {
    val alice = Level("alice")
    val bob = Level("bob")
    val charlie = Level("charlie")
    val principals = Set(alice, bob, charlie)
    UserDefined(
      Set(bottom, top) ++ principals,
      (for (p <- principals) yield (bottom -> p)) ++ (for (p <- principals) yield (p -> top))
    )
  }

}
