import AssemblyKeys._ // put this at the top of the file

assemblySettings

name		:= "moliere-demo"

version		:= "0.1.1-SNAPSHOT"

organization 	:= "io.nuvo"

homepage :=  Some(new java.net.URL("http://nuvo.io"))

scalaVersion 	:= "2.10.3"

resolvers += "Local Maven Repo" at "file://"+Path.userHome.absolutePath+"/.ivy2/local"

resolvers += "nuvo.io maven repo" at "http://nuvo-io.github.com/mvn-repo/snapshots"

libraryDependencies += "com.netflix.rxjava" % "rxjava-scala" % "0.16.0"

libraryDependencies += "io.nuvo" % "moliere_2.10" % "0.1.3-SNAPSHOT"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

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
