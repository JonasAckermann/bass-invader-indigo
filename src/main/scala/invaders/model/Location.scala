package invaders.model

import indigo._

/**
  * Like Point, but using Doubles
  *
  * @param x
  * @param y
  */
case class Location(x: Double, y: Double){
    def moveTo(location: Location): Location = Location(location.x, location.y)
    def moveBy(x: Double, y: Double): Location = Location(this.x + x, this.y + y)
    def toPoint: Point = Point(Math.round(x).toInt, Math.round(y).toInt)
}
object Location {
    def fromPoint(point: Point): Location = Location(point.x.toDouble, point.y.toDouble)
}