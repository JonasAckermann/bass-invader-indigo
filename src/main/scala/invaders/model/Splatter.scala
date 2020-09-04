package invaders.model

import indigo._
import invaders.Settings
import scala.util.Random

case class Splatter(location: Location, rotation: Radians, scale: Double, level: Double, initScale: Double) {
    def explode: Splatter = {
        val newLevel = Math.min(level + Settings.bloodFadeSpeed, 1.0)
        val newScale = newLevel * initScale
        this.copy(level = newLevel, scale = newScale)
    }
}

object Splatter {
    def fromLocation(location: Location): Splatter = {
        val rotation = Radians.fromDegrees(Random.between(0.0, 360.0))
        val scale = Random.between(0.2, 2.0)
        Splatter(location, rotation, 0.0, 0.0, scale)
    }
}