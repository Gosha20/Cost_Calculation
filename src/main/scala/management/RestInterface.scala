package management

import scala.concurrent.ExecutionContext

import akka.http.scaladsl.server.Route

import management.resources.PurchaseResource
import management.services.PurchaseService

trait RestInterface extends Resources {

  implicit def executionContext: ExecutionContext

  lazy val questionService = new PurchaseService

  val routes: Route = questionRoutes

}

trait Resources extends PurchaseResource

