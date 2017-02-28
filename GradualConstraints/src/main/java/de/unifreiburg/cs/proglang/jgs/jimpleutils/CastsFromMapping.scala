package de.unifreiburg.cs.proglang.jgs.jimpleutils

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain._
import de.unifreiburg.cs.proglang.jgs.instrumentation.{ACasts, Var}
import de.unifreiburg.cs.proglang.jgs.instrumentation.ACasts.{CxCast, ValueCast}
import de.unifreiburg.cs.proglang.jgs.jimpleutils.CastsFromMapping._
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser
import soot.SootMethod
import soot.jimple.StaticInvokeExpr

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}
import de.unifreiburg.cs.proglang.jgs.instrumentation.CastUtils._

import de.unifreiburg.cs.proglang.jgs.instrumentation.ACasts._;


/**
  * Create a cast module from mappings of fully qualified method names to casts
  */
case class CastsFromMapping[Level](valueCasts: Map[String, TypeViewConversion[Level]],
                                   cxCastBegins: Map[String, TypeViewConversion[Level]],
                                   cxCastEnd: String)
  extends ACasts[Level] {

  override def detectValueCastFromCall(e: StaticInvokeExpr): Try[Option[ValueCast[Level]]] = {
    val key = e.getMethod.toString
    Success(valueCasts.get(key)
      .map(c => new ValueCast[Level](c.source, c.dest, getCallArgument(e))
    ))
  }

  override def detectContextCastEndFromCall(e: StaticInvokeExpr): Boolean = {
    val key = e.getMethod.toString
    key == cxCastEnd
  }

  override def detectContextCastStartFromCall(e: StaticInvokeExpr): Try[Option[CxCast[Level]]] = {
    val key = e.getMethod.toString
    Success(cxCastBegins.get(key)
      .map(c => new CxCast[Level](c.source, c.dest))
    )
  }
}

object CastsFromMapping {

  def apply[Level](valueCasts: java.util.Map[String, TypeViewConversion[Level]],
                   cxCastBegins: java.util.Map[String, TypeViewConversion[Level]],
                   cxCastEnd: String) : CastsFromMapping[Level] = new CastsFromMapping[Level](valueCasts.asScala.toMap, cxCastBegins.asScala.toMap, cxCastEnd)

  private def getCallArgument(e: StaticInvokeExpr): Option[Var[_]] = {
    if (e.getArgCount != 1) {
      throw new IllegalArgumentException(s"Illegal parameter count on cast method `${e}'. Expected single argument method.")
    }
    Vars.getAll(e.getArg(0)).find(_ => true)
  }

}

