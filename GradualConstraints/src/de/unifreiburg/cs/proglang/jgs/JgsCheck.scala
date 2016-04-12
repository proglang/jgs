package de.unifreiburg.cs.proglang.jgs

import java.io.File
import java.util.logging.Logger

import de.unifreiburg.cs.proglang.jgs.cli.Format
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh
import de.unifreiburg.cs.proglang.jgs.constraints.{TypeDomain, TypeVars, _}
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods.extractStringArrayAnnotation
import de.unifreiburg.cs.proglang.jgs.jimpleutils.{Casts, _}
import de.unifreiburg.cs.proglang.jgs.signatures.Effects.{emptyEffect, makeEffects}
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures._
import de.unifreiburg.cs.proglang.jgs.signatures._
import de.unifreiburg.cs.proglang.jgs.signatures.parse.ConstraintParser
import de.unifreiburg.cs.proglang.jgs.typing.{ClassHierarchyTyping, MethodTyping, TypingAssertionFailure, TypingException}
import org.json4s._
import org.json4s.native.JsonMethods.{parse => parseJson}
import scopt.OptionParser
import soot.options.Options
import soot.{Scene, SootClass, SootField, SootMethod}

import scala.collection.JavaConversions._
import scala.io.Source
import scala.util.control.Exception.catching
import scala.util.{Failure, Success, Try}

object JgsCheck {


  sealed trait Verbosity

  case object Warn extends Verbosity

  case object Debug extends Verbosity

  sealed trait SecDomainChoice

  case object LowHigh extends SecDomainChoice

  case class UserDomain(val secDomainClass: File) extends SecDomainChoice


  sealed case class Opt
  (val sourcetree: Option[File],
   val support: File,
   val runtime: File,
   val externalAnnotations: File,
   val castMethods: File,
   // TODO: figure out how to
   val secdomainChoice: SecDomainChoice,
   val verbosity: Verbosity
  )

  val log: Logger = Logger.getLogger("de.unifreiburg.cs.proglang.jgs.typing.log")
  val debugLog: Logger = Logger.getLogger("de.unifreiburg.cs.proglang.jgs.typing.debug")

  def main(args: Array[String]): Unit = {
    val defOpt = Opt(
      sourcetree = Some(new File("JGSTestclasses/src")),
      support = new File("JGSSupport/bin"),
      runtime = new File("JGSSupport/rt.jar"),
      externalAnnotations = new File("JGSSupport/external-annotations.json"),
      castMethods = new File("JGSSupport/cast-methods.json"),
      secdomainChoice = LowHigh,
      verbosity = Warn
    )

    val addDefault = (s: String, get: Opt => Object) => s + s" (default: ${get(defOpt).toString})"

    val parser = new OptionParser[Opt]("jgs-check") {
      head("jgs-check", "0.1")
      opt[File]("support-classes")
        .action { (x, c) => c.copy(support = x) }
        .text(addDefault("directory containing support classes", _.support))
      opt[File]("runtime")
        .action { (x, c) => c.copy(runtime = x) }
        .text(addDefault("path to a Java 7 rt.jar runtime library", _.runtime))
      opt[Unit]("verbose")
        .action { (_, c) => c.copy(verbosity = Debug) }
      opt[File]("external-annotations")
        .action { (x, c) => c.copy(externalAnnotations = x) }
        .text(addDefault("json file with annotations for external methods and fields not part of the sourcetree", _.externalAnnotations))
      opt[File]("cast-methods")
        .action { (x, c) => c.copy(castMethods = x) }
        .text(addDefault("json file specificying the cast methods", _.castMethods))
      arg[File]("SOURCETREE")
        .action { (x, c) => c.copy(sourcetree = Some(x)) }
        .text("directory of sources subject to typechecking")
        .required()
    }

    val optParse = parser.parse(args, defOpt)
    optParse match {
      case None => System.exit(-1)
      case Some(opt) =>

        val sourcetree: File = opt.sourcetree.getOrElse(throw new RuntimeException("Unable to get sourcetree from command line args (should be caught by command line option checking)"))

        log.setLevel(if (opt.verbosity == Debug) java.util.logging.Level.INFO else java.util.logging.Level.WARNING)

        val o: Options = Options.v()
        o.set_soot_classpath(raw"${opt.support.getPath}:${sourcetree.getPath}:${opt.runtime.getPath}")
        o.set_process_dir(List(sourcetree.getPath))

        val s: Scene = Scene.v()
        log.info(s"Soot classpath: ${s.getSootClassPath}")

        s.loadNecessaryClasses()
        log.info(f"Number of classes found: ${s.getClasses.size()}%d")
        // Abort when no classes where found
        if (s.getClasses.isEmpty) {
          System.err.println("WARNING: did not find any classes to type-check. Aborting \n" +
            "Soot'classpath is: " + s.getSootClassPath + "\n" +
            "Working Directory is " + System.getProperty("user.dir"))
          System.exit(-1)
        }

        val classes: List[SootClass] = s.getApplicationClasses.toList


        /****************************
        * Configuration of security lattice
         ****************************/
        val cfg: Config[_] = opt.secdomainChoice match {
          case LowHigh => {
            val secdomain = new LowHigh()
            val types = new TypeDomain(secdomain)
            val csets = new NaiveConstraintsFactory(types)
            new Config(types, csets, opt)
          }
          case UserDomain(secDomainClass) =>
            throw new IllegalArgumentException("User defined security domains are not implemented yet.")
        }
        import cfg._
        typeCheck(s, classes)

    }
  }


  class Config[Level](val types: TypeDomain[Level],
                      val csets: ConstraintSetFactory[Level],
                      val opt: Opt) {

    val cstrs = new Constraints(types)

    def typeCheck(s: Scene, classes: List[SootClass]): Unit = {
      val fieldMap: Map[SootField, Type[Level]] =
        (for (c <- classes;
              f <- c.getFields)
          yield {
            val secAnnotations: List[String] =
              Methods.extractStringAnnotation("Lde/unifreiburg/cs/proglang/jgs/support/Sec;", f.getTags.iterator).toList
            val mt: Option[TypeDomain.Type[Level]] =
              for (typeStr <- getSingleAnnotation(secAnnotations, new IllegalArgumentException("Found more than one security level on " + f.getName()));
                   t <- types.typeParser().parse(typeStr))
                yield t
            val t: TypeDomain.Type[Level] = mt.getOrElse(types.pub())
            (f, t)
          }).toMap
      log.info("Field types: " + fieldMap.toString())

      val signatureMap: Map[SootMethod, Signature[Level]] =
        (for (c <- classes;
              m <- c.getMethods)
          yield {
            val constraintStrings: List[String] =
              getSingleAnnotation(
                extractStringArrayAnnotation("Lde/unifreiburg/cs/proglang/jgs/support/Constraints;", m.getTags().iterator).toList.map(_.toList),
                new IllegalArgumentException("Found more than one constraint annotation on " + m.getName())
              ).getOrElse(Nil)
            val effectStrings: List[String] = getSingleAnnotation(
              extractStringArrayAnnotation("Lde/unifreiburg/cs/proglang/jgs/support/Effects;", m.getTags().iterator()).toList.map(_.toList),
              new IllegalArgumentException("Found more than one effect annotation on " + m.getName())).getOrElse(Nil)
            val sig = makeSignature[Level](m.getParameterCount, parseConstraints(constraintStrings), makeEffects(parseEffects(effectStrings)))
            (m, sig)
          }).toMap
      log.info("Signature map: " + signatureMap.toString())

      // add signatures for constructors
      val specialSignatures: Map[SootMethod, Signature[Level]] =
        Map(
          // Object.<init> is harmless
          s.getSootClass("java.lang.Object").getMethodByName("<init>") -> makeSignature[Level](0, List(), emptyEffect[Level])
        )

      /** ****************************
        * Read configured signatures from file
        * *****************************/
      val annotationsJsonStr = Source.fromFile(opt.externalAnnotations).mkString
      val annotationsJson = Try(parseJson(annotationsJsonStr)) match {
        case Failure(exception) =>
          throw new IllegalArgumentException(s"Error parsing external annotations (${opt.castMethods}): ${exception.getMessage}")
        case Success(value) => value
      }

      val configuredSignatures: List[(SootMethod, Signature[Level])] =
        for {
          JObject(entries) <- annotationsJson \\ "methods"
          JField(name, JObject(JField("constraints", cs) :: JField("effects", effs) :: Nil)) <- entries
          m <- Try(s.getMethod(name)).map(m => List(m)).getOrElse(List())
        } yield {
          // TODO: emit a warning when a method cannot be found
//          val m : SootMethod = Try(s.getMethod(name)).getOrElse(
//            throw new IllegalArgumentException(s"Error when parsing external annotations from ${opt.externalAnnotations}: Cannot find method ${name}")
//          )
          val constraintStrings : List[String] = for (JArray(entries) <- cs; JString(s) <- entries) yield s
          val effectStrings : List[String] = for (JArray(entries) <- effs; JString(s) <- entries) yield s
          val sig : Signature[Level] = makeSignature[Level](m.getParameterCount, parseConstraints(constraintStrings), makeEffects(parseEffects(effectStrings)))
          m -> sig
        }

      /** ****************************
        * Read configured field types from file
        * *****************************/
      val configuredFields : List[(SootField, Type[Level])] =
        for {
          JObject(entries) <- annotationsJson \\ "fields"
          JField(name, JString(typeString)) <- entries
          f <- Try(s.getField(name)).map(f => List(f)).getOrElse(List())
        } yield {
          // TODO: emit a warning when a field cannot be found
//          val f : SootField = Try(s.getField(name)).getOrElse(
//            throw new IllegalArgumentException(s"Error when parsing external annotations from ${opt.externalAnnotations}: Cannot find field ${name}")
//          )
          val t : Type[Level] = types.typeParser().parse(typeString).getOrElse(
            throw new IllegalArgumentException(s"Error when parsing external annotations from ${opt.externalAnnotations}: Error parsing type ${typeString} of field ${name}")
          )
          f -> t
        }


      /** ***********************************
        * set signature table and field table
        * *****************************/
      val signatures = SignatureTable.makeTable[Level](signatureMap ++ specialSignatures ++ configuredSignatures)
      val fieldTable = new FieldTable[Level](fieldMap ++ configuredFields)

      /** *********************
        * Read casts from config file
        * *********************/
      val castJsonStr = Source.fromFile(opt.castMethods).mkString
      val castJson = Try(parseJson(castJsonStr)) match {
        case Failure(exception) =>
          throw new IllegalArgumentException(s"Error parsing casts (${opt.castMethods}): ${exception.getMessage}")
        case Success(value) => value
      }

      val getAssocs: String => List[(String, String)] = sel =>
        for {
          JObject(entries) <- castJson \\ sel
          JField(name, JString(conv)) <- entries
        } yield name -> conv

      val valueCasts: Map[String, String] = getAssocs("valuecasts").toMap
      val contextCasts: Map[String, String] = getAssocs("contextcasts").toMap
      val contextCastEnd = (castJson \\ "contextcastend") match {
        case JString(s) => s
        case _ => throw new IllegalArgumentException(s"Cannot find entry for contextcastend in ${opt.castMethods}")
      }

      // TODO: some validation would be nice
      /*
      println(valueCasts)
      println(contextCasts)
      println(contextCastEnd)
      */

      val casts: Casts[Level] = CastsFromMapping(
        CastsFromMapping.parseConversionMap(types.typeParser(), valueCasts).get,
        CastsFromMapping.parseConversionMap(types.typeParser(), contextCasts).get, contextCastEnd)

      /** ***********************
        * Class hierarchy check
        * ************************/
      println("Checking class hierarchy: ")
      for (c <- classes; mSub <- c.getMethods; mSup <- Supertypes.findOverridden(mSub)) {
        val result = ClassHierarchyTyping.checkTwoMethods(csets, types, signatures, mSub, mSup)
        print(s"* method ${mSub} overriding method ${mSup}: ")
        println(Format.pprint(Format.classHierarchyCheck(result)))
      }
      println

      /** *********************
        * Method signature check
        ************************/

      println("Checking method bodies: ")
      for (c <- classes if !c.isInterface; m <- c.getMethods if !m.isAbstract) {
        val methodTyping = new MethodTyping(csets, cstrs, casts)
        val mresult = catching(classOf[TypingException], classOf[TypingAssertionFailure]).either(methodTyping.check(new TypeVars(), signatures, fieldTable, m))
        val resultReport = Format.pprint(mresult.fold(Format.typingException(_), Format.methodTypingResult(_)))
        println(s"* Type checking method ${m.toString}: ${resultReport}")
        println()
      }
    }

    private def parseConstraints(constraintStrings: List[String]): List[SigConstraint[Level]] =
    // TODO: good error message when parsing fails
      constraintStrings.map(s => new ConstraintParser(types.typeParser()).parseConstraints(s).get)

    private def parseEffects(effectStrings: List[String]): List[Type[Level]] =
      effectStrings.map(s => types.typeParser().parse(s) match {
        case Some(t) => t
        case None => throw new RuntimeException(String.format("Error parsing type %s", s))
      })
  }

  private def getSingleAnnotation[A](annotations: List[A], tooManyError: RuntimeException): Option[A] =
    annotations match {
      case Nil => None
      case x :: Nil => Some(x)
      case _ => throw tooManyError
    }
}

