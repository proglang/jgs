package de.unifreiburg.cs.proglang.jgs.typing

import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet.RefinementCheckResult
import de.unifreiburg.cs.proglang.jgs.constraints.{ConstraintSet, ConstraintSetFactory, TypeDomain}
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Supertypes
import de.unifreiburg.cs.proglang.jgs.signatures.Effects.EffectRefinementResult
import de.unifreiburg.cs.proglang.jgs.signatures.{Signature, MethodSignatures, SignatureTable}
import de.unifreiburg.cs.proglang.jgs.util.Interop
import de.unifreiburg.cs.proglang.jgs.util.Interop.{asScalaIterator, asScalaOption}
import org.apache.commons.lang3.tuple.Pair
import soot.SootClass
import soot.SootMethod


/**
  * Check that all overriding methods are subsumed by their
  * "super"-implementations
  */
object ClassHierarchyTyping {

  /**
    * Result of a class hierarchy check. If the error is present, then it also
    * contains a counterexample, i.e. error.counterExample.isPresent() returns
    * true.
    */
  class Result[Level] private(val error: Option[SubtypeError[Level]]) {

    def isSuccess: Boolean = error.isEmpty

    override def toString: String = error.toString
  }

  private object Result {
    def apply[Level] (error : Option[SubtypeError[Level]]) = new Result[Level](error)
  }

  class SubtypeError[Level](
                             val subtypeMethod: SootMethod,
                             val superTypeMethod: SootMethod,
                             val constraintsCheckResult: ConstraintSet.RefinementCheckResult[Level],
                             val effectCheckResult: EffectRefinementResult[Level]
                           ) {
    if (constraintsCheckResult.isSuccess && effectCheckResult.isSuccess) {
      throw new IllegalArgumentException("Subtyping check is successful")
    }

    override def toString: String = {
      String.format("Overriding method: %s\n" + "Super method: %s\n" + "Constraint check: %s\n" + "Effect check: %s", subtypeMethod, superTypeMethod, constraintsCheckResult, effectCheckResult)
    }
  }

  def checkTwoMethods[Level](csets: ConstraintSetFactory[Level], types: TypeDomain[Level], signatures: SignatureTable[Level], subMethod: SootMethod, superMethod: SootMethod): Result[Level] = {
    val errorMsgTail: String = "when checking that " + subMethod.toString + " refines " + superMethod.toString
    val sig1: Signature[Level] = signatures.get(subMethod).getOrElse(
      throw new TypingAssertionFailure(String.format("No signature found for %s %s", subMethod.toString(), errorMsgTail)))
    val sig2: Signature[Level] = signatures.get(superMethod).getOrElse(
      throw new TypingAssertionFailure(String.format("No signature found for %s %s", superMethod.toString(), errorMsgTail)))

    val result: Pair[ConstraintSet.RefinementCheckResult[Level], EffectRefinementResult[Level]] = sig1.refines(csets, types, sig2)
    if (result.getLeft.isSuccess && result.getRight.isSuccess) {
      Result(None)
    }
    else {
      Result(Some(new SubtypeError[Level](subMethod, superMethod, result.getLeft, result.getRight)))
    }
  }

  /**
    * Check if the overriding methods of {@code methodStream} refine their
    * super-implementations.
    * <p>
    * The signature table <code>Signatures</code> has to include a signature
    * for all methods.
    */
  def checkMethods[Level](csets: ConstraintSetFactory[Level], types: TypeDomain[Level], signatures: SignatureTable[Level], methodStream: Iterator[SootMethod]): Result[Level] = {
    val errors: Iterator[SubtypeError[Level]] =
      for (m1 <- methodStream;
           m2 <- Supertypes.findOverridden(m1);
           r <- checkTwoMethods(csets, types, signatures, m1, m2).error.iterator)
        yield r
    Result(errors.find(_ => true))
  }

  def check[Level](csets: ConstraintSetFactory[Level], types: TypeDomain[Level], signatures: SignatureTable[Level], classes: Iterator[SootClass]): Result[Level] = {
    checkMethods(csets, types, signatures, classes.flatMap(c => asScalaIterator(c.getMethods())))
  }
}