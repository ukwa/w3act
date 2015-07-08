name := "w3act"

version := "2.0.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

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
  "info.cukes" % "cucumber-java" % "1.2.2",
  "info.cukes" % "cucumber-junit" % "1.2.2",
  "net.sourceforge.htmlunit" % "htmlunit" % "2.15",
  "uk.bl.wa.whois" % "jruby-whois" % "3.5.9.2"
)

{
  if( sys.env.contains("TRAVIS") ) {
    // Torqbox rubegems repository (Travis-CI mirror)
    resolvers  += "rubygems-releases-mirror" at "http://maven.travis-ci.org/nexus/rubygems/maven/releases/"
  } else {
    // Torquebox rubygems repository
    resolvers += "rubygems-releases" at "http://rubygems-proxy.torquebox.org/releases/"
  }
}


