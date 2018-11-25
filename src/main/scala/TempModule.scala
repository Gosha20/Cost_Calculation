import java.sql.ResultSet
import DataBase.DBRes

object TempModule {

  def read: ResultSet => String = {rs =>
    val res = rs.getString("place") + " " + rs.getString("thing") + " " +rs.getString("money")
    res
  }
  def viewMostExpensive(): DBRes[List[String]] = DBRes.select("SELECT * FROM costCalculator ORDER BY money DESC LIMIT 1", List())(read)
  def viewAllDb(): DBRes[List[String]] = DBRes.select("SELECT * FROM costCalculator", List())(read)
}
