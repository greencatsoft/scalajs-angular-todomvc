package controllers

import scala.util.{ Failure, Success }

import play.api.libs.json.JsObject
import play.api.mvc.{ Action, Controller }
import prickle.{ Pickle, Unpickle }
import todomvc.example.{ Task, TaskStore }
import views.html.index

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
