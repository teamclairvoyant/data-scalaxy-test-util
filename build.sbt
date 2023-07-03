ThisBuild / scalaVersion := "3.3.0"

lazy val scalacOptions = Seq("-Xmax-inlines", "50")

// ----- VARIABLES ----- //

val organizationName = "com.clairvoyant"
val projectName = "<repo_name>"
val releaseVersion = "1.0.0"

val scalaTestVersion = "3.2.15"
val scalaXmlVersion = "2.1.0"
val scalaParserCombinatorsVersion = "2.2.0"

// ----- TOOL DEPENDENCIES ----- //

val scalaTestDependencies = Seq("org.scalatest" %% "scalatest" % scalaTestVersion)

val scalaXmlDependencies = Seq("org.scala-lang.modules" %% "scala-xml" % scalaXmlVersion)

val scalaParserCombinatorsDependencies = Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserCombinatorsVersion
)

// ----- MODULE DEPENDENCIES ----- //

val rootDependencies =
  scalaTestDependencies.map(_ % "it,test") ++
    scalaXmlDependencies ++
    scalaParserCombinatorsDependencies

// ----- SETTINGS ----- //

val rootSettings =
  Seq(
    organization := organizationName,
    version := releaseVersion,
    Keys.scalacOptions ++= scalacOptions,
    libraryDependencies ++= rootDependencies
  ) ++ Defaults.itSettings

// ----- PROJECTS ----- //

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(rootSettings)

// ----- PUBLISH TO GITHUB PACKAGES ----- //

ThisBuild / publishTo := Some("Github Repo" at s"https://maven.pkg.github.com/teamclairvoyant/$projectName/")

ThisBuild / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "teamclairvoyant",
  System.getenv("GITHUB_TOKEN")
)
