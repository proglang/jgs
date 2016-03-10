package de.unifreiburg.cs.proglang.jgs.util

import java.util.Optional

/**
  * Created by fennell on 3/9/16.
  */
object Interop {

  def asJavaOptional[T](o: Option[T]): Optional[T] =
    o.map(Optional.of(_)).getOrElse(Optional.empty())

  def asScalaOption[T](o: Optional[T]): Option[T] =
    o.map[Option[T]](new java.util.function.Function[T, Option[T]] {
      override def apply(t: T): Option[T] = Some(t)
    }).orElse(None)

}
