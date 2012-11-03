import sbt._
import sbt.Keys._
import sbt.PlayProject._
import com.github.play2war.plugin._
import com.github.play2war.plugin.Play2WarKeys._

object ApplicationBuild extends Build {

    val appName         = "Docear Frontend"
    val appVersion      = "0.1-SNAPSHOT"

    val appDependencies = Seq(
      "info.schleichardt" %% "play-2-twitter-bootstrap-integration" % "0.1-SNAPSHOT"
      , "info.schleichardt" %% "play-2-basic-auth" % "0.1-SNAPSHOT"
      , "commons-io" % "commons-io" % "2.4"//heroku does not find it without the explicit dependency
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      coffeescriptOptions := Seq("bare")//coffee script code will not be wrapped in an anonymous function, necessary for tests
      , resolvers += "schleichardts Github" at "http://schleichardt.github.com/jvmrepo/"
      ,  Play2WarKeys.servletVersion := "3.0"
    ).settings(Play2WarPlugin.play2WarSettings: _*)

}
