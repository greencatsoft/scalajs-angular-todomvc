package todomvc.example

import javax.inject.{ Inject, Singleton }

import play.api.libs.json.JsObject
import play.api.mvc.{ Action, Controller }

import prickle.{ Pickle, Unpickle }

import views.html.index

import scala.util.{ Failure, Success }

@Singleton
class TodoController @Inject() (store: TaskStore) extends Controller {
  require(store != null, "Missing argument 'store'.")

  def home() = Action {
    Ok(index())
  }

  def list() = Action {
    Ok(Pickle.intoString(store.findAll))
  }

  def create() = process(store.create(_))

  def update(id: Long) = process(store.update)

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
    store.delete(id)
    Ok
  }

  def markAll(completed: Boolean) = Action {
    store.markAll(completed)
    Ok
  }

  def clearAll() = Action {
    store.clearAll()
    Ok
  }
}
