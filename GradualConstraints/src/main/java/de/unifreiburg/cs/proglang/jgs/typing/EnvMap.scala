package de.unifreiburg.cs.proglang.jgs.typing

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType
import de.unifreiburg.cs.proglang.jgs.constraints.{CTypes, Constraint}
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var
import de.unifreiburg.cs.proglang.jgs.typing.Environment.JoinResult
import de.unifreiburg.cs.proglang.jgs.util.Extra
import soot.jimple.Stmt

import scala.collection.JavaConversions._


/**
  * A map from statements to pre- and post- environments
  */
case class EnvMap private (private val m : Map[Stmt, (EnvMap.MultiEnv, EnvMap.MultiEnv)]) {

  def putNew(s : Stmt, pre : Environment, post : Environment) : EnvMap = {
    if (this.m.isDefinedAt(s)) {
      throw new IllegalArgumentException(s"Environment map already contains an entry for statement ${s}: ${this.m}")
    }
    new EnvMap(this.m + (s -> (EnvMap.multiEnvFromEnv(pre), EnvMap.multiEnvFromEnv(post))))
  }


  def join(other : EnvMap) : EnvMap = {
    def joinPrePost(p1 : (EnvMap.MultiEnv, EnvMap.MultiEnv),  p2 : (EnvMap.MultiEnv, EnvMap.MultiEnv)) : (EnvMap.MultiEnv, EnvMap.MultiEnv) =
      Extra.combinePairs(EnvMap.joinMultis, p1, p2)

    new EnvMap(Extra.joinWith(joinPrePost, this.m, other.m))
  }

  /*
  /**
    *
    * Combine two envmaps. The entries of statements that occur in both envmaps need to be unified. If these entries
    * have different mappings for common variables then they are unified using the translation of  `joinResult`
    * (effectively this maps them to a type variable that represents the lub).
    */
  def joinWith[Level](joinResult : JoinResult[Level], other : EnvMap) : EnvMap = {

    def joinEnv(env1 : Map[Var[_], TypeVar], env2 : Map[Var[_], TypeVar]) : Environment = {
      val combine = (v1 : TypeVar, v2 : TypeVar) => if (v1 != v2) { joinResult.translate(v1) } else {v1}
      Environments.fromMap(Extra.joinWith(combine, env1, env2))
      /*
      val commonKeys : Set[Var[_]] = env1.keySet & env2.keySet
      val joins : Map[Var[_], TypeVar] = (for {
        key <- commonKeys
        v1 = env1(key)
        v2 = env2(key)
        v = if (v1 != v2) { joinResult.translate(v1) } else { v1 }
        } yield key -> v).toMap

      Environments.fromMap(env1 ++ env2 ++ joins)
      */
    }

    type EnvPair = (Environment, Environment)
    def joinEnvs(envPair1 : EnvPair, envPair2 : EnvPair) : EnvPair =
      (joinEnv(envPair1._1.getMap.toMap, envPair2._1.getMap.toMap), joinEnv(envPair1._2.getMap.toMap, envPair2._2.getMap.toMap))
    val result : Map[Stmt, EnvPair] =
      Extra.joinWith[Stmt, EnvPair](joinEnvs, this.m, other.m)
    /*
    val commonDomain = for {
      key <- this.m.keySet & other.m.keySet
      thisVal = this.m.get(key).get
      otherVal = other.m.get(key).get
      preEnv = joinEnv(thisVal._1.getMap.toMap, otherVal._1.getMap.toMap)
      postEnv = joinEnv(thisVal._2.getMap.toMap, otherVal._2.getMap.toMap)
    } // TODO I need to map the individual vars to the joined ones... requires a map to see what was joined
      yield key ->(preEnv, postEnv)


    new EnvMap(
      this.m.filterKeys(k => !other.m.isDefinedAt(k)) ++
      other.m.filterKeys(k => !this.m.isDefinedAt(k)) ++
      commonDomain
    )
    */
    new EnvMap(result)
  }
  */

  def getPre(s : Stmt) : Option[EnvMap.MultiEnv] = m.get(s).map(_._1)
  def getPost(s : Stmt) : Option[EnvMap.MultiEnv] = m.get(s).map(_._2)


  def preAsConstraints[Level](mkConstraint : CType[Level] => Constraint[Level], s : Stmt, x : Var[_]) =
    EnvMap.asConstraints(mkConstraint, this.getPre(s).get, x)

  def postAsConstraints[Level](mkConstraint : CType[Level] => Constraint[Level], s : Stmt, x : Var[_]) =
    EnvMap.asConstraints(mkConstraint, this.getPost(s).get, x)

}

object EnvMap {
  type MultiEnv = Map[Var[_], Set[TypeVar]]

  def multiEnvFromEnv(env : Environment) : MultiEnv =
    (for ((v , tv) <- env.getMap) yield v -> Set(tv)).toMap

  def joinMultis(m1 : MultiEnv, m2 : MultiEnv) = Extra.joinWith((v1 : Set[TypeVar], v2 : Set[TypeVar]) => v1 ++ v2, m1, m2)

  def apply(kv : (Stmt, (Environment, Environment))*) = {
    val m = (for ((k, (v1, v2)) <- kv) yield k ->  (multiEnvFromEnv(v1), multiEnvFromEnv(v2))).toMap
    new EnvMap(m)
  }

  def asConstraints[Level](mkConstraint : CType[Level] => Constraint[Level], env : MultiEnv, v : Var[_]) :
    java.util.Set[Constraint[Level]] =
    for (tv <- env(v)) yield mkConstraint(CTypes.variable[Level](tv))

}
