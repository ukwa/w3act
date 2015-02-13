import play.Project._

name := "w3act"

version := "1.0"

libraryDependencies ++= Seq(javaJdbc, javaEbean)     

libraryDependencies += "commons-validator" % "commons-validator" % "1.4.1"

playJavaSettings

