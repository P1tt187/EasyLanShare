name := "EasyLanShare"
     
version := "1.0"
     
scalaVersion := "2.10.1"

EclipseKeys.withSource := true
     
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
     
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.4"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.1.4"

libraryDependencies +="com.typesafe.akka" %% "akka-remote" % "2.1.4"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.5"

libraryDependencies += "com.typesafe.akka" % "akka-slf4j_2.10" % "2.1.4"

resourceDirectory <<= baseDirectory(_ / "src" / "resources")

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.13"


