package de.unifreiburg.cs.proglang.jgs.constraints

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView
import de.unifreiburg.cs.proglang.jgs.util.NotImplemented

import scala.collection.JavaConversions._

/**
  * An assignment from type variables to types.
  *
  * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
  * @param < Level>
  */
case class Assignment[Level]
(private val ass: Map[TypeVars.TypeVar, TypeView[Level]])
{


  /**
    * @return An immutable reference to the map that represents the assignment.
    */
  def getForJava: java.util.Map[TypeVars.TypeVar, TypeView[Level]] =  ass

  def get : Map[TypeVar, TypeView[Level]] = ass

  /**
    * Apply an assignment to a constraint element to get a proper type
    *
    * @param t The constraint element
    * @return The type after applying the assignment.
    */
  def applyTo(t: CTypes.CType[Level]): TypeView[Level] = {
    throw new NotImplemented("Assignment.applyTo")
  }

  override def toString: String = {
    return ass.toString
  }

  def mappedVariables: java.util.Set[TypeVars.TypeVar] = {
    return ass.keySet
  }
}