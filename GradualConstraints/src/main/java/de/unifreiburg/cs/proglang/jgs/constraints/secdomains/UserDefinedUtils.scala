package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import UserDefined._
import org.json4s._

/**
  * Utilities for dealing with user defined security domains.
  */
object UserDefinedUtils {

  sealed case class Spec(levels : Set[Level], edges : Set[(Level, Level)])

  def fromJSon(json : JValue) : Spec = {
    val levels : Seq[UserDefined.Level] = for {
      JObject(entries) <- json
      JField("levels", ls) <- entries
      JString(s) <- ls
    } yield UserDefined.Level(s)
    val edges : Seq[(UserDefined.Level, UserDefined.Level)] = for {
      JObject(entries) <- json
      JField("lt-edges", ls) <- entries
      JArray(List(JString(edge1), JString(edge2))) <- ls
    } yield UserDefined.Level(edge1) -> UserDefined.Level(edge2)

    Spec(levels.toSet, edges.toSet)
  }


}
