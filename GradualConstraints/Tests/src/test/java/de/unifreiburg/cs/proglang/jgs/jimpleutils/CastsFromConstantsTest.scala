package de.unifreiburg.cs.proglang.jgs.jimpleutils

import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts.{CxCast, ValueCast}
import de.unifreiburg.cs.proglang.jgs.TestDomain
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh
import de.unifreiburg.cs.proglang.jgs.constraints.{TypeDomain, secdomains}
import org.scalatest.{Matchers, FlatSpec}
import soot.{Modifier, VoidType, RefType, SootMethod}
import soot.jimple.{StringConstant, Jimple, StaticInvokeExpr}

import scala.collection.JavaConversions._


import TestDomain._

import scala.util.Success

class CastsFromConstantsTest extends FlatSpec with Matchers{
  import CastsFromConstantsTest._

  val casts = new CastsFromConstants(types.typeParser(),"<testCasts: java.lang.Object cast(java.lang.String,java.lang.Object)>",
          "<testCasts: void castCx(java.lang.String)>", "<testCasts: void castCxEnd()>")


  "valueCastMethod.toString" should "be <testCasts: java.lang.Object cast(java.lang.String,java.lang.Object)>" in {
    valueCastMethod.toString should be ("<testCasts: java.lang.Object cast(java.lang.String,java.lang.Object)>")
  }

  "cast(LOW ~> HIGH, x)" should "convert from LOW to HIGH" in {
    casts.detectValueCastFromCall(lowToHigh) should be (Success(Some(new ValueCast[LowHigh.Level](TLOW, THIGH, Some(varX)))))
  }

  it should "not be a context casts" in {
    casts.detectContextCastStartFromCall(lowToHigh) should be (Success(None))
    casts.detectContextCastEndFromCall(lowToHigh) should be (false)
  }

  "cast(y,x)" should "throw an exception" in {
    an [IllegalArgumentException] should be thrownBy casts.detectValueCastFromCall(wrongLowToHigh).get

  }

  "cxCast(? ~> LOW)" should "convert cx from ? to LOW" in {
    casts.detectContextCastStartFromCall(cxDynToLow) should be (Success(Some (new CxCast[LowHigh.Level](DYN, TLOW))))
  }

  it should "not be a value cast or cx-cast end" in {
    casts.detectValueCastFromCall(cxDynToLow)  should be (Success(None))
    casts.detectContextCastEndFromCall(cxDynToLow) should be (false)
  }

  "cxCaseEnd" should "be a cx cast end" in {
    casts.detectContextCastEndFromCall(cxEnd) should be (true)
  }

  it should "not be a value cast or context cast" in {
    casts.detectValueCastFromCall(cxEnd) should be (Success(None))
    casts.detectContextCastStartFromCall(cxEnd) should be (Success(None))
  }

}

object CastsFromConstantsTest {
  // utilities
  val tString = RefType.v("java.lang.String")
  val tObject = RefType.v("java.lang.Object")
  val tVoid = VoidType.v()
  val localX = Jimple.v().newLocal("x", tObject)
  val localString = Jimple.v().newLocal("aString", tString)
  val varX = Var.fromLocal(localX)
  val valueCastMethod = new SootMethod("cast", List(tString, tObject),tObject,Modifier.STATIC)
  castClass.addMethod(valueCastMethod)
  val cxCastMethod = new SootMethod("castCx", List(tString), tVoid, Modifier.STATIC)
  castClass.addMethod(cxCastMethod)

  // ATTENTION: do not add cxCastEnd, as it is already present in TestDomain
  //
  // val cxCastEndMethod = new SootMethod("castCxEnd", List(), tVoid, Modifier.STATIC)
  // castClass.addMethod(cxCastEndMethod)


  // LOW ~> HIGH
  val lowToHigh = Jimple.v().newStaticInvokeExpr(
    valueCastMethod.makeRef(),
    StringConstant.v("LOW ~> HIGH"), localX)

  val wrongLowToHigh = Jimple.v().newStaticInvokeExpr(
    valueCastMethod.makeRef(), localString, localX
  )

  // cx ? ~> LOW
  val cxDynToLow = Jimple.v().newStaticInvokeExpr(
    cxCastMethod.makeRef(), StringConstant.v("? ~> LOW"))

  // cx end
  val cxEnd = Jimple.v().newStaticInvokeExpr((
    castCxEnd.makeRef()
    ))

}
