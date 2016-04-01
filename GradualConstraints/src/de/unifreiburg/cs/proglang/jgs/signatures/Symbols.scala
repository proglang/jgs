package de.unifreiburg.cs.proglang.jgs.signatures

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeViews.{Variable, Lit, CTypeView}
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var
import de.unifreiburg.cs.proglang.jgs.signatures.SymbolViews.{Literal, Return, SymbolView, Param}
import org.apache.commons.lang3.tuple.Pair
import soot.SootMethod
import java.util.stream.Collectors
import java.util.stream.Stream
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol.param

import scala.collection.JavaConverters._

/**
  * Operations on and with Symbols (i.e. the "type variables" that appear in method signatures)
  */
object Symbols {
  /**
    * Get the list of Params from a SootMethod.
    */
  def methodParameters[Level](m: SootMethod): java.util.List[Symbol.Param[Level]] = {
    val types: java.util.List[Type[Level]] = m.getParameterTypes.asInstanceOf[java.util.List[Type[Level]]]
    val result: java.util.List[Symbol.Param[Level]] = new java.util.ArrayList[Symbol.Param[Level]]
    for (pos <- Range(0, m.getParameterCount)) {
      result.add(param(pos))
    }
    return result
  }

  /**
    * Get an identity mapping for signatures
    */
  def identityMapping[Level](tvars: TypeVars, sig: java.util.Set[Symbol[Level]]): java.util.Map[Symbol[Level], CTypes.CType[Level]] = {
    def asTypeVar(s : SymbolView[Level]) : Option[CType[Level]] =
      s match {
        case Param(position) => Some(CTypes.variable[Level](tvars.param(Var.fromParam(param(position)))))
        case Return() => Some(CTypes.variable(tvars.ret()))
        case Literal(level) => None
      }
    val assocs : Seq[(Symbol[Level], CType[Level])] =
     for (s <- sig.asScala.toSeq;
          ct <- asTypeVar(s.inspect)
         ) yield s -> ct
    return Map(assocs:_*).asJava
  }

  /**
    * Convert ctypes to symbols given the variable-to-parameter mapping `params`
    */
  def ctypeToSymbol[Level](params : java.util.Map[TypeVar, Symbol.Param[Level]], ct : CTypeView[Level]) : Symbol[Level] =
    ct match {
      case Lit(t) => Symbol.literal(t)
      case Variable(v) => Option(params.get(v))
                           .map(p => Symbol.param[Level](p.position))
                           .getOrElse(throw new NoSuchElementException(String.format("Type variable %s not found in parameter map: %s", v, params)))
    }

  /**
    * See [[ctypeToSymbol(java.util.Map[TypeVar,Param[Level], CTypeView[Level])]]
    */
  def ctypeToSymbol[Level](params : java.util.Map[TypeVar, Symbol.Param[Level]], ct : CType[Level]) : Symbol[Level] =
    ctypeToSymbol(params, ct.inspect())
}