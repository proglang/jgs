package de.unifreiburg.cs.proglang.jgs.constraints

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeViews.{CTypeView, Lit, Variable}
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.{Dyn, Pub, TypeView}
import de.unifreiburg.cs.proglang.jgs.instrumentation.{Type, Var}
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
      result.append(String.format("general constraints: \n %s\n", this.abstractConstraints.toString.replace(",", ",\n")))
      result.append(String.format("refined constraints: \n %s\n", this.concreteConstraints.toString.replace(",", ",\n")))
      result.append(String.format("counterexample: %s\n", this.counterExample))
      result.append(String.format("conflicting: %s\n", this.getConflicting))
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
    * @return the stream of constraints where assignment <code>a</code> is applied where possible.
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
  def asSignatureConstraints(tvars: TypeVars, params: Iterator[Var[Integer]]): ConstraintSet[Level] = {
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

  def upperBounds(tv : TypeVars.TypeVar) : Set[CTypeView[Level]]

  /**
    * Return the type that is the greatest lower bound of tv, if it exists.
    */
  def greatestLowerBound(tv : TypeVars.TypeVar) : Option[TypeView[Level]] = {
    val bounds = lowerBounds(tv).filter(p => p match {
      case Lit(t) => true
      case Variable(v) => false
    })
    val firstLit = bounds.find(ct => ct match {
      case Lit(t) => true
      case Variable(v) => false
    }).map(_.asInstanceOf[Lit[Level]])

    firstLit.flatMap(lit =>
      bounds.foldLeft(Option(lit.t))((res, ct) => ct match {
        case Lit(t) => res.flatMap(t2 => types.lub(t, t2))
        case Variable(v) => res
      }
    ))
  }

  private sealed trait InstrumentationType extends Type[Level] {
    override def isStatic: Boolean = this == Static

    override def isDynamic: Boolean = this == Dynamic

    override def isPublic: Boolean = this == Public

    override def getLevel: Level =
      // throw new UnsupportedOperationException("Try to get the security level from an instrumentation type")
    // TODO: this is a bad hack. Fix it by not exposing a concrete security level through the Type interface
      this match {
        case Static => types.getSecDomain.bottom()
        case Dynamic => throw new UnsupportedOperationException("dynamic type has no level")
        case Public => throw new UnsupportedOperationException("public type has no level")
      }
  }

  private case object Static extends InstrumentationType
  private case object Dynamic extends InstrumentationType
  private case object Public extends InstrumentationType

  private def glb(t1 : InstrumentationType, t2 : InstrumentationType): InstrumentationType = {
      t1 match {
        case Static => t2 match {
          case Static => Static
          case _ => Public
        }
        case Dynamic => t2 match {
          case Dynamic => Dynamic
          case _ => Public
        }
        case Public => Public
      }
  }

  private def lub(t1 : InstrumentationType, t2: InstrumentationType) : Option[InstrumentationType] = {
    t1 match {
      case Static => t2 match {
        case Dynamic => None
        case _ => Some(Static)
      }
      case Dynamic => t2 match {
        case Static => None
        case _ => Some(Dynamic)
      }
      case Public => Some(t2)
    }
  }

  /**
    * Return the instrumentation type of tv (i.e. whether tv is static, dynamic, or public), if it can be determined unambigously.
    *
    * Return None if tv is polymorphic.
    *
    * This method only works for satisfiable constraints
    *
    * @throws IllegalArgumentException when encountering conflicts in the constraints
    */
  def instrumentationType(tv : TypeVars.TypeVar) : Option[Type[Level]] = {
    def fromCType(ct : CTypeView[Level]) : Option[InstrumentationType] =
      ct match {
        case Lit(t) => Some(t match {
          case TypeViews.Lit(level) => Static
          case Dyn() => Dynamic
          case Pub() => Public
        })
        case Variable(v) => None
      }
    def iTypes (bounds : Seq[CTypeView[Level]]) : Seq[InstrumentationType] =
      for {
        ct <- bounds
        itype <- fromCType(ct)
      } yield itype

    def fold1[A](xs : Seq[A], op : (A, A) => A ) : Option[A] = {
      xs.headOption.map(hd => xs.fold(hd)(op))
    }


    val upLimit : Option[InstrumentationType] = fold1(iTypes(upperBounds(tv).to), glb)
    val lowLimit = fold1(iTypes(lowerBounds(tv).to),
      (t1 : InstrumentationType, t2 : InstrumentationType) =>
        lub(t1, t2).getOrElse(throw new IllegalArgumentException(s"Unsatisfiable constraints: ${this}")))
    upLimit.orElse(lowLimit)
  }
}