import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

organization in ThisBuild := "com.greencatsoft"

version in ThisBuild := "0.8-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.11"

scalacOptions in ThisBuild ++= Seq("-deprecation", "-unchecked", "-feature")

resolvers in ThisBuild += Resolver.sonatypeRepo("snapshots")

import Dependencies._

lazy val crossType = CrossType.Full

lazy val root = project.in(file("."))
  .aggregate(client, server)
  .settings(
    name := "todomvc",
    run in Compile := (run in Compile in server).evaluated)

lazy val server = todomvc.jvm
  .enablePlugins(PlayScala)
  .settings(
    name := "todomvc-server",
    testOptions in Test += {
      Tests.Argument("-oFD", "-u", ((target in Test).value / "test-reports").getCanonicalPath)
    },
    pipelineStages := Seq(scalaJSProd),
    scalaJSProjects := Seq(todomvc.js),
    stage := { stage dependsOn(WebKeys.assets, fullOptJS in(client, Compile)) }.value,
    routesGenerator := InjectedRoutesGenerator,
    libraryDependencies ++= Seq(
      guice,
      db.driver,
      db.slick.api,
      db.slick.evolutions,
      prickle.jvm,
      scalaJs.stubs,
      js.jquery,
      js.angular))

lazy val client = todomvc.js
  .enablePlugins(ScalaJSPlay)
  .settings(
    name := "todomvc-client",
    libraryDependencies ++= Seq(
      scalaJs.angular,
      scalaJs.jquery,
      prickle.js),
    jsDependencies ++= Seq(
      js.jquery / "dist/jquery.js" minified "dist/jquery.min.js",
      js.angular / "angular.js" minified "angular.min.js" dependsOn "dist/jquery.js",
      RuntimeDOM),
    persistLauncher := true,
    relativeSourceMaps := true,
    skip in packageJSDependencies := false,
    jsEnv in Test := PhantomJSEnv(args = Seq("--web-security=no")).value)

lazy val todomvc = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("."))
  .settings(
    name := "todomvc-common",
    unmanagedSourceDirectories in Compile :=
      Seq((scalaSource in Compile).value) ++
        crossType.sharedSrcDir(baseDirectory.value, "main"),
    unmanagedSourceDirectories in Test :=
      Seq((scalaSource in Test).value) ++
        crossType.sharedSrcDir(baseDirectory.value, "test"),
    testOptions in Test := Seq(Tests.Filter(_.endsWith("Test"))))

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
