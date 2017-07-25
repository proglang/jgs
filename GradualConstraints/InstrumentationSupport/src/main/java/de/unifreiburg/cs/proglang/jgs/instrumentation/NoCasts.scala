package de.unifreiburg.cs.proglang.jgs.instrumentation
import soot.jimple.StaticInvokeExpr

import scala.util.Try

case class NoCasts[Level]() extends ACasts[Level] {
  override def detectValueCastFromCall(e: StaticInvokeExpr): Try[Option[ACasts.ValueCast[Level]]] =
    Try(None)

  override def detectContextCastStartFromCall(e: StaticInvokeExpr): Try[Option[ACasts.CxCast[Level]]] =
    Try(None)

  override def detectContextCastEndFromCall(e: StaticInvokeExpr): Boolean =
    false
}
