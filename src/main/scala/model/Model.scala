package model

import indigo._

case class Model(skrillex: Skrillex, shots: List[Shot], shotSpeed: Double, lights: List[LightWithLocation], grandma: Grandma, grandmaSpeed: Double) {

  private def boundedX(x: Double, minX: Double, maxX: Double): Double = 
    if(x > maxX) maxX
    else if(x < minX) minX
    else x

  private def checkHits(shotsToCheck: List[Shot]): (List[Shot], Boolean) = {
    val filteredShots = shotsToCheck.filterNot(_.hitBox.overlaps(grandma.hitBox))
    (filteredShots, filteredShots.length < shotsToCheck.length)
  }

  def distanceFromDelta(pxPerSec: Double, delta: Seconds): Double = 60.0*pxPerSec*delta.value

  // Only update skrillex location
  def updateSkrillex(skrillexChange: Location, config: GameConfig): Model = {
    val newSkrillex = Skrillex(Location(boundedX(skrillexChange.x, 0.0, config.viewport.width.toDouble), skrillex.location.y))
    this.copy(skrillex = newSkrillex)
  }

  // Only update new shots with location
  def updateShot(): Model = {
    val newShots = this.shots ++ List(Shot.newShot(skrillex.location))
    this.copy(shots = newShots)
  }

  // Update everything else, in particular constant motion an locations
  def updateTick(config: GameConfig, delta: Seconds): Model = {
    val newLights = lights.map(_.moveBy(distanceFromDelta(10, delta), distanceFromDelta(10, delta), config))
    val (filteredShots, grandmaIsHit) = checkHits(shots)
    val newShots = filteredShots.map(_.moveBy(distanceFromDelta(shotSpeed, delta)))
    val newGrandma = if(grandmaIsHit) grandma.reset else grandma.moveBy(distanceFromDelta(grandmaSpeed, delta), distanceFromDelta(grandmaSpeed, delta), config)
    this.copy(skrillex, newShots, shotSpeed, newLights, newGrandma, grandmaSpeed)
  }
}
object Model {
  def initial(config: GameConfig, shotSpeed: Double, grandmaSpeed: Double): Model =
    Model(
      Skrillex.initial(Location.fromPoint(config.viewport.center), config.viewport.height-50),
      List.empty,
      shotSpeed,
      List(LightWithLocation(RGB.Magenta, Location(0, 0)), LightWithLocation(RGB.Cyan, Location(300, 700)), LightWithLocation(RGB.Yellow, Location(800, 100))),
      Grandma.initial(config),
      grandmaSpeed
    )
}