package de.unifreiburg.cs.proglang.jgs.instrumentation

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh
import de.unifreiburg.cs.proglang.jgs.examples.{AnalysisResults, Code}
import org.scalatest.{FlatSpec, Matchers}
import de.unifreiburg.cs.proglang.jgs.instrumentation.BodyTypingSpec._

/**
  * Test if the type checker gives the right instrumentation hints
  */
class InstrumentationTypingResultsTest extends FlatSpec with Matchers {

  // TypeMappings for the max method.
  //   7 lines, variables x,y,z
  val x = Code.localX
  val y = Code.localY
  val z = Code.localZ

  def xyzMap (cxPhase : Phase, varPhases : Tuple2[Phase, Phase]*) = makeMapper(x, y, z)(cxPhase, varPhases:_*)
  val mappings_max_DD_D =
    Seq(
      xyzMap(P, P -> D, P -> P, P -> P),
      xyzMap(P, D -> D, P -> D, P -> P),
      xyzMap(P, D -> D, D -> D, P -> D),
      // if ...
      xyzMap(P, D -> D, D -> D, D -> D),
      // then
      xyzMap(D, D -> D, D -> D, D -> D),
      // else
      xyzMap(D, D -> D, D -> D, D -> D),
      // end if
      xyzMap(P, D -> D, D -> D, D -> D)
    )

  val mappings_max_DP_D =
    Seq(
      xyzMap(P, P -> D, P -> P, P -> P),
      xyzMap(P, D -> D, P -> P, P -> P),
      xyzMap(P, D -> D, P -> P, P -> D),
      // if ...
      xyzMap(D, D -> D, P -> P, D -> D),
      // then
      xyzMap(D, D -> D, P -> P, D -> D),
      // else
      xyzMap(P, D -> D, P -> P, D -> D),
      xyzMap(P, D -> D, P -> P, D -> D)
    )

  val mappings_max_SP_S =
    Seq(
      xyzMap(P, P -> S, P -> P, P -> P),
      xyzMap(P, S -> S, P -> P, P -> P),
      xyzMap(P, S -> S, P -> P, P -> S),
      // if ...
      xyzMap(D, S -> S, P -> P, S -> S),
      // then
      xyzMap(S, S -> S, P -> P, S -> S),
      // else
      xyzMap(P, S -> S, P -> P, S -> S),
      xyzMap(P, S -> S, P -> P, S -> S)
    )

  "max_DD_D's example mappings" should "correspond to mappings_max_DD_D" in {
    val results = new AnalysisResults[LowHigh]()
    mappingsFromResults(
      results.max_methods_D_D__D.getMonomorphicInstantiation(Code.max),
      results.max_cxTyping,
      results.max_varTyping,
      Code.max) should be(mappings_max_DD_D)

  }



}
