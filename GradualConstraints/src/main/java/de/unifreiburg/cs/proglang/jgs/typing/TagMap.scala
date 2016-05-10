package de.unifreiburg.cs.proglang.jgs.typing

import de.unifreiburg.cs.proglang.jgs.constraints.Constraint
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVarTags.TypeVarTag

import scala.collection.JavaConverters._

/**
  * Created by fennell on 2/29/16.
  */
case class TagMap[Level](tags : Map[Constraint[Level], TypeVarTag]) {

  def add(c : Constraint[Level], tag : TypeVarTag) =
    TagMap(tags + Tuple2(c, tag))

  def addAll(otherTags : TagMap[Level]) =
    TagMap(this.tags ++ otherTags.tags)

  def getJavaMap : java.util.Map[Constraint[Level], TypeVarTag] =
    tags.asJava
}

object TagMap {
  def empty[Level]() : TagMap[Level] = new TagMap[Level](Map())
  def of[Level](c : Constraint[Level], tag: TypeVarTag) : TagMap[Level] = empty().add(c, tag)
  def of[Level](cs : java.util.Map[Constraint[Level], TypeVarTag]) : TagMap[Level] =
    new TagMap[Level](cs.asScala.toMap)

  class Builder[Level] {
    private var tagMap : TagMap[Level] = TagMap.empty()
    def add(c : Constraint[Level], tag : TypeVarTag) : Builder[Level] = {
      tagMap = tagMap.add(c, tag)
      return this
    }
    def build() : TagMap[Level] = tagMap
  }

  def builder[Level]() = new Builder[Level]

}
