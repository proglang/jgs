package utils.staticResults

import de.unifreiburg.cs.proglang.jgs.examples.ExampleTypes.Public

import scala.collection.immutable.HashMap

/**
  * Create a custom typing for a given.
  * Tuple in map is of type (bool, bool), representing (Dynamic, Dynamic)
  */
object CustomTyping {


  // locals: r0, b0, b1, i2, $r1
  // custom test-typing for testclasses.LowPlusPublic: everything (important) is dynamic
  val LowPlusPublic_allDynamic  = scala.collection.immutable.Map(
    ("r0 := @parameter0: java.lang.String[]", "b0") -> (true, true),
    ("b0 = 9", "b0") -> (true, true),
    ("b1 = 10", "b0") -> (true, true),
    ("i2 = b0 + b1", "b0") -> (true, true),
    ("$r1 = <java.lang.System: java.io.PrintStream out>", "b0") -> (true, true),
    ("virtualinvoke $r1.<java.io.PrintStream: void println(int)>(i2)", "b0") -> (true, true),

    ("r0 := @parameter0: java.lang.String[]", "b1") -> (true, true),
    ("b0 = 9", "b1") -> (true, true),
    ("b1 = 10", "b1") -> (true, true),
    ("i2 = b0 + b1", "b1") -> (true, true),
    ("$r1 = <java.lang.System: java.io.PrintStream out>", "b1") -> (true, true),
    ("virtualinvoke $r1.<java.io.PrintStream: void println(int)>(i2)", "b1") -> (true, true),


    ("r0 := @parameter0: java.lang.String[]", "i2") -> (true, true),
    ("b0 = 9", "b1") -> (true, true),
    ("b1 = 10", "b1") -> (true, true),
    ("i2 = b0 + b1", "b1") -> (true, true),
    ("$r1 = <java.lang.System: java.io.PrintStream out>", "b1") -> (true, true),
    ("virtualinvoke $r1.<java.io.PrintStream: void println(int)>(i2)", "b1") -> (true, true)

  )


  // Helper

  /**
    * Return true if type is dynamic
    */
  def getBefore(map :Map[(String, String), (Boolean, Boolean)], method: String, local: String) = {
    map.getOrElse((method, local), (false, false))._1;
  }

  /**
    * Return true if type is dynamic
    */
  def getAfter(map :Map[(String, String), (Boolean, Boolean)], method: String, local: String) = {
    map.getOrElse((method, local), (false, false))._2;
  }
}



