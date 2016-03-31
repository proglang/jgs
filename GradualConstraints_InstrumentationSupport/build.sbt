lazy val GradualConstraints_InstrumentationSupport = (project in file(".")).
  settings(
    name := "GradualConstraints_InstrumentationSupport",
    organization := "de.unifreiburg.cs.proglang",
    scalaVersion := "2.11.7",

    libraryDependencies ++= Seq(
      "org.apache.commons" % "commons-lang3" % "3.4",
      "org.apache.commons" % "commons-collections4" % "4.1"
    )
  )