package de.unifreiburg.cs.proglang.jgs.signatures.parse

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined

/**
  * Utilities for implementing level parsers.
  */
object ParseUtils {

  def literalLevels(literals : String*) : AnnotationParser[String] = {
    val litMap : Map[String, String] = (for (l <- literals) yield (l -> l)).toMap
    new AnnotationParser[String] {
      override def parse(input: String): Option[String] = litMap.get(input)
    }
  }

  /**
    * Recognize the strings "[bot]" and "[top]" (as bottom and top, resp) by default.
    */
  def addDefaults[Level](dom : SecDomain[Level], p : AnnotationParser[Level]) : AnnotationParser[Level] = {
    new AnnotationParser[Level] {
      override def parse(input: String): Option[Level] =
      input match {
        case "[bot]" => Some(dom.bottom())
        case "[top]" => Some(dom.top())
        case  _ => p.parse(input)
      }
    }
  }

}
