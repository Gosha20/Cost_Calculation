package management

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Route
import cats.effect.IO
import management.services.{AccountService, PurchaseService}
import doobie.Transactor.Aux
import management.routing.{AccountResource, PurchaseResource}

trait RestInterface extends Resources {
  implicit val xa:Aux[IO, Unit]
  implicit def executionContext: ExecutionContext

  lazy val purchaseService = new PurchaseService(xa)
  lazy val accountService = new AccountService(xa)

  val routes: Route

}

trait Resources extends PurchaseResource with AccountResource

