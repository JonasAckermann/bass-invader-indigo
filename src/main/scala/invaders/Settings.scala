package invaders

import indigo.{ClearColor, GameConfig, GameViewport}

object Settings {

  val speed = 40.0
  val shotSpeed = 3.0
  val grandmaSpeed = 3.0

  val config =
    GameConfig(
      viewport = GameViewport(1200, 800),
      frameRate = 60,
      clearColor = ClearColor.Black,
      magnification = 1
    )

}
