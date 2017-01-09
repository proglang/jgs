package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import de.unifreiburg.cs.proglang.jgs.JgsCheck
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.{Edge}
import org.scalatest._

class UserDefinedParseTest extends FlatSpec with Matchers {

  val domainSpecFileContent : String = """
    levels:
      - bot
      - alice
      - bob
      - charlie
      - top

    lt-edges:
      - [ bot , alice ]
      - [ bot , bob ]
      - [ bot , charlie ]
      - [ alice , top ]
      - [ bob, top ]
      - [ charlie, top ]
  """

  val domainSpec = UserDefinedUtils.fromJSon(JgsCheck.parseJson(domainSpecFileContent))

  def levelSet(ls : String*) : Set[String] =
    ls.toSet
  def edgeSet(edgeStrings : Set[(String,String)]) : Set[Edge] =
    edgeStrings.map(e => Edge(e._1, e._2))
  def edgeSet(es : (String,String)*) : Set[Edge] = edgeSet(es.toSet)

  "domainSpec" should "contain all the levels" in {
    domainSpec.levels should be (levelSet(
      "bot",
      "alice",
      "bob",
    "charlie",
      "top"
    ))
  }
  it should "contain all the edges" in {
     domainSpec.edges should be(edgeSet(
       "bot" -> "alice",
       "bot" -> "bob",
       "bot" -> "charlie",
       "alice" -> "top",
       "bob" -> "top",
       "charlie" -> "top"
     ))
  }




}
