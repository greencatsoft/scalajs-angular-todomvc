package todomvc.example

import scala.scalajs.js
import scala.scalajs.js.Any.{ fromFunction0, fromFunction1, fromString }
import scala.scalajs.js.UndefOr
import scala.scalajs.js.UndefOr.undefOr2ops
import scala.scalajs.js.annotation.{ JSBracketAccess, JSExport, JSExportAll }

import org.scalajs.dom.{ Element, HTMLElement, KeyboardEvent }

import com.greencatsoft.angularjs.controller.Controller
import com.greencatsoft.angularjs.directive.{ AttributeDirective, Attributes, ElementDirective, IsolatedScope, TemplateUrlProvider }
import com.greencatsoft.angularjs.scope.Scope
import com.greencatsoft.angularjs.timer.TimeoutAware

@JSExport
object TodoItemDirective extends ElementDirective with TemplateUrlProvider with IsolatedScope {

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

    var title: String = ???

    var editing: Boolean = ???

    def todo: Task = ???

    def fireOnRemove(): Unit = ???

    def fireOnChange(): Unit = ???
  }
}

object EscapeDirective extends AttributeDirective {

  override val name = "todoEscape"

  override def link(scope: ScopeType, elems: Seq[Element], attrs: Attributes, controllers: Controller*) {
    elems.headOption.map(_.asInstanceOf[HTMLElement]) foreach { elem =>
      elem.onkeydown = (event: KeyboardEvent) =>
        if (event.keyCode == 27) scope.$apply(attrs(name))
    }
  }
}

object FocusDirective extends AttributeDirective with TimeoutAware {

  override val name = "todoFocus"

  override def link(scope: ScopeType, elems: Seq[Element], attrs: Attributes, controllers: Controller*) {
    elems.headOption.map(_.asInstanceOf[HTMLElement]) foreach { elem =>

      scope.$watch(attrs(name),
        (newVal: UndefOr[js.Any]) => if (newVal.isDefined) timeout(() => elem.focus(), 0, false))
    }
  }
}
