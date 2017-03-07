package de.unifreiburg.cs.proglang.jgs.instrumentation

import java.util.Collections

import de.unifreiburg.cs.proglang.jgs.TestDomain
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.{Dyn, Pub}
import de.unifreiburg.cs.proglang.jgs.constraints.{TypeDomain, TypeVars}
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level
import de.unifreiburg.cs.proglang.jgs.examples.{AnalysisResults, Code}
import org.scalatest.{FlatSpec, Matchers}
import de.unifreiburg.cs.proglang.jgs.instrumentation.BodyTypingSpec._
import de.unifreiburg.cs.proglang.jgs.signatures._
import de.unifreiburg.cs.proglang.jgs.typing.{Environments, MethodBodyTyping}
import soot.SootMethod
import de.unifreiburg.cs.proglang.jgs.jimpleutils.{Methods, Vars}
import soot.jimple.Stmt

import scala.collection.JavaConversions._

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

  // utilities for creating variable mappings, etc
  val utils = new TypingUtils(new LowHigh)

  // a fake cx typing that always returns public
  val publicCxTyping =  new CxTyping[LowHigh.Level]() {
    override def get(instantiation: Instantiation[Level], s: Stmt): Type[Level] = utils.instrumentationType(Pub())
  }

  "max_DD_D's example mappings" should "correspond to mappings_max_DD_D" in {
    val results = new AnalysisResults[LowHigh]()
    mappingsFromResults(
      results.max_methods_D_D__D.getMonomorphicInstantiation(Code.max),
      results.max_cxTyping,
      results.max_varTyping,
      Code.max) should be(mappings_max_DD_D)
  }


  "env map of max" should "map localX after first statement (and nothing else)" in {
    val tvs = new TypeVars()
    val bodyResult = new MethodBodyTyping[LowHigh.Level](Code.max, tvs, TestDomain.csets, TestDomain.cstrs,
      TestDomain.casts,
        SignatureTable.makeTable(Map(
          Code.max -> MethodSignatures.makeSignature[LowHigh.Level](2,
            List(
              MethodSignatures.le[LowHigh.Level](Param(0), Literal(TestDomain.DYN)),
              MethodSignatures.le[LowHigh.Level](Param(1), Literal(TestDomain.DYN)),
              MethodSignatures.le[LowHigh.Level](Return(), Literal(TestDomain.DYN))
            ),
      Effects.emptyEffect()))), new FieldTable(Collections.emptyMap())).generateResult(
        Code.max.retrieveActiveBody(),
      tvs.topLevelContext(),
      Environments.forParamMap(tvs, de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods.symbolMapForMethod(tvs, Code.max))
        )

    bodyResult.envMap.getPre(Code.max_0_id_X_p0).get.get(Vars.fromLocal(Code.localZ)) shouldBe empty
    bodyResult.envMap.getPre(Code.max_0_id_X_p0).get.get(Vars.fromLocal(Code.localX)) shouldBe empty
    bodyResult.envMap.getPost(Code.max_0_id_X_p0).get.get(Vars.fromLocal(Code.localX)) should not be empty
    bodyResult.envMap.getPost(Code.max_0_id_X_p0).get.get(Vars.fromLocal(Code.localX)).get should have size 1


  }

  "vartyping of max from env map" should "map everything to public before first statement and localX to dyn afterwards" in {
    val tvs = new TypeVars()
    val bodyResult = new MethodBodyTyping[LowHigh.Level](Code.max, tvs, TestDomain.csets, TestDomain.cstrs,
      TestDomain.casts,
      SignatureTable.makeTable(Map(
        Code.max -> MethodSignatures.makeSignature[LowHigh.Level](2,
          List(
            MethodSignatures.le[LowHigh.Level](Param(0), Literal(TestDomain.DYN)),
            MethodSignatures.le[LowHigh.Level](Param(1), Literal(TestDomain.DYN)),
            MethodSignatures.le[LowHigh.Level](Return(), Literal(TestDomain.DYN))
          ),
          Effects.emptyEffect()))), new FieldTable(Collections.emptyMap())).generateResult(
      Code.max.retrieveActiveBody(),
      tvs.topLevelContext(),
      Environments.forParamMap(tvs, de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods.symbolMapForMethod(tvs, Code.max))
    )

    val typing = utils.varTypingFromEnvMap(bodyResult.envMap, bodyResult.constraints)
    val allDynamicInst = new Instantiation[LowHigh.Level] {
      override def get(param: Int): Type[Level] = utils.instrumentationType(Dyn())

      override def getReturn: Type[Level] = utils.instrumentationType(Dyn())
    }

    val publicType = utils.instrumentationType(Pub())

    typing.getBefore(allDynamicInst, Code.max_0_id_X_p0, Code.localX) shouldBe 'isPublic
    typing.getBefore(allDynamicInst, Code.max_0_id_X_p0, Code.localZ) shouldBe 'isPublic

    typing.getAfter(allDynamicInst, Code.max_0_id_X_p0, Code.localX) shouldBe 'isDynamic

  }

  "vartyping" should "be complete for all statements of max" in {

    val dynInstantiation = new AnalysisResults[LowHigh.Level]().max_methods_D_D__D.getMonomorphicInstantiation(Code.max)
    val result = TestDomain.mtyping.check(new TypeVars(),
      SignatureTable.makeTable(Map(
        Code.max -> MethodSignatures.makeSignature[LowHigh.Level](2,
          List(
            MethodSignatures.le[LowHigh.Level](Param(0), Literal(TestDomain.DYN)),
            MethodSignatures.le[LowHigh.Level](Param(1), Literal(TestDomain.DYN)),
            MethodSignatures.le[LowHigh.Level](Return(), Literal(TestDomain.DYN))
          ),
          Effects.emptyEffect()))),
      new FieldTable[LowHigh.Level](java.util.Collections.emptyMap()), Code.max)

    val body = Code.max.retrieveActiveBody()
    for { unit <- body.getUnits
          stmt = unit.asInstanceOf[Stmt]
          l <- body.getLocals
        } {

      noException should be thrownBy result.variableTyping.getBefore(dynInstantiation, stmt, l)
      noException should be thrownBy result.variableTyping.getAfter(dynInstantiation, stmt, l)
    }
  }

  "type-checking max with a DD_D instantiation" should " have same vartyping as mappings_max_DD_D" in {
    val fakeResults = new AnalysisResults[LowHigh.Level]()
    val result = TestDomain.mtyping.check(new TypeVars(),
      SignatureTable.makeTable(Map(
        Code.max -> MethodSignatures.makeSignature[LowHigh.Level](2,
          List(
            MethodSignatures.le[LowHigh.Level](Param(0), Literal(TestDomain.DYN)),
            MethodSignatures.le[LowHigh.Level](Param(1), Literal(TestDomain.DYN)),
            MethodSignatures.le[LowHigh.Level](Return(), Literal(TestDomain.DYN))
          ),
          Effects.emptyEffect()))),
      new FieldTable[LowHigh.Level](java.util.Collections.emptyMap()), Code.max)

    val expected = for (m <- mappings_max_DD_D) yield m.copy(cxPhase = P)

    mappingsFromResults[LowHigh.Level](
      fakeResults.max_methods_D_D__D.getMonomorphicInstantiation(Code.max),
      publicCxTyping,
      result.variableTyping,
      Code.max
    ) shouldBe expected
  }



}
