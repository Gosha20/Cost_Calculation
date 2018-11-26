package parser

import atto.Atto._
import atto._
import cats.implicits._
import java.time._

object Parser {

  val whitespaces: Parser[Unit] =
    takeWhile(c => c == ' ').void

  val word: Parser[String] =
    takeWhile(c => c != ' ').map(s => s)

  val money: Parser[Int] = {
    val a = takeWhile(c => c != '$')
    a.map(_.mkString).flatMap { s =>
      try ok(s.toInt)
      catch { case e: NumberFormatException => err(e.toString) }
    }
  }

  val wordIntoBark
    : Parser[String] = char('"') ~> takeWhile1(c => c != '"') | endOfInput.map(
    _ => "somewhere")

  val buyShort: Parser[Command] =
    (stringCI("/buy ") ~> money).map(SimpleCommand.Buy("somewhere", "some", _))

  val buyDetail: Parser[Command] =
    (stringCI("/buy ") ~> wordIntoBark <~ string("\""),
     whitespaces ~> word <~ whitespaces,
     money).mapN(SimpleCommand.Buy.apply)

  val buyMedium: Parser[Command] =
    (stringCI("/buy ") ~> wordIntoBark <~ string("\""), whitespaces ~> money)
      .mapN(SimpleCommand.Buy(_, "some", _))

  val command: Parser[Command] = choice(buyShort, buyDetail, buyMedium)

  def getCommand(strCmd: String): Option[Command] =
    command.parseOnly(strCmd).option

}
