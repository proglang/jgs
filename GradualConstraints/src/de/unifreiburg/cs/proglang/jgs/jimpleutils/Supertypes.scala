package de.unifreiburg.cs.proglang.jgs.jimpleutils

import soot.RefType
import soot.SootClass
import soot.SootMethod
import soot.Type

import scala.collection.JavaConversions._

object Supertypes {
  /**
    * @return The supertypes of { @code sootClass}. Traverses the supertype DAG in preorder. The stream may contain
    *                                   duplicates.
    */
  def enumerate(sootClass: SootClass): Iterator[SootClass] = {
    val immediateSuperTypes: List[SootClass] = (if (sootClass.hasSuperclass) List(sootClass.getSuperclass) else List()) ++ sootClass.getInterfaces
    immediateSuperTypes.iterator ++ immediateSuperTypes.flatMap(Supertypes.enumerate)
  }

  /**
    * @return The methods that m1 overrides.
    */
  def findOverridden(m1: SootMethod): Iterator[SootMethod] = {
    val c1: SootClass = m1.getDeclaringClass

    //Supertypes.enumerate(c1).flatMap(c -> c.getMethods().stream()).filter(m -> overrides(m1, m))
    for (c <- Supertypes.enumerate(c1);
         m <- c.getMethods if overrides(m1, m))
      yield m
  }

  /**
    * @return true if { @code m1} overrides { @code m2}.
    */
  def overrides(m1: SootMethod, m2: SootMethod): Boolean = {
    if (m1.isStatic || m2.isStatic) {
      return false
    }
    if ((m1.getName == "<init>") || (m2.getName == "<init>")) {
      return false
    }
    if ((m1.getName == m2.getName) && subTypeOf(m1.getDeclaringClass, m2.getDeclaringClass) && (m1.getParameterTypes == m2.getParameterTypes)) {
      val rt1: Type = m1.getReturnType
      val rt2: Type = m2.getReturnType
      if (rt1.isInstanceOf[RefType] && rt2.isInstanceOf[RefType]) {
        return subTypeOf((rt1.asInstanceOf[RefType]).getSootClass, (rt2.asInstanceOf[RefType]).getSootClass)
      }
      else {
        return rt1 == rt2
      }
    }
    else {
      return false
    }
  }

  /**
    * @return true if { @code c1} is a subtype of { @code c2}
    */
  def subTypeOf(c1: SootClass, c2: SootClass): Boolean = Supertypes.enumerate(c1).contains(c2)
}