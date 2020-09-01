package model

import indigo._

case class Shot(location: Point, hitBox: ShotHitBox){
    def moveBy(shotSpeed: Int): Shot = {
        val newLocation = Point(this.location.x, this.location.y - shotSpeed)
        Shot(newLocation, ShotHitBox(20, 30, newLocation))
    }
}
object Shot{
    def newShot(location: Point): Shot = Shot(location, ShotHitBox(20, 30, location))
}