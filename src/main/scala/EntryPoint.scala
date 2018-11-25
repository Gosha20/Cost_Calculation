import DataBase.DBRes
import TempModule._
import parser.Parser

import scala.io.Source

object EntryPoint extends App{


  val uri = "jdbc:h2:~/test"

//    val p = for {
//    _ <- DBRes.update("DROP TABLE costCalculator", List.empty)
//    _ <- DBRes.update("CREATE TABLE costCalculator(place VARCHAR(256), thing VARCHAR(256), money INT)",
//      List.empty)
//
//  } yield ()
//  p.execute(uri)

//  val fileName = "commands.txt"
//
//  for (line <- Source.fromFile(fileName).getLines){
//    Parser.getCommand(line) match {
//      case Some(cmd) => cmd.execute().execute(uri)
//      case None => None
//    }
//  }


val request = for {
  allDB <- viewAllDb()
  mostExpensive <- viewMostExpensive()
} yield (allDB, mostExpensive)
  println(request.execute(uri))
}

