import java.io.FileNotFoundException
lazy val commonSettings = Seq(

  scalaVersion := "2.11.7",
  organization := "de.unifreiburg.cs.proglang",

  unmanagedBase := file("lib"),

  libraryDependencies ++= Seq(

    "org.apache.commons" % "commons-lang3" % "3.4",
    "org.apache.commons" % "commons-collections4" % "4.1"
  )
)

lazy val ecoop = "ECOOP2016 artifact VM"
lazy val vmJarName = settingKey[String](s"The jar file for ${ecoop}")
lazy val vmAuxFilesDir = settingKey[File](s"The directory with aux. files for ${ecoop}")
// lazy val vmJdkPattern = settingKey[String](s"Name of the JDK used for ${ecoop}")
lazy val vmPackageName = settingKey[String](s"Package basename for ${ecoop}")
lazy val vmArchive = settingKey[File](s"Archive for ${ecoop}")

lazy val packageForVM = taskKey[File](s"Build an archive for use in ${ecoop}")


lazy val GradualConstraints =
  (project in file(".")).
    aggregate(InstrumentationSupport).
    dependsOn(InstrumentationSupport).
    settings(commonSettings:_*).
    settings(


      libraryDependencies ++= Seq(
        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
	      "org.json4s" %% "json4s-native" % "3.3.0",
	      "com.github.scopt" %% "scopt" % "3.4.0",
	      "com.googlecode.kiama" %% "kiama" % "1.8.0"
      ),
      
      vmJarName := "GradualConstraints.jar",
      vmAuxFilesDir := file("aux-files-for-ecoop-vm"),
      // vmJdkPattern := "jdk1.7*",
      vmPackageName := "ecoop2016-artifact-update",
      vmArchive := file(vmPackageName.value + ".zip"),


      packageForVM := {
        // check for the existance of required files
        if (!vmAuxFilesDir.value.exists())
          throw new FileNotFoundException(vmAuxFilesDir.value.toString)
        // create the zip
        val sbtJar = (assembly in Compile).value
	val srcJar = (Keys.`packageSrc` in Compile).value
        val auxFilesDir = vmAuxFilesDir.value.getAbsoluteFile
        val filesToZip = auxFilesDir.**("*" -- auxFilesDir.base) // subtracting auxFilesDir.base is required because relativeTo fails on the root
          .pair(rebase(auxFilesDir, vmPackageName.value)) ++ 
	    Seq(
	      sbtJar -> (file(vmPackageName.value) / vmJarName.value).toString,
	      srcJar -> (file(vmPackageName.value) / srcJar.name).toString
	    )
        val archive = vmArchive.value.getAbsoluteFile // use an absolute file, otherwise zip will NPE (as there is no parent)
        print(s"Creating ECOOP2016 VM package in ${archive.toString} ...")
        IO.zip(filesToZip, archive)
        println(s"done")
        archive
      }
    )

lazy val InstrumentationSupport =
  (project in file("InstrumentationSupport")).
    settings(commonSettings:_*)