package de.unifreiburg.cs.proglang.jgs.instrumentation

import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView
import de.unifreiburg.cs.proglang.jgs.signatures.parse.AnnotationParser

import scala.util.{Failure, Success, Try}

/**
  * Utilities for implementing and using "cast detectors" (i.e., instances of `Cast`)
  */
object CastUtils {

  sealed case class TypeViewConversion[Level](val source: TypeView[Level],
                                              val dest: TypeView[Level])

  def parseConversion[Level](typeParser : AnnotationParser[TypeView[Level]], s : String) : Try[TypeViewConversion[Level]] = {
    val err = new IllegalArgumentException(s"Unable to parse `${s}' as a type conversion. Expected: [type] ~> [type].")
    s.trim.split("\\s") match {
      case Array(st1, "~>", st2) =>
        val result = for (
          t1 <- typeParser.parse(st1);
          t2 <- typeParser.parse(st2)
        ) yield TypeViewConversion(t1, t2)
        result match {
          case Some(conv) => Success(conv)
          case _ => Failure(err)
        }
      case _ => Failure(err)
    }
  }



}
