package de.unifreiburg.cs.proglang.jgs.signatures

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeViews.{CTypeView, Lit, Variable}
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType
import de.unifreiburg.cs.proglang.jgs.constraints.{CTypes, TypeVars}
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Vars
import de.unifreiburg.cs.proglang.jgs.signatures
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol._
import soot.SootMethod

import scala.collection.JavaConverters._

/**
  * Operations on and with Symbols (i.e. the "type variables" that appear in method signatures)
  */
object Symbols {
  /**
    * Get the list of Params from a SootMethod.
    */
  def methodParameters[Level](m: SootMethod): java.util.List[Param[Level]] = {
    val types: java.util.List[TypeView[Level]] = m.getParameterTypes.asInstanceOf[java.util.List[TypeView[Level]]]
    val result: java.util.List[Param[Level]] = new java.util.ArrayList[Param[Level]]
    for (pos <- Range(0, m.getParameterCount)) {
      result.add(signatures.Param(pos))
    }
    return result
  }

  /**
    * Get an identity mapping for signatures
    */
  def identityMapping[Level](tvars: TypeVars, sig: java.util.Set[Symbol[Level]]): java.util.Map[Symbol[Level], CTypes.CType[Level]] = {
    def asTypeVar(s : Symbol[Level]) : Option[CType[Level]] =
      s match {
        case Param(position) => Some(CTypes.variable[Level](tvars.param(Vars.fromParam(position))))
        case Return() => Some(CTypes.variable(tvars.ret()))
        case Literal(level) => None
      }
    val assocs : Seq[(Symbol[Level], CType[Level])] =
     for (s <- sig.asScala.toSeq;
          ct <- asTypeVar(s)
         ) yield s -> ct
    return Map(assocs:_*).asJava
  }

  def symbolToCType[Level](tvarMapping : java.util.Map[Symbol[Level], CType[Level]], s : Symbol[Level]) =
    s match {
      case Param(position) =>
        tvarMapping.asScala.getOrElse(s, throw new NoSuchElementException(s"No mapping for ${s} in ${tvarMapping}"))
      case Return() =>
        tvarMapping.asScala.getOrElse(s, throw new NoSuchElementException(s"No mapping for ${s} in ${tvarMapping}"))
      case Literal(level) => CTypes.literal(level)
    }

  /**
    * Convert ctypes to symbols given the variable-to-parameter mapping `params`
    */
  def ctypeToSymbol[Level](params : java.util.Map[TypeVar, Param[Level]], ct : CTypeView[Level]) : Symbol[Level] =
    ct match {
      case Lit(t) => Literal(t)
      case Variable(v) => Option(params.get(v))
                           .map(p => Param[Level](p.position))
                           .getOrElse(throw new NoSuchElementException(String.format("Type variable %s not found in parameter map: %s", v, params)))
    }

  /**
    * See [[ctypeToSymbol(java.util.Map[TypeVar,Param[Level], CTypeView[Level])]]
    */
  def ctypeToSymbol[Level](params : java.util.Map[TypeVar, Param[Level]], ct : CType[Level]) : Symbol[Level] =
    ctypeToSymbol(params, ct.inspect())
}