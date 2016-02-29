package de.unifreiburg.cs.proglang.jgs.cli

import java.util
import java.util.function.Function
import java.util.stream.Collectors

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeViews.Variable
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType
import de.unifreiburg.cs.proglang.jgs.constraints.Constraint.Kind
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVarViews.{Internal, Ret, Cx, Param}
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.{Pub, Dyn, Lit}
import de.unifreiburg.cs.proglang.jgs.constraints._
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet.RefinementCheckResult
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.EffectRefinementResult
import de.unifreiburg.cs.proglang.jgs.typing.{MethodTyping, ClassHierarchyTyping}
import org.kiama.output.PrettyPrinter._

import scala.collection.JavaConverters._

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars._
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain._

object Format {

  def pprint(d: Doc): String = pretty(d)

  def fun[A, B](f: A => B): Function[A, B] = new Function[A, B] {
    override def apply(t: A): B = f(t)
  }

  def classHierarchyCheck[Level](result: ClassHierarchyTyping.Result[Level]): Doc = {
    if (result.isSuccess) {
      text("Success.")
    } else {
      val error = result.error.get()
      text("Failed.") <> nest(linebreak <> {
        val subMethod = text(error.subtypeMethod.toString)
        val superMethod = text(error.superTypeMethod.toString)
        val methods = text("methods:") <+> subMethod <+> text(" overrides ") <+> superMethod

        val csResult = text("constraint refinement:") <>
          refinementCheckResult(
            abstractName = "Signature of supermethod",
            concreteName = "Signature of submethod")(error.constraintsCheckResult)
        val effResult = text("effect refinement:") <> effectCheck(error.effectCheckResult)

        methods <@@> csResult <@@> effResult
      }, 2)
    }
  }

  def methodTypingResult[Level](result: MethodTyping.Result[Level]): Doc = {
    if (result.isSuccess) {
      "Success" <+> (if (result.signatureHasSolution) {
        empty
      } else {
        "(but method is uncallable)"
      })
    } else {
      val when = (b: Boolean, t: Doc) => if (b) {
        t <> linebreak
      } else {
        empty
      }

      val reason = cat(List(
        when(!result.bodyHasSolution(),
          "Unsatisfiable constraints in method body: " <+> result.completeBodyConstraints.toString),
        when(!result.refinementCheckResult.isSuccess,
          "Method body violates constraints in the signature: " <>
            nest(linebreak <>
              refinementCheckResult(abstractName = "Signature constraints", concreteName = "Constraints inferred for body")(result.refinementCheckResult),
              2)),
        when(!result.missedEffects.isEmpty,
          "Signature misses following effects detected in the body: " <+> result.missedEffects.toString)
      ))
      "Failed for the following reasons:" <> nest(linebreak <> reason, 2)
    }
  }

  def refinementCheckResult[Level](abstractName: String, concreteName: String)
                                  (result: RefinementCheckResult[Level]): Doc = {
    result.counterExample
      .map[Doc](
      fun(cs => {
        nest(linebreak <>
          "Counterexample:" <+> assignment(cs) <@@>
          abstractName <+> constraintSet(copyToList(result.abstractConstraints.stream())) <@@>
          concreteName <+> constraintSet(copyToList(result.concreteConstraints.stream())) <@@>
          "Constraints conflicting with counterexample: " <+> constraintSet(copyToList(result.getConflicting.stream())), 2)
      }))
      .orElse(space <> text("ok"))
  }

  private def copyToList[T](c: util.stream.Stream[T]): List[T] = {
    c.collect(Collectors.toList()).asScala.toList
  }

  def constraintSet[Level](cs: List[Constraint[Level]]): Doc = {
      braces(hsep(cs.map(constraint(_)), ","))
  }

  def constraint[Level](c: Constraint[Level]): Doc = {
    val lhs = ctype(c.getLhs)
    val rhs = ctype(c.getRhs)
    val op = kind(c.kind)
    lhs <+> op <+> rhs
  }

  def ctype[Level](ct: CType[Level]): Doc = {
    ct.inspect() match {
      case CTypeViews.Lit(t) => sectype(t)
      case Variable(v) => typeVar(v)
    }
  }

  def kind(k: Constraint.Kind): Doc = {
    k match {
      case Kind.LE => "<="
      case Kind.COMP => "~"
      case Kind.DIMPL => "?~>"
    }
  }

  def assignment[Level](ass: Assignment[Level]): Doc = {
    val mapping: Map[TypeVar, Type[Level]] = ass.get().asScala.toMap
    val kvDocs = mapping.map(kv => typeVar(kv._1) <> "=" <> sectype(kv._2)).toList
    braces(hsep(kvDocs, ","))
  }

  def typeVar(v: TypeVar): Doc = {
    v.inspect() match {
      case Param(pos) => s"@${pos}"
      case Cx() => "<top-level-cx>"
      case Ret() => "@ret"
      case Internal(description) => angles(s"internal: ${description}")
    }
  }

  def sectype[Level](t: Type[Level]): Doc = {
    t.inspect() match {
      case Lit(level) => level.toString
      case Dyn() => "?"
      case Pub() => "pub"
    }
  }

  def effectCheck[Level](result: EffectRefinementResult[Level]): Doc = {
    if (result.missingEffects.isEmpty) {
      space <> "ok"
    } else {
      nest(softline <> result.missingEffects.toString <+> "missing in super-method", 2)
    }
  }
}
