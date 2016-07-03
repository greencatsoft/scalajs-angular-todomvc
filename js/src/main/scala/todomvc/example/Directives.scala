package todomvc.example

import scala.scalajs.js
import scala.scalajs.js.Any.{ fromFunction0, fromFunction1, fromString }
import scala.scalajs.js.UndefOr
import scala.scalajs.js.UndefOr.{ undefOr2jsAny, undefOr2ops }
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.{ Element, KeyboardEvent }
import org.scalajs.dom.html.Html

import com.greencatsoft.angularjs.{ AttributeDirective, Attributes, ElementDirective, IsolatedScope, TemplatedDirective }
import com.greencatsoft.angularjs.{ inject, injectable }
import com.greencatsoft.angularjs.core.{ Scope, Timeout }

@JSExport
@injectable("todoItem")
class TodoItemDirective extends ElementDirective with TemplatedDirective with IsolatedScope {

  override val templateUrl = "assets/templates/todo-item.html"

  bindings ++= Seq(
    "todo" := "item",
    "fireOnRemove" :& "onRemove",
    "fireOnChange" :& "onChange")

  @JSExport
  def onEditStart(scope: TodoItemScope) {
    scope.editing = true
    scope.title = scope.todo.title
  }

  @JSExport
  def onEditEnd(scope: TodoItemScope) {
    scope.editing = false
    scope.todo.title = scope.title

    scope.fireOnChange()
  }

  @JSExport
  def onEditCancel(scope: TodoItemScope) {
    scope.editing = false
    scope.title = scope.todo.title
  }
}

@injectable("todoEscape")
class EscapeDirective extends AttributeDirective {

  override def link(scope: ScopeType, elems: Seq[Element], attrs: Attributes) {
    elems.headOption.map(_.asInstanceOf[Html]) foreach { elem =>
      elem.onkeydown = (event: KeyboardEvent) =>
        if (event.keyCode == 27) scope.$apply(attrs("todoEscape"))
    }
  }
}

@injectable("todoFocus")
class FocusDirective(timeout: Timeout) extends AttributeDirective {
  require(timeout != null, "Missing argument 'timeout'.")

  override def link(scope: ScopeType, elems: Seq[Element], attrs: Attributes) {
    elems.headOption.map(_.asInstanceOf[Html]) foreach { elem =>

      scope.$watch(attrs("todoFocus"),
        (newVal: UndefOr[js.Any]) => if (newVal.isDefined) timeout(() => elem.focus(), 0, false))
    }
  }
}
