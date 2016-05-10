package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.Level
import org.scalatest._

class UserDefinedTest extends FlatSpec with Matchers {

  import UserDefinedLevels._

  "Closing {public -> manager, manager -> top}" should "add {public -> top}" in {
    UserDefined.closeTransitively(Set(levPublic -> levManager, levManager -> levTop)) should
      be(Set(levPublic -> levManager, levManager -> levTop, levPublic -> levTop))
  }

  val corpPrincipals = Set(levPublic, levEmployee, levManager, levTop)
  val corpEdges =
    Set(
      levPublic -> levEmployee,
      levEmployee -> levManager,
      levManager -> levTop
    )

  "Closing corporate edges" should "yield (public -> employee -> manager -> top)" in {
    UserDefined.closeTransitively(corpEdges) should
      be (corpEdges ++ Set(
        levPublic -> levTop,
        levPublic -> levManager,
        levEmployee -> levTop
      ))
  }

  "Upper bounds" should "work for {public, employee}" in {
    val lt = UserDefined.closeTransitively(corpEdges)
    val le = (l1 : Level, l2 : Level) => lt(l1, l2) || l1 == l2
    UserDefined.upperBounds(corpPrincipals, le, levPublic, levEmployee) should
      be (Set(levManager, levEmployee, levTop))
  }

}
