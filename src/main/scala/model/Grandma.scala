package model

import indigo._

case class Grandma(location: Location, direction: Direction, hitBox: GrandmaHitBox){
  def moveBy(x: Double, y: Double, config: GameConfig): Grandma = {
    val newDirection = direction.fromLocation(location, config)
    // need to use new direction, otherwise stuck in cancelling directions
    val newY = if (location.y + newDirection.vertical.inDirection(y) > config.viewport.height) 0 else location.y + newDirection.vertical.inDirection(y)
    val newLocation = Location(location.x + newDirection.horizontal.inDirection(x), newY)
    val newHitbox = GrandmaHitBox(hitBox.width, hitBox.height, newLocation.toPoint)
    Grandma(newLocation, newDirection, newHitbox)
  }
  def reset: Grandma = Grandma(Location(location.x, 0), direction, hitBox.copy(location = Point(location.toPoint.x, 0)))
}
object Grandma{
    def initial(config: GameConfig): Grandma = {
        val initX = scala.util.Random.between(0, config.viewport.width).toDouble
        val initLocation = Location(initX, 0)
        val initDir = if(scala.util.Random.nextBoolean()) Horizontal.Left else Horizontal.Right
        val initHitBox = GrandmaHitBox(40, 50, initLocation.toPoint)
        Grandma(initLocation, Direction(Vertical.Down, initDir), initHitBox)
    }
}