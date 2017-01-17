package de.unifreiburg.cs.proglang.jgs.examples

import de.unifreiburg.cs.proglang.jgs.examples.ExampleTypes.{Dynamic, Public, Static, lub}
import de.unifreiburg.cs.proglang.jgs.instrumentation._
import soot.Local
import soot.SootMethod
import soot.jimple.Stmt

/**
  * Created by Nicolas Müller on 17.01.17.
  */
class AnalysisResults2[Level] {

  //
  val max_varTyping: VarTyping[Level] = {
    import Code._
    makeVarTyping(
      (instantiation : Instantiation[Level]) =>
        Map.apply[(Stmt, Local), (Type[Level], Type[Level])](
          (max_01_id_X_p0, localX) -> (Public[Level](), instantiation.get(0)),
          (max_01_id_X_p0, localY) -> (Public[Level](), Public[Level]),
          (max_01_id_X_p0, localZ) -> (Public[Level](), Public[Level])
          ))
  }


  // define Program Counter at individual statements of ExampleTest2.sum_D_D__D.
  val sum_cxTyping: CxTyping[Level] = {
    import Code._
    makeCxTyping(instantiation => Map (
      add_2_Z__ADD_X_Y -> Public[Level]()
    ))
  }

  // ====== Utilities ======
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
}
