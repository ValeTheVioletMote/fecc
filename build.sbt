scalaVersion := "3.1.3"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
)

version := "3.1.0"

assembly / mainClass := Some("FECharMaker.fecc")