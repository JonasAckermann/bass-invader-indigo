package invaders

import indigo.{ClearColor, GameConfig, GameViewport}

object Settings {

  val speed = 40.0
  val shotSpeed = 3.0
  val grandmaSpeed = 2.0
  val lives = 10
  val bloodFadeSpeed = 0.05
  val grandmaFadeSpeed = 0.05
  val grandmaDirectionChangePer = 120 // at 60 fps statistically every 2s

  val config =
    GameConfig(
      viewport = GameViewport(1200, 800),
      frameRate = 60,
      clearColor = ClearColor.Black,
      magnification = 1
    )

}
