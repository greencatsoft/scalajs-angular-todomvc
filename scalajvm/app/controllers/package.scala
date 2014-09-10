import scala.language.implicitConversions

import org.scalajs.spickling.PicklerRegistry

import play.api.libs.json.{ Format, JsSuccess, JsValue }
import todomvc.example.Task

package object controllers {

  class TaskFormat extends Format[Task] {
    import org.scalajs.spickling.playjson._

    def reads(json: JsValue) =
      JsSuccess(PicklerRegistry.unpickle(json).asInstanceOf[Task])

    def writes(model: Task) = PicklerRegistry.pickle(model)
  }

  implicit val taskFormat = new TaskFormat
}