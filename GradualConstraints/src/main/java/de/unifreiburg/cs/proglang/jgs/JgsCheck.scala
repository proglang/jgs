package de.unifreiburg.cs.proglang.jgs

import java.io.File
import java.util.logging.Logger

import com.fasterxml.jackson.dataformat.yaml.{YAMLFactory, YAMLMapper}
import de.unifreiburg.cs.proglang.jgs.JgsCheck.Opt
import de.unifreiburg.cs.proglang.jgs.TestCollector.{Exceptional, UnexpectedFailure, UnexpectedSuccess}
import de.unifreiburg.cs.proglang.jgs.cli.Format
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.{ExampleDomains, LowHigh, UserDefined, UserDefinedUtils}
import de.unifreiburg.cs.proglang.jgs.constraints.{TypeDomain, TypeVars, _}
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods.extractStringArrayAnnotation
import de.unifreiburg.cs.proglang.jgs.signatures.Effects.{emptyEffect, makeEffects}
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures._
import de.unifreiburg.cs.proglang.jgs.signatures._
import de.unifreiburg.cs.proglang.jgs.signatures.parse.ConstraintParser
import de.unifreiburg.cs.proglang.jgs.typing.{ClassHierarchyTyping, MethodTyping, TypingAssertionFailure, TypingException}
import de.unifreiburg.cs.proglang.jgs.util.NotImplemented
import de.unifreiburg.cs.proglang.jgs.Util._
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView
import de.unifreiburg.cs.proglang.jgs.instrumentation.CastUtils.TypeViewConversion
import de.unifreiburg.cs.proglang.jgs.instrumentation._
import de.unifreiburg.cs.proglang.jgs.jimpleutils.{CastsFromMapping, Methods, Supertypes}
import org.json4s._
import org.json4s.jackson.{Json, Json4sScalaModule}
import scopt.OptionParser
import soot.options.Options
import soot.{Scene, SootClass, SootField, SootMethod, VoidType}

import scala.collection.JavaConversions._
import scala.io.Source
import scala.util.control.Exception.catching
import scala.util.{Failure, Success, Try}
import scala.util.control.Exception._

object JgsCheck {


  sealed trait Verbosity

  case object Warn extends Verbosity

  case object Debug extends Verbosity

  sealed trait SecDomainChoice

  case object LowHigh extends SecDomainChoice

  case object AliceBobCharlie extends SecDomainChoice

  case class UserDomain(val secDomainClass: File) extends SecDomainChoice


  sealed case class Opt
  (val sourcetree: Option[File],
   val support: File,
   val runtime: String,
   val externalAnnotations: File,
   val castMethods: File,
   val genericCasts: Boolean,
   val secdomainChoice: SecDomainChoice,
   val verbosity: Verbosity,
   testMode: Boolean
  )



  // configure a json4s capable yaml parser
  val parseJson = {
    val yamlMapper = new YAMLMapper()
    yamlMapper.registerModule(new Json4sScalaModule)
    val json = Json(DefaultFormats, yamlMapper)
    (s : String) => json.parse(s)
  }

  def die(msg : String, exitCode : Int = -1): Unit = {
    System.err.println(msg)
    System.err.println(
            "Soot'classpath is: " + Scene.v().getSootClassPath + "\n" +
            "Working Directory is " + System.getProperty("user.dir"))
    System.exit(exitCode)
  }

  def main(args: Array[String]): Unit = {

    val log: Logger = Logger.getLogger("de.unifreiburg.cs.proglang.jgs.typing.log")
    val debugLog: Logger = Logger.getLogger("de.unifreiburg.cs.proglang.jgs.typing.debug")

    val defOpt = Opt(
      sourcetree = Some(new File("JGSTestclasses/src")),
      support = new File("JGSSupport/bin"),
      runtime = sys.props.get("sun.boot.class.path")
        .getOrElse(sys.error("Unable to look up system property `sun.boot.class.path'")),
      externalAnnotations = new File("external-annotations.yaml"),
      castMethods = new File("cast-methods.yaml"),
      genericCasts = false,
      secdomainChoice = LowHigh,
      verbosity = Warn,
      testMode = false
    )

    val addDefault = (s: String, get: Opt => Object) => s + s" (default: ${get(defOpt).toString})"

    val parser = new OptionParser[Opt]("jgs-check") {
      head("jgs-check", "0.1")
      // TODO: try to "built-in" the support classes (at least when doing assembly)
      opt[File]("support-classes")
        .action { (x, c) => c.copy(support = x) }
        .text(addDefault("directory containing support classes", _.support))
      opt[String]("boot-classes")
        .action { (x, c) => c.copy(runtime = x) }
        .text("Classpath with Java 7 boot classes. (default: value of the sun.boot.class.path system property)")
      opt[Unit]("verbose")
        .action { (_, c) => c.copy(verbosity = Debug) }
      opt[File]("external-annotations")
        .action { (x, c) => c.copy(externalAnnotations = x) }
        .text(addDefault("json file with annotations for external methods and fields not part of the sourcetree", _.externalAnnotations))
      opt[File]("cast-methods")
        .action { (x, c) => c.copy(castMethods = x) }
        .text(addDefault("json file specificying the cast methods", _.castMethods))
      opt[Unit]("alice-bob-charlie")
        .action { (x, c) => c.copy(secdomainChoice = AliceBobCharlie) }
        .text("Use the {bottom, alice, bob, charlie, top} security domain instead of {LOW, HIGH}")
      opt[File]("security-domain")
          .action { (f, c) => c.copy(secdomainChoice = UserDomain(f))}
      opt[Unit]("generic-casts")
        .action { (_, c) => c.copy(genericCasts = true)}
        .text("Use the generic casts from cast-methods")
      opt[Unit]("test-mode")
        .action { (_, c) => c.copy(testMode = true) }
        .text("Enable test mode (only useful for unit-tests)")
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
        o.set_soot_classpath(Seq(opt.support.getPath, sourcetree.getPath, opt.runtime).mkString(File.pathSeparator))
        o.set_process_dir(List(sourcetree.getPath))

        val s: Scene = Scene.v()

        try {
          s.loadNecessaryClasses()
        } catch {
          // TODO: check if this loading error can be detected earlier and more precicely.
          case e : NullPointerException =>
            die("ERROR loading classes to analyze. Aborting.\n"
                + s"  maybe the source tree is wrong? (${sourcetree.getPath}) ")
        }
        log.info(f"Number of classes found: ${s.getClasses.size()}%d")
        // Abort when no classes where found
        if (s.getClasses.isEmpty) {
          die("WARNING: did not find any classes to type-check. Aborting \n")
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
          case AliceBobCharlie => {
            val secdomain = ExampleDomains.aliceBobCharlie
            val types = new TypeDomain(secdomain)
            val csets =new NaiveConstraintsFactory(types)
            new Config(types, csets, opt)
          }
          case UserDomain(secDomainClass) =>

            // create the domains
            val domainSpec = UserDefinedUtils.fromJSon(secDomainClass)
            val secdomain = UserDefinedUtils.makeSecDomain(domainSpec)
            val types = new TypeDomain(secdomain)
            val csets = new NaiveConstraintsFactory(types)
            new Config(types, csets, opt)
        }
        cfg.typeCheck(s, classes, log)

    }

  }

  case class Annotation(constraintStrings : Array[String], effectStrings : Array[String])

  def typeCheck[Level](mainClass : String,
                       otherClasses : Array[String],
                       sootClasspath : Array[String],
                       // support : Array[String],
                       externalMethodAnnotations : java.util.Map[String, Annotation],
                       externalFieldAnnotations : java.util.Map[String, String],
                       secdomain : SecDomain[Level],
                       casts : ACasts[Level],
                       log : Logger,
                       errors : java.util.List[String],
                       forceMonomorphicMethods : Boolean) : MethodTypings[Level] = {


    val o: Options = Options.v()
    o.set_main_class(mainClass)

    val s: Scene = Scene.v()
    s.setSootClassPath(sootClasspath.mkString(File.pathSeparator) ++ File.pathSeparator ++ s.getSootClassPath())

    log.info(s"Soot classpath: ${s.getSootClassPath}")

    for (c <- otherClasses) {
      s.addBasicClass(c)
    }

    val c = s.loadClassAndSupport(mainClass)
    c.setApplicationClass()

    typeCheck(s, externalMethodAnnotations, externalFieldAnnotations, secdomain, casts, log, errors,
      forceMonomorphicMethods = forceMonomorphicMethods)
  }

  def typeCheck[Level](s : Scene,
                       externalMethodAnnotations : java.util.Map[String, Annotation],
                       externalFieldAnnotations : java.util.Map[String, String],
                       secdomain : SecDomain[Level],
                       casts : ACasts[Level],
                       log : Logger,
                       errors : java.util.List[String],
                       forceMonomorphicMethods : Boolean) : MethodTypings[Level] = {
    try {
      s.loadNecessaryClasses()
    } catch {
      // TODO: check if this loading error can be detected earlier and more precicely.
      case e : NullPointerException =>
        die("ERROR loading classes to analyze. Aborting.\n")
    }

    log.info(f"Number of classes found: ${s.getClasses.size()}%d")
    log.info(f"Number of application classes found: ${s.getApplicationClasses.size()}%d")
    log.info(s"Main class: ${s.getMainClass}")
    // Abort when no classes where found
    if (s.getClasses.isEmpty) {
      die("WARNING: did not find any classes to type-check. Aborting \n")
    }

    // Initialize the essential datastructures for typechecking
    val classes: List[SootClass] = s.getApplicationClasses.toList
    val types = new TypeDomain(secdomain)
    val csets = new NaiveConstraintsFactory(types)
    val cstrs = new Constraints(types)

    // utilities
    def parseConstraints(constraintStrings: List[String]): Try[List[SigConstraint[Level]]] =
    // TODO: good error message when parsing fails
    Try(constraintStrings.map(s => {
      new ConstraintParser(types).parseConstraints(s) match {
        case Failure(exception) => throw exception
        case Success(value) => value
      }
    }))

    def parseEffects(effectStrings: List[String]): Try[List[TypeView[Level]]] =
      Try(effectStrings.map(
        s => asTry(new RuntimeException(String.format("Error parsing type %s", s)), types.readType(s)).get
      ))

    // Parse typing declarations from source code
    val fieldMap: Map[SootField, TypeView[Level]] =
    (for (c <- classes;
          f <- c.getFields)
      yield {
        val secAnnotations: List[String] =
          Methods.extractStringAnnotation("Lde/unifreiburg/cs/proglang/jgs/support/Sec;", f.getTags.iterator).toList
        val mt: Option[TypeView[Level]] = {
          val moreThanOneErr = new IllegalArgumentException("Found more than one security level on " + f.getName())
          for { typeStr <- getSingleAnnotation(secAnnotations, moreThanOneErr)
                t <- Try(types.readType(typeStr)).toOption
          }
            yield t
        }
        val t: TypeView[Level] = mt.getOrElse(types.pub())
        (f, t)
      }).toMap
    log.info("Field types: " + fieldMap.toString())

    val signatureMap: Map[SootMethod, Signature[Level]] =
      (for {
        c <- classes
        m <- c.getMethods
        sig <- { val constraintStrings: List[String] =
          getSingleAnnotation(
            extractStringArrayAnnotation("Lde/unifreiburg/cs/proglang/jgs/support/Constraints;", m.getTags().iterator).toList.map(_.toList),
            new IllegalArgumentException("Found more than one constraint annotation on " + m.getName())
          ).getOrElse(Nil)
          val effectStrings: List[String] = getSingleAnnotation(
            extractStringArrayAnnotation("Lde/unifreiburg/cs/proglang/jgs/support/Effects;", m.getTags().iterator()).toList.map(_.toList),
            new IllegalArgumentException("Found more than one effect annotation on " + m.getName())).getOrElse(Nil)
          val maybeEntry =
            for {
              cs <- parseConstraints(constraintStrings)
              effs <- parseEffects(effectStrings)
            } yield makeSignature[Level](m.getParameterCount, cs, makeEffects(effs))
          skipAndReportFailure(log, s"Error parsing signature for method ${m}: ", maybeEntry)
        }
      } yield m -> sig).toMap

    log.info("Signature map: " + signatureMap.toString())

    // add signatures for constructors
    val specialSignatures: Map[SootMethod, Signature[Level]] =
    Map(
      // Object.<init> is harmless
      s.getSootClass("java.lang.Object").getMethodByName("<init>") -> makeSignature[Level](0, List(), emptyEffect[Level])
    )

    val configuredSignatures : Map[SootMethod, Signature[Level]] =
      (for {
        (methodSignature, Annotation(constraintStrings, effectStrings)) <- externalMethodAnnotations
        method <- Try(s.getMethod(methodSignature)).toOption
        maybeEntry =
          for {
            constraints <- parseConstraints(constraintStrings.toList)
            effects <- parseEffects(effectStrings.toList)
          } yield makeSignature[Level](method.getParameterCount, constraints, makeEffects(effects))
        entry <- skipAndReportFailure(log, s"Error parsing external annotation for method ${method}:", maybeEntry)
      } yield {
        method -> entry
      }).toMap

      val configuredFields : Map[SootField, TypeView[Level]] = {
        for {
          (fieldName, typeString) <- externalFieldAnnotations.toMap
          f <- Try(s.getField(fieldName)).toOption
          fieldType <- {
            val t : Try[TypeView[Level]] =
              asTry(
                new RuntimeException(s"Error parsing type ${typeString} of field ${fieldName}"),
                types.readType(typeString)
              )
            skipAndReportFailure(log, "", t)
        }
      } yield {
        // TODO: emit a warning when a field cannot be found
        //          val f : SootField = Try(s.getField(name)).getOrElse(
        //            throw new IllegalArgumentException(s"Error when parsing external annotations from ${opt.externalAnnotations}: Cannot find field ${name}")
        //          )
        f -> fieldType
      }
      }

      val monomorphicOverrides : Map[SootMethod, Signature[Level]] =
        if (forceMonomorphicMethods) {
          val tvs = new TypeVars
          for ((m, sig) <- signatureMap) yield {
            val params : Set[Symbol[Level]] = Symbols.methodParameters[Level](m).toSet
            val cs : List[Constraint[Level]] =
              sig.constraints.toTypingConstraints(Symbols.identityMapping(tvs, params + Symbol.ret[Level])).toList
            val cset = csets.fromCollection(cs)
            val additionalConstraints =
              for {
                i <- 0 until m.getParameterCount
                tv = tvs.param(Var.fromParam(i))
                if cset.instrumentationType(tv).isEmpty
              } yield {
                log.warning(s"Forcing monomorphic parameters of method ${m}: ${0} <= pub")
                MethodSignatures.makeSigConstraint(Symbol.param(i), Symbol.literal(types.pub()), ConstraintKind.LE)
            }
            val newSig = sig.addConstraints(additionalConstraints.toIterator)
            (m -> newSig)
          }
        } else Map()

      /** ***********************************
        * set signature table and field table
        * *****************************/
      val signatures = SignatureTable.of[Level](
        signatureMap ++ specialSignatures ++ configuredSignatures ++ monomorphicOverrides
      )
      val fieldTable = FieldTable.of[Level](fieldMap ++ configuredFields)

      /** ***********************
        * Class hierarchy check
        * ************************/
      log.info("Checking class hierarchy: ")
      for (c <- classes; mSub <- c.getMethods; mSup <- Supertypes.findOverridden(mSub)) {
        val result = ClassHierarchyTyping.checkTwoMethods(csets, types, signatures, mSub, mSup)
        log.info(s"* method ${mSub} overriding method ${mSup}: " + Format.pprint(Format.classHierarchyCheck(result)))
      }


      /** *********************
        * Method signature check
        ************************/
      log.info("Checking method bodies: ")
      val relevantMethods : Seq[SootMethod] =
        for {c <- classes if !c.isInterface
             m <- c.getMethods if !m.isAbstract } yield m
      val methodResults : Map[SootMethod, MethodTyping.Result[Level]] = (for { m <- relevantMethods
           methodTyping = new MethodTyping(csets, cstrs, casts)
           // TODO: clarify the difference between TypingException and TypingAssertionFailure
           mresult = catching(classOf[TypingException],
             classOf[TypingAssertionFailure],
             classOf[NotImplemented]).either {
               methodTyping.check(new TypeVars(), signatures, fieldTable, m)
             }
           resultReport = Format.pprint(mresult.fold(Format.typingException(_), Format.methodTypingResult(_)))
           _ = {
             val msg = s"* Type checking method ${m.toString}: ${resultReport}"
             if (mresult.isLeft || ! mresult.right.get.isSuccess) {
               errors.add(msg)
               log.severe(msg)
             } else {
               log.info(msg)
             }
           }
           result <- mresult.right.toOption
      } yield (m -> result)).toMap

    val mtyping = new MethodTypings[Level] {
      def getResult(m : SootMethod) : MethodTyping.Result[Level] =
        methodResults.getOrElse(m, throw new NoSuchElementException(s"No typing result for method ${m}"))

      override def getSingleInstantiation(m: SootMethod, defaultType : Type[Level]): Instantiation[Level] = {
        val result = getResult(m)
        val signatureConstraints = result.refinementCheckResult.abstractConstraints

        // check that we really can instantiate the method monomorphically.
        val paramInstantiation : Map[Int, Type[Level]] = (for (i <- Range(0, m.getParameterCount))
          yield i -> result.parameterInstantiation(i).getOrElse(defaultType)).toMap

        val mIsVoid = m.getReturnType == VoidType.v()

        val returnInstantiation = if (mIsVoid) {
          Failure(new IllegalArgumentException(s"Try to get type of a void method: ${m}"))
        } else {
          Success(result.returnInstantiation.getOrElse(defaultType))
        }

        new Instantiation[Level] {
          override def getReturn: Type[Level] = returnInstantiation.get

          override def get(param: Int): Type[Level] = paramInstantiation(param)

          override def toString: String =
            s"Instantiation for ${m}: \n " +
            s"Parameters: ${paramInstantiation} \n " +
            s"return: ${if (mIsVoid) "void" else returnInstantiation}"
        }

      }

      override def getVarTyping(m: SootMethod): VarTyping[Level] =
        getResult(m).variableTyping

      override def getCxTyping(m: SootMethod): CxTyping[Level] =
        getResult(m).cxTyping

      override def getEffectType(m: SootMethod): Effect[Level] = ???
    }

    mtyping
  }

  class Config[Level](val types: TypeDomain[Level],
                      val csets: ConstraintSetFactory[Level],
                      val opt: Opt) {
    val testCollector : TestCollector[Level] = TestCollector()


    def typeCheck(s: Scene, classes: List[SootClass], log : Logger): Unit = {

      /** ****************************
        * Read configured signatures from file
        * *****************************/
      val annotationsJsonStr = Source.fromFile(opt.externalAnnotations).mkString


      val annotationsJson = Try(parseJson(annotationsJsonStr)) match {
        case Failure(exception) =>
          throw new IllegalArgumentException(s"Error parsing external annotations (${opt.externalAnnotations}): ${exception.getMessage}")
        case Success(value) => value
      }

      val configuredSignatures: List[(String, JgsCheck.Annotation)] =
        for {
          JObject(entries) <- annotationsJson \\ "methods"
          JField(name, JObject(JField("constraints", cs) :: JField("effects", effs) :: Nil)) <- entries
        } yield {
          val constraintStrings : List[String] = for (JArray(entries) <- cs; JString(s) <- entries) yield s
          val effectStrings : List[String] = for (JArray(entries) <- effs; JString(s) <- entries) yield s
          name -> Annotation(constraintStrings.toArray, effectStrings.toArray)
        }

      /** ****************************
        * Read configured field types from file
        * *****************************/
      val configuredFields : List[(String, String)] =
        for {
          JObject(entries) <- annotationsJson \\ "fields"
          JField(name, JString(typeString)) <- entries
        } yield {
          // TODO: emit a warning when a field cannot be found
//          val f : SootField = Try(s.getField(name)).getOrElse(
//            throw new IllegalArgumentException(s"Error when parsing external annotations from ${opt.externalAnnotations}: Cannot find field ${name}")
//          )
          name -> typeString
        }



      /** *********************
        * Read casts from config file
        * *********************/
      val castJsonStr = Source.fromFile(opt.castMethods).mkString
      val castJson = Try(parseJson(castJsonStr)) match {
        case Failure(exception) =>
          throw new IllegalArgumentException(s"Error parsing casts (${opt.castMethods}): ${exception.getMessage}")
        case Success(value) => value
      }

      def constructMappingCasts(log : Logger) = {
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

        // TODO: some validation would be nice: (i) method exists in CP? (ii) conversion could be parsed? (iii) conversion is compatible?

        def makeCastMap(casts: Map[String, String]): Map[String, TypeViewConversion[Level]] = {
          for {
            (m, convString) <- casts
            conv <- skipAndReportFailure(log, s"Error parsing conversion for cast-method ${m}",
              CastUtils.parseConversion(types, convString))
          } yield m -> conv
        }
        CastsFromMapping(makeCastMap(valueCasts), makeCastMap(contextCasts), contextCastEnd)
      }

      def constructGenericCasts = {
        def getString(key : String) : String =
          castJson \\ key match {
            case JString(s) => s
            case _ => throw new IllegalArgumentException(s"Cannot find string entry ${key} in cast-method file ${opt.castMethods}")
          }
        new CastsFromConstants(
          types,
          getString("valuecast-generic"),
          getString("contextcast-generic"),
          getString("contextcastend")
        )
      }

      // TODO: combine generic casts and mapping casts.. generic ones should have precedence (?)
      val casts: ACasts[Level] =
        if (opt.genericCasts) {
          constructGenericCasts
        } else {
          constructMappingCasts(log)
        }

      // TODO:  finish this
      // JgsCheck.typeCheck()

      // TODO: re-implement the testcollector stuff somehow... or something equivalent that is more elegant
      /*
      println("Checking method bodies: ")
      for (c <- classes if !c.isInterface; m <- c.getMethods if !m.isAbstract) {
        val methodTyping = new MethodTyping(csets, cstrs, casts)
        // TODO: clarify the difference between TypingException and TypingAssertionFailure
        val mresult = catching(classOf[TypingException],
                               classOf[TypingAssertionFailure],
                               classOf[NotImplemented]).either {
          methodTyping.check(new TypeVars(), signatures, fieldTable, m)
        }
        if (opt.testMode) {
          mresult.fold(testCollector.observeError(m, _), testCollector.observe(m, _))
        } else {
          val resultReport = Format.pprint(mresult.fold(Format.typingException(_), Format.methodTypingResult(_)))
          println(s"* Type checking method ${m.toString}: ${resultReport}")
          println()
        }
      }
      if (opt.testMode) {
        val failures = testCollector.getFailures
        val errors = testCollector.getErrors
        val obeserved = testCollector.getObserved

        val retVal =
          if (failures.isEmpty && errors.isEmpty) {
            0
          } else {
            for (fail <- failures) {
              fail match {
                case UnexpectedSuccess(method) =>
                  println(s" !! Method ${method} succeeded unexpectedly")
                case UnexpectedFailure(method, result) =>
                  val resultReport = Format.pprint(Format.methodTypingResult(result))
                  println(s" !! Method ${method} failed unexpecedly: ${resultReport}\n")
              }
            }
            for (Exceptional(method, exc) <- errors) {
              val excMessage = Format.pprint(Format.typingException(exc))
              println(s" !! Error while checking method ${method}: ${excMessage}\n")
            }
            -1
          }
        println(s"(Tested ${obeserved.size} methods, ${failures.size} failures, ${errors.size} errors)")
        sys.exit(retVal)
      }
      */
    }

  }

  private def getSingleAnnotation[A](annotations: List[A], tooManyError: RuntimeException): Option[A] =
    annotations match {
      case Nil => None
      case x :: Nil => Some(x)
      case _ => throw tooManyError
    }
}

