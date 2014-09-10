package controllers

import play.api.libs.json.Json.toJson
import play.api.mvc.{ Action, Controller }
import todomvc.example.{ Task, TaskStore }
import views.html.index

object TodoController extends Controller {

  import org.scalajs.spickling.playjson._

  def home() = Action {
    Ok(index())
  }

  def list() = Action {
    Ok(toJson(TaskStore.findAll))
  }

  def create() = Action(parse.json) { request =>
    val task = request.body.as[Task]

    TaskStore.create(task)

    Ok(toJson(task))
  }

  def update(id: Long) = Action(parse.json) { request =>
    val task = request.body.as[Task]

    TaskStore.update(task)

    Ok(toJson(task))
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
