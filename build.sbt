import AssemblyKeys._ // put this at the top of the file

assemblySettings

name		:= "moliere-demo"

version		:= "0.3.0-SNAPSHOT"

organization 	:= "io.nuvo"

homepage :=  Some(new java.net.URL("http://nuvo.io"))

scalaVersion 	:= "2.11.1"

resolvers += "nuvo.io maven repo" at "http://nuvo-io.github.com/mvn-repo/snapshots"

resolvers += "Vortex Public Repo" at "https://dl.dropboxusercontent.com/u/19238968/public/mvn-repo/snapshots"

resolvers += "Vortex Snapshot Repo" at "https://dl.dropboxusercontent.com/u/19238968/devel/mvn-repo/snapshots"

libraryDependencies += "com.netflix.rxjava" % "rxjava-scala" % "0.16.0"

libraryDependencies += "io.nuvo" % "moliere_2.11" % "0.3.0-SNAPSHOT"

libraryDependencies += "com.prismtech.cafe" % "cafe" % "2.0.0-SNAPSHOT"

libraryDependencies += "com.acme.chat" % "chat-typelib_2.11" % "0.2.0-SNAPSHOT"

libraryDependencies += "io.nuvo" % "ishapes-typelib_2.10" % "2.0.0"

autoCompilerPlugins := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-optimise"

scalacOptions += "-feature"

scalacOptions += "-language:postfixOps"

proguardSettings

ProguardKeys.options in Proguard += """
-dontnote
-dontwarn
-ignorewarnings
-dontobfuscate
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-keeppackagenames **
-optimizationpasses 3
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
-keep public class org.opensplice.mobile.core.ServiceEnvironmentImpl
-keep public class org.slf4j.ILoggerFactor {
      *;
}
-keep  class dds.demo.*Helper {  
       *; 
}
"""

ProguardKeys.options in Proguard += ProguardOptions.keepMain("dds.demo.oximeter.Oximeter")
