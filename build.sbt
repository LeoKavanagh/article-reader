name := "Article Reader"
version := "1.0"
scalaVersion := "2.12.6"

resolvers += "micronautics/scala on bintray" at "http://dl.bintray.com/micronautics/scala"

libraryDependencies += "org.jsoup" % "jsoup" % "1.11.3"
libraryDependencies += "com.micronautics" %% "awslib_scala" % "1.1.12" withSources()
