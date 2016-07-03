package todomvc.example

import play.api.db.slick.DatabaseConfigProvider

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait DatabaseSupport {

  val dbConfigProvider: DatabaseConfigProvider

  val dbConfig: DatabaseConfig[JdbcProfile] =
    dbConfigProvider.get[JdbcProfile] ensuring (_ != null)
}
