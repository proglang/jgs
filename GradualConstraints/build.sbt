import java.io.FileNotFoundException

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

lazy val jgsCheckDeps = Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  // "org.json4s" %% "json4s-native" % "3.3.0",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.7.4",
  "com.github.scopt" %% "scopt" % "3.4.0",
  "com.googlecode.kiama" %% "kiama" % "1.8.0"
  )

lazy val ecoop = "ECOOP2016 artifact VM"
lazy val vmJarName = settingKey[String](s"The jar file for ${ecoop}")
lazy val vmAuxFilesDir = settingKey[File](s"The directory with aux. files for ${ecoop}")
lazy val vmPackageName = settingKey[String](s"Package basename for ${ecoop}")
lazy val vmArchive = settingKey[File](s"Archive for ${ecoop}")
lazy val testclasses = settingKey[File](s"Directory containing the testclasses for ${ecoop}")
lazy val configFiles = settingKey[Seq[File]]("Configuration files for casts, external methods and security domains")

lazy val packageForVM = taskKey[File](s"Build an archive for use in ${ecoop}")

lazy val GradualConstraints =
  (project in file(".")).
    aggregate(InstrumentationSupport).
    dependsOn(InstrumentationSupport).
    settings(commonSettings:_*).
    settings(


      libraryDependencies ++= jgsCheckDeps,
      
      vmJarName := "GradualConstraints.jar",
      vmAuxFilesDir := file("aux-files-for-ecoop-vm"),
      vmPackageName := "ecoop2016-artifact-update",
      vmArchive := file(vmPackageName.value + ".zip"),
      testclasses := file("JGSTestclasses"),
      configFiles := Seq("cast-methods.yaml", "external-annotations.yaml", "secdomain-alice-bob-charlie.yaml").map(file(_)),


      packageForVM := {
        // check for the existance of required files
        if (!vmAuxFilesDir.value.exists())
          throw new FileNotFoundException(vmAuxFilesDir.value.toString)
        // create the zip
        val sbtJar = (assembly in Compile).value
        val srcJar = (Keys.`packageSrc` in Compile).value
        val supportJar = (Keys.`package` in Compile in JGSSupport).value
        val auxFilesDir = vmAuxFilesDir.value.getAbsoluteFile
        val testclassesDir = testclasses.value.getAbsoluteFile
        val filesToZip =
        // subtracting auxFilesDir.base is required because relativeTo fails on the root
          auxFilesDir.**("*" -- auxFilesDir.base).pair(rebase(auxFilesDir, vmPackageName.value)) ++
            testclassesDir.**("*" -- testclassesDir.base).pair(rebase(testclassesDir, (file(vmPackageName.value) /
              testclassesDir.base).toString)) ++
            Seq(
              sbtJar -> (file(vmPackageName.value) / vmJarName.value).toString,
              srcJar -> (file(vmPackageName.value) / srcJar.name).toString,
              supportJar -> (file(vmPackageName.value) / "jgs-support.jar").toString
            ) ++
            (for (f <- configFiles.value) yield f -> (file(vmPackageName.value) / f.name).toString)

        val archive = vmArchive.value.getAbsoluteFile // use an absolute file, otherwise zip will NPE (as there is no
        // parent)
        print(s"Creating ECOOP2016 VM package in ${archive.toString} ...")
        IO.zip(filesToZip, archive)
        println(s"done")
        archive
      }
    )

lazy val Tests =
  (project in file("Tests")).
    dependsOn(GradualConstraints).
    settings(commonSettings:_*).
    settings(
      libraryDependencies ++=
        jgsCheckDeps ++
          Seq(
            "com.novocode" % "junit-interface" % "0.11" % "test",
            "org.scalactic" %% "scalactic" % "2.2.6",
            "org.scalatest" %% "scalatest" % "2.2.6" % "test"
          )
    )

lazy val InstrumentationSupport =
  (project in file("InstrumentationSupport")).
    settings(commonSettings:_*).
    settings(
      libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
    )


lazy val JGSSupport = project.settings(setScalaVersion)
