package de.unifreiburg.cs.proglang.jgs.constraints

/**
  * An Alg.DT for Types. When TypeViews from Java, either use (instanceof) or implement an instance of the visitor TypeViewSwitch
  */
object TypeViews {

  sealed trait TypeView[Level]
  case class Lit[Level](val level : Level) extends TypeView[Level]
  case class Dyn[Level]() extends TypeView[Level]
  case class Pub[Level]() extends TypeView[Level]


  /**
    * Apply a TypeViewSwitch to a TypeView, returning the result
    */
  def applySwitch[Level, T](tv : TypeView[Level], sw : TypeViewSwitch[Level, T]) : T =
    tv match {
      case Lit(level) => sw.caseLit(level)
      case Dyn() => sw.caseDyn()
      case Pub() => sw.casePub()
    }

}
