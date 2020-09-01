package model

import indigo._

case class Shot(location: Point, hitBox: Rectangle){
    def moveBy(shotSpeed: Int): Shot = {
        val newLocation = Point(this.location.x, this.location.y - shotSpeed)
        Shot(newLocation, hitBox.moveTo(newLocation))
    }
}
object Shot{
    def newShot(location: Point): Shot = Shot(location, Rectangle(location, Point(20,30)))
}