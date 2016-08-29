package todomvc.example

import com.greencatsoft.angularjs.core.{ Location, Timeout }
import com.greencatsoft.angularjs.{ AngularExecutionContextProvider, AbstractController, injectable }

import org.scalajs.dom.console

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.JSExport
import scala.util.{ Failure, Success }

/**
 * The main controller for the application.
 *
 * The controller
 * <ul>
 *   <li>retrieves and persists the model via the $http service</li>
 *   <li>exposes the model to the template and provides event handlers</li>
 * </ul>
 */
@JSExport
@injectable("todoCtrl")
class TodoCtrl(
  scope: TodoScope,
  location: Location,
  service: TaskServiceProxy,
  val timeout: Timeout)
  extends AbstractController[TodoScope](scope)
  with AngularExecutionContextProvider {

  // Need to initialize scope properties here, since we cannot declare default values  
  // for properties of a class which extends js.Object. 
  scope.todos = js.Array[Task]()
  scope.newTitle = ""
  scope.location = location
  scope.statusFilter = literal()

  service.findAll() onComplete {
    case Success(tasks) =>
      scope.todos = tasks.toJSArray

      update()
    case Failure(t) => handleError(t)
  }

  def todos: Seq[Task] = scope.todos

  @JSExport
  def save(todo: Task) {
    service.update(todo) onComplete {
      case Success(task) =>
        scope.todos = scope.todos map {
          case t if t.id == task.id => task
          case t => t
        }

        update()
      case Failure(t) => handleError(t)
    }
  }

  @JSExport
  def add() {
    Option(scope.newTitle).map(_.trim).filter(_.nonEmpty) foreach {
      add(_, scope)
    }
  }

  private def add(title: String, scope: TodoScope) {
    service.create(title) onComplete {
      case Success(newTask) =>
        scope.todos :+= newTask
        scope.newTitle = ""

        update()
      case Failure(t) => handleError(t)
    }
  }

  @JSExport
  def remove(todo: Task) {
    service.delete(todo.id) onComplete {
      case Success(_) =>
        scope.todos = todos.filter(_ != todo).toJSArray
        update()
      case Failure(t) => handleError(t)
    }
  }

  @JSExport
  def clearCompleted() {
    service.clearAll() onComplete {
      case Success(_) =>
        scope.todos = todos.filter(!_.completed).toJSArray
        update()
      case Failure(t) => handleError(t)
    }
  }

  @JSExport
  def markAll(completed: Boolean) {
    service.markAll(completed) onComplete {
      case Success(_) =>
        scope.todos = todos.map(_.complete(completed)).toJSArray
        update()
      case Failure(t) => handleError(t)
    }
  }

  private def update() {
    scope.remainingCount = todos.count(!_.completed)
    scope.allChecked = scope.remainingCount == 0
  }

  private def handleError(t: Throwable) {
    console.error(s"An error has occurred: '$t'.")

    t.printStackTrace()
  }
}
