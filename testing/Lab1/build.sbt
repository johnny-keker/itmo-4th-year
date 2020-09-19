name := "Lab1"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies += "org.junit.jupiter" % "junit-jupiter-api" % "5.0.0-RC3" % Test
libraryDependencies += "org.junit.vintage" % "junit-vintage-engine" % "4.12.0-RC3" % Test
libraryDependencies += "org.junit.platform" % "junit-platform-runner" % "1.0.0-RC3" % Test
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
libraryDependencies -= "junit" % "junit" % "4.12" % Test