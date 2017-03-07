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
class AnalysisResults2[Level] {

  //make Variable Typing:
  // at statement max_01_id_x_p0, what value does variable localX have before (first entry) and after (snd entry) the statement?!
  val sum_varTyping: VarTyping[Level] = {
    import Code._
    makeVarTyping(
      (instantiation : Instantiation[Level]) =>
        Map.apply[(Stmt, Local), (Type[Level], Type[Level])](
          (max_0_id_X_p0, localX) -> (Public[Level](), instantiation.get(0)),    // before statement max_01_.., localX has level Public.
                                                                                  // after the statement, localX has the level of instantiation_0:
                                                                                  // instantiation_0 is just the element with key 0 in the instantiation's input type map.
                                                                                  // eg.: sum_D_D__D would have a map={1, DYN; 2, DYN}.
          (max_0_id_X_p0, localY) -> (Public[Level](), Public[Level]),
          (max_0_id_X_p0, localZ) -> (Public[Level](), Public[Level]),

          (max_1_id_Y_p1, localX) -> (instantiation.get(0), instantiation.get(0)),
          (max_1_id_Y_p1, localY) -> (Public[Level](), instantiation.get(1)),
          (max_1_id_Y_p1, localZ) -> (Public[Level](), Public[Level]()),

          (add_2_assign_Z_SUM_X_Y, localX) -> (instantiation.get(0), instantiation.get(0)),
          (add_2_assign_Z_SUM_X_Y, localY) -> (instantiation.get(1), instantiation.get(1)),
          (add_2_assign_Z_SUM_X_Y, localZ) -> (Public[Level], lub(instantiation.get(0),instantiation.get(1))
          )))
  }


  // define Program Counter at individual statements of ExampleTest2.sum_D_D__D.
  val sum_cxTyping: CxTyping[Level] = {
    import Code._
    makeCxTyping(instantiation => Map(
      max_0_id_X_p0 -> Public[Level](),
      max_1_id_Y_p1 -> Public[Level](),
      add_2_assign_Z_SUM_X_Y -> Public[Level](),
      add_3_return_Z -> Public[Level]()
    ))
  }

  // Returns "a mapping from methods to their instantiations".
  // Returns D_D__D, basically.
  val sum_methods_D_D__D: Methods[Level] =
    new Methods[Level] {
      override def getEffectType(m: SootMethod): Effect[Level] = makeEmptyEffect()

      override def getMonomorphicInstantiation(m: SootMethod): Instantiation[Level] =
        makeInstantiation(Map(0 -> Dynamic[Level](), 1 -> Dynamic[Level]()), Dynamic[Level]())

      override def getVarTyping(m: SootMethod): VarTyping[Level] = ???

      override def getCxTyping(m: SootMethod): CxTyping[Level] = ???
    }


  // Returns "a mapping from methods to their instantiations".
  // Returns P_D__D, basically.
  val sum_methods_P_D__D: Methods[Level] =
  new Methods[Level] {
    override def getEffectType(m: SootMethod): Effect[Level] = makeEmptyEffect()

    override def getMonomorphicInstantiation(m: SootMethod): Instantiation[Level] =
      makeInstantiation(Map(0 -> Public[Level](), 1 -> Dynamic[Level]()), Dynamic[Level]())

    override def getVarTyping(m: SootMethod): VarTyping[Level] = ???

    override def getCxTyping(m: SootMethod): CxTyping[Level] = ???
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
