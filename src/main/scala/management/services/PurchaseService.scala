package management.services

import cats.effect.IO
import management.entities.{Purchase, PurchaseCreate, PurchaseUpdate}
import doobie.Transactor.Aux

import scala.concurrent.{ExecutionContext, Future}
import doobie.implicits._

class PurchaseService(val xa:Aux[IO, Unit])(implicit val executionContext: ExecutionContext) {

  def createPurchase(purchase: PurchaseCreate, login: String): Future[Option[Int]] = {
        val place = purchase.place.getOrElse("some where")
        val thing = purchase.thing.getOrElse("some thing")
        val money = purchase.money

        sql"""insert into buys (place, thing, money, login)
         values ($place,$thing,$money, $login) returning id""".query[Int].option.transact(xa).unsafeToFuture()
    }

  def getPurchase(id: Int): Future[Option[Purchase]] =
    sql"""select place, thing, money, id from buys where buys.id = ${id}""".query[Purchase].option.transact(xa).unsafeToFuture()

  def getAll(login:String): Future[List[Purchase]] = sql"""select place, thing, money, id from buys where login = $login""".query[Purchase].stream.compile.toList.transact(xa).unsafeToFuture()
}

