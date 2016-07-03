package todomvc.example

import com.greencatsoft.angularjs.Angular

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport
object TodoApp extends JSApp {

  override def main() {
    Angular.module("todomvc")
      .controller[TodoCtrl]
      .directive[TodoItemDirective]
      .directive[EscapeDirective]
      .directive[FocusDirective]
      .filter[StatusFilter]
      .factory[TaskServiceProxy.Factory]
  }
}
