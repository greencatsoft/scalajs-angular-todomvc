package todomvc.example

import scala.scalajs.js
import com.greencatsoft.angularjs.core.{ Location, Scope }
import com.greencatsoft.angularjs.injectable

trait TodoScope extends Scope {

  var todos: js.Array[Task] = js.native

  var newTitle: String = js.native

  var allChecked: Boolean = js.native

  var remainingCount: Int = js.native

  var location: Location = js.native

  var statusFilter: js.Dynamic = js.native
}

trait TodoItemScope extends Scope {

  var title: String = js.native

  var editing: Boolean = js.native

  def todo: Task = js.native

  def fireOnRemove(): Unit = js.native

  def fireOnChange(): Unit = js.native
}