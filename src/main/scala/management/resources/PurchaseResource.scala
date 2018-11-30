package management.resources

import akka.http.scaladsl.server.Route
import management.entities.{Purchase, PurchaseCreate, PurchaseUpdate}
import management.routing.MyResource
import management.services.PurchaseService
import management.routing.MyResource

trait PurchaseResource extends MyResource {

  val questionService: PurchaseService

  def questionRoutes: Route = pathPrefix("purchase") {
    (path("add") & post) {
        entity(as[PurchaseCreate]) { purchase => {
          println("post")
          completeWithLocationHeader(
            resourceId = questionService.createPurchase(purchase),
            ifDefinedStatus = 201, ifEmptyStatus = 409)}
          }
    } ~
    path(IntNumber) { id =>
      get {
        complete(questionService.getPurchase(id))
      } ~
//      put {
//        entity(as[PurchaseUpdate]) { update =>
//          complete(questionService.updatePurchase(id, update))
//        }
//      } ~
      delete {
        complete(questionService.deleteQuestion(id))
      }
    }

  }
}

