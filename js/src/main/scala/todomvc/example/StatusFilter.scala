package todomvc.example

import scala.scalajs.js
import scala.scalajs.js.Any.jsArrayOps
import scala.scalajs.js.UndefOr
import scala.scalajs.js.UndefOr.{ any2undefOrA, undefOr2ops }

import com.greencatsoft.angularjs.Filter
import com.greencatsoft.angularjs.core.Location
import com.greencatsoft.angularjs.injectable

@injectable("statusFilter")
class StatusFilter(location: Location) extends Filter[js.Array[Task]] {

  override def filter(tasks: js.Array[Task]): js.Array[Task] = {
    val path: UndefOr[String] = location.path

    path.toOption match {
      case Some("/active") => tasks.filterNot(_.completed)
      case Some("/completed") => tasks.filter(_.completed)
      case _ => tasks
    }
  }
}