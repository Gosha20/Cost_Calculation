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
import doobie.implicits._
import scala.concurrent.ExecutionContextExecutor

object Main extends App with RestInterface {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val system: ActorSystem = ActorSystem("cost-management-service")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(10 seconds)

  implicit val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/test",
    "postgres",
    "postgres"
  )

  val sessionConfig = SessionConfig.default(
    "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiop")

  implicit val sessionManager: SessionManager[UserSession] =
    new SessionManager[UserSession](sessionConfig)

  val routes: Route = purchaseRouters ~ accountResourse

  val api = routes

  Http().bindAndHandle(handler = api, interface = host, port = port) map { binding =>
    println(s"REST interface bound to ${binding.localAddress}") } recover { case ex =>
    println(s"REST interface could not bind to $host:$port", ex.getMessage)
  }
}
