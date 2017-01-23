package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import java.io.File

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined.Edge
import org.json4s._
import org.json4s.jackson.{Json, Json4sScalaModule}

import scala.io._
import scala.util.{Failure, Success, Try}
import scala.collection.JavaConversions._



/**
  * Utilities for dealing with user defined security domains.
  */
object UserDefinedUtils {

  /**
    * Type for UserDefined security levels which are described by strings
    */
  @deprecated("No real advantage over plain strings as Levels")
  sealed case class Level(name : String) {
    override def toString = name
  }


  sealed case class Spec(levels : Set[String], edges : Set[Edge])
  sealed case class DomainParameters( levels : java.util.Set[String],
                                      lt : java.util.Set[Edge],
                                      lubMap : java.util.Map[Edge, String],
                                      glbMap : java.util.Map[Edge, String],
                                      topLevel : String,
                                      bottomLevel : String) {
    def writeToDir(dir : File): Unit = {
      throw new RuntimeException("NOT IMPLEMENTED")
    }
  }

  // TODO: there is a duplicate of this code in JgsCheck. They should be merged.
  val parseJson = {
    val yamlMapper = new YAMLMapper()
    yamlMapper.registerModule(new Json4sScalaModule)
    val json = Json(DefaultFormats, yamlMapper)
    (s : String) => json.parse(s)
  }

  def fromJSon(json : JValue) : Spec = {
    val levels : Seq[String] = for {
      JObject(entries) <- json
      JField("levels", ls) <- entries
      JString(s) <- ls
    } yield s
    val edges : Seq[Edge] = for {
      JObject(entries) <- json
      JField("lt-edges", ls) <- entries
      JArray(List(JString(edge1), JString(edge2))) <- ls
    } yield new Edge(edge1, edge2)

    Spec(levels.toSet, edges.toSet)
  }

  /**
    * Create a domainspec directly from a JSON or YAML file.
    */
  def fromJSon(jsonFile : File) : Spec = {
    val domainSpecJson = Try(parseJson(Source.fromFile(jsonFile).mkString)) match {
      case Failure(exc) =>
        throw new IllegalArgumentException(
          s"Error parsing security domain specification ${jsonFile}: ${exc.getMessage}"
        )
      case Success(json) => json
    }
    val domainSpec = UserDefinedUtils.fromJSon(domainSpecJson)
    // a little validation
    if (domainSpec.levels.isEmpty) {
      throw new IllegalArgumentException(
        s"No security levels found in ${jsonFile}"
      )
    }
    if (domainSpec.edges.isEmpty) {
      throw new IllegalArgumentException(
        s"No less-than edges found in ${jsonFile}"
      )
    }
    domainSpec
  }

  /**
    * Construct a UserDefined lattice of security levels.
    *
    * @param levels The set of levels
    * @param ltEgdes The essential less-than pairs that induce the ordering on `levels` by closing transitively
    *
    *  If `ltEdges` does not induce a lattice for `levels` an IllegalArgumentException is thrown.
    */
  def makeSecDomain(levels : Set[String], ltEgdes : Set[Edge]) : UserDefined = {
    val DomainParameters(_, lt, lubMap, glbMap, top, bottom) = secDomainParameters(levels, ltEgdes)
    new UserDefined(levels, lt, lubMap, glbMap, top, bottom)
  }

  def makeSecDomain(spec: UserDefinedUtils.Spec) : UserDefined = makeSecDomain(spec.levels, spec.edges)

  def secDomainParameters(levels : Set[String], ltEgdes : Set[Edge]) : DomainParameters = {
    val lt = closeTransitively(ltEgdes)
    checkIrreflexivity(lt)
    checkAsymmetry(lt)
    val gt = for (p <- lt) yield new Edge(p.right, p.left)
    val lubMap = bounds(levels, (l1, l2) => lt.contains(new Edge(l1, l2)))
    val glbMap = bounds(levels, (l1, l2) => gt.contains(new Edge(l1, l2)))
    val top = levels.find(l => levels.forall(otherL => otherL == l || lt(new Edge(otherL, l))))
    if (top.isEmpty) throw new IllegalArgumentException("Unable to find top element for lattice")
    val bottom = levels.find(l => levels.forall(otherL => otherL == l || lt(new Edge(l, otherL))))
    if (bottom.isEmpty) throw new IllegalArgumentException("Unable to find bottom element for lattice")

    DomainParameters(levels, lt, lubMap, glbMap, top.get, bottom.get)
  }

  private def bounds(levels : Set[String], lt : (String, String) => Boolean) : Map[Edge, String] = {
    val le = (l1 : String, l2 : String) => lt(l1, l2) || l1 == l2
    (for {l1 <- levels
          l2 <- levels }
      yield {
        val ubs = upperBounds(levels, le, l1, l2)
        // TODO: improve error messages... at least state that this is a "lattice-error" or something
        if (ubs.isEmpty) throw new IllegalArgumentException(s"No upper bound found for levels ${l1} and ${l2}")
        val lub = ubs.min(Ordering.fromLessThan(lt))
        new Edge(l1, l2) -> lub
      }).toMap
  }

  def upperBounds(levels : Set[String], le : (String, String) => Boolean, l1 : String, l2 : String) : Set[String] = {
    for (l <- levels; if le(l1, l) && le(l2, l)) yield l
  }

  def edgeAsTuple (e : Edge) : (String, String) = (e.left, e.right)


  def closeTransitively(edges : Set[Edge]) : Set[Edge] = {
    val next : Set[Edge] =
      edges ++
        (for { (lhs, mid1) <- edges.map(edgeAsTuple)
               (mid2, rhs) <- edges.map(edgeAsTuple)
               if mid1 == mid2 }
          yield new Edge(lhs, rhs))
    if (next == edges) next else closeTransitively(next)
  }

  def checkIrreflexivity(rel : Set[Edge]) : Unit = {
    for { (l1, l2) <- rel.map(edgeAsTuple); if l1 == l2} throw new IllegalArgumentException(s"Found reflexive pair in a strict ordering: ${l1}, ${l2}")
  }

  def checkAsymmetry(rel : Set[Edge]) : Unit = {
    for { p1 <- rel
          p2 <- rel
          if p1.left == p2.right && p1.right == p2.left && p1.left != p1.right} {
      throw new IllegalArgumentException(s"Found symmetric pair in an ordering: ${p1}, ${p2}")
    }

  }

}
