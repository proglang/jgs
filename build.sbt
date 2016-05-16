lazy val commonSettings = Seq(

    scalaVersion := "2.11.7",
    organization := "de.unifreiburg.cs.proglang",

    unmanagedBase := file("lib"),
    
    libraryDependencies ++= Seq(
      "org.apache.commons" % "commons-lang3" % "3.4",
      "org.apache.commons" % "commons-collections4" % "4.1"
    )
)

lazy val GradualConstraints = 
  (project in file("GradualConstraints")).
    dependsOn(GradualConstraints_InstrumentationSupport).
    settings(commonSettings:_*).
    settings(
      libraryDependencies ++= Seq(
        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
	"org.json4s" %% "json4s-native" % "3.3.0",
	"com.github.scopt" %% "scopt" % "3.4.0",
	"com.googlecode.kiama" %% "kiama" % "1.8.0"
      )
    )

lazy val GradualConstraints_InstrumentationSupport = 
  (project in file("GradualConstraints_InstrumentationSupport")).
    settings(commonSettings:_*)