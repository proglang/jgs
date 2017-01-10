package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.Edge
import org.scalatest._

class UserDefinedTest extends FlatSpec with Matchers {

  import UserDefinedLevels._


  // some utils for converting between Edge and (String, String)
  def closeTransitively(edges : Set[(String, String)]) : Set[Edge] =
    UserDefinedUtils.closeTransitively(asEdgeSet(edges))
  def asEdgeSet(edges : Set[(String, String)]) : Set[Edge] = (for {(left, right) <- edges} yield Edge(left, right))


  "Closing {public -> manager, manager -> top}" should "add {public -> top}" in {
    closeTransitively(Set(levPublic -> levManager, levManager -> levTop)) should
      be(asEdgeSet(Set(levPublic -> levManager, levManager -> levTop, levPublic -> levTop)))
  }

  val corpPrincipals = Set(levPublic, levEmployee, levManager, levTop)
  val corpEdges =
    asEdgeSet(Set(
      levPublic -> levEmployee,
      levEmployee -> levManager,
      levManager -> levTop
    ))

  "Closing corporate edges" should "yield (public -> employee -> manager -> top)" in {
    UserDefinedUtils.closeTransitively(corpEdges) should
      be (corpEdges ++ asEdgeSet(Set(
        levPublic -> levTop,
        levPublic -> levManager,
        levEmployee -> levTop
      )))
  }

  "Upper bounds" should "work for {public, employee}" in {
    val lt = UserDefinedUtils.closeTransitively(corpEdges)
    val le = (l1 : String, l2 : String) => lt(Edge(l1, l2)) || l1 == l2
    UserDefinedUtils.upperBounds(corpPrincipals, le, levPublic, levEmployee) should
      be (Set(levManager, levEmployee, levTop))
  }

}
