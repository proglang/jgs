package de.unifreiburg.cs.proglang.jgs.instrumentation

import soot.jimple.Stmt
import soot.{Local, SootMethod}

import scala.collection.JavaConversions._

/**
  * Utilities to make mock instances of the results of body typing relevant for instrumentation.
  */
object BodyTypingSpec {

  sealed trait Phase
  case object P extends Phase // Public
  case object S extends Phase // Static
  case object D extends Phase // Dynamic

  def phaseFromType[Level](t : Type[Level]) : Phase = {
    if (t.isDynamic) {
      D
    } else if (t.isPublic) {
      P
    } else {
      S
    }
  }

  type VarMapping = Map[Local, (Phase, Phase)]
  case class TypeMapping( cxPhase : Phase
                        , varPhases : VarMapping)

  def makeMapper(locals : Local*)(cxPhase : Phase, varPhases : Tuple2[Phase, Phase]*) = {
    if (locals.size != varPhases.size) {
      throw new IllegalArgumentException(s"Size of phases (${varPhases.size}) does not match size of entities (${locals.size}, ${locals})")
    }
    TypeMapping(cxPhase, locals.zip(varPhases).toMap)
  }



  def mappingsFromResults[Level](instantiation : Instantiation[Level],
                                 cxTyping : CxTyping[Level],
                                 varTyping : VarTyping[Level],
                                 m : SootMethod) : Seq[TypeMapping] = {
    val stms = m.getActiveBody.getUnits.toSeq
    val locals = m.getActiveBody.getLocals.toSeq

    def getMappings(s : Stmt) : TypeMapping = {
      val varPhases = (for (l <- locals)
        yield l -> (phaseFromType(varTyping.getBefore(instantiation, s, l)),
                    phaseFromType(varTyping.getAfter(instantiation, s, l))))
        .toMap
      TypeMapping(phaseFromType(cxTyping.get(instantiation, s)), varPhases)
    }
    for (s <- stms) yield getMappings(s.asInstanceOf[Stmt])
  }

  /*
  Example
  Seq(
    Map(Cx -> P, V(localX) -> D, V(localZ) -> D),
    Map(... )
  )

  For a body of n statements we need n+1 specs.
   */

}
