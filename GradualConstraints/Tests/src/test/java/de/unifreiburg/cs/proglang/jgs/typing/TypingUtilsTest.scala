package de.unifreiburg.cs.proglang.jgs.typing

import de.unifreiburg.cs.proglang.jgs.Code
import de.unifreiburg.cs.proglang.jgs.constraints.{CTypes, ConstraintSet, TypeVars}
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHighTest
import de.unifreiburg.cs.proglang.jgs.instrumentation.{Instantiation, TypingUtils}
import de.unifreiburg.cs.proglang.jgs.util.Interop
import org.junit.Test
import java.util.stream.Collectors
import java.util.stream.Stream

import de.unifreiburg.cs.proglang.jgs.util.Interop.asScalaSet
import java.util.Arrays.asList

import de.unifreiburg.cs.proglang.jgs.TestDomain._
import java.util.stream.Collectors.toSet

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeViews.CTypeView
import junit.framework.Assert._

class TypingUtilsTest {
  final private val tvs = new TypeVars
  final private val code = new Code(tvs)
  final private val typingUtils = new TypingUtils[LowHigh.Level](levels)
  // ..getMonomorphicInstantiation has a dummy argument.. allDynamic does not care
  val dynInstantiation: Instantiation[LowHigh.Level] =
    FixedTypings.allDynamic[LowHigh.Level]().getMonomorphicInstantiation(code.ignore0Low1ReturnHigh);

  @Test def testLowerBounds(): Unit = {
    val cs = csets.fromCollection(asList(leC(CDYN, code.tvarX),
                                         leC(code.tvarX, code.tvarY),
                                         leC(code.tvarZ, code.tvarY)))
    assertEquals(Set(), cs.lowerBounds(code.tvarZ))

    val expected : Set[CTypeView[LowHigh.Level]] = Set(CDYN.inspect(),
                                                       CTypes.variable(code.tvarX).inspect(),
                                                       CTypes.variable(code.tvarZ).inspect())
    assertEquals(expected,
                 cs.lowerBounds(code.tvarY))
  }

  @Test def testConcreteLowerBounds(): Unit = {
    val cs = csets.fromCollection(asList(leC(CDYN, code.tvarX),
                                         leC(code.tvarX, code.tvarY),
                                         leC(code.tvarZ, code.tvarY)))

    val lbs1 = typingUtils.concreteLowerBound(
      Set(code.tvarX, code.tvarY),
      cs,
      dynInstantiation
      )
    // TODO: should work
    // assertEquals(lbs1, CDYN)
    assertTrue(s"not dynamic: ${lbs1}", lbs1.isDynamic)

    val lbs2 = typingUtils.concreteLowerBound(
      Set(code.tvarZ),
      cs,
      dynInstantiation)
    val realLbs = typingUtils.lowerBoundLiteralsOrParams(cs, code.tvarZ, dynInstantiation)
    assertEquals(realLbs, Set())
    assertTrue(s"not public: ${lbs2}", lbs2.isPublic)

  }

  @Test def testConcreteLowerBoundsWithComp(): Unit = {
    val cs = csets.fromCollection(asList(leC(CDYN, code.tvarX), leC(code.tvarX, code.tvarY)))
  }
}