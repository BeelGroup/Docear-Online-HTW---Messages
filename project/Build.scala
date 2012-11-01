import sbt._
import sbt.Keys._
import sbt.PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Docear Frontend"
    val appVersion      = "0.1-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      coffeescriptOptions := Seq("bare")//coffee script code will not be wrapped in an anonymous function, necessary for tests
    )

}
