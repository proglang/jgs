package de.unifreiburg.cs.proglang.jgs.typing

import com.sun.istack.internal.NotNull
import de.unifreiburg.cs.proglang.jgs.constraints._
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var
import de.unifreiburg.cs.proglang.jgs.signatures.Effects
import de.unifreiburg.cs.proglang.jgs.signatures.Effects.emptyEffect

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
    return new BodyTypingResult[Level](csets.empty, emptyEffect[Level], Environments.makeEmpty, TagMap.empty[Level])
  }

  def fromEnv[Level](csets: ConstraintSetFactory[Level], env: Environment): BodyTypingResult[Level] = {
    return new BodyTypingResult[Level](csets.empty, emptyEffect[Level], env, TagMap.empty[Level])
  }

  def join[Level](r1: BodyTypingResult[Level], r2: BodyTypingResult[Level], csets: ConstraintSetFactory[Level], tvars: TypeVars, condition: String): BodyTypingResult[Level] = {
    val envJoin: Environment.JoinResult[Level] = Environment.join(tvars, r1.getFinalEnv, r2.getFinalEnv)
    val csList: List[Constraint[Level]] = envJoin.constraints.toList
    val joinTags: TagMap.Builder[Level] = TagMap.builder[Level]
    csList.foreach(c => joinTags.add(c, new TypeVarTags.Join(condition)))
    val cs: ConstraintSet[Level] = r1.constraints.add(r2.constraints).add(csets.fromCollection(csList))
    return new BodyTypingResult[Level](cs, r1.effects.add(r2.effects), envJoin.env, r1.tags.addAll(r2.tags).addAll(joinTags.build))
  }

  def addConstraints[Level](r: BodyTypingResult[Level], constraints: ConstraintSet[Level]): BodyTypingResult[Level] = {
    return new BodyTypingResult[Level](r.constraints.add(constraints), r.effects, r.env, r.tags)
  }

  def addEffects[Level](r: BodyTypingResult[Level], effects: Effects[Level]): BodyTypingResult[Level] = {
    return new BodyTypingResult[Level](r.constraints, r.effects.add(effects), r.env, r.tags)
  }
}

case class BodyTypingResult[LevelT](
  val constraints: ConstraintSet[LevelT],
  val effects: Effects[LevelT],
  val env: Environment,
  val tags: TagMap[LevelT]
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