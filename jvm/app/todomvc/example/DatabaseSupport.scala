package todomvc.example

import play.api.db.slick.DatabaseConfigProvider

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait DatabaseSupport {

  val dbConfigProvider: DatabaseConfigProvider

  val dbConfig: DatabaseConfig[JdbcProfile] =
    dbConfigProvider.get[JdbcProfile] ensuring (_ != null)
}
