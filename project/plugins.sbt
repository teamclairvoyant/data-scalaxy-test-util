val scalafmtVersion = "2.4.6"
val scalafixVersion = "0.11.0"
val wartremoverVersion = "3.1.3"

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % scalafmtVersion)
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % scalafixVersion)
addSbtPlugin("org.wartremover" % "sbt-wartremover" % wartremoverVersion)
