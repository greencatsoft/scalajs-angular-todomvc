package todomvc.example

import com.greencatsoft.angularjs.core.Timeout
import com.greencatsoft.angularjs._
import org.scalajs.dom.html.Html
import org.scalajs.dom.{ Element, KeyboardEvent }

import scala.scalajs.js
import scala.scalajs.js.{ Dictionary, UndefOr }
import scala.scalajs.js.annotation.JSExport

@JSExport
@injectable("todoItem")
class TodoItemDirective extends ElementDirective with TemplatedDirective with IsolatedScope {

  override type ScopeType = TodoItemScope

  override val templateUrl = "assets/templates/todo-item.html"

  bindings ++= Seq(
    "todo" := "item",
    "fireOnRemove" :& "onRemove",
    "fireOnChange" :& "onChange")

  override def link(scope: ScopeType, elems: Seq[Element], attrs: Attributes) {
    super.link(scope, elems, attrs)

    scope.title = scope.todo.title
    scope.completed = scope.todo.completed
  }

  @JSExport
  def onEditStart(scope: TodoItemScope) {
    scope.editing = true
  }

  @JSExport
  def onEditEnd(scope: TodoItemScope) {
    scope.editing = false

    onChange(scope)
  }

  @JSExport
  def onEditCancel(scope: TodoItemScope) {
    scope.editing = false
    scope.title = scope.todo.title
  }

  @JSExport
  def onChange(scope: TodoItemScope) {
    scope.todo = scope.todo.update(scope.title, scope.completed)

    scope.fireOnChange(Dictionary("task" -> scope.todo))
  }

  @JSExport
  def onRemove(scope: TodoItemScope) {
    scope.fireOnRemove(Dictionary("task" -> scope.todo))
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
        (newVal: UndefOr[js.Any]) =>
          if (newVal.isDefined) timeout(() => elem.focus(), 0, invokeApply = false))
    }
  }
}
