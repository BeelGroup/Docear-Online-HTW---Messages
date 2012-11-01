import play.api.GlobalSettings
import play.api.mvc.RequestHeader
import info.schleichardt.play2.basicauth._

object Global extends GlobalSettings {

  val credentialSource = new CredentialsFromConfCheck

  override def onRouteRequest(request: RequestHeader) =
    requireBasicAuthentication(request, credentialSource) {
      super.onRouteRequest(request)
    }
}