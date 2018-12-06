package management.entities

import com.softwaremill.session.{SessionSerializer, SingleValueSessionSerializer}
import io.circe.generic.JsonCodec
import scala.util.Success

object UserSession {
  @JsonCodec(encodeOnly = true) final case class UserSession(login: String)

  implicit val sessionSerializer: SessionSerializer[UserSession, String] =
    new SingleValueSessionSerializer[UserSession, String](_.login, login => Success(UserSession(login)))

  @JsonCodec(decodeOnly = true) final case class AuthFormat(login: String, password: String)

  @JsonCodec(decodeOnly = true) final case class RegisterFormat(
                                                                 login: String,
                                                                 name: String,
                                                                 password: String
                                                               )
}
