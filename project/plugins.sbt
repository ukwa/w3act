// Disable hash checks as there are problems with Maven Central for jetty-parent
checksums in update := Nil

// Comment to get more information during initialization
//logLevel := Level.Warn
logLevel := Level.Info

resolvers += "Typesafe repository" at "https://dl.bintray.com/typesafe/maven-releases/"

// For Eclipse (under Play 2.4.x)
//addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")

// Dependencies
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.5")

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.10")

// web plugins

addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.0.0")
