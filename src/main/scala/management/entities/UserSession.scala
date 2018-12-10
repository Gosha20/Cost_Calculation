package management.entities

import com.softwaremill.session.{
  SessionSerializer,
  SingleValueSessionSerializer
}
import scala.util.Success

object UserSession {
  final case class UserSession(login: String)

  implicit val sessionSerializer: SessionSerializer[UserSession, String] =
    new SingleValueSessionSerializer[UserSession, String](
      _.login,
      login => Success(UserSession(login)))

  final case class AuthFormat(login: String,
                                                            password: String)

  final case class RegisterFormat(
      login: String,
      name: String,
      password: String
  )
}
