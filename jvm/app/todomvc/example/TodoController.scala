package todomvc.example

import javax.inject.{ Inject, Singleton }

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{ Action, Controller }

import views.html.index

@Singleton
class TodoController @Inject() (store: TaskServiceImpl) extends Controller
  with MarshallingSupport {
  require(store != null, "Missing argument 'store'.")

  import TodoController._

  def home() = Action {
    Ok(index())
  }

  def list() = Action async {
    store.findAll() map {
      Ok(_)
    }
  }

  def create() = Action.async(parse.form(createForm)) {
    implicit request =>
      store.create(request.body.title) map {
        Ok(_)
      }
  }

  def update(id: Long) = Action.async(parse.form(updateForm)) {
    implicit request =>
      store.update(request.body.task(id)) map {
        Ok(_)
      }
  }

  def delete(id: Long) = Action async {
    store.delete(id) map {
      _ => Ok
    }
  }

  def markAll(completed: Boolean) = Action async {
    store.markAll(completed) map {
      _ => Ok
    }
  }

  def clearAll() = Action async {
    store.clearAll() map {
      _ => Ok
    }
  }
}

object TodoController {

  case class CreateForm(title: String)

  case class UpdateForm(title: String, completed: Boolean) {

    def task(id: Long): Task = Task(id, title, completed)
  }

  object Constraints {
    val title = "title" -> text(minLength = 1, maxLength = 150)
  }

  val createForm = Form(
    mapping(
      Constraints.title)(CreateForm.apply)(CreateForm.unapply))

  val updateForm = Form(
    mapping(
      Constraints.title,
      "completed" -> boolean)(UpdateForm.apply)(UpdateForm.unapply))
}