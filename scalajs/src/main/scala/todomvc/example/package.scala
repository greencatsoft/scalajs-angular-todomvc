package todomvc

import scala.collection.mutable
import scala.scalajs.js.annotation.JSExportAll
import scala.util.{ Success, Try }

import prickle.{ PConfig, PickleState, Pickler, Unpickler }
import todomvc.example.Task

package object example {

  // This shouldn't be needed, but it seems there's a limitation in either Prickle or Scala.js 
  // which prevents automatic pickling for case classes annotated with @JSExport from working.
  //
  // The issue will be tracked in the upstream project, and below lines will be removed when 
  // it's fixed.
  implicit object TaskUnpickler extends Unpickler[Task] {

    override def unpickle[P](pickle: P, state: mutable.Map[String, Any])(implicit config: PConfig[P]): Try[Task] = for {
      id <- config.readObjectFieldNum(pickle, "id").map(_.toLong)
      title <- config.readObjectFieldStr(pickle, "title")
      completed <- config.readObjectField(pickle, "completed").map(config.readBoolean(_)).flatten
    } yield Task(title, completed, id)
  }
}