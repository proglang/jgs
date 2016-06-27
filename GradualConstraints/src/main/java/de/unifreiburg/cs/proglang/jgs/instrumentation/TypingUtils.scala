package de.unifreiburg.cs.proglang.jgs.instrumentation

import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView
import de.unifreiburg.cs.proglang.jgs.signatures.FieldTable
import de.unifreiburg.cs.proglang.jgs.typing.EnvMap
import soot.{Local, SootField}
import soot.jimple.Stmt

/**
  * Methods for creating fieldtypings.
  */
object TypingUtils {

  def fieldTypingfromFieldTable[Level](t: FieldTable[Level]): FieldTyping[Level] =
    new FieldTyping[Level] {
      override def get(f: SootField): TypeView[Level] = {
        t.get(f).map(_.inspect()).getOrElse {
          throw new IllegalArgumentException(s"Could not find a type for field ${f}")
        }
      }
    }

  def cxTypingFromEnvMap[Level](envMap : EnvMap, constraints : ConstraintSet[Level]) : CxTyping[Level] =
    new CxTyping[Level] {
      override def get(instantiation: Instantiation[Level], s: Stmt): TypeView[Level] = ???
    }

  def varTypingFromEnvMap[Level](envMap : EnvMap, constraints : ConstraintSet[Level]) : VarTyping[Level] =
    new VarTyping[Level] {
      override def getBefore(instantiation: Instantiation[Level], s: Stmt, l: Local): TypeView[Level] = ???

      override def getAfter(instantiation: Instantiation[Level], s: Stmt, l: Local): TypeView[Level] = ???
    }
}
