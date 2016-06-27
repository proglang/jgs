package de.unifreiburg.cs.proglang.jgs.util

/**
  * General purpose utilities missing from the scala standard library.
  */
object Extra {

  def joinWith[A,B](combine : (B, B) => B, m1 : Map[A,B], m2 : Map[A,B]) : Map[A,B] = {
    val commonKeys = m1.keySet & m2.keySet
    val commonEntries = (for {
      key <- commonKeys
      v = combine(m1(key), m2(key))
    } yield key -> v).toMap
    m1 ++ m2 ++ commonEntries
  }

  def combinePairs[A](combine : (A, A) => A, p1 : (A, A), p2 : (A, A)) : (A, A) =
    (p1, p2) match {
      case ((a11, a12), (a21, a22)) => (combine(a11, a21), combine(a12, a22))
    }

}
