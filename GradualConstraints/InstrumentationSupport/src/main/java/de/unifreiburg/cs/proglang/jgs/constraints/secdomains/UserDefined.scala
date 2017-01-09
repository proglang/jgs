package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.Edge



/**
  * User defined, finite security levels whose lattice structure is determined by tuples, sets, and maps of Strings.
  *
  * `levels`, `lt`, `lubMap`, `glbMap`, `topLevel`, `bottomLevel` should form a lattice
  */
class UserDefined (val levels : java.util.Set[String],
                           lt : java.util.Set[Edge],
                           lubMap : java.util.Map[Edge, String],
                           glbMap : java.util.Map[Edge, String],
                           topLevel : String,
                           bottomLevel : String
                          )
  extends SecDomain[String]{


  override def bottom(): String = bottomLevel

  override def enumerate(): java.util.Iterator[String] = levels.iterator

  override def top(): String = topLevel

  override def readLevel(s : String) : String = {
    if (!levels.contains(s)) {
      throw new UnknownSecurityLevelException(s);
    }
    return s;
  }


  override def le(l1: String, l2: String): Boolean = l1 == l2 || lt.contains(Edge(l1, l2))

  override def lub(l1: String, l2: String): String = lubMap.get(Edge(l1, l2))

  override def glb(l1: String, l2: String): String = glbMap.get(Edge(l1, l2))

  override def toString : String = String.format("lt: %s, top: %s, bottom: %s", lt.toString, topLevel, bottomLevel)


}

object UserDefined {
  final case class Edge(left : String, right : String)
}
