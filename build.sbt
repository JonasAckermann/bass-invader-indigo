lazy val bassinvader =
  (project in file("."))
    .enablePlugins(ScalaJSPlugin, SbtIndigo)
    .settings( // Normal SBT settings
      name := "bassinvader",
      version := "0.0.1",
      scalaVersion := "2.13.3",
      organization := "bassinvader",
      libraryDependencies ++= Seq(
        "com.lihaoyi"    %%% "utest"      % "0.7.4"  % "test",
        "org.scalacheck" %%% "scalacheck" % "1.14.3" % "test"
      ),
      testFrameworks += new TestFramework("utest.runner.Framework")
    )
    .settings( // Indigo specific settings
      showCursor := true,
      title := "Bass Invader - Made with Indigo",
      gameAssetsDirectory := "assets",
      windowStartWidth := 1200,
      windowStartHeight := 800,
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "indigo-json-circe" % "0.3.0",
        "io.indigoengine" %%% "indigo"            % "0.3.0",
        "io.indigoengine" %%% "indigo-extras"     % "0.3.0"
      )
    )

addCommandAlias("buildGame", ";compile;fastOptJS;indigoBuild")
addCommandAlias("runGame", ";compile;fastOptJS;indigoRun")
