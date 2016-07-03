package todomvc.example

import com.greencatsoft.angularjs
import com.greencatsoft.angularjs.core.{ HttpService, Timeout }
import com.greencatsoft.angularjs.{ AngularExecutionContextProvider, Service, injectable }

import prickle.Unpickle

import scala.concurrent.Future
import scala.scalajs.js.{ Date, JSON }

@injectable("taskService")
class TaskServiceProxy(val http: HttpService, val timeout: Timeout) extends TaskService
  with HttpClientSupport with AngularExecutionContextProvider with Service {
  require(http != null, "Missing argument 'http'.")
  require(timeout != null, "Missing argument 'timeout'.")

  override def findAll(): Future[Seq[Task]] = flatten {
    // Append a timestamp to prevent some old browsers from caching the result.
    httpGet("/api/todos", "ts" -> Date.now)
      .map(JSON.stringify(_))
      .map(Unpickle[Seq[Task]].fromString(_))
  }

  override def create(title: String): Future[Task] = flatten {
    require(title != null, "Missing argument 'title'.")

    httpPut("/api/todos", "title" -> title)
      .map(_.toString)
      .map(Unpickle[Task].fromString(_))
  }

  override def update(task: Task): Future[Task] = flatten {
    require(task != null, "Missing argument 'task'.")

    val Task(id, title, completed) = task

    httpPost(s"/api/todos/$id", "title" -> title, "completed" -> completed)
      .map(_.toString)
      .map(Unpickle[Task].fromString(_))
  }

  override def delete(id: Long): Future[Unit] = {
    httpDelete(s"/api/todos/$id") map { _ => }
  }

  override def clearAll(): Future[Unit] = {
    httpPost("/api/todos/clearAll") map { _ => }
  }

  override def markAll(completed: Boolean): Future[Unit] = {
    httpPost(s"/api/todos/markAll?completed=${!completed}") map { _ => }
  }
}

object TaskServiceProxy {

  @injectable("taskService")
  class Factory(http: HttpService, timeout: Timeout)
    extends angularjs.Factory[TaskService] {

    override def apply(): TaskService = new TaskServiceProxy(http, timeout)
  }
}
