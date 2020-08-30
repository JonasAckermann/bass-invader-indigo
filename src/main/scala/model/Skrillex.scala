package model

import indigo._

case class Skrillex(location: Point)
object Skrillex {
  def initial(center: Point, height: Int) = Skrillex(Point(center.x, height))
}