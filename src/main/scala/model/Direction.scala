package model

import indigo._

sealed trait Vertical {
  val opposite: Vertical
  def inDirection(by: Double): Double
}
object Vertical{
  case object Up extends Vertical {
    val opposite: Vertical = Down
    def inDirection(by: Double): Double = -by
  }
  case object Down extends Vertical {
    val opposite: Vertical = Up
    def inDirection(by: Double): Double = by
  }
}
sealed trait Horizontal {
  val opposite: Horizontal
  def inDirection(by: Double): Double
}
object Horizontal{
  case object Right extends Horizontal {
    val opposite = Left
    def inDirection(by: Double): Double = by
  }
  case object Left extends Horizontal {
    val opposite = Right
    def inDirection(by: Double): Double = -by
  }
}
case class Direction(vertical: Vertical, horizontal: Horizontal){
  def fromLocation(location: Location, config: GameConfig): Direction = {
    val newVertical = if(location.y > config.viewport.height || location.y < 0) vertical.opposite else vertical
    val newHorizontal = if(location.x > config.viewport.width || location.x < 0) horizontal.opposite else horizontal
    Direction(newVertical, newHorizontal)
  }
  def fromLocationWithNegativeY(location: Location, config: GameConfig): Direction = {
    val newVertical = if(location.y > config.viewport.height) vertical.opposite else vertical
    val newHorizontal = if(location.x > config.viewport.width || location.x < 0) horizontal.opposite else horizontal
    Direction(newVertical, newHorizontal)
  }
}