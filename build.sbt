import play.Project._

name := "w3act"

version := "1.0"

libraryDependencies ++= Seq(
	javaJdbc, 
	javaEbean,
	"info.cukes" % "cucumber-java" % "1.2.2",
  	"info.cukes" % "cucumber-junit" % "1.2.2",
  	"net.sourceforge.htmlunit" % "htmlunit" % "2.15"
	)     

libraryDependencies += "commons-validator" % "commons-validator" % "1.4.1"

playJavaSettings

