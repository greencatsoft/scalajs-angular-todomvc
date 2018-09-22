package todomvc.example

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExportTopLevel

import com.greencatsoft.angularjs.core.HttpProvider
import com.greencatsoft.angularjs.{ Angular, Config }

@JSExportTopLevel("TodoApp")
object TodoApp extends JSApp {

  override def main() {
    Angular.module("todomvc")
      .config[TodoAppConfig]
      .controller[TodoCtrl]
      .directive[TodoItemDirective]
      .directive[EscapeDirective]
      .directive[FocusDirective]
      .filter[StatusFilter]
      .factory[TaskServiceProxy.Factory]
  }

  class TodoAppConfig(httpProvider: HttpProvider) extends Config {

    httpProvider.defaults.xsrfHeaderName = "Csrf-Token"
  }
}
