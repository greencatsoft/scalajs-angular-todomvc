package todomvc.example

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

import com.greencatsoft.angularjs.Angular

@JSExport
object TodoApp extends JSApp {

  override def main() {
    val module = Angular.module("todomvc")

    module.controller(TodoCtrl)

    module.directive(TodoItemDirective)
    module.directive(EscapeDirective)
    module.directive(FocusDirective)

    module.factory(TaskServiceFactory)
  }
}
