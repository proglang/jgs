package de.unifreiburg.cs.proglang.jgs.instrumentation

import de.unifreiburg.cs.proglang.jgs.constraints.{ConstraintSet, SecDomain, TypeDomain}
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.{Dyn, Lit, Pub, TypeView}
import de.unifreiburg.cs.proglang.jgs.signatures.FieldTable
import de.unifreiburg.cs.proglang.jgs.typing.EnvMap
import soot.{Local, SootField}
import soot.jimple.Stmt
import soot.jimple.toolkits.annotation.parity.ParityAnalysis

/**
  * Methods for creating fieldtypings.
  */
class TypingUtils[Level] (secdomain : SecDomain[Level]){

  def instrumentationType(tv : TypeView[Level]) : Type[Level] =
    new Type[Level]{
      override def isStatic: Boolean = PartialFunction.cond(tv)({case Lit(_) => true})

      override def isDynamic: Boolean = PartialFunction.cond(tv){case Dyn() => true}

      override def isPublic: Boolean = PartialFunction.cond(tv){case Pub() => true}

      override def getLevel: Level = tv match {
        case Lit(level) => level
        case Pub() => secdomain.bottom()
        case Dyn() => throw new IllegalArgumentException("Trying to get a level from a dynamic type")
      }
    }

  def fieldTypingfromFieldTable(t: FieldTable[Level]): FieldTyping[Level] =
    new FieldTyping[Level] {
      override def get(f: SootField): Type[Level] = {
        t.get(f).map(t => instrumentationType(t.inspect())).getOrElse {
          throw new IllegalArgumentException(s"Could not find a type for field ${f}")
        }
      }
    }

  def cxTypingFromEnvMap[Level](envMap : EnvMap, constraints : ConstraintSet[Level]) : CxTyping[Level] =
    new CxTyping[Level] {
      override def get(instantiation: Instantiation[Level], s: Stmt): Type[Level] = ???
    }

  def varTypingFromEnvMap[Level](envMap : EnvMap, constraints : ConstraintSet[Level]) : VarTyping[Level] =
    new VarTyping[Level] {
      override def getBefore(instantiation: Instantiation[Level], s: Stmt, l: Local): Type[Level] = ???

      override def getAfter(instantiation: Instantiation[Level], s: Stmt, l: Local): Type[Level] = ???
    }
}
