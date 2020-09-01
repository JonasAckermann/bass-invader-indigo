package model

case class Shot(location: Location, hitBox: ShotHitBox){
    def moveBy(shotSpeed: Double): Shot = {
        val newLocation = Location(this.location.x, this.location.y - shotSpeed)
        Shot(newLocation, ShotHitBox(20, 30, newLocation.toPoint))
    }
}
object Shot{
    def newShot(location: Location): Shot = Shot(location, ShotHitBox(20, 30, location.toPoint))
}