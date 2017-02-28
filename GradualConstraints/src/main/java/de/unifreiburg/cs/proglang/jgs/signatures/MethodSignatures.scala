package de.unifreiburg.cs.proglang.jgs.signatures

import de.unifreiburg.cs.proglang.jgs.constraints.{TypeDomain, TypeVars, _}
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol._

import scala.collection.JavaConversions._

/**
  * Method signtures. Internal representation of method signatures of the form
  * <p>
  * M where <signature-constraints> and <effect>
  * <p>
  * Signature constraints are similar to regular constraints but instead of
  * relating type variables, they relate special symbols, which are:
  * <p>
  * - Parameter names - "@return" - Security Levels
  * <p>
  * Effects are just sets of security Levels
  *
  * @author fennell
  */
object MethodSignatures {
  /**
    * Make a new JGS security signature for a method from a method's parameter
    * count, some constraints and effects. The constraints should only mention
    * parameters in the range of 0..paramCount.
    */
  def makeSignature[Level](paramCount: Int, constraints: java.util.Collection[SigConstraint[Level]], effects: Effects[Level]): Signature[Level] = {
    val paramInRange = (p :  Param[_])=> p.position < paramCount && 0 <= p.position
    if (!constraints.iterator().flatMap(c => c.symbols().iterator()).forall(s => !(s.isInstanceOf[Param[_]])
      || paramInRange((s.asInstanceOf[Param[Level]])))) {
      throw new IllegalArgumentException(String.format(s"Illegal parameter for parameter count ${paramCount} in constraint ${constraints}"))
    }
    val paramConstraints: Iterator[SigConstraint[Level]] = (0 until paramCount).iterator.map(i => trivial(Param[Level](i)))
    val allConstraints: Iterator[SigConstraint[Level]] = (constraints.iterator() ++ Iterator(trivial(Return[Level])) ++ paramConstraints)
    return new Signature[Level](signatureConstraints(allConstraints), effects)
  }

  private def signatureConstraints[Level](sigSet: Iterator[SigConstraint[Level]]): SigConstraintSet[Level] = {
    return new SigConstraintSet[Level](sigSet.toSet)
  }

  def toSignatureConstraintSet[Level](constraints: ConstraintSet[Level], params: java.util.Map[TypeVars.TypeVar, Param[Level]], retVar: TypeVars.TypeVar): SigConstraintSet[Level] = {
    val relevantVars: Set[TypeVars.TypeVar] = Set(retVar) ++ params.keySet
    signatureConstraints(constraints.projectTo(params.keySet).stream.map(c => {
      val lhs = Symbols.ctypeToSymbol(params, c.getLhs);
      val rhs = Symbols.ctypeToSymbol(params, c.getRhs);
      c.kind match {
        case ConstraintKind.LE => le(lhs, rhs);
        case ConstraintKind.COMP => comp(lhs, rhs);
        case ConstraintKind.DIMPL => dimpl(lhs, rhs);
      }
    }))
  }

  /**
    * @return A trivial constraint mentioning { @code s}.
    */
  def trivial[Level](s: Symbol[Level]): SigConstraint[Level] = {
    return le(s, s)
  }

  def le[Level](lhs: Symbol[Level], rhs: Symbol[Level]): SigConstraint[Level] = {
    return new SigConstraint[Level](lhs, rhs, ConstraintKind.LE)
  }

  def comp[Level](lhs: Symbol[Level], rhs: Symbol[Level]): SigConstraint[Level] = {
    return new SigConstraint[Level](lhs, rhs, ConstraintKind.COMP)
  }

  def dimpl[Level](lhs: Symbol[Level], rhs: Symbol[Level]): SigConstraint[Level] = {
    return new SigConstraint[Level](lhs, rhs, ConstraintKind.DIMPL)
  }

  def makeSigConstraint[Level](lhs: Symbol[Level], rhs: Symbol[Level], kind: ConstraintKind): SigConstraint[Level] = {
    return new SigConstraint[Level](lhs, rhs, kind)
  }

  case class SigConstraintSet[Level](private val sigSet: Set[SigConstraint[Level]])
  {

    def toTypingConstraints(mapping: java.util.Map[Symbol[Level], CTypes.CType[Level]]): Iterator[Constraint[Level]] = {
      return this.stream.map(c => c.toTypingConstraint(mapping))
    }

    override def toString: String = {
      return sigSet.toString
    }

    def symbols: Iterator[Symbol[Level]] = {
      return this.stream.flatMap(c => c.symbols().iterator())
    }

    def stream: Iterator[SigConstraint[Level]] = {
      return sigSet.iterator
    }

    def addAll(sigs: Iterator[SigConstraint[Level]]): SigConstraintSet[Level] = {
      return signatureConstraints(this.stream ++ sigs)
    }

    /**
      * Check if this signature refines another one. This check is at the
      * heart of validating a sound class hierarchy.
      */
    def refines(csets: ConstraintSetFactory[Level], types: TypeDomain[Level], other: SigConstraintSet[Level]): ConstraintSet.RefinementCheckResult[Level] = {
      val tvars: TypeVars = new TypeVars
      val symbols: Set[Symbol[Level]] = (this.symbols ++ other.symbols).toSet
      val cs1: ConstraintSet[Level] = csets.fromCollection(this.toTypingConstraints(Symbols.identityMapping(tvars, symbols)).toList)
      val cs2: ConstraintSet[Level] = csets.fromCollection(other.toTypingConstraints(Symbols.identityMapping(tvars, symbols)).toList)
      return cs1.refines(cs2)
    }
  }

}

