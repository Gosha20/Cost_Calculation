package management.routing

import akka.http.scaladsl.server.Route
import com.softwaremill.session.SessionDirectives.{setSession, touchRequiredSession}
import com.softwaremill.session.SessionManager
import com.softwaremill.session.SessionOptions.{oneOff, usingHeaders}
import management.entities.PurchaseCreate
import management.entities.UserSession.UserSession
import management.services.PurchaseService

trait PurchaseResource extends Resource {
  implicit val sessionManager: SessionManager[UserSession]
  val purchaseService: PurchaseService

  def purchaseRouters: Route = touchRequiredSession(oneOff, usingHeaders) { session =>
    pathPrefix("purchase") {
      (path("add") & post) {
        entity(as[PurchaseCreate]) { purchase => {
          completeWithLocationHeader(
            resourceId = purchaseService.createPurchase(purchase, session.login),
            ifDefinedStatus = 201, ifEmptyStatus = 409)
        }
        }
      } ~
        path(IntNumber) { id =>
          get {
            complete(purchaseService.getPurchase(id, session.login))
          }
        } ~
        (path("all") & get) {
          get {
            complete(purchaseService.getAll(session.login))
          }
        }
    }
  }
}
