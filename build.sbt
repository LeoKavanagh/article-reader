name := """article-reader"""
organization := "com.github.leokavanagh"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.7"

resolvers += "micronautics/scala on bintray" at "http://dl.bintray.com/micronautics/scala"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "org.jsoup" % "jsoup" % "1.11.3"
libraryDependencies += "com.micronautics" %% "awslib_scala" % "1.1.12" withSources()

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.github.leokavanagh.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.github.leokavanagh.binders._"

