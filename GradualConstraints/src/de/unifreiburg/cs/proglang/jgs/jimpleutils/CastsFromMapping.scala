package de.unifreiburg.cs.proglang.jgs.jimpleutils

import java.util.{Optional}

import Casts.{ValueCast, CxCast}
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser
import soot.SootMethod
import soot.jimple.{StaticInvokeExpr}

import TypeDomain._


import scala.collection.JavaConverters._

import CastsFromMapping._
import de.unifreiburg.cs.proglang.jgs.util.Interop._

import scala.util.{Success, Failure, Try}


/**
  * Create a cast module from mappings of fully qualified method names to casts
  */
case class CastsFromMapping[Level](valueCasts: Map[String, Conversion[Level]],
                                   cxCastBegins: Map[String, Conversion[Level]],
                                   cxCastEnd: String)
  extends Casts[Level] {

  override def detectValueCastFromCall(e: StaticInvokeExpr): Option[ValueCast[Level]] = {
    val key = e.getMethod.toString // fqName(e.getMethod) TODO: remove fqname based key
    valueCasts.get(key)
      .map(c => new ValueCast[Level](c.source, c.dest, asScalaOption(getCallArgument(e)))
    )
  }

  override def detectContextCastEndFromCall(e: StaticInvokeExpr): Boolean = {
    val key = e.getMethod.toString // fqName(e.getMethod) TODO: remove fqname based key
    key == cxCastEnd
  }

  override def detectContextCastStartFromCall(e: StaticInvokeExpr): Option[CxCast[Level]] = {
    val key = e.getMethod.toString // fqName(e.getMethod) TODO: remove fqname based key
    cxCastBegins.get(key)
      .map(c => new CxCast[Level](c.source, c.dest))
  }
}

object CastsFromMapping {

  sealed case class Conversion[Level](val source: Type[Level],
                                      val dest: Type[Level])

  def fqName(m: SootMethod): String = {
    s"${m.getDeclaringClass.getName}.${m.getName}"
  }


  private def getCallArgument(e: StaticInvokeExpr): Optional[Var[_]] = {
    if (e.getArgCount != 1) {
      throw new IllegalArgumentException(s"Illegal parameter count on cast method `${e}'. Expected single argument method.")
    }
    Vars.getAll(e.getArg(0)).findFirst()
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
            t1 <- typeParser.parse(st1);
            t2 <- typeParser.parse(st2)
          ) yield Conversion(t1, t2)
          result match {
            case Some(conv) => tryM.map[Map[String, Conversion[Level]]](m => m + Tuple2(k, conv))
            case _ => Failure(err)
          }
        case _ => Failure(err)
      }
    })
  }
}

