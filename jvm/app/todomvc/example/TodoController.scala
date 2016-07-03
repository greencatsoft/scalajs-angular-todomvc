package todomvc.example

import play.api.libs.json.JsObject
import play.api.mvc.{ Action, Controller }
import prickle.{ Pickle, Unpickle }
import views.html.index

import scala.util.{ Failure, Success }

object TodoController extends Controller {

  def home() = Action {
    Ok(index())
  }

  def list() = Action {
    Ok(Pickle.intoString(TaskStore.findAll))
  }

  def create() = process(TaskStore.create(_))

  def update(id: Long) = process(TaskStore.update)

  def process(updater: Task => Unit) = Action(parse.json) { request =>
    val data = request.body.as[JsObject]

    Unpickle[Task].fromString(data.toString) match {
      case Success(task) =>
        updater(task)
        Ok(Pickle.intoString(task))
      case Failure(e) =>
        BadRequest(s"Failed to parse request : $e")
    }
  }

  def delete(id: Long) = Action {
    TaskStore.delete(id)
    Ok
  }

  def markAll(completed: Boolean) = Action {
    TaskStore.markAll(completed)
    Ok
  }

  def clearAll() = Action {
    TaskStore.clearAll()
    Ok
  }
}
