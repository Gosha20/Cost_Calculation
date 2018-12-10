package management

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{HttpApp, Route}
import com.softwaremill.session.{SessionConfig, SessionManager}
import management.entities.UserSession.UserSession
import management.routing.{AccountResource, PurchaseResource}
import management.services.{AccountService, PurchaseService}

import scala.concurrent.{ExecutionContext}

class Server(host: String, port: Int)(
    implicit system: ActorSystem,
    implicit val accountService: AccountService,
    implicit val purchaseService: PurchaseService,
    implicit val executionContext: ExecutionContext)
    extends HttpApp
    with PurchaseResource
    with AccountResource {

  def start(): Unit = startServer(host, port, system)

  val sessionConfig = SessionConfig.default(
    "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiop")

  implicit val sessionManager: SessionManager[UserSession] =
    new SessionManager[UserSession](sessionConfig)

  override protected def routes: Route = purchaseRoutes ~ accountRoutes
}
