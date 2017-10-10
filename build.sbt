name := """framgia-play-core"""
organization := "com.framgia"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.2"

libraryDependencies += guice

libraryDependencies += javaJpa

libraryDependencies += javaWs

libraryDependencies += "org.hibernate" % "hibernate-core" % "5.2.5.Final"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"

libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"
