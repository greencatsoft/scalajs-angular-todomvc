package todomvc.example

import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class Task(var title: String, var completed: Boolean = false, val id: Long = -1)