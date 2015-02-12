package todomvc.example

import scala.scalajs.js
import scala.scalajs.js.Any.{ fromFunction0, fromFunction1, fromString }
import scala.scalajs.js.UndefOr
import scala.scalajs.js.UndefOr.{ undefOr2jsAny, undefOr2ops }
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.{ Element, KeyboardEvent }
import org.scalajs.dom.html.Html

import com.greencatsoft.angularjs.{ AttributeDirective, Attributes, Controller, ElementDirective, IsolatedScope, TemplatedDirective }
import com.greencatsoft.angularjs.core.{ Scope, Timeout }
import com.greencatsoft.angularjs.inject

@JSExport
object TodoItemDirective extends ElementDirective with TemplatedDirective with IsolatedScope {

  override val name = "todoItem"

  override val templateUrl = "assets/templates/todo-item.html"

  bindings ++= Seq(
    "todo" := "item",
    "fireOnRemove" :& "onRemove",
    "fireOnChange" :& "onChange")

  @JSExport
  def onEditStart(scope: ScopeType) {
    scope.editing = true
    scope.title = scope.todo.title
  }

  @JSExport
  def onEditEnd(scope: ScopeType) {
    scope.editing = false
    scope.todo.title = scope.title

    scope.fireOnChange()
  }

  @JSExport
  def onEditCancel(scope: ScopeType) {
    scope.editing = false
    scope.title = scope.todo.title
  }

  class ScopeType extends Scope {

    var title: String = js.native

    var editing: Boolean = js.native

    def todo: Task = js.native

    def fireOnRemove(): Unit = js.native

    def fireOnChange(): Unit = js.native
  }
}

object EscapeDirective extends AttributeDirective {

  override val name = "todoEscape"

  override def link(scope: ScopeType, elems: Seq[Element], attrs: Attributes, controllers: Controller*) {
    elems.headOption.map(_.asInstanceOf[Html]) foreach { elem =>
      elem.onkeydown = (event: KeyboardEvent) =>
        if (event.keyCode == 27) scope.$apply(attrs(name))
    }
  }
}

object FocusDirective extends AttributeDirective {

  override val name = "todoFocus"

  @inject
  var timeout: Timeout = _

  override def link(scope: ScopeType, elems: Seq[Element], attrs: Attributes, controllers: Controller*) {
    elems.headOption.map(_.asInstanceOf[Html]) foreach { elem =>

      scope.$watch(attrs(name),
        (newVal: UndefOr[js.Any]) => if (newVal.isDefined) timeout(() => elem.focus(), 0, false))
    }
  }
}
