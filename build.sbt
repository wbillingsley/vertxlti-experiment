val dottyVersion = "3.0.0-M3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "vertxlti",
    version := "0.1.0-SNAPTHOT",

    scalaVersion := dottyVersion,
    
    libraryDependencies ++= Seq(
      "com.wbillingsley" %% "handy" % "0.11.0-SNAPSHOT",
      
      "io.vertx" % "vertx-core" % "4.0.0",
      "io.vertx" % "vertx-web" % "4.0.0",
      
      "org.scalameta" %% "munit" % "0.7.20" % Test
    ),
  )
