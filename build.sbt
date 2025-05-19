ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaDiscountFlow",


    libraryDependencies += "com.oracle.database.jdbc" % "ojdbc8" % "19.3.0.0"
  )
