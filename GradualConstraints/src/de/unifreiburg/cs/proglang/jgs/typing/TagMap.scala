package de.unifreiburg.cs.proglang.jgs.typing

import de.unifreiburg.cs.proglang.jgs.constraints.Constraint
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVarTags.TypeVarTag

/**
  * Created by fennell on 2/29/16.
  */
case class TagMap[Level](tags : Map[Constraint[Level], TypeVarTag]) {

  def add(c : Constraint[Level], tag : TypeVarTag) =
    TagMap(tags + Tuple2(c, tag))

  def addAll(otherTags : TagMap[Level]) =
    TagMap(this.tags ++ otherTags.tags)
}

object TagMap {
  def empty[Level]() : TagMap[Level] = new TagMap[Level](Map())
  def of[Level](c : Constraint[Level], tag: TypeVarTag) : TagMap[Level] = empty().add(c, tag)
}
