package de.unifreiburg.cs.proglang.jgs.sparta

/**
  * A model for SPARTA type annotations. http://types.cs.washington.edu/sparta/current/sparta-manual.html#flow-checker
  */
object Annotations {

  // SPARTA type annotations consist of source permission and sink permissions
  //   - source permissions specify what may flow into a variable/field
  //   - sink permissions specify where the information in a variable/field may flow
  sealed case class Type(source : Permissions, sink : Permissions)

  // Permissions are either ANY or a set of explicit PermissionS
  sealed trait Permissions
  case object Any extends Permissions
  sealed case class PermSet(perms : Set[Permission])

  // An explicit permission consists of a name and a list of parameters
  sealed case class Permission(name : String , params : Seq[String])


  // SPARTA signatures specify potentially polymorphic return- and parameter-types
  sealed case class Signature(returnType : SigType,
                              paramTypes : Seq[SigType])


  // The types for signatures, analogous to `Type`
  sealed case class SigType(source : SigPermissions, sink : SigPermissions)

  // Permissions in signatures can be polymorphic, polymorphic wrt the receiver, or regular permissions
  sealed trait SigPermissions
  case object Poly extends SigPermissions
  case object PolyR extends SigPermissions
  sealed case class Mono(permissions : Permissions) extends SigPermissions

  // Policies are a set of edges between Permissions. They are interpreted intransitively.
  class Policy(edges : Set[(Permission, Permission)]) {

    def allowsFlow(p1 : Permission, p2 : Permission) = edges.contains(p1 -> p2)

  }

}
