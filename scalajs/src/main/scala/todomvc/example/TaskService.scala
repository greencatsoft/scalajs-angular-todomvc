package todomvc.example

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js
import scala.scalajs.js.{ Date, JSON }
import scala.scalajs.js.Any.fromString
import scala.util.{ Failure, Success, Try }

import com.greencatsoft.angularjs.{ Factory, Service }
import com.greencatsoft.angularjs.core.HttpPromise.promise2future
import com.greencatsoft.angularjs.core.HttpService
import com.greencatsoft.angularjs.injectable

import prickle.{ Pickle, Unpickle }

@injectable("taskService")
class TaskService(http: HttpService) extends Service {
  require(http != null, "Missing argument 'http'.")

  def findAll(): Future[Seq[Task]] = flatten {
    // Append a timestamp to prevent some old browsers from caching the result.
    val url = parameterizeUrl("/api/todos", Map("ts" -> Date.now))

    http.get[js.Any](url)
      .map(JSON.stringify(_))
      .map(Unpickle[Seq[Task]].fromString(_))
  }

  def create(task: Task): Future[Task] = flatten {
    require(task != null, "Missing argument 'task'.")

    http.put[js.Any](s"/api/todos", Pickle.intoString(task))
      .map(JSON.stringify(_))
      .map(Unpickle[Task].fromString(_))
  }

  def update(task: Task): Future[Task] = flatten {
    require(task != null, "Missing argument 'task'.")

    http.post[js.Any](s"/api/todos/${task.id}", Pickle.intoString(task))
      .map(JSON.stringify(_))
      .map(Unpickle[Task].fromString(_))
  }

  def delete(id: Long): Future[Unit] = http.delete[Unit](s"/api/todos/$id")

  def clearAll(): Future[Unit] = http.post[Unit]("/api/todos/clearAll")

  def markAll(completed: Boolean): Future[Unit] =
    http.post[Unit](s"/api/todos/markAll?completed=${!completed}")

  protected def parameterizeUrl(url: String, parameters: Map[String, Any]): String = {
    require(url != null, "Missing argument 'url'.")
    require(parameters != null, "Missing argument 'parameters'.")

    parameters.foldLeft(url)((base, kv) =>
      base ++ { if (base.contains("?")) "&" else "?" } ++ kv._1 ++ "=" + kv._2)
  }

  protected def flatten[T](future: Future[Try[T]]): Future[T] = future flatMap {
    case Success(s) => Future.successful(s)
    case Failure(f) => Future.failed(f)
  }
}

@injectable("taskService")
class TaskServiceFactory(http: HttpService) extends Factory[TaskService] {

  override def apply(): TaskService = new TaskService(http)
}
