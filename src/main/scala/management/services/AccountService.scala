package management.services

import cats.effect.IO
import doobie.Transactor.Aux

import scala.concurrent.{ExecutionContext, Future}
import doobie.implicits._
import doobie.util.transactor.Transactor
import management.entities.UserSession.{AuthFormat, RegisterFormat}

class AccountService(implicit val xa: Transactor[IO]) {

  def login(authFormat: AuthFormat): IO[Option[String]] =
    sql"""select login from users where login = ${authFormat.login} and password = ${authFormat.password}"""
      .query[String]
      .option
      .transact(xa)

  def registration(registerFormat: RegisterFormat): IO[Int] =
    sql"""insert into users (login, password, name)
         values (${registerFormat.login}, ${registerFormat.password}, ${registerFormat.name})
         on conflict do nothing""".update.run.transact(xa)
}
