name		:= "moliere-demo"

version		:= "0.9.3-SNAPSHOT"

organization 	:= "io.nuvo"

homepage :=  Some(new java.net.URL("http://nuvo.io"))

scalaVersion 	:= "2.10.6"

//resolvers += "nuvo.io maven repo" at "http://nuvo-io.github.com/mvn-repo/snapshots"

// This is used to fetch the jar for the DDS implementation (such as OpenSplice Mobile)

// resolvers += "Vortex Snapshot Repo" at "https://dl.dropboxusercontent.com/u/19238968/vortex/mvn-repo"

// resolvers += "Vortex Snapshot Repo" at "file://" +  Path.userHome.absolutePath +"/.m2/repository"

// resolvers += "Nuvo Snapshot Repo" at "file://" +  Path.userHome.absolutePath + "/.ivy2/local"

resolvers += "nuvo.io releases repo" at "http://nuvo-io.github.com/mvn-repo/snapshots"
    
resolvers += "Vortex Snapshot Repo" at "https://dl.dropboxusercontent.com/u/19238968/vortex/mvn-repo"

libraryDependencies += "com.espertech" % "esper" % "5.3.0"

libraryDependencies += "com.prismtech.cafe" % "cafe" % "2.1.2-SNAPSHOT"

libraryDependencies += "io.nuvo" % "moliere_2.10" % "0.9.2-SNAPSHOT"

// libraryDependencies += "org.omg.dds.types" % "builtin-typelib_2.10" % "4.3.1-SNAPSHOT"

libraryDependencies += "com.google.code.gson" % "gson" % "2.3.1"

// libraryDependencies += "com.github.sarxos" % "webcam-capture" % "0.3.10"

libraryDependencies += "jline" % "jline" % "2.12"

libraryDependencies += "ch.qos.logback" % "logback-core" % "1.1.1"
//
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.1"

autoCompilerPlugins := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-optimise"

scalacOptions += "-feature"

scalacOptions += "-language:postfixOps"

// proguardSettings

// ProguardKeys.options in Proguard += """
// -dontnote
// -dontwarn
// -ignorewarnings
// -dontobfuscate
// -dontusemixedcaseclassnames
// -dontskipnonpubliclibraryclasses
// -keeppackagenames **
// -optimizationpasses 3
// -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
// -keep public class org.opensplice.mobile.core.ServiceEnvironmentImpl
// -keep public class org.slf4j.ILoggerFactor {
//       *;
// }
// -keep  class dds.demo.*Helper {  
//        *; 
// }
// """

// ProguardKeys.options in Proguard += ProguardOptions.keepMain("dds.demo.oximeter.Oximeter")
