package de.unifreiburg.cs.proglang.jgs.jimpleutils

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type
import de.unifreiburg.cs.proglang.jgs.jimpleutils.CastUtils.Conversion
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts.{ValueCast, CxCast}
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser
import soot.SootMethod
import soot.jimple.{StringConstant, StaticInvokeExpr}

import scala.util.{Success, Failure, Try}

/**
  * Cast detectors that read their conversion from String constants
  */
class CastsFromConstants[Level](typeParser: AnnotationParser[Type[Level]],
                                valueCast: String,
                                cxCastBegin: String, cxCastEnd: String)
extends Casts[Level] {
  override def detectValueCastFromCall(e: StaticInvokeExpr): Try[Option[ValueCast[Level]]] = {
    def cont(e : StaticInvokeExpr) : Try[Option[ValueCast[Level]]] =
          for { conv <- CastsFromConstants.getConversionFromCall(typeParser, e)
            v = CastsFromConstants.getVariableFromCall(e)
          } yield Some(new ValueCast[Level](conv.source, conv.dest, v))
    CastsFromConstants.detectForMatchingMethod(valueCast, e, cont)
  }

  override def detectContextCastEndFromCall(e: StaticInvokeExpr): Boolean = {
    e.getMethod.toString == cxCastEnd
  }

  override def detectContextCastStartFromCall(e: StaticInvokeExpr): Try[Option[CxCast[Level]]] = {
    CastsFromConstants.detectForMatchingMethod(cxCastBegin, e, e => {
      for (conv <- CastsFromConstants.getConversionFromCall(typeParser, e))
        yield Some(new CxCast[Level](conv.source, conv.dest))
    })
  }
}

object CastsFromConstants {

  def getConversionFromCall[Level](typeParser : AnnotationParser[Type[Level]], expr: StaticInvokeExpr) : Try[Conversion[Level]] = {
    val maybeConv = for {
      e <- Some(expr)
      if e.getArgCount >= 1
      sConst <- PartialFunction.condOpt(e.getArg(0)) { case s: StringConstant => s }
    } yield sConst.value

    maybeConv match {
      case Some(convString) => CastUtils.parseConversion(typeParser, convString)
      case None => Failure(new IllegalArgumentException(s"No conversion parameter in cast method `${expr}'. " +
        s"Expected a string as first argument."))
    }
  }

  def detectForMatchingMethod[A](methodName : String, e : StaticInvokeExpr, cont : StaticInvokeExpr => Try[Option[A]]) : Try[Option[A]] = {
    if (e.getMethod.toString == methodName) {
      cont(e)
    } else {
      Success(None)
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
