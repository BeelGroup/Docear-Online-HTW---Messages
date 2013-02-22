import sbt._
import sbt.Keys._

object MessagesBuild extends Build {

  lazy val messages = Project(
    id = "messages",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "messages",
      organization := "org.docear",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0",
      resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      javacOptions ++= Seq("-source", "1.6", "-target", "1.6")//for compatibility with Debian Squeeze
      // add other settings here
    ) ++ seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)
  )
}
