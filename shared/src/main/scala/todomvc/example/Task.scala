package todomvc.example

import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class Task(id: Long, title: String, completed: Boolean = false) {

  def complete(): Task = Task(id, title, completed = true)

  def update(newTitle: String, done: Boolean): Task = {
    Task(id, newTitle, completed = done)
  }
}