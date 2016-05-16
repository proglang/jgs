package de.unifreiburg.cs.proglang.jgs.typing

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVarTags.TypeVarTag

/**
  * Created by fennell on 2/29/16.
  */
sealed trait ConflictCause[Level]
sealed case class FlowConflict[Level](val lowerType : Type[Level],
                                     val lowerTag : TypeVarTag,
                                     val upperLevel : Type[Level],
                                     val upperTag : TypeVarTag) extends ConflictCause[Level]
sealed case class CompatibilityConflict[Level](val type1 : Type[Level],
                                              val type2: Type[Level],
                                               val upperTag : TypeVarTag) extends ConflictCause[Level]
