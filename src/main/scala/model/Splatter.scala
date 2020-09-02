package model

import indigo._
import scala.util.Random

case class Splatter(location: Location, rotation: Radians, scale: Double)

object Splatter {
    def fromLocation(location: Location): Splatter = {
        val rotation = Radians.fromDegrees(Random.between(0.0, 360.0))
        val scale = Random.between(0.2, 2.0)
        Splatter(location, rotation, scale)
    }
}