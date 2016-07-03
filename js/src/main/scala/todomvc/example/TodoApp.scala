package todomvc.example

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

import com.greencatsoft.angularjs.{ Angular, injectable }

@JSExport
object TodoApp extends JSApp {

  override def main() {
    val module = Angular.module("todomvc")

    module
      .controller[TodoCtrl]
      .directive[TodoItemDirective]
      .directive[EscapeDirective]
      .directive[FocusDirective]
      .filter[StatusFilter]
      .factory[TaskServiceFactory]
  }
}
