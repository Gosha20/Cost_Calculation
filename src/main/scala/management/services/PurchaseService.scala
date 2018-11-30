package management.services

import management.entities.{Purchase, PurchaseCreate, PurchaseUpdate}

import scala.concurrent.{ExecutionContext, Future}

class PurchaseService(implicit val executionContext: ExecutionContext) {

  var purchases = Vector.empty[Purchase]

  def createPurchase(purchase: PurchaseCreate): Future[Option[Int]] = Future {
        val place = purchase.place.getOrElse("some where")
        val thing = purchase.thing.getOrElse("some thing")
        val money = purchase.money
        val id = purchases.length
        val newPurchase = Purchase(id, place, thing, money)
        purchases = purchases :+ newPurchase
        Some(newPurchase.id)
    }

  def getPurchase(id: Int): Future[Option[Purchase]] = Future {
    purchases.find(_.id == id)
  }

  //TODO

//  def updatePurchase(id: Int, update: PurchaseUpdate): Future[Option[Purchase]] = {
//
//    def updateEntity(purchase: Purchase): Purchase = {
//      val place = update.place.getOrElse(purchase.place)
//      val thing = update.thing.getOrElse(purchase.thing)
//      val money = update.money.getOrElse(purchase.money)
//      Purchase(id, place, thing, money)
//    }
//
//    getPurchase(id).flatMap {
//      case None => Future {
//        None
//      } // No question found, nothing to update
//      case Some(purchase) =>
//        val updatedPurchase = updateEntity(purchase)
//        deleteQuestion(id).flatMap { _ =>
//          createPurchase(updatedPurchase).map(_ => Some(updatedPurchase))
//        }
//    }
//  }

  def deleteQuestion(id: Int): Future[Unit] = Future {
    purchases = purchases.filterNot(_.id == id)
  }


}

