package todomvc.example

import scala.concurrent.ExecutionContext

import javax.inject.{ Inject, Singleton }

import play.api.Environment
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{ BaseController, ControllerComponents, Request }
import play.filters.csrf.CSRF

import views.html.index

@Singleton
class TodoController @Inject()(val controllerComponents: ControllerComponents,
  store: TaskServiceImpl, env: Environment)(implicit ec: ExecutionContext) extends BaseController
  with MarshallingSupport {
  require(store != null, "Missing argument 'store'.")
  require(env != null, "Missing argument 'env'.")

  import TodoController._

  def home() = Action { implicit request =>
    accessToken // request is passed implicitly to accessToken
    Ok(index(env.mode))
  }

  def accessToken(implicit request: Request[_]) = {
    val token = CSRF.getToken // request is passed implicitly to CSRF.getToken
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
