//TODO this can be done in Java too

import _root_.info.schleichardt.play2.basicauth._
import java.net.{URISyntaxException, URI}
import play.api.{Application, Logger, Play, GlobalSettings}
import play.api.mvc.RequestHeader

object Global extends GlobalSettings {
  //-Dbasic.auth.enabled=true
  lazy val basicAuthEnabled: Boolean = {
    val enabled = Play.current.configuration.getBoolean("basic.auth.enabled").getOrElse(false)
    Logger.info("basic auth enabled: " + enabled)
    enabled
  }

  val credentialSource = new CredentialsFromConfCheck

  override def onRouteRequest(request: RequestHeader) = {
    if(Logger.isDebugEnabled && !request.path.startsWith("/assets")) {
      Logger.debug(request.path)
    }

    if (basicAuthEnabled) {
      requireBasicAuthentication(request, credentialSource) {
        super.onRouteRequest(request)
      }
    } else {
      super.onRouteRequest(request)
    }
  }

  override def onStart(app: Application) {
    checkBackendUrl(app)
    super.onStart(app)
  }

  def checkBackendUrl(app: Application) {
    if (!app.configuration.getBoolean("backend.mock").getOrElse(false)) {
      val maybeBackendUrl: Option[String] = app.configuration.getString("backend.url")
      if(maybeBackendUrl.isDefined) {
        val url: String = maybeBackendUrl.get
        try {
          new URI(url)//check for syntax exception
        } catch {
          case e: URISyntaxException => val message: String = "you must specify a valid backend.url in application.conf or in command line"
          Logger.error(message, e); throw new URISyntaxException(url, message, 0)
        }
      } else {
        Logger.error("backend.url must be specified")
      }
    }
  }
}