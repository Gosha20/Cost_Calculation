package management

import cats.effect.IO
import scala.concurrent.duration._
import akka.actor._
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import doobie._
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux

import scala.concurrent.{ExecutionContext, Future}
import com.typesafe.config.ConfigFactory

object Main extends App with RestInterface {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val system = ActorSystem("quiz-management-service")
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

//
//  val xa = Transactor.fromDriverManager[IO](
//    "org.postgresql.Driver", "jdbc:postgresql://localhost:5432/test", "postgres", "postgres"
//  )
//
//  val create: Update0 =
//    sql"""
//    CREATE TABLE person (
//      id   SMALLINT,
//      name VARCHAR NOT NULL UNIQUE,
//      age  SMALLINT
//    )
//  """.update
//
//val append = sql"""insert into person (id, name, age)
//         values (1, Gosha, 24)
//         on conflict do nothing""".update
//val view = sql"""select name from person""".query[String].option
//  println(create.run.transact(xa).unsafeToFuture())
////  println(append.run.transact(xa).attempt.unsafeToFuture())
//  println(view.transact(xa).unsafeToFuture())
  val api = routes
//
  Http().bindAndHandle(handler = api, interface = host, port = port) map { binding =>
    println(s"REST interface bound to ${binding.localAddress}") } recover { case ex =>
    println(s"REST interface could not bind to $host:$port", ex.getMessage)
  }
}
