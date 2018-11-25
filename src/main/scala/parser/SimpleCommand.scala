package parser

import java.sql.ResultSet

import DataBase.DBRes

object SimpleCommand {

  case class Buy(place: String, thing: String, sum: Any) extends Command {
    override def execute(): DBRes[Unit] = {
      DBRes.update("INSERT INTO costCalculator(place, thing, money) VALUES (?, ?, ?)",
      List(place, thing, sum))

    }
  }

  //TODO
//  case class FindMostExpensive(implicit read: ResultSet => String) extends Command {
//    override def execute(): DBRes[Unit] = {
//      DBRes.select("SELECT * FROM costCalculator ORDER BY money DESC", List())(read)
//    }
//  }

}
