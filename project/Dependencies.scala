import sbt._
import org.scalajs.sbtplugin.ScalaJSCrossVersion
import org.scalajs.sbtplugin.ScalaJSPlugin.AutoImport._

object Dependencies {

  object scalaJs {
    def stubs = "org.scala-js" %% "scalajs-stubs" % scalaJSVersion
    def angular = "com.greencatsoft" % "scalajs-angular" % "0.8-SNAPSHOT" cross ScalaJSCrossVersion.binary
    def jquery = "be.doeraene" % "scalajs-jquery" % "0.9.0" cross ScalaJSCrossVersion.binary
  }

  object js {
    def angular = "org.webjars.bower" % "angular" % "1.5.7"
    def jquery = "org.webjars.bower" % "jquery" % "2.2.4" force ()
  }

  object prickle {
    val version = "1.1.10"

    def js = "com.github.benhutchison" % "prickle" % version cross ScalaJSCrossVersion.binary
    def jvm = "com.github.benhutchison" %% "prickle" % version
  }

  object db {
    def driver = "com.h2database" % "h2" % "1.4.192"

    object slick {
      val version = "3.0.3"

      def api = "com.typesafe.play" %% "play-slick" % version
      def evolutions = "com.typesafe.play" %% "play-slick-evolutions" % version
    }
  }
}
