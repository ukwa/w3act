// Comment to get more information during initialization
logLevel := Level.Warn

// Torqbox rubegems repository (Travis Mirror)
resolvers += Resolver.url("rubygems-releases-mirror", url("http://maven.travis-ci.org/nexus/rubygems/maven/releases/"))

// Torquebox rubygems repository
resolvers += Resolver.url("rubygems-releases", url("http://rubygems-proxy.torquebox.org/releases/"))

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % System.getProperty("play.version"))
