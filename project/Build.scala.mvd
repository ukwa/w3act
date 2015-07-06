import sbt._
import Keys._

import play.Project._

object ApplicationBuild extends Build {

    val appName         = "w3act"
    val appVersion      = "1.0.5"

    val appDependencies = Seq(
      javaCore,
      javaJdbc,
      javaEbean,
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
      "com.maxmind.geoip2" % "geoip2" % "0.7.0",
      "org.apache.commons" % "commons-email" % "1.3.2",
      "org.apache.commons" % "commons-lang3" % "3.3.2",
      "org.apache.tika" % "tika-core" % "1.8",
      "org.apache.tika" % "tika-parsers" % "1.8",
//      "org.apache.commons" % "commons-validator" % "1.4.0",
      "com.typesafe" %% "play-plugins-mailer" % "2.1.0",
      "org.mindrot" % "jbcrypt" % "0.3m",
      "commons-io" % "commons-io" % "2.3",
      "com.rabbitmq" % "amqp-client" % "3.3.1",
      "org.avaje" % "ebean" % "2.7.1",
      "uk.bl.wa.whois" % "jruby-whois" % "3.5.9.2"
    )
    
    val main = play.Project(appName, appVersion, appDependencies).settings(
        // enable eBean - not sure we need this anymore
    	ebeanEnabled := true,
    	
    	// Torqbox rubegems repository (Travis-CI mirror)
    	resolvers += "rubygems-releases-mirror" at "http://maven.travis-ci.org/nexus/rubygems/maven/releases/",

		// Torquebox rubygems repository
		resolvers += "rubygems-releases" at "http://rubygems-proxy.torquebox.org/releases/"

    )    

}
            
