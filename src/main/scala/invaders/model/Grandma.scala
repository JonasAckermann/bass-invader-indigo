package invaders.model

import indigo._
import invaders.Settings

case class Grandma(
    location: Location,
    direction: Direction,
    hitBox: Rectangle,
    dead: Boolean,
    fade: Double,
    didHitFloor: Boolean
) {
  def kill: Grandma     = this.copy(dead = true, fade = 1.0)
  def hitFloor: Grandma = this.copy(location = Location(this.location.x, Grandma.aboveScreen), didHitFloor = true)
  def faded: Grandma    = this.copy(location = Location(this.location.x, Grandma.aboveScreen), dead = false, fade = 1.0)
  def moveBy(x: Double, y: Double, config: GameConfig): Grandma =
    if (dead == true && fade <= 0.0) this.faded
    else if (dead == true) this.copy(fade = this.fade - Settings.grandmaFadeSpeed)
    else {
      val newDirection = direction.fromLocationWithNegativeY(location, config)
      val newY         = location.y + newDirection.vertical.inDirection(y)
      // need to use new direction, otherwise stuck in cancelling directions
      if (newY > config.viewport.height) this.hitFloor
      else {
        val newLocation = Location(location.x + newDirection.horizontal.inDirection(x), newY)
        Grandma(newLocation, newDirection, hitBox.moveTo(newLocation.toPoint), false, 1.0, false)
      }
    }
}
object Grandma {
  val aboveScreen: Double = -50
  def initial(config: GameConfig): Grandma = {
    val initX        = scala.util.Random.between(0, config.viewport.width).toDouble
    val initLocation = Location(initX, Grandma.aboveScreen)
    val initDir      = if (scala.util.Random.nextBoolean()) Horizontal.Left else Horizontal.Right
    val initHitBox   = Rectangle(initLocation.toPoint, Point(40, 50))
    Grandma(initLocation, Direction(Vertical.Down, initDir), initHitBox, false, 1.0, false)
  }
}
