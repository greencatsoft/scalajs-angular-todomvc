package todomvc.example

import com.greencatsoft.angularjs.core.{ Location, Scope }

import scala.scalajs.js
import scala.scalajs.js.Dictionary

@js.native
trait TodoScope extends Scope {

  var todos: js.Array[Task] = js.native

  var newTitle: String = js.native

  var allChecked: Boolean = js.native

  var remainingCount: Int = js.native

  var location: Location = js.native

  var statusFilter: js.Dynamic = js.native
}

@js.native
trait TodoItemScope extends Scope {

  var title: String = js.native

  var completed: Boolean = js.native

  var editing: Boolean = js.native

  var todo: Task = js.native

  def fireOnRemove(arg: Dictionary[Task]): Unit = js.native

  def fireOnChange(arg: Dictionary[Task]): Unit = js.native
}