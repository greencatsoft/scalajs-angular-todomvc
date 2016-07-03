package todomvc.example

import com.greencatsoft.angularjs.Service
import com.greencatsoft.angularjs.core.{ HttpConfig, HttpService }

import org.scalajs.jquery.jQuery

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.Dictionary
import scala.util.{ Failure, Success, Try }

trait HttpClientSupport {
  this: Service =>

  def http: HttpService

  def httpGet(url: String, parameters: (String, Any)*): Future[js.Any] = {
    require(url != null, "Missing argument 'url'.")
    require(parameters != null, "Missing argument 'parameters'.")

    val fullUrl = parameterizeUrl(url, parameters: _*)

    http.get[js.Any](fullUrl)
  }

  def httpPost(url: String, parameters: (String, Any)*): Future[js.Any] = {
    require(url != null, "Missing argument 'url'.")
    require(parameters != null, "Missing argument 'parameters'.")

    http.post[js.Any](url, encodeFormData(parameters: _*), HttpConfig.postHandler)
  }

  def httpPut(url: String, parameters: (String, Any)*): Future[js.Any] = {
    require(url != null, "Missing argument 'url'.")
    require(parameters != null, "Missing argument 'parameters'.")

    http.put[js.Any](url, encodeFormData(parameters: _*), HttpConfig.postHandler)
  }

  def httpDelete(url: String, parameters: (String, Any)*): Future[js.Any] = {
    require(url != null, "Missing argument 'url'.")
    require(parameters != null, "Missing argument 'parameters'.")

    val fullUrl = parameterizeUrl(url, parameters: _*)

    http.delete[js.Any](fullUrl)
  }

  def encodeFormData(parameters: (String, Any)*): String = {
    require(parameters != null, "Missing argument 'parameters'.")

    jQuery.param(Dictionary[Any](parameters: _*), traditional = true)
  }

  def parameterizeUrl(url: String, parameters: (String, Any)*): String = {
    require(url != null, "Missing argument 'url'.")
    require(parameters != null, "Missing argument 'parameters'.")

    parameters.foldLeft(url)((base, kv) =>
      base ++ { if (base.contains("?")) "&" else "?" } ++ kv._1 ++ "=" + kv._2)
  }

  def flatten[T](future: Future[Try[T]]): Future[T] = {
    require(future != null, "Missing argument 'future'.")

    future flatMap {
      case Success(s) => Future.successful(s)
      case Failure(f) => Future.failed(f)
    }
  }
}
