
lazy val akkaVersion = "2.5.14"

lazy val root = (project in file(".")).
  enablePlugins(ParadoxPlugin).
  settings(
    name := "akka-stream-quickstart",
    version := "1.0",
    scalaVersion := "2.12.6",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json-joda" % "2.6.0",
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-kafka" % "0.22",
      "org.scalatest" %% "scalatest" % "3.0.5" % "test"
    ),
    paradoxTheme := Some(builtinParadoxTheme("generic"))
  )