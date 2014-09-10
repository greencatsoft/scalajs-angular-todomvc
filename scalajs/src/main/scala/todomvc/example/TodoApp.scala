package todomvc.example

import scala.scalajs.js
import scala.scalajs.js.{ JSApp, UndefOr }
import scala.scalajs.js.Any.{ fromArray, fromFunction1, fromFunction2 }
import scala.scalajs.js.UndefOr.undefOr2ops
import scala.scalajs.js.annotation.JSExport

import org.scalajs.spickling.PicklerRegistry

import com.greencatsoft.angularjs.{ Module, angular }
import com.greencatsoft.angularjs.http.HttpProviderAware

@JSExport
object TodoApp extends JSApp with HttpProviderAware {

  override def main() {
    val app = angular.module("todomvc", Array.empty[String])

    // Module(app) returns a proxy for our module that provides the bridge for integration
    // between standard AngularJS and our typesafe + 'fluent' scalajs-angular bindings
    Module(app)
      .config(this)
      .controller(TodoCtrl)
      .directive(TodoItemDirective, EscapeDirective, FocusDirective)
  }

  override def initialize() {
    import org.scalajs.spickling.jsany._

    val converter = (data: js.Any) => {
      val typeInfo = data.asInstanceOf[js.Dynamic].t

      typeInfo.asInstanceOf[UndefOr[String]].fold(data) {
        t => PicklerRegistry.unpickle(data).asInstanceOf[js.Any]
      }
    }

    val unpickler = (data: js.Any, headers: js.Any) => data match {
      case list: js.Array[_] => list map {
        (item: Any) => converter(item.asInstanceOf[js.Any])
      }
      case _ => converter(data)
    }

    val pickler = (data: js.Any, headers: js.Any) => {
      data match {
        case _: Task => PicklerRegistry.pickle(data)
        case _ => data
      }
    }

    httpProvider.defaults.transformRequest.unshift(pickler)
    httpProvider.defaults.transformResponse.push(unpickler)

    PicklerRegistry.register[Task]
  }
}
