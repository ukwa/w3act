name := "w3act"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.maxmind.geoip2" % "geoip2" % "0.7.0",
  "org.apache.commons" % "commons-email" % "1.3.2",
  "commons-io" % "commons-io" % "2.3",
  "com.rabbitmq" % "amqp-client" % "3.3.1",
  "org.jsoup" % "jsoup" % "1.8.1"
  //"org.avaje" % "ebean" % "2.7.1"  
)
