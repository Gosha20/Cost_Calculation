package management
import akka.actor._
import akka.stream.ActorMaterializer
import cats.effect.IO
import com.typesafe.config.ConfigFactory
import doobie.util.transactor.Transactor
import management.services.{AccountService, PurchaseService}

import scala.concurrent.ExecutionContextExecutor

object Main extends App {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val system: ActorSystem = ActorSystem("cost-management-service")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  implicit val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    config.getString("db.url"),
    config.getString("db.user"),
    config.getString("db.password")
  )

  implicit val accountService: AccountService = new AccountService
  implicit val purchaseService: PurchaseService = new PurchaseService
  new Server(host, port).start()
}
