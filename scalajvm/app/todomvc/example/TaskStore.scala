package todomvc.example

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.PrimitiveTypeMode.update

import play.api.Logger
import todomvc.example.TaskSchema.tasks

object TaskStore {

  def create(task: Task): Task = {
    require(task != null, "Missing argument 'task'.")

    Logger.info(s"Creating task: $task")

    inTransaction {
      tasks insert task
    }
  }

  def update(task: Task) {
    require(task != null, "Missing argument 'task'.")

    Logger.info(s"Updating task: $task")

    inTransaction {
      tasks update task
    }
  }

  def delete(id: Long) {
    Logger.info(s"Deleting task: $id")

    inTransaction {
      tasks deleteWhere (_.id === id)
    }
  }

  def findAll(): Seq[Task] = {
    inTransaction {
      from(tasks)(select(_)).toList
    }
  }

  def markAll(completed: Boolean) {
    inTransaction {
      tasks.update(t =>
        where(t.completed <> completed)
          set (t.completed := completed))
    }
  }

  def clearAll() {
    inTransaction {
      tasks deleteWhere (_.completed === true)
    }
  }
}