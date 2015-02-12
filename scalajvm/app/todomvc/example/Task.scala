package todomvc.example

import org.squeryl
import org.squeryl.KeyedEntity

/**
 * @todo We should move this class to a shared project, once this issue is resolved:
 * https://github.com/scala-js/scala-js/issues/1006
 */
case class Task(var title: String, var completed: Boolean = false, id: Long = -1) extends KeyedEntity[Long]

object TaskSchema extends squeryl.Schema {

  val tasks = table[Task]
}