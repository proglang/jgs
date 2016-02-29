package de.unifreiburg.cs.proglang.jgs.jimpleutils

import java.util.logging.Logger
import java.util.{Properties, Optional}

import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts.{ValueCast, CxCast}
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser
import soot.SootMethod
import soot.jimple.{InvokeExpr, StaticInvokeExpr}

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain._


import scala.collection.JavaConverters._

import CastsFromMapping._

import scala.util.{Success, Failure, Try}

/**
  * Create a cast module from mappings of fully qualified method names to casts
  */
case class CastsFromMapping[Level](valueCasts: Map[String, Conversion[Level]],
                                   cxCastBegins: Map[String, Conversion[Level]],
                                   cxCastEnd: String)
  extends Casts[Level] {

  override def detectValueCastFromCall(e: StaticInvokeExpr): Optional[ValueCast[Level]] = {
    asJavaOptional(valueCasts.get(fqName(e.getMethod))
      .map(c => new ValueCast[Level](c.source, c.dest, getCallArgument(e)))
    )
  }

  override def detectContextCastEndFromCall(e: StaticInvokeExpr): Boolean =
    CastsFromMapping.fqName(e.getMethod) == cxCastEnd

  override def detectContextCastStartFromCall(e: StaticInvokeExpr): Optional[CxCast[Level]] =
    asJavaOptional(cxCastBegins.get(fqName(e.getMethod))
      .map(c => new CxCast[Level](c.source, c.dest)))
}

object CastsFromMapping {

  sealed case class Conversion[Level](val source: Type[Level],
                                      val dest: Type[Level])

  def fqName(m: SootMethod): String = {
    s"${m.getDeclaringClass.getName}.${m.getName}"
  }

  private def asJavaOptional[T](o: Option[T]): Optional[T] =
    o.map(Optional.of(_)).getOrElse(Optional.empty())

  private def asScalaOption[T](o: Optional[T]): Option[T] =
    o.map[Option[T]](new java.util.function.Function[T, Option[T]] {
      override def apply(t: T): Option[T] = Some(t)
    }).orElse(None)

  private def getCallArgument(e: StaticInvokeExpr): Optional[Var[_]] = {
    if (e.getArgCount != 1) {
      throw new IllegalArgumentException(s"Illegal parameter count on cast method `${e}'. Expected single argument method.")
    }
    Var.getAll(e.getArg(0)).findFirst()
  }

  def parseConversionMap[Level](typeParser: AnnotationParser[Type[Level]],
                                psJ: java.util.Map[String, String]): Try[Map[String, Conversion[Level]]] = {
    val ps: Map[String, String] = psJ.asScala.toMap
    parseConversionMap(typeParser, ps)
  }

  def parseConversionMap[Level](typeParser: AnnotationParser[Type[Level]],
                                ps: Map[String, String]): Try[Map[String, Conversion[Level]]] = {
    ps.foldLeft[Try[Map[String, Conversion[Level]]]](Success(Map()))((tryM, kv) => {
      val (k, s) = kv
      val err = new IllegalArgumentException(s"Unable to parse `${s}' as a type conversion. Expected: [type] ~> [type]. Method ${k}.")
      s.trim.split("\\s") match {
        case Array(st1, "~>", st2) =>
          val result = for (
            t1 <- asScalaOption(typeParser.parse(st1));
            t2 <- asScalaOption(typeParser.parse(st2))
          ) yield Conversion(t1, t2)
          result match {
            case Some(conv) => tryM.map(m => m + Tuple2(k, conv))
            case _ => Failure(err)
          }
        case _ => Failure(err)
      }
    })
  }
}

