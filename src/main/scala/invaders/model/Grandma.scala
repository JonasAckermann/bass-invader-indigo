package invaders.model

import indigo._

case class Grandma(location: Location, direction: Direction, hitBox: Rectangle){
  def moveBy(x: Double, y: Double, config: GameConfig): Grandma = {
    val newDirection = direction.fromLocationWithNegativeY(location, config)
    // need to use new direction, otherwise stuck in cancelling directions
    val newY = if (location.y + newDirection.vertical.inDirection(y) > config.viewport.height) Grandma.aboveScreen else location.y + newDirection.vertical.inDirection(y)
    val newLocation = Location(location.x + newDirection.horizontal.inDirection(x), newY)
    Grandma(newLocation, newDirection, hitBox.moveTo(newLocation.toPoint))
  }
  def reset: Grandma = Grandma(Location(location.x, Grandma.aboveScreen), direction, hitBox.moveTo(Point(location.toPoint.x, Grandma.aboveScreen.toInt)))
}
object Grandma{
    val aboveScreen: Double = -50
    def initial(config: GameConfig): Grandma = {
        val initX = scala.util.Random.between(0, config.viewport.width).toDouble
        val initLocation = Location(initX, Grandma.aboveScreen)
        val initDir = if(scala.util.Random.nextBoolean()) Horizontal.Left else Horizontal.Right
        val initHitBox = Rectangle(initLocation.toPoint, Point(40,50))
        Grandma(initLocation, Direction(Vertical.Down, initDir), initHitBox)
    }
}