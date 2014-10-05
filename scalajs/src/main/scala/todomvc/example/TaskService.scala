package todomvc.example

import scala.collection.mutable
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js
import scala.scalajs.js.{ Date, JSON }
import scala.scalajs.js.Any.fromString
import scala.util.{ Failure, Success, Try }

import com.greencatsoft.angularjs.http.HttpPromise.promise2future
import com.greencatsoft.angularjs.http.HttpService

import microjson.{ JsValue, Json }
import prickle.{ PConfig, Pickle, Unpickle }

object TaskService {

  def findAll()(implicit http: HttpService): Future[Seq[Task]] = flatten {
    // Append a timestamp to prevent some old browsers from caching the result.
    val url = parameterizeUrl("/api/todos", Map("ts" -> Date.now))

    val future: Future[js.Any] = http.get(url)

    future
      .map(JSON.stringify(_))
      .map(Unpickle[Seq[Task]].fromString(_))
  }

  def create(task: Task)(implicit http: HttpService): Future[Task] = flatten {
    require(task != null, "Missing argument 'task'.")

    val future: Future[js.Any] = http.put(s"/api/todos", Pickle.intoString(task))

    future
      .map(JSON.stringify(_))
      .map(Unpickle[Task].fromString(_))
  }

  def update(task: Task)(implicit http: HttpService): Future[Task] = flatten {
    require(task != null, "Missing argument 'task'.")

    val future: Future[js.Any] = http.post(s"/api/todos/${task.id}", Pickle.intoString(task))

    future
      .map(JSON.stringify(_))
      .map(Unpickle[Task].fromString(_))
  }

  def delete(id: Long)(implicit http: HttpService): Future[Unit] = http.delete(s"/api/todos/$id")

  def clearAll()(implicit http: HttpService): Future[Unit] = http.post("/api/todos/clearAll")

  def markAll(completed: Boolean)(implicit http: HttpService): Future[Unit] =
    http.post(s"/api/todos/markAll?completed=${!completed}")

  protected def parameterizeUrl(url: String, parameters: Map[String, Any]): String = {
    require(url != null, "Missing argument 'url'.")
    require(parameters != null, "Missing argument 'parameters'.")

    parameters.foldLeft(url)((base, kv) =>
      base ++ { if (base.contains("?")) "&" else "?" } ++ kv._1 ++ "=" + kv._2)
  }

  protected def flatten[T](future: Future[Try[T]]): Future[T] = future flatMap {
    _ match {
      case Success(s) => Future.successful(s)
      case Failure(f) => Future.failed(f)
    }
  }
}