package de.unifreiburg.cs.proglang.jgs.cli

import java.util
import java.util.function.Function
import java.util.stream.Collectors

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeViews.Variable
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType
import de.unifreiburg.cs.proglang.jgs.constraints.Constraint.Kind
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVarTags._
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVarViews.{Internal, Ret, Cx, Param}
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.{Pub, Dyn, Lit}
import de.unifreiburg.cs.proglang.jgs.constraints._
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet.RefinementCheckResult
import de.unifreiburg.cs.proglang.jgs.jimpleutils.CastsFromMapping.Conversion
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.EffectRefinementResult
import de.unifreiburg.cs.proglang.jgs.typing._
import org.kiama.output.PrettyPrinter._

import scala.collection.JavaConverters._

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars._
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain._

import scala.util.{Success, Failure, Try}

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

        val classHierarchyRefinement =
          refinementCheckResult[Level](
            abstractDescription = "Signature constraints of supermethod",
            concreteDescription = "Signature constraints of overriding method",
            conflictDescription = "Conflicts in overriding constraints") _

        val csResult = text("constraint refinement:") <> classHierarchyRefinement(error.constraintsCheckResult)
        val effResult = text("effect refinement:") <> effectCheck(error.effectCheckResult)

        methods <@@> csResult <@@> effResult
      }, 2)
    }
  }

  def typingException[Level](e: Throwable) : Doc = {
    "Exceptional error " <> nest(linebreak <> e.getMessage, 2)
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
        List(t <> linebreak)
      } else {
        List()
      }

      val reason = cat(List(
        if (!result.bodyHasSolution()) {
          val explanation = {
            val mcauses = result.conflictCauses.get()
            if (mcauses.isEmpty) {
              "Unable to find succinct causes, sorry. Complete constraints of body:" <>
              nest(linebreak<> vcat(copyToList(result.completeBodyConstraints.stream()).map(constraint(_))), 2)
            } else {
              vcat(mcauses.asScala.toList.distinct.map(conflictCause))
            }
          }
          List("Unsatisfiable constraints in method body: " <>
            nest(linebreak <> explanation, 2))
        } else if (!result.refinementCheckResult.isSuccess) {
          val bodyRefinenemtResult =
            refinementCheckResult[Level](
              abstractDescription = "Signature constraints",
              concreteDescription = "Constraints inferred for body",
              conflictDescription = "Conflicts of body constraints with counterexample") _
          List("Signature constraints are insufficient: " <>
            nest(linebreak <> bodyRefinenemtResult(result.refinementCheckResult), 2))
        } else {
          List()
        },
        when(!result.missedEffects.isEmpty,
          "Signature misses following effects detected in the body: " <+> result.missedEffects.toString)
      ).flatten)
      "Failed for the following reasons:" <> nest(linebreak <> reason, 2)
    }
  }

  def conflictCause[Level](c : ConflictCause[Level]) : Doc = {
    c match {
      case c : FlowConflict[Level] => flowConflictCause(c)
      case c : CompatibilityConflict[Level] => compatibilityConflictCause(c)
    }
  }

  def compatibilityConflictCause[Level](c : CompatibilityConflict[Level]) : Doc = {
    val tag = conflictDest(c.upperTag)
    sectype(c.type1) <+> "and" <+> sectype(c.type2) <+> "both flow into" <+> tag
  }

  def flowConflictCause[Level](c : FlowConflict[Level]) : Doc = {
    val src : Doc = conflictSource(c.lowerTag)
    val dest : Doc = conflictDest(c.upperTag)
    src <+> parens(sectype(c.lowerType)) <+> "flows into" <+> dest <+> parens(sectype(c.upperLevel))
  }

  def conflictSource(tag: TypeVarTag) : Doc = conflictTag(tag)
  def conflictDest(tag : TypeVarTag) : Doc = conflictTag(tag)
  def conflictTag(tag : TypeVarTag) : Doc = tag match {
    case Symbol(symbol) => "parameter" <+> symbol.toString
    case Field(field) => "field" <+> field.toString
    case Cast(conv) => "destination of a" <+> braces(conversion(conv))<+> "value cast"
    case CxCast(conv) => braces(conversion(conv)) <+> "context cast"
    case MethodReturn(method) => "call to"<+>method.toString
    case MethodArg(method, pos) => s"argument ${pos} of method ${method}"
    case Join(condition) => {
      raw"""join point of condition "${condition}""""
    }
  }


  def conversion[Level](c : Conversion[Level]) : Doc = {
    sectype(c.source) <+> "~>" <+> sectype(c.dest)
  }

  def refinementCheckResult[Level](abstractDescription: String,
                                   concreteDescription: String,
                                   conflictDescription: String)
                                  (result: RefinementCheckResult[Level]): Doc = {
    result.counterExample
      .map[Doc](
      fun(cs => {
        nest(linebreak <>
          "Counterexample:" <+> assignment(cs) <@@>
          s"${abstractDescription}:" <+> constraintSet(copyToList(result.abstractConstraints.stream())) <@@>
          s"${concreteDescription}:" <+> constraintSet(copyToList(result.concreteConstraints.stream())) <@@>
          s"${conflictDescription}: " <+> constraintSet(copyToList(result.getConflicting.stream())), 2)
      }))
      .orElse(space <> text("ok"))
  }

  private def copyToList[T](c: util.stream.Stream[T]): List[T] = {
    c.collect(Collectors.toList()).asScala.toList
  }

  def constraintSet[Level](cs: List[Constraint[Level]]): Doc = {
    def isReflexive(c : Constraint[Level]) = c.getLhs == c.getRhs
    // TODO: Why add reflexive constraints when I want to remove them later anyway? Fix the representation of constraints s.t. it is not necessary to "saturate" constraint sets to contain all parameters.
    val simplifiedCs = cs.filter(!isReflexive(_))
      braces(hsep(simplifiedCs.map(constraint(_)), ","))
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
