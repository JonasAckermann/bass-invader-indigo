package model

import indigo._

case class Shot(location: Location, hitBox: Rectangle){
    def moveBy(shotSpeed: Double): Shot = {
        val newLocation = Location(this.location.x, this.location.y - shotSpeed)
        Shot(newLocation, hitBox.moveTo(newLocation.toPoint))
    }
}
object Shot{
    def newShot(location: Location): Shot = Shot(location, Rectangle(location.toPoint, Point(20,30)))
}