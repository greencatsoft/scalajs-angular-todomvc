import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.AutoImport._

object Dependencies {

  object angular {
    val version = "1.5.7"

    def api = "org.webjars.bower" % "angular" % version
    def scala = "com.greencatsoft" %%%! "scalajs-angular" % "0.7-SNAPSHOT"
  }

  object jquery {
    val version = "2.2.4"

    def api = "org.webjars.bower" % "jquery" % version force ()
  }

  object prickle {
    val version = "1.1.10"

    def js = "com.github.benhutchison" %%%! "prickle" % version
    def jvm = "com.github.benhutchison" %% "prickle" % version
  }

  object squeryl {
    def api = "org.squeryl" %% "squeryl" % "0.9.5-7"
  }
}