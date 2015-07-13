import sbt._
import Keys._
import play.Play._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import com.typesafe.sbt.packager.universal.UniversalKeys
import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys

import play.Play.autoImport._
import PlayKeys._

object ApplicationBuild extends Build with UniversalKeys {

  val scalajsOutputDir = Def.settingKey[File]("Directory for Javascript files output by ScalaJS")

  val commonSettings = Seq(
  	scalaVersion in ThisBuild := Versions.scala,
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")
  )

  override def rootProject = Some(scalajvm)

  lazy val scalajvm = Project(
    id = "scalajvm",
    base = file("scalajvm"))
  .enablePlugins(play.PlayScala)
  .settings(commonSettings: _*)
  .settings(scalajvmSettings: _*)
  .aggregate(scalajs)

  lazy val scalajs = Project(
    id   = "scalajs",
    base = file("scalajs"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(scalajsSettings: _*)
  .settings(
    relativeSourceMaps := true
  )

  lazy val scalajvmSettings =
    Seq(
      name := "todomvc-server",
      version := Versions.app,
      scalaVersion := Versions.scala,
      scalajsOutputDir := (crossTarget in Compile).value / "classes" / "public" / "javascripts",
      compile in Compile <<= 
        (compile in Compile) dependsOn (fastOptJS in (scalajs, Compile)),
      dist <<= dist dependsOn (fullOptJS in (scalajs, Compile)),
      libraryDependencies ++= Seq(
        jdbc,
        "com.github.benhutchison" %% "prickle" % Versions.prickle,
        "org.squeryl" %% "squeryl" % "0.9.5-7",
        "org.webjars" % "jquery" % "2.1.3",
        "org.webjars" % "angularjs" % "1.3.13"
      ),
      commands += preStartCommand,
      EclipseKeys.skipParents in ThisBuild := false
    ) ++ (
      // ask scalajs project to put its outputs in scalajsOutputDir
      Seq(packageScalaJSLauncher, fastOptJS, fullOptJS) map { packageJSKey =>
        crossTarget in (scalajs, Compile, packageJSKey) := scalajsOutputDir.value
      }
    )

  lazy val scalajsSettings =
    Seq(
      name := "todomvc-client",
      version := Versions.app,
      scalaVersion := Versions.scala,
      persistLauncher := true,
      persistLauncher in Test := false,
      resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      libraryDependencies ++= Seq(
        "com.greencatsoft" %%% "scalajs-angular" % Versions.library,
        "com.github.benhutchison" %%% "prickle" % Versions.prickle,
        "com.greencatsoft" %%% "greenlight" % "0.2-SNAPSHOT" % "test"
      )
    )

  // Use reflection to rename the 'start' command to 'play-start'
  Option(play.Play.playStartCommand.getClass.getDeclaredField("name")) map { field =>
    field.setAccessible(true)
    field.set(playStartCommand, "play-start")
  }

  // The new 'start' command optimises the JS before calling the Play 'start' renamed 'play-start'
  val preStartCommand = Command.args("start", "<port>") { (state: State, args: Seq[String]) =>
    Project.runTask(fullOptJS in (scalajs, Compile), state)
    state.copy(remainingCommands = ("play-start " + args.mkString(" ")) +: state.remainingCommands)
  }
}

object Versions {
  val app = "0.5-SNAPSHOT"
  val library = "0.5-SNAPSHOT"
  val scala = "2.11.7"
  val prickle = "1.1.4"
}
