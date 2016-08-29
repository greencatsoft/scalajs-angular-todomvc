package todomvc.example

import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class Task(id: Long, title: String, completed: Boolean = false) {

  def complete(completed: Boolean = true): Task = Task(id, title, completed = completed)

  def update(newTitle: String, done: Boolean): Task = {
    Task(id, newTitle, completed = done)
  }
}