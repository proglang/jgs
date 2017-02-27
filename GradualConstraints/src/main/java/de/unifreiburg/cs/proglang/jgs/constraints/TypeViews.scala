package de.unifreiburg.cs.proglang.jgs.constraints

import de.unifreiburg.cs.proglang.jgs.instrumentation.Type

/**
  * An Alg.DT for Types. When TypeViews from Java, either use (instanceof) or implement an instance of the visitor TypeViewSwitch
  */
object TypeViews {

  sealed trait TypeView[Level] extends Type[Level]
  case class Lit[Level](level : Level) extends TypeView[Level] {
    override def isStatic: Boolean = true

    override def isDynamic: Boolean = false

    override def isPublic: Boolean = false

    override def getLevel: Level = level
  }

  case class Dyn[Level]() extends TypeView[Level] {
    override def isStatic: Boolean = false

    override def isDynamic: Boolean = true

    override def isPublic: Boolean = false

    override def getLevel: Level = throw new IllegalArgumentException("dynamic type has not level")
  }

  case class Pub[Level]() extends TypeView[Level] {
    override def isStatic: Boolean = false

    override def isDynamic: Boolean = false

    override def isPublic: Boolean = true

    override def getLevel: Level = throw new IllegalArgumentException("public type has no level")
  }
}
