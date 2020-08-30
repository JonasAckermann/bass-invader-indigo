package model

import indigo._

case class LightWithLocation(color: RGB, location: Point, direction: Direction = Direction(Vertical.Down, Horizontal.Right)){
  def moveBy(x: Int, y: Int, config: GameConfig): LightWithLocation = {
    val newDirection = direction.fromLocation(location, config)
    // need to use new direction, otherwise stuck in cancelling directions
    val newLocation = Point(location.x + newDirection.horizontal.inDirection(x), location.y + newDirection.vertical.inDirection(y))
    LightWithLocation(color, newLocation, newDirection)
  }
}