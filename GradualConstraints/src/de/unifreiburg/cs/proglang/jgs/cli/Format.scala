package de.unifreiburg.cs.proglang.jgs.cli

import java.util.function.Function

import de.unifreiburg.cs.proglang.jgs.constraints.Assignment
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet.RefinementCheckResult
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.EffectRefinementResult
import de.unifreiburg.cs.proglang.jgs.typing.ClassHierarchyTyping
import org.kiama.output.PrettyPrinter._


object Format {

  def pprint(d: Doc): String = pretty(d)

  def fun[A, B](f: A => B): Function[A, B] = new Function[A, B] {
    override def apply(t: A): B = f(t)
  }

  def classHierarchyCheck[Level](result: ClassHierarchyTyping.Result[Level]): Doc = {
    if (result.isSuccess) {
      text("Success")
    } else {
      val error = result.error.get()
      text("Failed:") <> nest(linebreak <> {
        val subMethod = text(error.subtypeMethod.toString)
        val superMethod = text(error.superTypeMethod.toString)
        val methods = text("methods:") <+> subMethod <+> text(" overrides ") <+> superMethod

        val csResult = text("constraint refinement:") <> refinementCheckResult(error.constraintsCheckResult)
        val effResult = text("effect refinement:") <> effectCheck(error.effectCheckResult)

        methods <@@> csResult <@@> effResult
      }, 2)
    }
  }

  def refinementCheckResult[Level](result: RefinementCheckResult[Level]): Doc = {
    result.counterExample
      .map[Doc](
      fun(cs => {
        nest(linebreak <>
          "Counterexample:" <+> cs.toString <@@>
          "Abstract:" <+> result.abstractConstraints.toString <@@>
          "Concrete:" <+> result.concreteConstraints.toString <@@>
          "Conflicting:" <+> result.getConflicting.toString, 2)
      }))
      .orElse(space <> text("ok"))
  }

  def effectCheck[Level](result: EffectRefinementResult[Level]): Doc = {
    if (result.missingEffects.isEmpty) {
      space <> "ok"
    } else {
      nest(softline <> result.missingEffects.toString <+> "missing in super-method", 2)
    }
  }
}
