import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.scalamacros",
    version := "1.0.0",
    scalaVersion := "2.10.1",
    scalacOptions ++= Seq(),
    resolvers += "repo.scalatools snaps" at "https://oss.sonatype.org/content/repositories/snapshots/",
    resolvers += "spray" at "http://repo.spray.io/",
    libraryDependencies += "com.novus" %% "salat" % "1.9.3",
    libraryDependencies += "io.spray" %%  "spray-json" % "1.2.5",
    libraryDependencies += "com.gensler" %% "scalavro" % "0.4.0"
  )
}

object MyBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("core"),
    settings = buildSettings
  ) aggregate(macros, core)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _))
  )

  lazy val core: Project = Project(
    "core",
    file("core"),
    settings = buildSettings
  ) dependsOn(macros)
}
