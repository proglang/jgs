package de.unifreiburg.cs.proglang.jgs.constraints

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeViews.CTypeView
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol
import de.unifreiburg.cs.proglang.jgs.typing.{ConflictCause, TagMap}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

/**
  * An immutable set of constraints.
  *
  * @param < Level>
  * @author fennell
  */
object ConstraintSet {

  class RefinementCheckResult[Level](val abstractConstraints: ConstraintSet[Level],
                                     val concreteConstraints: ConstraintSet[Level],
                                     val counterExample: Option[Assignment[Level]]) {

    /**
      * @return A set of constraints that conflicts with the counterexample (if present)
      */
    def getConflicting: java.util.Set[Constraint[Level]] = {
      (for (ce <- counterExample.toSeq;
            c <- concreteConstraints.stream
            if !Constraints.isTrivial(abstractConstraints.types, c.apply(ce)))
        yield c).toSet.asJava
    }

    override def toString: String = {
      val result: StringBuilder = new StringBuilder
      result.append(String.format("Abstract: %s\n", this.abstractConstraints.toString.replace(",", ",\n")))
      result.append(String.format("Concrete: %s\n", this.concreteConstraints.toString.replace(",", ",\n")))
      result.append(String.format("Counterexample: %s\n", this.counterExample))
      result.append(String.format("Conflicting: %s\n", this.getConflicting))
      return result.toString
    }

    def isSuccess: Boolean = {
      return this.counterExample.isEmpty
    }
  }

}

abstract case class ConstraintSet[Level] (
  val types: TypeDomain[Level]
                                    )
{

  /**
    * Check if an assignment satisfies this constraint set.
    *
    * @param a
    * @return
    */
  def isSatisfiedFor(types: TypeDomain[Level], a: Assignment[Level]): Boolean

  /**
    * Return a new set with the given constraint <code>c</code> added.
    */
  def add(c: Constraint[Level]): ConstraintSet[Level]

  def add(other: ConstraintSet[Level]): ConstraintSet[Level]

  /**
    * @return True if the constraints of this set imply those of <code>other</code>
    */
  def implies(types: TypeDomain[Level], other: ConstraintSet[Level]): Boolean

  /**
    * @return True if this set of constraints is satisfiable.
    */
  def isSat(types: TypeDomain[Level]): Boolean

  /**
    * @return A counterexample why <code>this</code> does not subsume <code>other</code>
    */
  def doesNotSubsume(other: ConstraintSet[Level]): Option[Assignment[Level]]

  /**
    * Check if this constraint set refines the constraint set {@code other}.
    */
  def refines(other: ConstraintSet[Level]): ConstraintSet.RefinementCheckResult[Level] = {
    val counterExample: Option[Assignment[Level]] = other.doesNotSubsume(this)
    return new ConstraintSet.RefinementCheckResult[Level](other, this, counterExample)
  }

  /**
    * Find a satisfying assignment for this constraint set. (Optional)
    *
    * @param requiredVariables The minimal set of variables that should be bound by a satisfying assignment. The actual
    *                          assignments may bind more variables.
    */
  def satisfyingAssignment(types: TypeDomain[Level], requiredVariables: java.util.Collection[TypeVars.TypeVar]): Option[Assignment[Level]] = {
    throw new RuntimeException("sat() operation not supported")
  }

  def stream: Iterator[Constraint[Level]]

  /**
    * @return The stream of variables contained in this set.
    */
  def variables: Iterator[TypeVars.TypeVar] = {
    return this.stream.flatMap(c => c.variables)
  }

  /**
    * @return the stream of constriants where assignement <code>a</code> is applied where possible.
    */
  def apply(a: Assignment[Level]): Iterator[Constraint[Level]] = {
    return this.stream.map(c => c.apply(a))
  }

  /**
    * @return True if <code>other</code> is satisfiable for all of the satisfying assignments of <code>this</code>.
    */
  def subsumes(other: ConstraintSet[Level]): Boolean = {
    return this.doesNotSubsume(other).isEmpty
  }

  /**
    * Project a constraint set to the symbols relevant for a method signature, which are the parameters, and the return symbol
    */
  def asSignatureConstraints(tvars: TypeVars, params: Iterator[Var[Symbol.Param[_]]]): ConstraintSet[Level] = {
    val vars: Set[TypeVars.TypeVar] = (Iterator(tvars.ret) ++ params.map(tvars.param)).toSet
    return this.projectTo(vars)
  }

  /**
    * Create a set of constraints that only mentions the type variables in <code>typeVars</code>. The resulting set,
    * interpreted as a ConstraintSet, should impose the same restrictions on the variables in <code>typeVars</code> as
    * the original set.
    */
  def projectTo(typeVars: java.util.Set[TypeVars.TypeVar]): ConstraintSet[Level]

  def findConflictCause(tags: TagMap[Level]): java.util.List[ConflictCause[Level]] = {
    List()
  }

  /**
    * Return the set of ctypes that strictly lower bound the the type variable <code>tv</code>.
    */
  def lowerBounds(tv : TypeVars.TypeVar) : Set[CTypeView[Level]]
}