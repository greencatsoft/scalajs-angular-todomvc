package todomvc

import prickle.{ PConfig, PickleState, Pickler }
import todomvc.example.Task

package object example {

  // This shouldn't be needed, but it seems there's a limitation in either Prickle or Scala.js 
  // which prevents automatic pickling for case classes annotated with @JSExport from working.
  //
  // The issue will be tracked in the upstream project, and below lines will be removed when 
  // it's fixed.
  implicit object TaskPickler extends Pickler[Task] {

    override def pickle[P](task: Task, state: PickleState)(implicit config: PConfig[P]): P =
      config.makeObjectFrom(
        ("id", config.makeNumber(task.id)),
        ("title", config.makeString(task.title)),
        ("completed", config.makeBoolean(task.completed)))
  }
}