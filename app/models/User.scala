package models

import play.api.libs.json.Json
import models.Name.NameBSONReader
import models.Name.NameBSONWriter
import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONObjectIDIdentity
import reactivemongo.bson.BSONStringHandler
import reactivemongo.bson.Producer.nameValue2Producer
import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat

case class User(id: Option[BSONObjectID],
                name: Name,
                displayName: String,
                email: String,
                avatarUrl: String,
                website: String,
                shortBio: String)

object User {
  implicit val userFormat = Json.format[User]

  implicit object UserBSONWriter extends BSONDocumentWriter[User] {
    def write(user: User): BSONDocument =
      BSONDocument(
        "_id" -> user.id.getOrElse(BSONObjectID.generate),
        "name" -> user.name,
        "displayName" -> user.displayName,
        "email" -> user.email,
        "avatarUrl" -> user.avatarUrl,
        "website" -> user.website,
        "shortBio" -> user.shortBio)
  }

  implicit object UserBSONReader extends BSONDocumentReader[User] {
    def read(doc: BSONDocument): User =
      User(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[Name]("name").get,
        doc.getAs[String]("displayName").get,
        doc.getAs[String]("email").get,
        doc.getAs[String]("avatarUrl").get,
        doc.getAs[String]("website").get,
        doc.getAs[String]("shortBio").get
      )
  }
}
