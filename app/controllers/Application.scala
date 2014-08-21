package controllers

import play.Play
import play.api.mvc.{Action, Controller}
import java.io.File

object Application extends Controller {

  /* method serves index.scala.html page */
  def index(any: String) = Action {
    Ok(views.html.index())
  }

  /* resolves *any into corresponding html page uri */
  def getURI(any: String): String = any match {
    case "main" => "/public/html/main.html"
    case "profile" => "/public/html/detail.html"
    case _ => "error"
  }

  /* method loads html page from public/html */
  def loadPublicHTML(any: String) = Action {
    val projectRoot = Play.application().path()
    val file = new File(projectRoot + getURI(any) )
    if ( file.exists() )
      Ok(scala.io.Source.fromFile(file.getCanonicalPath()).mkString).as("text/html");
    else
      NotFound
  }
}