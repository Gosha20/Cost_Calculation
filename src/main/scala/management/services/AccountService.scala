package management.services

import cats.effect.IO
import doobie.Transactor.Aux
import scala.concurrent.{ExecutionContext, Future}
import doobie.implicits._
import management.entities.UserSession.{AuthFormat, RegisterFormat}

class AccountService(implicit val executionContext: ExecutionContext, implicit val xa:Aux[IO, Unit]) {

  def login(authFormat: AuthFormat): Future[Option[String]] = {
    sql"""select login from users where login = ${authFormat.login} and password = ${authFormat.password}""".query[String].option.transact(xa).unsafeToFuture()
  }

  def registration(registerFormat: RegisterFormat): Future[Int] =
    sql"""insert into users (login, password, name)
         values (${registerFormat.login}, ${registerFormat.password}, ${registerFormat.name})
         on conflict do nothing""".update.run.transact(xa).unsafeToFuture()
}