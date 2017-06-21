lazy val setScalaVersion = scalaVersion := "2.11.7"

lazy val commonSettings = Seq(

  setScalaVersion,
  organization := "de.unifreiburg.cs.proglang",
  unmanagedBase := file("lib"),

  libraryDependencies ++= Seq(
    "org.apache.commons" % "commons-lang3" % "3.4",
    "org.apache.commons" % "commons-collections4" % "4.1"
  )
)

lazy val gradConstraintsDeps = Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  // "org.json4s" %% "json4s-native" % "3.3.0",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.7.4",
  "com.github.scopt" %% "scopt" % "3.4.0",
  "com.googlecode.kiama" %% "kiama" % "1.8.0"
)

lazy val dynAnalyzerDeps = Seq(
  "org.apache.commons" % "commons-lang3" % "3.4",
  "org.apache.commons" % "commons-collections4" % "4.1",
  "junit" % "junit" % "4.12",
  "org.apache.ant" % "ant-junit" % "1.9.7",
  "commons-cli" % "commons-cli" % "1.3.1",
  // "ca.mcgill.sable" % "soot" % "RELEASE",
  "org.hamcrest" % "hamcrest-library" % "1.3"
)

lazy val GradualConstraints =
  (project in file("GradualConstraints")).
    dependsOn(InstrumentationSupport).
    settings(commonSettings:_*).
    settings(
      libraryDependencies ++= gradConstraintsDeps,
      javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
    )


lazy val jgs =
  (project in file(".")).
    dependsOn(GradualConstraints, DynamicAnalyzer, ScratchTestclasses, DemoTestclasses).
    settings(commonSettings:_*).
    settings(
      unmanagedClasspath in Runtime ++= {
        sys.env.get("JGS_SECDOMAIN_CLASSES")
          .map(path => path.split(":").map(new File(_)).toList)
          .getOrElse(List())
      }
    )

lazy val DynamicAnalyzer =
  (project in file("DynamicAnalyzer")).
    dependsOn(InstrumentationSupport, JGSSupport).
    settings(commonSettings:_*).
    settings(
      libraryDependencies ++= dynAnalyzerDeps,
      javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
    )

lazy val GradualConstraintsTests =
  (project in file("GradualConstraints/Tests")).
    dependsOn(GradualConstraints).
    settings(commonSettings:_*).
    settings(
      libraryDependencies ++=
        gradConstraintsDeps ++
          Seq(
            "com.novocode" % "junit-interface" % "0.11" % "test",
            "org.scalactic" %% "scalactic" % "2.2.6",
            "org.scalatest" %% "scalatest" % "2.2.6" % "test"
          )
       ,
       javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
    )

lazy val InstrumentationSupport =
  (project in file("GradualConstraints/InstrumentationSupport")).
    settings(commonSettings:_*).
    settings(
      libraryDependencies ++=
        Seq("com.novocode" % "junit-interface" % "0.11" % "test",
          "org.json4s" %% "json4s-jackson" % "3.3.0",
          "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.7.4"
        ),
      javacOptions ++= Seq("-source", "1.7", "-target", "1.7"),
      artifactName := {
        (sv : ScalaVersion , mod : ModuleID , artifact : Artifact) => "gradualconstraints_" + Artifact.artifactName(sv, mod, artifact)
      }

    )


lazy val JGSSupport =
  (project in file("JGSSupport")).
    settings(setScalaVersion)

lazy val ScratchTestclasses =
  (project in file ("JGSTestclasses/Scratch")).
    dependsOn(JGSSupport, DynamicAnalyzer).
    settings(setScalaVersion)

lazy val DemoTestclasses =
  (project in file ("JGSTestclasses/Demo")).
    dependsOn(JGSSupport, DynamicAnalyzer).
    settings(setScalaVersion)

lazy val UserDefinedSecurityDomain =
  (project in file ("UserDefinedSecurityDomain")).
    dependsOn(InstrumentationSupport).
    settings(setScalaVersion)

lazy val LMHSecurityDomain =
  (project in file ("LMHSecurityDomain")).
    dependsOn(InstrumentationSupport).
    settings(setScalaVersion)
