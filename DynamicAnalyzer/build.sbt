lazy val setScalaVersion = scalaVersion := "2.11.7"
lazy val baseDir = file("/Users/nicolasm/Dropbox/Hiwi/ProgLang/jgs/DynamicAnalyzer")

lazy val commonSettings = Seq(

  setScalaVersion,
  fork := true,
  // baseDirectory in run := baseDir,
  // baseDirectory in test := baseDir,
  organization := "de.unifreiburg.cs.proglang",
  unmanagedBase := file("lib"),

  libraryDependencies ++= Seq(
    "org.apache.commons" % "commons-lang3" % "3.4",
    "org.apache.commons" % "commons-collections4" % "4.1",
    "junit" % "junit" % "4.12",
    "org.apache.ant" % "ant-junit" % "1.9.7",
    "commons-cli" % "commons-cli" % "1.3.1",
    // "ca.mcgill.sable" % "soot" % "RELEASE",
    "org.hamcrest" % "hamcrest-library" % "1.3"
  )
)

lazy val DynamicAnalyzer =
  (project in file(".")).
    // dependsOn(InstrumentationSupport).
    settings(commonSettings:_*)

