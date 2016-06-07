package de.unifreiburg.cs.proglang.jgs.signatures.parse

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.Level

/**
  * Utilities for implementing level parsers.
  */
object ParseUtils {

  def literalLevels(literals : String*) : AnnotationParser[UserDefined.Level] = {
    val litMap : Map[String, UserDefined.Level] = (for (l <- literals) yield (l -> UserDefined.Level(l))).toMap
    new AnnotationParser[Level] {
      override def parse(input: String): Option[Level] = litMap.get(input)
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
