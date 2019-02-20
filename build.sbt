lazy val setScalaVersion = scalaVersion := "2.11.11"

lazy val setVersion = version := "0.1"

// we use the same name for all security domains s.t. can be references more easily from JgsDemo
lazy val setUnversionedJar = Seq(
  artifactName := ((_, _, _) => "secdomain.jar")
)

lazy val commonSettings = Seq(

  setScalaVersion,
  setVersion,
  organization := "de.unifreiburg.cs.proglang",
  unmanagedBase := file("lib"),

  libraryDependencies ++= Seq(
    "org.apache.commons" % "commons-lang3" % "3.4",
    "org.apache.commons" % "commons-collections4" % "4.1"
  ),

  scalacOptions += "-target:jvm-1.8",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

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
      libraryDependencies ++= gradConstraintsDeps
    )


lazy val jgs =
  (project in file(".")).
    dependsOn(GradualConstraints, DynamicAnalyzer, ScratchTestclasses, DemoTestclasses).
    settings(commonSettings:_*).
    settings(
      unmanagedClasspath in Runtime ++= {
        // List(baseDirectory.value / "LMHSecurityDomain/target/scala-2.11/lmhsecuritydomain_2.11-0.1-SNAPSHOT.jar")
        sys.env.get("JGS_SECDOMAIN_JARS")
          .map(path => path.split(":").map(new File(_)).toList).getOrElse(List())
      }
    )

lazy val jgsDemo =
  (project in file("jgsDemo")).
    settings(commonSettings:_*)

lazy val DynamicAnalyzer =
  (project in file("DynamicAnalyzer")).
    dependsOn(InstrumentationSupport, JGSSupport).
    settings(commonSettings:_*).
    settings(
      libraryDependencies ++= dynAnalyzerDeps
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
    settings(setScalaVersion).
    settings(setUnversionedJar)

lazy val LMHSecurityDomain =
  (project in file ("LMHSecurityDomain")).
    dependsOn(InstrumentationSupport).
    settings(setScalaVersion).
    settings(setVersion).
    settings(setUnversionedJar)
