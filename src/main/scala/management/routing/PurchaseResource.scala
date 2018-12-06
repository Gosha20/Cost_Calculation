package management.routing

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Route
import com.softwaremill.session.SessionDirectives.{setSession, touchRequiredSession}
import com.softwaremill.session.SessionManager
import com.softwaremill.session.SessionOptions.{oneOff, usingHeaders}
import management.entities.PurchaseCreate
import management.entities.UserSession.{AuthFormat, RegisterFormat, UserSession}
import management.services.PurchaseService

import scala.collection.mutable
import scala.concurrent.Future

trait PurchaseResource extends MyResource {
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
            complete(purchaseService.getPurchase(id))
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
