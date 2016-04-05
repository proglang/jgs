package de.unifreiburg.cs.proglang.jgs.constraints

import java.util.Collections

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeViews.Lit
import de.unifreiburg.cs.proglang.jgs.typing.CompatibilityConflict
import de.unifreiburg.cs.proglang.jgs.typing.ConflictCause
import de.unifreiburg.cs.proglang.jgs.typing.FlowConflict
import de.unifreiburg.cs.proglang.jgs.typing.TagMap
import de.unifreiburg.cs.proglang.jgs.util.Interop
import scala.Option
import scala.collection._
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars._
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes._
import de.unifreiburg.cs.proglang.jgs.util.Interop.asJavaStream
import de.unifreiburg.cs.proglang.jgs.util.Interop.asScalaOption


import JavaConversions._
import JavaConverters._


/**
  * A naive implementation of a constraint set. It is based on
  * java.util.Set<Constraint<Level>> and checks satisfiability by enumerating all
  * assignments.
  *
  * @author fennell
  */
object NaiveConstraints {

  private[constraints] def close[Level](cs: java.util.Set[Constraint[Level]]): java.util.Set[Constraint[Level]] = {
    val isLeConstraint = (c: Constraint[Level]) => c.kind.equals(Constraint.Kind.LE)
    val result: mutable.HashSet[Constraint[Level]] = mutable.HashSet(cs.toSeq: _*)
    val old: mutable.HashSet[Constraint[Level]] = mutable.HashSet()
    do {
      old.addAll(result)
      val oldLes: Set[Constraint[Level]] = old.filter(isLeConstraint).toSet
      oldLes.foreach(x_to_y => {
        // first the transitive le constraints
        val transCands: Iterator[CType[Level]] =
          oldLes.iterator.filter(c => c.getLhs().equals(x_to_y.getRhs())).map(_.getRhs)
        transCands.foreach(rhs => {
          val lhs: CType[Level] = x_to_y.getLhs();
          if (!lhs.equals(rhs)) {
            result.add(Constraints.le(lhs, rhs));
          }
        });
        // then the compatibility constraints
        val compCands: Iterator[CType[Level]] =
          oldLes.iterator.filter(c => c.getRhs().equals(x_to_y.getRhs())).map(_.getLhs);
        compCands.foreach(lhs1 => {
          val lhs2 = x_to_y.getLhs();
          if (!lhs1.equals(lhs2)) {
            val cand = Constraints.comp(lhs1, lhs2);
            // do not add symmetric compatibilities.
            // TODO: actually compatibility is an equivalence class. It should be represented as such, instead of trying to naively keep symmetric pairs out
            if (!result.contains(Constraints.symmetricOf(cand)))
              result.add(cand);
          }
        });
      })
    } while (!(result == old))
    return result
  }

  def minimize[Level](cs: ConstraintSet[Level]): ConstraintSet[Level] = {
    return new NaiveConstraints(cs.types, minimize(cs.stream.toSet))
  }

  /**
    * Try minimize to <code>cs</code>. Currently checks if the le constraints
    * cover any other kinds of constraints.
    */
  private[constraints] def minimize[Level](cs: java.util.Set[Constraint[Level]]): java.util.Set[Constraint[Level]] = {
    val isLeConstraint = (c: Constraint[Level]) => c.kind.equals(Constraint.Kind.LE)
    val covers = (cLe: Constraint[Level], c: Constraint[Level]) => {
      val scLe = Constraints.symmetricOf(cLe)
      Constraints.equalComponents(cLe, c) || Constraints.equalComponents(scLe, c);
    }

    val leCs = () => cs.iterator.filter(isLeConstraint)
    val notLeCs: Iterator[Constraint[Level]] = cs.iterator.filter(!isLeConstraint(_))
    val minimizedNonLEs: Iterator[Constraint[Level]] =
      for (cNle <- notLeCs if leCs().forall(c => !covers(c, cNle))) yield cNle
    (leCs() ++ minimizedNonLEs).toSet.asJava
  }

  /*
  static <Level> Set<Constraint<Level>> minimize(Set<Constraint<Level>> cs) {
        Predicate<Constraint<Level>> isLeConstraint =
                c -> c.kind.equals(Constraint.Kind.LE);
        BiFunction<Constraint<Level>, Constraint<Level>, Boolean> covers =
                (cLe, c) -> {
                    Constraint<Level> scLe = Constraints.symmetricOf(cLe);
                    return Constraints.equalComponents(cLe, c)
                           || Constraints.equalComponents(scLe, c);
                };
        Supplier<Stream<Constraint<Level>>> leCs =
                () -> cs.stream().filter(isLeConstraint);
        Stream<Constraint<Level>> notLeCs =
                cs.stream().filter(isLeConstraint.negate());
        HashSet<Constraint<Level>> seen = new HashSet<>();
        Stream<Constraint<Level>> minimizedNonLEs = notLeCs.flatMap(cNle -> {
            if (leCs.get().anyMatch(c -> covers.apply(c, cNle))) {
                return Stream.<Constraint<Level>>empty();
            } else {
                return Stream.<Constraint<Level>>of(cNle);
            }
        });
        return Stream.concat(leCs.<Constraint<Level>>get(), minimizedNonLEs).collect(toSet());
    }
   */

  def withImplications[Level](cs: Iterator[Constraint[Level]]): Iterator[Constraint[Level]] =
    cs.flatMap(c => Interop.asScalaIterator(Constraints.implicationsOf(c).iterator()))

  def projectTo[Level](cs: java.util.Set[Constraint[Level]], typeVarCol: java.util.Collection[TypeVars.TypeVar]): Iterator[Constraint[Level]] = {
    val closure: Set[Constraint[Level]] = close(cs)
    val typeVars = typeVarCol.iterator.map(CTypes.variable[Level]).toSet
    closure.iterator.filter(c =>
      c.variables().iterator.forall(v => typeVars.contains(variable(v)))
    )
  }

  private def findCompatibilityConflicts[Level](closed: Iterable[Constraint[Level]], cmpConflicts: List[Constraint[Level]], tags: TagMap[Level]): Iterator[CompatibilityConflict[Level]] = {
    for {
      confl <- cmpConflicts.iterator
      tLeft = confl.getLhs.inspect().asInstanceOf[Lit[Level]].t
      tRight = confl.getRhs.inspect().asInstanceOf[Lit[Level]].t
      (candC , typeVarTag) <- tags.tags.iterator
      if {
        val testLe = (conflT : CType[Level]) => conflT.equals(candC.getLhs()) || closed.iterator.exists(c => conflT.equals(c.getLhs()) && c.getRhs().equals(candC.getLhs()))
        testLe(confl.getLhs) && testLe(confl.getRhs)
      }
    } yield new CompatibilityConflict[Level](tLeft, tRight, typeVarTag)
  }

  private def findFlowConflict[Level](closed: Iterable[Constraint[Level]], leConflicts: List[Constraint[Level]], tags: TagMap[Level]): Iterator[FlowConflict[Level]] = {
    val sourceStream: Iterator[TypeDomain.Type[Level]] = leConflicts.iterator.map(c => {
      val ct = c.getLhs()
      val lit = ct.inspect().asInstanceOf[CTypeViews.Lit[Level]]
      lit.t
    })
    val sources: List[TypeDomain.Type[Level]] = sourceStream.toList
    val sinkStream: Iterator[TypeDomain.Type[Level]] = leConflicts.iterator.map(c => {
      val ct = c.getRhs();
      if (!(ct.inspect().isInstanceOf[CTypeViews.Lit[Level]])) {
        throw new IllegalStateException(
          "Unexpected: conflicting constraint does not consist of literals:  "
            + c.toString());
      }
      val lit = ct.inspect().asInstanceOf[CTypeViews.Lit[Level]]
      lit.t
    })
    val sinks: Set[TypeDomain.Type[Level]] = sinkStream.toSet
    for {
      t <- sources.iterator
      sourceTags = tags.tags.filter(kv => kv._1.getLhs().equals(literal(t))).toList
      tSink <- sinks.iterator
      sinkTags = tags.tags.filter(kv => kv._1.getRhs().equals(literal(tSink))).toList
      srcT <- sourceTags
      snkT <- sinkTags
      if {
        val srcRhs = srcT._1.getRhs
        val snkLhs = snkT._1.getLhs
        srcRhs.equals(snkLhs) || closed.exists(clC => srcRhs.equals(clC.getLhs())
                                  && snkLhs.equals(clC.getRhs()))
      }
    } yield new FlowConflict(t, srcT._2, tSink, snkT._2)
  }
}

class NaiveConstraints[Level](types: TypeDomain[Level], cs: Set[Constraint[Level]])
  extends ConstraintSet[Level](types)  {

  def this(types: TypeDomain[Level], cs: java.util.Collection[Constraint[Level]]) {
    this(types, Set(cs.toSeq:_*))
  }

  def isSatisfiedFor(types: TypeDomain[Level], a: Assignment[Level]): Boolean = {
    return this.cs.forall(c => c.isSatisfied(types, a))
  }

  def add(other: java.util.Collection[Constraint[Level]]): ConstraintSet[Level] = {
    val otherSet = Set(other.toSeq:_*)
    return new NaiveConstraints[Level](types, this.cs ++ otherSet)
  }

  def add(c: Constraint[Level]): ConstraintSet[Level] = {
    return this.add(Collections.singleton(c))
  }

  def add(other: ConstraintSet[Level]): ConstraintSet[Level] = {
    return this.add(other.stream.toList)
  }

  def stream: Iterator[Constraint[Level]] = {
    return Interop.asScalaIterator(this.cs.iterator)
  }

  def projectTo(typeVars: java.util.Set[TypeVars.TypeVar]): ConstraintSet[Level] = {
    val constraintSet = this.cs
    return new NaiveConstraints[Level](this.types, NaiveConstraints.projectTo(constraintSet, typeVars).toSet)
  }

  def implies(types: TypeDomain[Level], other: ConstraintSet[Level]): Boolean = {
    return this.enumerateSatisfyingAssignments(types, other.variables.toSet.asJavaCollection).forall(ass => other.isSatisfiedFor(types, ass))
  }

  def isSat(types: TypeDomain[Level]): Boolean = {
    return NaiveConstraints.close[Level](this.stream.toSet.asJava).forall(c => c.isSatisfiable(types))
  }

  def doesNotSubsume(other: ConstraintSet[Level]): Option[Assignment[Level]] = {
    return this.enumerateSatisfyingAssignments(types, Collections.emptyList[TypeVars.TypeVar]).filter((a : Assignment [ Level ] ) => {
      val cStream = other.apply(a);
      val cs = cStream.toList;
      !new NaiveConstraints(types, cs).isSat(types);
    }).find(_ => true)
  }

  /**
    * Enumerate all assignments that satisfy this constraint set.
    *
    * @param requiredVariables The minimal set of variables that should be
    *                          bound by a satisfying assignment. The actual
    *                          assignments may bind more variables.
    */
  def enumerateSatisfyingAssignments(types: TypeDomain[Level], requiredVariables: java.util.Collection[TypeVars.TypeVar]): Iterator[Assignment[Level]] = {
    val variables: Set[TypeVars.TypeVar] = cs.flatMap(c => c.variables().iterator().toSet)
    Assignments.enumerateAll(types, requiredVariables.toSet ++ variables).iterator.filter(a => this.isSatisfiedFor(types, a))
  }

  override def satisfyingAssignment(types: TypeDomain[Level], requiredVariables: java.util.Collection[TypeVars.TypeVar]): Option[Assignment[Level]] = {
    return this.enumerateSatisfyingAssignments(types, requiredVariables).find(_ => true)
  }

  /**
    * Try to find a conflict cause that tries to succinctly explain the
    * conflicting flows.
    */
  override def findConflictCause(tags: TagMap[Level]): java.util.List[ConflictCause[Level]] = {
    val closed: Set[Constraint[Level]] = NaiveConstraints.close(this.cs)
    val conflicts: List[Constraint[Level]] = closed.filter(c => !c.isSatisfiable(types)).toList
    conflicts.foreach(c => {
      val isLit = (ct: CType [ Level ]) => ct.inspect().isInstanceOf[CTypeViews.Lit[Level]];
      if (!(isLit(c.getLhs()) && isLit(c.getRhs()))) {
        throw new IllegalStateException(
          "Unexpected: conflicting constraint does not consist of literals:  "
            + c.toString());
      }
    })
    val leConflicts: List[Constraint[Level]] = conflicts.filter(c => c.kind.equals(Constraint.Kind.LE))
    val cmpConflicts: List[Constraint[Level]] = conflicts.filter(c => c.kind.equals(Constraint.Kind.COMP))
    return (NaiveConstraints.findFlowConflict(closed, leConflicts, tags) ++ NaiveConstraints.findCompatibilityConflicts(closed, cmpConflicts, tags)).toList
  }

  override def toString: String = {
    val result: StringBuilder = new StringBuilder("{")
    val isLe = (c : Constraint[Level]) => c.kind.equals(Constraint.Kind.LE)
    val append = (c : Constraint[Level]) => {
      result.append(c.toString());
      result.append(", ");
    }
    cs.filter(isLe).foreach(append)
    cs.filter(!isLe(_)).foreach(append)
    result.append("}")
    return result.toString
  }
}