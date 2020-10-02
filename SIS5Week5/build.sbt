name := "SIS5Week5"

version := "0.1"

scalaVersion := "2.13.3"
val circeVersion = "0.12.3"
libraryDependencies += "tech.sparse" %%  "translit-scala" % "0.1.2"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)