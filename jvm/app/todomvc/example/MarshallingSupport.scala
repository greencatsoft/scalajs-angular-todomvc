package todomvc.example

import play.api.http.Writeable
import play.api.libs.json._
import play.api.mvc.{ Codec, Controller }

import prickle.{ Pickle, Pickler, Unpickle, Unpickler }

import scala.util.{ Failure, Success }

trait MarshallingSupport {
  this: Controller =>

  implicit def modelReads[A](implicit unpickler: Unpickler[A]): Reads[A] = {
    new Reads[A] {
      def reads(json: JsValue) = {
        Unpickle[A].fromString(json.toString) match {
          case Success(value) => JsSuccess(value)
          case Failure(t) => JsError("error.expected.jsnumber")
        }
      }
    }
  }

  implicit def modelWriteable[A](implicit pickler: Pickler[A], codec: Codec): Writeable[A] = {
    Writeable(data => codec.encode(Pickle.intoString(data)), Some(JSON))
  }

  implicit def writeableModelSeq[A](implicit pickler: Pickler[A], codec: Codec): Writeable[Seq[A]] = {
    Writeable(data => codec.encode(Pickle.intoString(data)), Some(JSON))
  }
}
