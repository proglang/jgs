package de.unifreiburg.cs.proglang.jgs.jimpleutils

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars
import de.unifreiburg.cs.proglang.jgs.signatures.Signature
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol._
import de.unifreiburg.cs.proglang.jgs.signatures.parse.ConstraintParser
import soot.SootMethod
import soot.tagkit._

import scala.collection.JavaConverters._

/**
  * Created by fennell on 1/22/16.
  */
object Methods {
  def parameters[Level](method: SootMethod): Iterator[Param[Level]] = {
    (for (pos <- 0 until method.getParameterCount) yield param[Level](pos)).iterator
  }

  def symbolMapForMethod[Level](tvars: TypeVars, method: SootMethod): java.util.Map[Param[Level], TypeVars.TypeVar] = {
    Methods.parameters(method).map((p : Param[Level]) => p -> tvars.param(Var.fromParam(p))).toMap.asJava
  }

  def extractAnntotation[T](annotationType: String, extract: AnnotationElem => T, tags: Iterator[Tag]): Iterator[T] = {
      for (t <- tags
           if t.isInstanceOf[VisibilityAnnotationTag];
           a <- t.asInstanceOf[VisibilityAnnotationTag].getAnnotations.asScala
           if a.getType == annotationType;
           i <- 0 until a.getNumElems
      ) yield extract(a.getElemAt(i))
  }

  def extractStringAnnotation(annotationType: String, tags: Iterator[Tag]): java.util.List[String] = {
    val extract = (e : AnnotationElem) => {
      val wrongType : IllegalArgumentException =
        new IllegalArgumentException(
          "Expected a string element but got: "
            + e.toString());
      if (!(e.isInstanceOf[AnnotationStringElem])) {
        throw wrongType;
      }
      (e.asInstanceOf[(AnnotationStringElem)]).getValue();
    }
    extractAnntotation(annotationType, extract, tags).toList.asJava
  }

  /**
    * Filter a stream of tags for Annotations and return the list of Strings
    * contained in them.
    *
    * @param annotationType The type of the Annotation class to be extracted, as a
    *             mangled class name. Should contain a "value" field containing
    *             a String array.
    */
  def extractStringArrayAnnotation(annotationType: String, tags: Iterator[Tag]): java.util.List[java.util.List[String]] = {
    val extract = (e : AnnotationElem) => {
      val wrongType : IllegalArgumentException =
        new IllegalArgumentException(
          "Expected a string array Element but got: "
            + e.toString)
      if (!e.isInstanceOf[AnnotationArrayElem]) throw wrongType
      val values =
        e.asInstanceOf[AnnotationArrayElem].getValues.asScala.map(
          s => {
            if (!s.isInstanceOf[AnnotationStringElem]) throw wrongType
            s.asInstanceOf[(AnnotationStringElem)].getValue
          }
        ).toList
      values.asJava
    }
    return extractAnntotation(annotationType, extract, tags).toList.asJava
  }

  def extractSignatureFromTags[Level](parser: ConstraintParser[Level], tags: java.util.List[Tag]): Signature[Level] = {
    val atags = tags.asScala.filter(t => t.isInstanceOf[AnnotationTag]).map((t : Tag) => t.asInstanceOf[AnnotationTag])
    throw new RuntimeException("Not implemented")
  }
}