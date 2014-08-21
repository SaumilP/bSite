import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "playSite"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // ReactiveMongo dependencies
    "org.reactivemongo" %% "reactivemongo" % "0.10.0",
    // ReactiveMongo Play plugin dependencies
    "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2")

  val main = play.Project(appName, appVersion, appDependencies).settings(
  )
}