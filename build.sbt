ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "zip-s3-proof-of-concept"
  )
libraryDependencies ++= Seq( "software.amazon.awssdk" % "aws-sdk-java" % "2.17.193","org.slf4j" % "slf4j-log4j12" % "1.7.36")