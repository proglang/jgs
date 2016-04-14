package de.unifreiburg.cs.proglang.jgs.constraints

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeOps.tryApply
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintKind._

/**
  * A context for constraints, i.e. constraints for a particular type domain.
  *
  * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
  * @param < Level>
  */
object Constraints {
  def make[Level](kind: ConstraintKind, lhs: CTypes.CType[Level], rhs: CTypes.CType[Level]): Constraint[Level] = {
    return new Constraint[Level](kind, lhs, rhs)
  }

  def le[Level](lhs: CTypes.CType[Level], rhs: CTypes.CType[Level]): Constraint[Level] = {
    return new Constraint[Level](LE, lhs, rhs)
  }

  def comp[Level](lhs: CTypes.CType[Level], rhs: CTypes.CType[Level]): Constraint[Level] = {
    return new Constraint[Level](COMP, lhs, rhs)
  }

  def dimpl[Level](lhs: CTypes.CType[Level], rhs: CTypes.CType[Level]): Constraint[Level] = {
    return new Constraint[Level](DIMPL, lhs, rhs)
  }

  def toKind[Level](kind: ConstraintKind, c: Constraint[Level]): Constraint[Level] = {
    return make(kind, c.getLhs, c.getRhs)
  }

  /**
    * Yields the symmetric constraint.
    */
  def symmetricOf[Level](c: Constraint[Level]): Constraint[Level] = {
    return make(c.kind, c.getRhs, c.getLhs)
  }

  def equalComponents[Level](c1: Constraint[Level], c2: Constraint[Level]): Boolean = {
    return (c1.getLhs == c2.getLhs) && (c1.getRhs == c2.getRhs)
  }

  def isTrivial[Level](types: TypeDomain[Level], c: Constraint[Level]): Boolean = {
    val a: Assignment[Level] = Assignments.empty[Level]
    tryApply(a, c.getLhs).isDefined && tryApply(a, c.getRhs).isDefined && c.isSatisfied(types, Assignments.empty[Level])
  }

  /**
    * Given the constraint <code>c</code>, return literally all the non-reflexive constraints that would be implied by <code>c</code>.
    */
  def implicationsOf[Level](c: Constraint[Level]): Iterator[Constraint[Level]] = {
    c.kind match {
      case LE =>
        Iterator(c) ++ implicationsOf(toKind(ConstraintKind.COMP, c))
      case COMP =>
        Iterator(c, symmetricOf(c))
      case DIMPL =>
        Iterator(c)
      case _ =>
        throw new RuntimeException("Impossible case")
    }
  }
}

class Constraints[Level]
(val types: TypeDomain[Level])
{

  def satisfyingAssignment(levelConstraintSet: ConstraintSet[Level], vars: java.util.Collection[TypeVars.TypeVar]): Option[Assignment[Level]] = {
    return levelConstraintSet.satisfyingAssignment(this.types, vars)
  }

  def implies(left: ConstraintSet[Level], right: ConstraintSet[Level]): Boolean = {
    return left.implies(types, right)
  }

  def subsubmes(left: ConstraintSet[Level], right: ConstraintSet[Level]): Boolean = {
    return left.subsumes(right)
  }

  def isSat(cs: ConstraintSet[Level]): Boolean = {
    return cs.isSat(types)
  }

  def isSatisfyingAssignment(c: Constraint[Level], ass: Assignment[Level]): Boolean = {
    return c.isSatisfied(types, ass)
  }

  def isSatisfyingAssignment(cs: ConstraintSet[Level], ass: Assignment[Level]): Boolean = {
    return cs.isSatisfiedFor(types, ass)
  }
}