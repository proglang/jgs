package de.unifreiburg.cs.proglang.jgs.instrumentation

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeViews.{CTypeView, Variable}
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVarViews.{Cx, Internal, Param, Ret}
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar
import de.unifreiburg.cs.proglang.jgs.constraints.{CTypeViews, _}
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.{Dyn, Lit, Pub, TypeView}
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var
import de.unifreiburg.cs.proglang.jgs.signatures.FieldTable
import de.unifreiburg.cs.proglang.jgs.typing.EnvMap
import soot.{Local, SootField}
import soot.jimple.Stmt

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

  def cxTypingFromEnvMap(envMap : EnvMap, constraints : ConstraintSet[Level]) : CxTyping[Level] =
    new CxTyping[Level] {
      override def get(instantiation: Instantiation[Level], s: Stmt): Type[Level] = {
        // TODO: this is wrong.. only a placeholder
        instrumentationType(Pub())
      }
    }

  def varTypingFromEnvMap(envMap : EnvMap, constraints : ConstraintSet[Level]) : VarTyping[Level] = {
    def get(typing : EnvMap.MultiEnv, instantiation: Instantiation[Level], l: Local) = {
      val tvs = typing.getOrElse(Var.fromLocal(l), Set())
      val lbs = for { tv <- tvs ; lb <- lowerBoundLiteralsOrParams(constraints, tv, instantiation) } yield lb
      joinLowerBounds(lbs)
    }
    new VarTyping[Level] {
      override def getBefore(instantiation: Instantiation[Level], s: Stmt, l: Local): Type[Level] = {
        val pre = envMap.getPre(s).getOrElse{ throw new IllegalArgumentException(s"No typing information before statement ${s}") }
        get(pre, instantiation, l)
      }

      override def getAfter(instantiation: Instantiation[Level], s: Stmt, l: Local): Type[Level] = {
        val post = envMap.getPost(s).getOrElse{ throw new IllegalArgumentException(s"No typing for information after statement ${s}") }
        get(post, instantiation, l)

      }
    }
  }

  /**
    * Returns the literals or parameters that form lower bounds of type variable tv
    */
  private def lowerBoundLiteralsOrParams(constraints : ConstraintSet[Level],
                                                tv : TypeVar,
                                                instantiation : Instantiation[Level])
    : Set[Type[Level]] = for {lb <- constraints.lowerBounds(tv)
                              t <- lb match {
                                    case CTypeViews.Lit(t) => Some(t)
                                    case Variable(v) => v.inspect() match {
                                      case Param(pos) => Some(instantiation.get(pos))
                                      case Cx() => None
                                      case Ret() => None
                                      case Internal(description) => None
                                    }
                             }} yield t

  private def joinLowerBounds(lbs : Set[Type[Level]]) : Type[Level] = {

    var result : TypeView[Level] = Pub[Level]()
    for (t <- lbs) {
        if (t.isPublic)  {
          // skip
        } else if (t.isDynamic) {
          return t
        } else if (t.isStatic) {
          result = result match {
            case Lit(level) => Lit(secdomain.lub(level, t.getLevel))
            case Dyn() => throw new IllegalArgumentException("Try to join static and dynamic... should not happen!")
            case Pub() => Lit(t.getLevel)
          }
        }
    }
    instrumentationType(result)
  }

}
