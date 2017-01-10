package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.{Edge}
import org.scalatest._
import matchers._

import scala.collection.JavaConversions._

class UserDefinedDomainsTest extends FlatSpec with Matchers {

  import UserDefinedLevels._

  val corporateDomain = UserDefinedUtils(
    Set(levPublic, levEmployee, levManager, levTop),
    Set(Edge(levPublic, levEmployee), Edge(levEmployee, levManager), Edge(levManager, levTop))
  )

  val personalDomain = {
    UserDefinedUtils(
      personalPrincipals ++ Set(levPublic, levTop),
      (for (l <- personalPrincipals) yield Edge(levPublic, l)) ++
        (for (l <- personalPrincipals) yield Edge(l, levTop))
    )
  }

  val validDomains = Seq(corporateDomain, personalDomain)

  def leWith(dom : SecDomain[String], greater : String) : Matcher[String] =
    new Matcher[String] {
      override def apply(left: String): MatchResult =
        MatchResult(
          dom.le(left, greater),
          s"${left} is not le ${greater}\n domain:  ${dom.toString}",
          s"${left} is le ${greater}\n domain:  ${dom.toString}"
        )
    }

  "Public/Top" should "be smaller/larger than anything" in {
    for (dom <- validDomains; l <- dom.enumerate()) {
      levPublic should leWith(dom, l)
      l should leWith(dom, levTop)
    }
  }

  it should "be bottom()/top(), resp." in {
    for (dom <- validDomains) {
      dom.bottom() should be (levPublic)
      dom.top() should be (levTop)
    }
  }

  "Employee" should "be smaller than Manager and Top" in {
    import corporateDomain._
    for (l <- Seq(levManager, levTop)) {
      le(levEmployee, l) should be (true)
    }
  }

  "Principals" should "not be related" in {
    import personalDomain._
    for (l1 <- personalPrincipals; l2 <- personalPrincipals; if l1 != l2) {
      le(l1, l2) should be (false)
    }
  }

  "Level.toString" should "give parseable results" in {
    import personalDomain._
    for (l <- personalPrincipals) {
      readLevel(l.toString) should be (l)
    }
  }

  "Parsing a string to level and printing" should "yield the original string" in {
    import personalDomain._
    for (s <- Seq("Alice", "Bob", "Charlie")) {
      readLevel(s).toString should be (s)
    }
  }




}
