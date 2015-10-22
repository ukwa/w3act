name := "w3act"

version := "1.1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.maxmind.geoip2" % "geoip2" % "0.7.0",
  "org.apache.commons" % "commons-email" % "1.3.2",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "commons-validator" % "commons-validator" % "1.4.1",
  "org.apache.tika" % "tika-core" % "1.8",
  "org.apache.tika" % "tika-parsers" % "1.8",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "commons-io" % "commons-io" % "2.3",
  "com.rabbitmq" % "amqp-client" % "3.3.1",
  "org.jsoup" % "jsoup" % "1.8.1",
  "org.apache.httpcomponents" % "httpclient" % "4.3.6",
  "org.apache.httpcomponents" % "httpcore" % "4.3.3",
  "info.cukes" % "cucumber-java" % "1.2.2" % "test",
  "info.cukes" % "cucumber-junit" % "1.2.2" % "test",
  "net.sourceforge.htmlunit" % "htmlunit" % "2.15" % "test"
)

libraryDependencies += "uk.bl.wa.whois" % "jruby-whois" % "3.5.9.2" notTransitive()

resolvers += "rubygems-release" at "http://rubygems-proxy.torquebox.org/releases"

javaOptions ++= collection.JavaConversions.propertiesAsScalaMap(System.getProperties).map{ case (key,value) => "-D" + key + "=" +value }.toSeq

// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)

parallelExecution in Test := false

net.virtualvoid.sbt.graph.Plugin.graphSettings
