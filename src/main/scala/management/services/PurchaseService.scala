package management.services

import cats.effect.IO
import management.entities.{Purchase, PurchaseCreate, PurchaseUpdate}
import doobie.Transactor.Aux

import scala.concurrent.{ExecutionContext, Future}
import doobie.implicits._

class PurchaseService(val xa:Aux[IO, Unit])(implicit val executionContext: ExecutionContext) {

  var purchases = Vector.empty[Purchase]

  def createPurchase(purchase: PurchaseCreate): Future[Option[Int]] = {
        val place = purchase.place.getOrElse("some where")
        val thing = purchase.thing.getOrElse("some thing")
        val money = purchase.money

        sql"""insert into buys (place, thing, money)
         values (${place},${thing},${money}) returning id""".query[Int].option.transact(xa).unsafeToFuture()
    }

  def getPurchase(id: Int): Future[Option[Purchase]] =
    sql"""select place, thing, money, id from buys where buys.id = ${id}""".query[Purchase].option.transact(xa).unsafeToFuture()

  def getAll: Future[List[Purchase]] = sql"""select place, thing, money, id from buys""".query[Purchase].stream.compile.toList.transact(xa).unsafeToFuture()
}

