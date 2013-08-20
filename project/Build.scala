import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "AppDirectEvents"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "org.openid4java" % "openid4java" % "0.9.8",
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
