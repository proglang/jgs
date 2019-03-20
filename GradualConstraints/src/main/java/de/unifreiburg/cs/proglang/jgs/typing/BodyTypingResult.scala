package de.unifreiburg.cs.proglang.jgs.typing

import com.sun.istack.internal.NotNull
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar
import de.unifreiburg.cs.proglang.jgs.constraints._
import de.unifreiburg.cs.proglang.jgs.instrumentation.Var
import de.unifreiburg.cs.proglang.jgs.signatures.Effects
import de.unifreiburg.cs.proglang.jgs.signatures.Effects.emptyEffect
import soot.jimple.Stmt

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
  * The result of typing a statement: a set of constraints, an environment that
  * holds _after_ execution of the statement, and an effect.
  *
  * @author fennell
  */
object BodyTypingResult {
  def trivialCase[Level](csets: ConstraintSetFactory[Level]): BodyTypingResult[Level] = {
    trivialCase(csets, EnvMap())
  }

  def trivialCase[Level](csets: ConstraintSetFactory[Level], envMap : EnvMap): BodyTypingResult[Level] = {
    new BodyTypingResult[Level](csets.empty, emptyEffect[Level], Environments.makeEmpty, TagMap.empty[Level], envMap)
  }

  // Make a result with the env map for a single statement
  def makeResult[LevelT](s : Stmt,
                         constraints: ConstraintSet[LevelT],
                         pcs : Set[TypeVar],
                         initialEnv : Environment, finalEnv: Environment,
                         effects: Effects[LevelT],
                         tags: TagMap[LevelT]): BodyTypingResult[LevelT] = {
    new BodyTypingResult[LevelT](constraints, effects, finalEnv, tags, EnvMap(s -> (pcs, (initialEnv, finalEnv))))
  }

  // create a result from a given pre-statement environment with no constraints.
  def trivialWithEnv[Level](s : Stmt, csets: ConstraintSetFactory[Level], pcs : Set[TypeVar], env: Environment): BodyTypingResult[Level] = {
    makeResult(s, csets.empty, pcs, env, env, emptyEffect[Level], TagMap.empty[Level])
  }

  // initial results at the beginning of type-checking a method
  def initial[Level](csets: ConstraintSetFactory[Level], env: Environment) : BodyTypingResult[Level] =
    new BodyTypingResult[Level](csets.empty(), emptyEffect(), env, TagMap.empty(), EnvMap.empty())

  def join[Level](r1: BodyTypingResult[Level], r2: BodyTypingResult[Level],
                  csets: ConstraintSetFactory[Level], tvars: TypeVars, condition: String): BodyTypingResult[Level] = {
    val envJoin: Environment.JoinResult[Level] = Environment.join(tvars, r1.getFinalEnv, r2.getFinalEnv)
    val csList: List[Constraint[Level]] = envJoin.constraints.toList
    val joinTags: TagMap.Builder[Level] = TagMap.builder[Level]
    csList.foreach(c => joinTags.add(c, new TypeVarTags.Join(condition)))
    val cs: ConstraintSet[Level] = r1.constraints.add(r2.constraints).add(csets.fromCollection(csList))
    return new BodyTypingResult[Level](cs, r1.effects.add(r2.effects), envJoin.env, r1.tags.addAll(r2.tags).addAll(joinTags.build), r1.envMap.join(r2.envMap))
  }

  def addConstraints[Level](r: BodyTypingResult[Level], constraints: ConstraintSet[Level]): BodyTypingResult[Level] = {
    return r.copy(constraints = r.constraints.add(constraints))
  }

  def addEffects[Level](r: BodyTypingResult[Level], effects: Effects[Level]): BodyTypingResult[Level] = {
    return r.copy(effects = r.effects.add(effects))
  }

  def replaceEffects[Level](r: BodyTypingResult[Level], effects: Effects[Level]): BodyTypingResult[Level] = {
    return r.copy(effects = effects)
  }

}

case class BodyTypingResult[LevelT](
  val constraints: ConstraintSet[LevelT],
  val effects: Effects[LevelT],
  val env: Environment,
  // for error messages
  val tags: TagMap[LevelT],
  // maps statements to pre- and post environments; for instrumentation
  val envMap: EnvMap
) {

  def getConstraints: ConstraintSet[LevelT] = {
    return this.constraints
  }

  def getFinalEnv: Environment = {
    return env
  }

  def getEffects: Effects[LevelT] = {
    return effects
  }

  def getTags: TagMap[LevelT] = {
    return tags
  }

  def finalTypeVariableOf(local: Var[_]): TypeVars.TypeVar = {
    return this.env.get(local)
  }

}