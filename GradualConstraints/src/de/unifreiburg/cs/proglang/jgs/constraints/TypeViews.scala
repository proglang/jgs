package de.unifreiburg.cs.proglang.jgs.constraints

/**
  * Created by fennell on 2/29/16.
  */
object TypeViews {

  sealed trait TypeView[Level]
  case class Lit[Level](level : Level) extends TypeView[Level]
  case class Dyn[Level]() extends TypeView[Level]
  case class Pub[Level]() extends TypeView[Level]

}
