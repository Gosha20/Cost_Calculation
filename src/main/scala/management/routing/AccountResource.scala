package management.routing

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Route
import com.softwaremill.session.SessionDirectives.setSession
import com.softwaremill.session.SessionManager
import com.softwaremill.session.SessionOptions.{oneOff, usingHeaders}
import management.entities.UserSession.{AuthFormat, RegisterFormat, UserSession}
import management.services.AccountService


trait AccountResource extends Resource {
  implicit val sessionManager: SessionManager[UserSession]
  val accountService: AccountService

  def accountRoutes: Route = pathPrefix("account") {
      (path("register") & post) {
        entity(as[RegisterFormat]){registerFormat =>
          complete(accountService.registration(registerFormat).unsafeToFuture())
        }
      }~
      (path("login") & post) {
        entity(as[AuthFormat]) { authFormat =>
          onSuccess(accountService.login(authFormat).unsafeToFuture()) {
            case Some(login) => setSession(oneOff, usingHeaders, UserSession(login)) {
              complete(HttpResponse(StatusCodes.OK, entity = sessionManager.clientSessionManager.createHeader(UserSession(login)).value))
            }
            case _ => complete(HttpResponse(StatusCodes.Unauthorized))
          }
        }
      }
  }
}
