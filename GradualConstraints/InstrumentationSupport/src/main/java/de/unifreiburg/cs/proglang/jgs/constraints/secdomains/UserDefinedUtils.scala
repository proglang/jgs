package de.unifreiburg.cs.proglang.jgs.constraints.secdomains

import java.io.File

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.UserDefined._
import org.json4s._
import org.json4s.jackson.{Json, Json4sScalaModule}

import scala.io._
import scala.util.{Failure, Success, Try}

/**
  * Utilities for dealing with user defined security domains.
  */
object UserDefinedUtils {

  sealed case class Spec(levels : Set[Level], edges : Set[(Level, Level)])

  // TODO: there is a duplicate of this code in JgsCheck. They should be merged.
  val parseJson = {
    val yamlMapper = new YAMLMapper()
    yamlMapper.registerModule(new Json4sScalaModule)
    val json = Json(DefaultFormats, yamlMapper)
    (s : String) => json.parse(s)
  }

  def fromJSon(json : JValue) : Spec = {
    val levels : Seq[UserDefined.Level] = for {
      JObject(entries) <- json
      JField("levels", ls) <- entries
      JString(s) <- ls
    } yield UserDefined.Level(s)
    val edges : Seq[(UserDefined.Level, UserDefined.Level)] = for {
      JObject(entries) <- json
      JField("lt-edges", ls) <- entries
      JArray(List(JString(edge1), JString(edge2))) <- ls
    } yield UserDefined.Level(edge1) -> UserDefined.Level(edge2)

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


}
