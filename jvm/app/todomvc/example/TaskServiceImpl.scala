package todomvc.example

import javax.inject.{ Inject, Singleton }

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Logger

import scala.concurrent.Future

@Singleton
class TaskServiceImpl @Inject() (dao: TaskDao) extends TaskService {
  require(dao != null, "Missing argument 'dao'.")

  import dao.dbConfig.db
  import dao.dbConfig.driver.api._
  import dao.tasks

  override def create(title: String): Future[Task] = {
    require(title != null, "Missing argument 'title'.")

    Logger.info(s"Creating task: '$title'.")

    db run {
      val rows = tasks returning tasks.map(_.id) into ((item, id) => item.copy(id = id))

      rows += Task(0, title, completed = false)
    }
  }

  override def update(task: Task): Future[Task] = {
    require(task != null, "Missing argument 'task'.")

    Logger.info(s"Updating task: '$task'.")

    val Task(id, title, completed) = task

    db run {
      tasks
        .filter(_.id === id)
        .map(t => (t.title, t.completed))
        .update((title, completed))
    }.map(_ => task)
  }

  override def delete(id: Long): Future[Unit] = {
    Logger.info(s"Deleting task: '$id'.")

    db run {
      tasks.filter(_.id === id).delete
    }.map(_ => Unit)
  }

  override def findAll(): Future[Seq[Task]] = {
    db run {
      tasks.sortBy(_.id).result
    }
  }

  override def markAll(completed: Boolean): Future[Unit] = {
    db run {
      tasks.map(_.completed).update(!completed)
    }.map(_ => Unit)
  }

  override def clearAll(): Future[Unit] = {
    db run {
      tasks.filter(_.completed === true).delete
    }.map(_ => Unit)
  }
}