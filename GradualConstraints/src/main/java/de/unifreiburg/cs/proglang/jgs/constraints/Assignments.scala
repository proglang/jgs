package de.unifreiburg.cs.proglang.jgs.constraints

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView

import scala.collection.JavaConversions._

/**
  * Class for static utility methods for working with assignments.
  *
  * @author fennell
  */
object Assignments {
  def empty[Level]: Assignment[Level] = {
    return new Assignment[Level](Map())
  }

  def enumerateAll[Level](types: TypeDomain[Level], variables: java.util.Set[TypeVars.TypeVar]): Iterator[Assignment[Level]] = {
    return enumerateAll(types, List(variables.toSeq: _*))
  }

  private def enumerateAll[Level](types: TypeDomain[Level], variables: List[TypeVars.TypeVar]): Iterator[Assignment[Level]] = {
    if (variables.isEmpty) {
      Iterator(Assignments.empty)
    } else {
      val v :: variableRest = variables
      def rest = enumerateAll(types, variableRest)
      for {
        t <- types.enumerate()
        ass <- rest
      } yield new Assignment[Level](ass.get + (v -> t))
    }
  }

  def builder[Level](v: TypeVars.TypeVar, t: TypeView[Level]): Builder[Level] = {
    return new Builder[Level]().add(v, t)
  }

  def builder[Level](): Builder[Level] = {
    return new Builder[Level]()
  }

  class Builder[Level] {
    private final val ass: java.util.Map[TypeVars.TypeVar, TypeView[Level]] = new java.util.HashMap[TypeVars.TypeVar, TypeView[Level]]

    def build: Assignment[Level] = {
      return new Assignment[Level](this.ass.toMap)
    }

    def add(v: TypeVars.TypeVar, t: TypeView[Level]): Builder[Level] = {
      this.ass.put(v, t)
      return this
    }

    def add(m : java.util.Map[TypeVar, TypeView[Level]]) : Builder[Level] = {
      this.ass.putAll(m)
      return this
    }
  }

}