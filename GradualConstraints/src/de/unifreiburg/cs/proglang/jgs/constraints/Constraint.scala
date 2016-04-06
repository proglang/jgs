package de.unifreiburg.cs.proglang.jgs.constraints

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain


final case class Constraint[Level]
(val kind: ConstraintKind,
 private val lhs: CTypes.CType[Level],
 private val rhs: CTypes.CType[Level]
) {


  def isSatisfied(types: TypeDomain[Level], a: Assignment[Level]): Boolean = {
    this.kind match {
      case ConstraintKind.LE =>
        return types.le(CTypeOps.apply(a, this.lhs), CTypeOps.apply(a, rhs))
      case ConstraintKind.COMP =>
        var tlhs: TypeDomain.Type[Level] = null
        var trhs: TypeDomain.Type[Level] = null
        tlhs = CTypeOps.apply[Level](a, this.lhs)
        trhs = CTypeOps.apply[Level](a, this.rhs)
        return types.le(tlhs, trhs) || types.le(trhs, tlhs)
      case ConstraintKind.DIMPL =>
        return (!(CTypeOps.apply(a, this.lhs) == types.dyn) || types.le(CTypeOps.apply(a, this.rhs), types.dyn))
      case _ =>
        throw new RuntimeException("Impossible case!")
    }
  }

  /**
    *
    * @return true if this single constraint, in isolation, is satisfiable
    */
  def isSatisfiable(types: TypeDomain[Level]): Boolean = {
    if (this.variables.size != 0) {
      return true
    }
    else {
      return this.isSatisfied(types, Assignments.empty[Level])
    }
  }

  def apply(a: Assignment[Level]): Constraint[Level] = {
    val newLhs: CTypes.CType[Level] = CTypeOps.applyWhenPossible(a, this.lhs)
    val newRhs: CTypes.CType[Level] = CTypeOps.applyWhenPossible(a, this.rhs)
    return new Constraint(this.kind, newLhs, newRhs)
  }

  def variables: Iterator[TypeVars.TypeVar] = {
    return CTypeOps.variables(lhs) ++ CTypeOps.variables(rhs)
  }

  def getLhs: CTypes.CType[Level] = {
    return this.lhs
  }

  def getRhs: CTypes.CType[Level] = {
    return this.rhs
  }

  override def toString: String = {
    return String.format("%s %s %s", this.lhs, this.kind.toString, this.rhs)
  }
}