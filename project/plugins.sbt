resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.4.0")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.2")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.9.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-proguard" % "0.2.1")