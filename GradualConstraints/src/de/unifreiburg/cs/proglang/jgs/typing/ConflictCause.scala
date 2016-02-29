package de.unifreiburg.cs.proglang.jgs.typing

import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVarTags.TypeVarTag

/**
  * Created by fennell on 2/29/16.
  */
sealed case class ConflictCause[Level](val lowerType : Type[Level],
                                       val lowerTag : TypeVarTag,
                                       val upperLevel : Type[Level],
                                       val upperTag : TypeVarTag)
