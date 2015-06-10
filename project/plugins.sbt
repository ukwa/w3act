// Comment to get more information during initialization
logLevel := Level.Warn

// Torqbox
resolvers += "Torqbox rubygems releases" at "http://rubygems-proxy.torquebox.org/releases"
resolvers += "Torqbox rubygems releases mirror" at "http://maven.travis-ci.org/nexus/rubygems/maven/releases"

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Local
// resolvers += "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % System.getProperty("play.version"))
