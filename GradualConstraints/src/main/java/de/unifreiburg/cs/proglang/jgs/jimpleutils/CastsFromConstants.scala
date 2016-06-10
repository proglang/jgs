package de.unifreiburg.cs.proglang.jgs.jimpleutils

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type
import de.unifreiburg.cs.proglang.jgs.jimpleutils.CastUtils.Conversion
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts.{ValueCast, CxCast}
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser
import soot.SootMethod
import soot.jimple.{StringConstant, StaticInvokeExpr}

/**
  * Cast detectors that read their conversion from String constants
  */
class CastsFromConstants[Level](typeParser: AnnotationParser[Type[Level]],
                                valueCast: String,
                                cxCastBegin: String, cxCastEnd: String)
extends Casts[Level] {
  override def detectValueCastFromCall(e: StaticInvokeExpr): Option[ValueCast[Level]] = {
    PartialFunction.condOpt(e.getMethod.toString) {
      case s if s == valueCast => {
        val conv = CastsFromConstants.getConversionFromCall(typeParser, e)
        val v = CastsFromConstants.getVariableFromCall(e)
        new ValueCast[Level](conv.source, conv.dest, v)
      }
    }
  }

  override def detectContextCastEndFromCall(e: StaticInvokeExpr): Boolean = {
    e.getMethod.toString == cxCastEnd
  }

  override def detectContextCastStartFromCall(e: StaticInvokeExpr): Option[CxCast[Level]] = {
    PartialFunction.condOpt(e.getMethod.toString) {
      case s if s == cxCastBegin => {
        val conv = CastsFromConstants.getConversionFromCall(typeParser, e)
        new CxCast[Level](conv.source, conv.dest)
      }
    }
  }
}

object CastsFromConstants {

  def getConversionFromCall[Level](typeParser : AnnotationParser[Type[Level]], expr: StaticInvokeExpr) : Conversion[Level] = {
    val maybeConv = for {
      e <- Some(expr)
      if e.getArgCount >= 1
      sConst <- PartialFunction.condOpt(e.getArg(0)) { case s: StringConstant => s }
    } yield sConst.value

    maybeConv match {
      case Some(convString) => CastUtils.parseConversion(typeParser, convString).get
      case None => throw new IllegalArgumentException(s"No conversion parameter in cast method `${expr}'. Expected a string as first argument.")
    }
  }

  def getVariableFromCall[Level](e: StaticInvokeExpr)
  : Option[Var[_]] = {
    if (e.getArgCount != 2) {
      throw new IllegalArgumentException(s"Illegal parameters cast method `${e}'. Expected two arguments.")
    }
    Vars.getAll(e.getArg(1)).find(_ => true)
  }
}
