package de.unifreiburg.cs.proglang.jgs.constraints


import de.unifreiburg.cs.proglang.jgs.constraints.CTypeViews.{CTypeView, Lit, Variable}
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView

import scala.util.{Failure, Try}

/**
  * Operation on the types that occur in constraints (literals and variables)
  */
object CTypeOps {


  /**
    * @return The type of `ct` given assignment `a`. May return nothing when `ct` is a variable which is not mapped by `a`.
    */
  def tryApply[Level](a: Assignment[Level], ct: CTypeView[Level]): Option[TypeView[Level]] =
    ct match {
      case Lit(t) => Some(t)
      case Variable(v) => a.get.get(v)
    }

  /**
    * See [[tryApply(Assignment, CTypeView)]]
    */
  def tryApply[Level](a: Assignment[Level], ct: CType[Level]): Option[TypeView[Level]] =
    tryApply(a, ct.inspect())


  //
  //        public abstract Iterator<TypeVar> variables();
  /**
    * @return A stream of variables contained in `ct`. Either one or none.
    */
  def variables[Level](ct: CTypeView[Level]): Iterator[TypeVar] =
    ct match {
      case Lit(t) => Seq().iterator
      case Variable(v) => Seq(v).iterator
    }

  /**
    * See [[variables(CTypeView)]]
    */
  def variables[Level](ct: CType[Level]): Iterator[TypeVar] =
    variables(ct.inspect())


  /**
    * Like [[tryApply]] but throws an error in case of nothing.
    */
  def apply[Level](a: Assignment[Level], ct: CType[Level]): TypeView[Level] = {
    val error = Failure(
      new RuntimeException("Unknown variable: "
        + this.toString()
        + " Ass.: "
        + a.mappedVariables
        .toString()))
    val mresult = Try(tryApply(a, ct).get).recoverWith({case _ => error})
    mresult.get
  }


  /**
    * Try to apply the assignment `a` to `ct`. If `ct` is a variable that is mapped by `a`, the corresponding literal is returned.
    */
  def applyWhenPossible[Level](a : Assignment[Level], ct : CType[Level]) : CType[Level] =
    Try(tryApply(a, ct).get).map(CTypes.literal[Level]).getOrElse(ct)
}
