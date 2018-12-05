package management.resources

import akka.http.scaladsl.server.Route
import management.entities.{Purchase, PurchaseCreate, PurchaseUpdate}
import akka.http.scaladsl.server.directives.RouteDirectives
import management.services.PurchaseService
import management.routing.MyResource

trait PurchaseResource extends MyResource {

  val purchaseService: PurchaseService

  def questionRoutes: Route = pathPrefix("purchase") {
    (path("add") & post) {
      entity(as[PurchaseCreate]) { purchase => {
                  completeWithLocationHeader(
                    resourceId = purchaseService.createPurchase(purchase),
                    ifDefinedStatus = 201, ifEmptyStatus = 409)}
                  }
      } ~
        path(IntNumber) { id =>
          get { complete(purchaseService.getPurchase(id)) }
        } ~
      (path("all") & get) {
        get { complete(purchaseService.getAll)}
      }
  }
}

