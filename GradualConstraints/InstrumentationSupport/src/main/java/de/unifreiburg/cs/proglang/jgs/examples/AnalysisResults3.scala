package de.unifreiburg.cs.proglang.jgs.examples

import de.unifreiburg.cs.proglang.jgs.examples.ExampleTypes.{Dynamic, Public, Static, lub}
import de.unifreiburg.cs.proglang.jgs.instrumentation._
import soot.Local
import soot.SootMethod
import soot.jimple.Stmt

/**
  * Artifical results for ExampleTests2
  * @tparam Level Level may be static, dynamic or public.
  */
class AnalysisResults3[Level] {

  //make Variable Typing:
  val up_varTyping: VarTyping[Level] = {
    import Code._
    makeVarTyping(
      (instantiation : Instantiation[Level]) =>
        Map.apply[(Stmt, Local), (Type[Level], Type[Level])](
          (up_0_id_X_p0, localB) -> (Public[Level](), Public[Level]()),
          (up_0_id_X_p0, localX) -> (Public[Level](), instantiation.get(1)),

          (up_1_id_B_p2, localB) -> (Public[Level](), instantiation.get(0)),
          (up_1_id_B_p2, localX) -> (instantiation.get(1), instantiation.get(1)),

          (up_2_if_not_B, localB) -> (instantiation.get(0), instantiation.get(0)),
          (up_2_if_not_B, localX) -> (instantiation.get(1), instantiation.get(1)),

          (up_3_inc_B, localB) -> (instantiation.get(0), instantiation.get(0)),
          (up_3_inc_B, localX) -> (instantiation.get(1), instantiation.get(1)),

          (up_4_return_B, localB) -> (instantiation.get(0), instantiation.get(0)),
          (up_4_return_B, localX) -> (instantiation.get(1), instantiation.get(1))
          ))
  }


  // define Program Counter at individual statements of ExampleTest2.sum_D_D__D.
  val up_cxTyping: CxTyping[Level] = {
    import Code._
    makeCxTyping(instantiation => Map(
      up_0_id_X_p0 -> Public[Level](),
      up_1_id_B_p2 -> Public[Level](),
      up_2_if_not_B -> Public[Level](),
      up_3_inc_B -> instantiation.get(0),
      up_4_return_B -> instantiation.get(0)
    ))
  }

  // Returns "a mapping from methods to their instantiations".
  // Returns D_D__D, basically.
  val up_methods_D_D__D: Methods[Level] =
    new Methods[Level] {
      override def getEffectType(m: SootMethod): Effect[Level] = makeEmptyEffect()

      override def getMonomorphicInstantiation(m: SootMethod): Instantiation[Level] =
        makeInstantiation(Map(0 -> Dynamic[Level](), 1 -> Dynamic[Level]()), Dynamic[Level]())
    }


  // Returns "a mapping from methods to their instantiations".
  // Returns P_D__D, basically.
  val up_methods_P_D__D: Methods[Level] =
  new Methods[Level] {
    override def getEffectType(m: SootMethod): Effect[Level] = makeEmptyEffect()

    override def getMonomorphicInstantiation(m: SootMethod): Instantiation[Level] =
      makeInstantiation(Map(0 -> Public[Level](), 1 -> Dynamic[Level]()), Dynamic[Level]())
  }


  // Returns P_P__P basically.
  val up_methods_P_P__P: Methods[Level] =
    new Methods[Level] {
      override def getEffectType(m: SootMethod): Effect[Level] = makeEmptyEffect()

      override def getMonomorphicInstantiation(m: SootMethod): Instantiation[Level] =
        makeInstantiation(Map(0 -> Public[Level](), 1 -> Public[Level]()), Public[Level]())
    }



  // =================================== Utilities ========================================
  def makeVarTyping(getMaps
                    : Instantiation[Level] => (Map[(Stmt, Local), (Type[Level], Type[Level])]))
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
    new Instantiation[Level] {override def get(param: Int): Type[Level] =
      m.getOrElse(param, throw new IllegalArgumentException(s"Type for parameter ${param} not known"))

      override def getReturn: Type[Level] = ret
    }


  def makeEmptyEffect(): Effect[Level] =
    new Effect[Level] {
      override def getType: Type[Level] = throw new IllegalArgumentException("Trying to get type from empty effect")

      override def isEmpty: Boolean = true
    }
}
