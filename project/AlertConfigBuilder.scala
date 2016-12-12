import sbt.Keys._
import sbt.Tests.{Group, SubProcess}
import sbt._
import uk.gov.hmrc.versioning.SbtGitVersioning

object AlertConfigBuilder extends Build {


  import TestPhases._
  import uk.gov.hmrc.SbtAutoBuildPlugin

  val appName = "alert-config-builder"

  lazy val library = Project(appName, file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
    .settings(
      scalaVersion := "2.11.6",
      scalacOptions ++= Seq(
        "-Xlint",
        "-target:jvm-1.8",
        "-Xmax-classfile-name", "100",
        "-encoding", "UTF-8"
      )
    )
    .settings(
      parallelExecution in Test := false,
      fork in Test := false,
      retrieveManaged := true
    )
    .settings(inConfig(TemplateTest)(Defaults.testSettings): _*)
    .settings(libraryDependencies ++= AppDependencies())

}

private object AppDependencies {

  val compile = Seq(
    "org.scala-lang" % "scala-reflect" % "2.11.7",
    "org.reflections" % "reflections" % "0.9.9-RC1",
    "io.spray" %% "spray-json" % "1.3.2",
    "org.yaml" % "snakeyaml" % "1.17"
  )


  lazy val test: Seq[ModuleID] = Seq(
    "org.json4s" % "json4s-jackson_2.10" % "3.1.0" % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "org.pegdown" % "pegdown" % "1.5.0" % "test"
  )

  def apply() = compile ++ test
}

private object TestPhases {

  val allPhases = "tt->test;test->test;test->compile;compile->compile"

  lazy val TemplateTest = config("tt") extend Test

  def oneForkedJvmPerTest(tests: Seq[TestDefinition]) =
    tests map {
      test => new Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name))))
    }
}
