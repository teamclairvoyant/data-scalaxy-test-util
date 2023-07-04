ThisBuild / scalaVersion := "3.3.0"

lazy val scalacOptions = Seq("-Xmax-inlines", "50")

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant"
val projectName = "data-scalaxy-test-util"
val releaseVersion = "1.0.0"

val catsVersion = "2.9.0"
val scalaTestVersion = "3.2.15"
val scalaXmlVersion = "2.1.0"
val sparkVersion = "3.4.1"

// ----- TOOL DEPENDENCIES ----- //

val catsDependencies = Seq("org.typelevel" %% "cats-core" % catsVersion)

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
    scalaTestDependencies ++
    scalaXmlDependencies ++
    sparkDependencies

// ----- SETTINGS ----- //

val rootSettings = Seq(
  organization := organizationName,
  version := releaseVersion,
  Keys.scalacOptions ++= scalacOptions,
  libraryDependencies ++= rootDependencies
)

// ----- PROJECTS ----- //

lazy val `data-scalaxy-test-util` = (project in file("."))
  .settings(rootSettings)

// ----- PUBLISH TO GITHUB PACKAGES ----- //

ThisBuild / publishTo := Some("Github Repo" at s"https://maven.pkg.github.com/teamclairvoyant/$projectName/")

ThisBuild / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "teamclairvoyant",
  System.getenv("GITHUB_TOKEN")
)
