package de.unifreiburg.cs.proglang.jgs.signatures.parse

import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintKind
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView
import de.unifreiburg.cs.proglang.jgs.signatures
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.makeSigConstraint
import de.unifreiburg.cs.proglang.jgs.signatures.SigConstraint

import scala.util.Try
import scala.util.parsing.combinator.RegexParsers

class ConstraintParser[Level](val typeParser : AnnotationParser[TypeView[Level]])
  extends RegexParsers {


  def constraintKindParser : Parser[ConstraintKind] =
    ("<=" | "~") ^^ {
      case "<=" => ConstraintKind.LE
      case "~" => ConstraintKind.COMP
    }

  def symbolParser : Parser[signatures.Symbol[Level]] = {
    val retParser: Parser[signatures.Symbol[Level]] = "ret" ^^^ signatures.Symbol.ret()
    val paramParser : Parser[signatures.Symbol[Level]] = "[0-9]+".r ^^ { d => signatures.Symbol.param(d.toInt) }
    val literalParser : Parser[signatures.Symbol[Level]] = "\\S+".r flatMap {
      s => typeParser.parse(s).map(t => success(signatures.Symbol.literal(t))).getOrElse(failure(s"Type parser: unable to parse `$s'"))
        }
    "@" ~> (retParser | paramParser) | literalParser
  }

  def constraintParser : Parser[SigConstraint[Level]] =
    symbolParser ~ constraintKindParser ~ symbolParser ^^ {
      case sym1 ~ k ~ sym2 => makeSigConstraint(sym1, sym2, k)
    }

  def parseConstraints(s : String) : Try[SigConstraint[Level]] =
    parseAll(constraintParser, s) match {
      case Success(result, next) => scala.util.Success(result)
      case err: NoSuccess => scala.util.Failure(new RuntimeException(s"Error parsing constraint $s: ${err.msg}"))
    }
}

