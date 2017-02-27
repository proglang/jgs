package de.unifreiburg.cs.proglang.jgs.typing

import de.unifreiburg.cs.proglang.jgs.constraints.TypeVarTags.TypeVarTag
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView

/**
  * Created by fennell on 2/29/16.
  */
sealed trait ConflictCause[Level]
sealed case class FlowConflict[Level](val lowerType : TypeView[Level],
                                     val lowerTag : TypeVarTag,
                                     val upperLevel : TypeView[Level],
                                     val upperTag : TypeVarTag) extends ConflictCause[Level]
sealed case class CompatibilityConflict[Level](val type1 : TypeView[Level],
                                              val type2: TypeView[Level],
                                               val upperTag : TypeVarTag) extends ConflictCause[Level]
