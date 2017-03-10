package de.unifreiburg.cs.proglang.jgs.jimpleutils

import de.unifreiburg.cs.proglang.jgs.TestDomain
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh
import de.unifreiburg.cs.proglang.jgs.constraints.{TypeDomain, secdomains}
import org.scalatest.{FlatSpec, Matchers}
import soot.{Modifier, RefType, SootMethod, VoidType}
import soot.jimple.{Jimple, StaticInvokeExpr, StringConstant}

import scala.collection.JavaConversions._
import TestDomain._
import de.unifreiburg.cs.proglang.jgs.instrumentation.ACasts.{CxCast, ValueCast}
import de.unifreiburg.cs.proglang.jgs.instrumentation.CastsFromConstants

import scala.util.Success



object CastsFromConstantsTest {
  // utilities
  val tString = RefType.v("java.lang.String")
  val tObject = RefType.v("java.lang.Object")
  val tVoid = VoidType.v()
  val localX = Jimple.v().newLocal("x", tObject)
  val localString = Jimple.v().newLocal("aString", tString)
  val varX = Vars.fromLocal(localX)
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
