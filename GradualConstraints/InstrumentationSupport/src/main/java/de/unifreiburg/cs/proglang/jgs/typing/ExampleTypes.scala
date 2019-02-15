package de.unifreiburg.cs.proglang.jgs.typing

import de.unifreiburg.cs.proglang.jgs.instrumentation.Type

/**
  * A simple version of Types that are just static/dynamic/public
  */
object ExampleTypes {

  sealed abstract class ExampleType[Level]() extends Type[Level] {
    override def isStatic: Boolean = false

    override def isDynamic: Boolean = false

    override def isPublic: Boolean = false

    override def getLevel: Level = throw new RuntimeException("NOT IMPLEMENTED")
  }

  sealed case class Static[Level]() extends ExampleType[Level] {
    override def isStatic : Boolean = true
  }

  sealed case class Dynamic[Level]() extends ExampleType[Level] {
    override def isDynamic : Boolean = true
  }

  sealed case class Public[Level]() extends ExampleType[Level] {
    override def isPublic : Boolean = true
  }

  def lub[Level](t1 : Type[Level], t2 : Type[Level]) : Type[Level] = {
    lub(t1.asInstanceOf[ExampleType[Level]], t2.asInstanceOf[ExampleType[Level]])
  }

  def lub[Level](t1 : ExampleType[Level], t2 : ExampleType[Level]) : Type[Level] = {
    if (t1 == t2) {
      t1
    } else if (t1 == Public()) {
      t2
    } else if (t2 == Public()) {
      t1
    } else {
      throw new RuntimeException(s"Types cannot be joined: ${t1}, ${t2}")
    }
  }

}
