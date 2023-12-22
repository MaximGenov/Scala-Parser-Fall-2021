

name := "expressions-scala"

version := "0.4"

scalaVersion := "3.0.1"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Yexplicit-nulls", "-language:strictEquality")

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.0.0",
  "org.scalatest"          %% "scalatest"                % "3.2.9" % Test,
//    minijs-scala-solution/build.sbt:  "org.jline"      %  "jline"                 % "3.21.0"
)

enablePlugins(JavaAppPackaging)

scalacOptions ++= Seq("-rewrite", "-new-syntax")
