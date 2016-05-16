package de.unifreiburg.cs.proglang.jgs.util

/**
  * Created by fennell on 4/6/16.
  */
object FunctionsForJava {

  def constantTrue[T] : Function1[T, Boolean] = Function.const(true)
}
