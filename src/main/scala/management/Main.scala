package management
import cats.effect.IO

import scala.concurrent.duration._
import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.softwaremill.session.{SessionConfig, SessionManager}
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import com.typesafe.config.ConfigFactory
import management.entities.UserSession.UserSession
import management.services.{AccountService, PurchaseService}

import scala.concurrent.ExecutionContextExecutor

object Main extends App {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val system: ActorSystem = ActorSystem("cost-management-service")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher


  implicit val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/test",
    "postgres",
    "postgres"
  )

  implicit val accountService: AccountService = new AccountService
  implicit val purchaseService: PurchaseService = new PurchaseService
  new Server(host, port).start()
}
