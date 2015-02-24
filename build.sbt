name		:= "moliere-demo"

version		:= "0.6.2-SNAPSHOT"

organization 	:= "io.nuvo"

homepage :=  Some(new java.net.URL("http://nuvo.io"))

scalaVersion 	:= "2.11.5"

resolvers += "nuvo.io maven repo" at "http://nuvo-io.github.com/mvn-repo/snapshots"

// This is used to fetch the jar for the DDS implementation (such as OpenSplice Mobile)


// libraryDependencies += "com.prismtech.cafe" % "cafe" % "2.1.0p1-SNAPSHOT"

libraryDependencies += "io.nuvo" % "moliere_2.11" % "0.6.3-SNAPSHOT"

libraryDependencies += "org.omg.dds.types" % "builtin-typelib_2.10" % "4.1.1-SNAPSHOT"

// libraryDependencies += "com.github.sarxos" % "webcam-capture" % "0.3.10"

libraryDependencies += "jline" % "jline" % "2.12"

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
