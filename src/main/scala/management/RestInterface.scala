package management

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Route
import cats.effect.IO
import management.resources.PurchaseResource
import management.services.PurchaseService
import doobie.Transactor.Aux

trait RestInterface extends Resources {
  implicit val xa:Aux[IO, Unit]
  implicit def executionContext: ExecutionContext

  lazy val purchaseService = new PurchaseService(xa)

  val routes: Route = questionRoutes

}

trait Resources extends PurchaseResource

