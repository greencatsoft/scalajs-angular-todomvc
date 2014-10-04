import org.squeryl.{ Session, SessionFactory }
import org.squeryl.internals.DatabaseAdapter

import play.api
import play.api.{ GlobalSettings, Logger }
import play.api.db.DB

object Application extends GlobalSettings {

  override def onStart(app: api.Application) {
    Logger.info("Starting TodoMVC application.")

    initializeDatabase(app)
  }

  protected def initializeDatabase(implicit app: api.Application) {
    Logger.info("Initializing database session.")

    val AdapterConfKey = "db.default.adapter"

    val adapterConf = app.configuration.getString(AdapterConfKey)

    val adapterClass = adapterConf getOrElse {
      throw configuration.reportError(AdapterConfKey, s"Missing configuration '$AdapterConfKey'.")
    }

    val adapter = Class.forName(adapterClass).newInstance().asInstanceOf[DatabaseAdapter]

    SessionFactory.concreteFactory = Some(() => Session.create(DB.getConnection(), adapter))
  }
}