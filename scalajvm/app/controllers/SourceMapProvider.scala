package controllers

import java.io.File

import play.api.Play
import play.api.mvc.{ Action, Controller }

object SourceMapProvider extends Controller {

  def source(path: String, project: String) = Action {
    if (Play.isProd(Play.current))
      Forbidden("Browsing source files in production mode is not permitted.")
    else
      Ok.sendFile(new File(s"$project/$path"))
  }

  def client(path: String) = source(path, "scalajs")

  def common(path: String) = source(path, "scala")
}
