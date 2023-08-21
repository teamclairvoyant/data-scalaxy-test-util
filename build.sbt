ThisBuild / scalaVersion := "3.3.0"

ThisBuild / organization := "com.clairvoyant.data.scalaxy"

ThisBuild / version := "1.0.0"

ThisBuild / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  System.getenv("GITHUB_USERNAME"),
  System.getenv("GITHUB_TOKEN")
)

// ----- PUBLISH TO GITHUB PACKAGES ----- //

ThisBuild / publishTo := Some("Github Repo" at "https://maven.pkg.github.com/teamclairvoyant/data-scalaxy-test-util/")

// ----- SCALAFIX ----- //

ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixOnCompile := true

// ----- WARTREMOVER ----- //

ThisBuild / wartremoverErrors ++= Warts.allBut(
  Wart.Any,
  Wart.DefaultArguments,
  Wart.Equals,
  Wart.IterableOps,
  Wart.Nothing,
  Wart.SizeIs,
  Wart.Throw
)

// ----- TOOL VERSIONS ----- //

val catsVersion = "2.9.0"
val s3MockVersion = "0.2.6"
val scalaTestVersion = "3.2.15"
val scalaXmlVersion = "2.1.0"
val sparkVersion = "3.4.1"

// ----- TOOL DEPENDENCIES ----- //

val catsDependencies = Seq("org.typelevel" %% "cats-core" % catsVersion)

val s3MockDependencies = Seq(
  "io.findify" %% "s3mock" % s3MockVersion
)
  .map(_.cross(CrossVersion.for3Use2_13))
  .map(_ excludeAll ("org.scala-lang.modules", "scala-xml"))

val scalaTestDependencies = Seq("org.scalatest" %% "scalatest" % scalaTestVersion)

val scalaXmlDependencies = Seq("org.scala-lang.modules" %% "scala-xml" % scalaXmlVersion)

val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)
  .map(_ excludeAll ("org.scala-lang.modules", "scala-xml"))
  .map(_.cross(CrossVersion.for3Use2_13))

// ----- MODULE DEPENDENCIES ----- //

val rootDependencies =
  catsDependencies ++
    s3MockDependencies ++
    scalaTestDependencies ++
    scalaXmlDependencies ++
    sparkDependencies

// ----- SETTINGS ----- //

val rootSettings = Seq(
  scalacOptions ++= Seq("-Xmax-inlines", "50"),
  libraryDependencies ++= rootDependencies
)

// ----- PROJECTS ----- //

lazy val `test-util` = (project in file("."))
  .settings(rootSettings)
