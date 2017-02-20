package utils.staticResults

import de.unifreiburg.cs.proglang.jgs.examples.ExampleTypes.Public

import scala.collection.immutable.HashMap

/**
  * Create a custom typing for a given
  */
object CustomTyping {

  val custom1  = scala.collection.immutable.Map(
    ("method", "var") -> (true, true),
    ("j", "j") -> (false, true)
  )


  // Helper
  def getBefore(map :Map[(String, String), (Boolean, Boolean)], method: String, local: String) = {
    map.getOrElse((method, local), (false, false))._1;
  }

  def getAfter(map :Map[(String, String), (Boolean, Boolean)], method: String, local: String) = {
    map.getOrElse((method, local), (false, false))._2;
  }
}



