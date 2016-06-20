package de.unifreiburg.cs.proglang.jgs

import java.util.logging.Logger

import scala.util.{Failure, Success, Try}

/**
  * Misc. Utilities
  */
object Util {

  /************************
   * Error handling
   ***********************/

  def asTry[A](exc : RuntimeException, maybeVal : Option[A]) : Try[A] =
    maybeVal match {
      case Some(x) => Success(x)
      case None => Failure(exc)
    }

  def asTry[A](msg : String, maybeVal : Option[A]) : Try[A] = asTry(new RuntimeException(msg), maybeVal)


  def skipAndReportFailure[A](log : Logger, msg : String, maybeValue : Try[A]) : Option[A] =
    maybeValue match {
      case Failure(exception) => {
        log.warning(s"${msg} ${exception.getMessage}")
        None
      }
      case Success(value) => Some(value)
    }



}
