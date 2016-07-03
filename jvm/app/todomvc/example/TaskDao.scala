package todomvc.example

import javax.inject.{ Inject, Singleton }

import play.api.db.slick.DatabaseConfigProvider

@Singleton
class TaskDao @Inject() (val dbConfigProvider: DatabaseConfigProvider)
  extends DatabaseSupport {
  require(dbConfigProvider != null, "Missing argument 'dbConfigProvider'.")

  import dbConfig.driver.api._

  val tasks: TableQuery[Schema] = TableQuery[Schema]

  class Schema(tag: Tag) extends Table[Task](tag, "Task") {

    def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

    def title = column[String]("title")

    def completed = column[Boolean]("completed")

    def * = (id, title, completed) <> (Schema.tupled, Schema.unapply)
  }

  object Schema {

    def tupled(value: (Long, String, Boolean)): Task = {
      Task(value._1, value._2, value._3)
    }

    def unapply(task: Task) = {
      val Task(id, title, completed) = task

      Some((id, title, completed))
    }
  }
}