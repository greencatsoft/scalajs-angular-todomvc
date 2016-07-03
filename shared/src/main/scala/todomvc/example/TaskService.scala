package todomvc.example

import scala.concurrent.Future

trait TaskService {

  def findAll(): Future[Seq[Task]]

  def create(title: String): Future[Task]

  def update(task: Task): Future[Task]

  def delete(id: Long): Future[Unit]

  def clearAll(): Future[Unit]

  def markAll(completed: Boolean): Future[Unit]
}
