name := """rx-samples"""

version := "1.0"

scalaVersion := "2.11.7"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies ++= Seq(
	"io.reactivex" % "rxscala_2.11" % "0.25.1",
	"org.jasypt" % "jasypt" % "1.9.2",
	"io.vertx" % "vertx-core" % "3.2.0")

