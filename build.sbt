name := "w3act"

version := "1.1.2"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  // Excluding this as there are problems downloading it under Debian (SSL CA problem?)
  "org.apache.commons" % "commons-compress" % "1.7" exclude("org.tukaani", "xz"),
  "org.apache.commons" % "commons-email" % "1.3.2",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "commons-validator" % "commons-validator" % "1.5.1",
  "commons-io" % "commons-io" % "2.3",
  "org.apache.tika" % "tika-core" % "1.11",
  "org.apache.tika" % "tika-parsers" % "1.11" exclude("org.tukaani", "xz"),
  "org.apache.httpcomponents" % "httpclient" % "4.3.6",
  "org.apache.httpcomponents" % "httpcore" % "4.3.3",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.maxmind.geoip2" % "geoip2" % "0.7.0",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.rabbitmq" % "amqp-client" % "3.3.1",
  "org.jsoup" % "jsoup" % "1.8.1",
  "eu.scape-project.bitwiser" % "bitwiser" % "1.0.0",
  "com.github.kevinsawicki" % "timeago" % "1.0.1",
  "info.cukes" % "cucumber-java" % "1.2.2" % "test",
  "info.cukes" % "cucumber-junit" % "1.2.2" % "test",
  "net.sourceforge.htmlunit" % "htmlunit" % "2.15" % "test"
)

libraryDependencies += "uk.bl.wa.whois" % "jruby-whois" % "3.5.9.2" notTransitive()

libraryDependencies += "org.julienrf" %% "play-jsmessages" % "1.6.2"

resolvers += "rubygems-release" at "http://rubygems-proxy.torquebox.org/releases"

javaOptions ++= collection.JavaConversions.propertiesAsScalaMap(System.getProperties).map{ case (key,value) => "-D" + key + "=" +value }.toSeq

// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)

parallelExecution in Test := false

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-q", "-a")

net.virtualvoid.sbt.graph.Plugin.graphSettings
