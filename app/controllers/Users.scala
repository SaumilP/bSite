package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import models.User
import models.User.userFormat
import models.User.UserBSONReader
import models.User.UserBSONWriter
import models.Name
import models.Name.nameFormat
import models.Name.NameBSONWriter
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentIdentity
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONObjectIDIdentity
import reactivemongo.bson.BSONStringHandler
import reactivemongo.bson.Producer.nameValue2Producer

object Users extends Controller with MongoController {
  val collection = db[BSONCollection]("users")

  def index = Action.async {
      // get all User fields
      val cursor = collection.find(BSONDocument(), BSONDocument()).cursor[User]

      // converting cursor to list of Users
      val futureList = cursor.collect[List]()

      futureList.map { users => Ok(Json.toJson(users)) }
  }

  /* POST request */
  def create() = Action.async(parse.json) { request =>
     val nameJson = request.body.\("name")
     val name = nameFormat.reads(nameJson).get

     val displayName = request.body.\("displayName").toString().replace("\"", "")
     val email = request.body.\("email").toString().replace("\"", "")
     val avatarUrl= request.body.\("avatarUrl").toString().replace("\"", "")
     val website = request.body.\("website").toString().replace("\"", "")
     val shortBio = request.body.\("displayName").toString().replace("\"", "")

    val user = User(Option(BSONObjectID.generate), name, displayName,email,avatarUrl,website, shortBio)

     collection.insert(user).map(
       _ => Ok(Json.toJson(user)))
  }

  /* pulls data from db */
  def show(id: String) = Action.async(parse.empty) { request =>
    //get corresponding BSONObject
    val objectID = new BSONObjectID(id)

    val futureUser = collection.find(BSONDocument("_id" -> objectID)).one[User]
    futureUser.map { user => Ok(Json.toJson(user)) }
  }

  /* DELETE Requests */
  def delete(id: String) = Action.async(parse.empty) { request =>
    val objectID = new BSONObjectID(id);
    collection.remove( BSONDocument("_id" -> objectID)).map(
      _ => Ok(Json.obj())).recover {
      case _ => InternalServerError
    }
  }

  /* UPDATE request */
  def update(id: String) = Action.async(parse.json) { request =>
    val objectID = new BSONObjectID(id);
    val nameJSON = request.body.\("name")
    val name = nameFormat.reads(nameJSON).get
    val displayName = request.body.\("displayName").toString().replace("\"", "")
    val email = request.body.\("email").toString().replace("\"", "")
    val avatarUrl = request.body.\("avatarUrl").toString().replace("\"", "")
    val website = request.body.\("website").toString().replace("\"", "")
    val shortBio = request.body.\("shortBio").toString().replace("\"", "")

    val modifier = BSONDocument(
     "$set" -> BSONDocument(
        "name" -> name,
        "displayName" -> displayName,
        "email" -> email,
        "avatarUrl" -> avatarUrl,
        "website" -> website,
        "shortBio" -> shortBio
      )
     )

    //return updated JSON
    collection.update(BSONDocument("_id" -> objectID), modifier).map(
      _ => Ok(Json.toJson(User(Option(objectID),name,displayName,email,avatarUrl,website,shortBio)))
    )
  }
}
