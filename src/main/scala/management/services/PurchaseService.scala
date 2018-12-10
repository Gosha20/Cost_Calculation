package management.services

import cats.effect.IO
import doobie.implicits._
import doobie.util.transactor.Transactor
import management.entities.{Purchase, PurchaseCreate}

class PurchaseService(implicit val xa: Transactor[IO]) {

  def createPurchase(purchase: PurchaseCreate,
                     login: String): IO[Option[Int]] = {
    val place = purchase.place.getOrElse("some where")
    val thing = purchase.thing.getOrElse("some thing")
    val money = purchase.money

    sql"""insert into buys (place, thing, money, login)
         values ($place,$thing,$money, $login) returning id"""
      .query[Int]
      .option
      .transact(xa)
  }

  def getPurchase(id: Int, login: String): IO[Option[Purchase]] =
    sql"""select place, thing, money, id, login from buys where buys.id = $id and buys.login = $login"""
      .query[Purchase]
      .option
      .transact(xa)

  def getAll(login: String): IO[List[Purchase]] =
    sql"""select place, thing, money, id, login from buys where login = $login"""
      .query[Purchase]
      .stream
      .compile
      .toList
      .transact(xa)
}
