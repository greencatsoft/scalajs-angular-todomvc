package todomvc.example

import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

import com.greencatsoft.angularjs.{ Module, angular }

@JSExport
object TodoApp extends JSApp {

  override def main() {
    val app = angular.module("todomvc", js.Array[String]())

    // Module(app) returns a proxy for our module that provides the bridge for integration
    // between standard AngularJS and our typesafe + 'fluent' scalajs-angular bindings
    val module = Module(app)

    module.controller(TodoCtrl)
    module.directive(TodoItemDirective)
    module.directive(EscapeDirective)
    module.directive(FocusDirective)
    module.factory(TaskServiceFactory)
  }
}
