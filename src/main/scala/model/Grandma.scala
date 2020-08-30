package model

import indigo._

case class Grandma(location: Point, direction: Direction){
  def moveBy(x: Int, y: Int, config: GameConfig): Grandma = {
    val newDirection = direction.fromLocation(location, config)
    // need to use new direction, otherwise stuck in cancelling directions
    val newY = if (location.y + newDirection.vertical.inDirection(y) > config.viewport.height) 0 else location.y + newDirection.vertical.inDirection(y)
    val newLocation = Point(location.x + newDirection.horizontal.inDirection(x), newY)
    Grandma(newLocation, newDirection)
  }
}
object Grandma{
    def initial(config: GameConfig): Grandma = {
        val initX = scala.util.Random.between(0, config.viewport.width)
        val initDir = if(scala.util.Random.nextBoolean()) Horizontal.Left else Horizontal.Right
        Grandma(Point(initX, 0), Direction(Vertical.Down, initDir))
    }
}