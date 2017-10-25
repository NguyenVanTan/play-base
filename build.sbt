name := """framgia-play-core"""
organization := "com.framgia"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.2"

libraryDependencies += guice

libraryDependencies += javaJpa

libraryDependencies += javaWs

libraryDependencies += "org.hibernate" % "hibernate-core" % "5.2.5.Final"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"

libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"

libraryDependencies += "net.sf.opencsv" % "opencsv" % "2.1"

libraryDependencies += "com.typesafe.play" %% "play-mailer" % "6.0.1"
libraryDependencies += "com.typesafe.play" %% "play-mailer-guice" % "6.0.1"
