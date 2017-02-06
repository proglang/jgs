package de.unifreiburg.cs.proglang.jgs.examples

import de.unifreiburg.cs.proglang.jgs.examples.ExampleTypes.{Dynamic, Public, Static, lub}
import de.unifreiburg.cs.proglang.jgs.instrumentation._
import soot.Local
import soot.SootMethod
import soot.jimple.Stmt

/**
  * Artifical results for ExampleTests
  * @tparam Level Level may be static, dynamic or public.
  */
class AnalysisResults[Level] {
  val max_varTyping: VarTyping[Level] = {
    import Code._

    makeVarTyping(
      (instantiation : Instantiation[Level]) =>
        Map.apply[(Stmt, Local), (Type[Level], Type[Level])](
          (max_0_id_X_p0, localX) -> (Public[Level](), instantiation.get(0)),
          (max_0_id_X_p0, localY) -> (Public[Level](), Public[Level]),
          (max_0_id_X_p0, localZ) -> (Public[Level](), Public[Level]),
          (max_1_id_Y_p1, localX) -> (instantiation.get(0), instantiation.get(0)),
          (max_1_id_Y_p1, localY) -> (Public[Level](), instantiation.get(1)),
          (max_1_id_Y_p1, localZ) -> (Public[Level](), Public[Level]),
          (max_2_assign_Z_X, localX) ->(instantiation.get(0), instantiation.get(0)),
          (max_2_assign_Z_X, localY) -> (instantiation.get(1), instantiation.get(1)),
          (max_2_assign_Z_X, localZ) ->(Public[Level](), instantiation.get(0)),
          (max_3_if_Z_lt_Y, localX) -> (instantiation.get(0), instantiation.get(0)),
          (max_3_if_Z_lt_Y, localY) -> (instantiation.get(1), instantiation.get(1)),
          (max_3_if_Z_lt_Y, localZ) ->(instantiation.get(0), instantiation.get(0)),
          (max_4_goto_6, localX) -> (instantiation.get(0), instantiation.get(0)),
          (max_4_goto_6, localY) -> (instantiation.get(1), instantiation.get(1)),
          (max_4_goto_6, localZ) -> (instantiation.get(0), instantiation.get(0)),
          (max_5_assign_Z_Y, localX) -> (instantiation.get(0), instantiation.get(0)),
          (max_5_assign_Z_Y, localY) -> (instantiation.get(1), instantiation.get(1)),
          (max_5_assign_Z_Y, localZ) ->(instantiation.get(0), lub(instantiation.get(0),instantiation.get(1))),
          (max_6_return_Z, localX) -> (instantiation.get(0), instantiation.get(0)),
          (max_6_return_Z, localY) ->  (instantiation.get(1), instantiation.get(1)),
          (max_6_return_Z, localZ) -> (lub(instantiation.get(1), instantiation.get(0)), lub(instantiation.get(0), instantiation.get(1)))
    ))
  }

  // define the Program Counters at the individual statements of ExampleTest.test_max_D_D__D and test_max_D_P__D.
  val max_cxTyping: CxTyping[Level] = {
    import Code._
    makeCxTyping(instantiation => Map(
      max_0_id_X_p0 -> Public[Level](),
      max_1_id_Y_p1 -> Public[Level](),
      max_2_assign_Z_X -> Public[Level](),
      max_3_if_Z_lt_Y -> Public[Level](),
      max_4_goto_6 -> lub(instantiation.get(0), instantiation.get(1)),
      max_5_assign_Z_Y -> lub(instantiation.get(0), instantiation.get(1)),
      max_6_return_Z -> Public[Level]()
    ))
  }



  private[examples] val max_methods_D_P__D: Methods[Level] = new Methods[Level]() {
    def getMonomorphicInstantiation(m: SootMethod): Instantiation[Level] = {
      makeInstantiation(Map(0 -> Dynamic[Level](), 1 -> Public[Level]()), Dynamic[Level]())
    }

    def getEffectType(m: SootMethod): Effect[Level] = makeEmptyEffect()

    }

  val max_methods_D_D__D: Methods[Level] =
    new Methods[Level] {
      override def getEffectType(m: SootMethod): Effect[Level] = makeEmptyEffect()

      override def getMonomorphicInstantiation(m: SootMethod): Instantiation[Level] =
        makeInstantiation(Map(0 -> Dynamic[Level](), 1 -> Dynamic[Level]()), Dynamic[Level]())
  }


  // Utilities
  def makeVarTyping(getMaps : Instantiation[Level] => (Map[(Stmt, Local), (Type[Level], Type[Level])]))
  : VarTyping[Level] = {

    def lookup(map: Map[(Stmt, Local), (Type[Level], Type[Level])], s: Stmt, l: Local) =
      map.getOrElse((s, l), throw new IllegalArgumentException(s"Type of a local ${l} in stmt ${s} not known"))

    new VarTyping[Level] {
      override def getBefore(instantiation: Instantiation[Level], s: Stmt, l: Local): Type[Level] = {
        lookup(getMaps(instantiation), s, l)._1
      }

      override def getAfter(instantiation: Instantiation[Level], s: Stmt, l: Local): Type[Level] =
        lookup(getMaps(instantiation), s, l)._2
    }
  }

  def makeCxTyping(getMap : Instantiation[Level] => Map[Stmt, Type[Level]]) =
    new CxTyping[Level] {
      override def get(instantiation: Instantiation[Level], s: Stmt): Type[Level] =
        getMap(instantiation).getOrElse(s, throw new IllegalArgumentException(s"Cx Type of stmt ${s} not known"))
    }

  def makeInstantiation(m : Map[Int, Type[Level]], ret : Type[Level]) : Instantiation[Level] =
    new Instantiation[Level] {
      override def get(param: Int): Type[Level] =
        m.getOrElse(param, throw new IllegalArgumentException(s"Type for parameter ${param} not known"))

      override def getReturn: Type[Level] = ret
    }

  def makeEmptyEffect(): Effect[Level] =
    new Effect[Level] {
      override def getType: Type[Level] = throw new IllegalArgumentException("Trying to get type from empty effect")

      override def isEmpty: Boolean = true
    }

  def makeEffect(ty : Type[Level]) : Effect[Level] =
    new Effect[Level] {override def isEmpty: Boolean = false

      override def getType: Type[Level] = ty
    }
}