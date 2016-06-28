package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.Level
import de.unifreiburg.cs.proglang.jgs.signatures.parse.{ParseUtils, AnnotationParser}

import scala.collection.JavaConversions._


/**
  * User defined, finite security levels that can be parsed from a json file.
  *
  * `levels`, `lt`, `lubMap`, `glbMap`, `topLevel`, `bottomLevel` should form a lattice
  */
class UserDefined private (val levels : Set[Level],
                           lt : Set[Tuple2[Level, Level]],
                           lubMap : Map[Tuple2[Level, Level], Level],
                           glbMap : Map[Tuple2[Level, Level], Level],
                           topLevel : Level,
                           bottomLevel : Level
                          )
  extends SecDomain[Level]{



  override def bottom(): Level = bottomLevel

  override def enumerate(): java.util.Iterator[Level] = levels.iterator

  override def top(): Level = topLevel

  override def levelParser(): AnnotationParser[Level] = ParseUtils.addDefaults(this, new AnnotationParser[Level] {
    override def parse(input: String): Option[Level] = levels.find(l => l.name == input)
  })

  override def le(l1: Level, l2: Level): Boolean = l1 == l2 || lt.contains(l1 -> l2)

  override def lub(l1: Level, l2: Level): Level = lubMap(l1, l2)

  override def glb(l1: Level, l2: Level): Level = glbMap(l1, l2)

}

object UserDefined {

  /**
    * Type for UserDefined security levels which are described by strings
    */
  sealed case class Level(name : String) {
    override def toString = name
  }

  /**
    * Construct a UserDefined lattice of security levels.
 *
    * @param levels The set of levels
    * @param ltEgdes The essential less-than pairs that induce the ordering on `levels` by closing transitively
    *
    *  If `ltEdges` does not induce a lattice for `levels` an IllegalArgumentException is thrown.
    */
  def apply(levels : Set[Level], ltEgdes : Set[(Level, Level)]) : UserDefined = {
    val lt = closeTransitively(ltEgdes)
    checkIrreflexivity(lt)
    checkAsymmetry(lt)
    val gt = for (p <- lt) yield (p._2, p._1)
    val lubMap = bounds(levels, (l1, l2) => lt.contains((l1, l2)))
    val glbMap = bounds(levels, (l1, l2) => gt.contains((l1, l2)))
    val top = levels.find(l => levels.forall(otherL => otherL == l || lt(otherL, l)))
    if (top.isEmpty) throw new IllegalArgumentException("Unable to find top element for lattice")
    val bottom = levels.find(l => levels.forall(otherL => otherL == l || lt(l, otherL)))
    if (bottom.isEmpty) throw new IllegalArgumentException("Unable to find bottom element for lattice")
    new UserDefined(levels, lt, lubMap, glbMap, top.get, bottom.get)
  }


  private def bounds(levels : Set[Level], lt : (Level, Level) => Boolean) : Map[Tuple2[Level, Level], Level] = {
    val le = (l1 : Level, l2 : Level) => lt(l1, l2) || l1 == l2
    (for {l1 <- levels
          l2 <- levels }
      yield {
        val ubs = upperBounds(levels, le, l1, l2)
        // TODO: improve error messages... at least state that this is a "lattice-error" or something
        if (ubs.isEmpty) throw new IllegalArgumentException(s"No upper bound found for levels ${l1} and ${l2}")
        val lub = ubs.min(Ordering.fromLessThan(lt))
        (l1, l2) -> lub
      }).toMap
  }

  def upperBounds(levels : Set[Level], le : (Level, Level) => Boolean, l1 : Level, l2 : Level) : Set[Level] = {
    for (l <- levels; if le(l1, l) && le(l2, l)) yield l
  }


  def closeTransitively(edges : Set[Tuple2[Level, Level]]) : Set[Tuple2[Level, Level]] = {
    val next : Set[Tuple2[Level, Level]] =
      edges ++
      (for { (lhs, mid1) <- edges
            (mid2, rhs) <- edges
            if mid1 == mid2 }
        yield (lhs, rhs))
    if (next == edges) next else closeTransitively(next)
  }

  def checkIrreflexivity(rel : Set[Tuple2[Level, Level]]) : Unit = {
    for { (l1, l2) <- rel; if l1 == l2} throw new IllegalArgumentException(s"Found reflexive pair in a strict ordering: ${l1}, ${l2}")
  }

  def checkAsymmetry(rel : Set[Tuple2[Level, Level]]) : Unit = {
    for { p1 <- rel
          p2 <- rel
          if p1._1 == p2._2 && p1._2 == p2._1 && p1._1 != p1._2} {
      throw new IllegalArgumentException(s"Found symmetric pair in an ordering: ${p1}, ${p2}")
    }

  }
}
