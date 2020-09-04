package invaders.model

import indigo._

case class CheckHitsResult(shots: List[Shot], remainingGrandmas: List[Grandma], resetGrandmas: List[Grandma], points: Int)

case class Model(
  skrillex: Skrillex, 
  shots: List[Shot], 
  shotSpeed: Double, 
  lights: List[LightWithLocation], 
  grandmas: List[Grandma], 
  grandmaSpeed: Double, 
  points: Int, 
  strikes: Int,
  splats: List[Splatter]) {

  private def boundedX(x: Double, minX: Double, maxX: Double): Double = 
    if(x > maxX) maxX
    else if(x < minX) minX
    else x

  private def checkHits(shotsToCheck: List[Shot], grandmasToCheck: List[Grandma]): CheckHitsResult = {
    val filteredShots = shotsToCheck.filterNot(shot => grandmasToCheck.exists(grandma => shot.hitBox.overlaps(grandma.hitBox)))
    val (resetGrandmas, remainingGrandmas) = grandmasToCheck.partition(grandma => shotsToCheck.exists(shot => grandma.hitBox.overlaps(shot.hitBox)))
    CheckHitsResult(filteredShots, remainingGrandmas, resetGrandmas, resetGrandmas.length)
  }

  private def moveAndCheckStrikes(grandmasToMove: List[Grandma], delta: Seconds, config: GameConfig): (List[Grandma], Int) = {
    val movedGrandmas: List[Grandma] = grandmasToMove.map(_.moveBy(distanceFromDelta(grandmaSpeed, delta), distanceFromDelta(grandmaSpeed, delta), config))
    // Fragile: Count all grandmas that have been reset to the reset position by the moveBy method.
    // All grandmas reset to that position by hits will have been moved out of the reset position by this point.
    val strikes: Int = movedGrandmas.count(_.location.y == Grandma.aboveScreen )
    (movedGrandmas, strikes)
  }

  def distanceFromDelta(pxPerSec: Double, delta: Seconds): Double = 60.0*pxPerSec*delta.value

  // Only update skrillex location
  def updateSkrillex(skrillexChange: Location, config: GameConfig): Model = {
    val newSkrillex = Skrillex(Location(boundedX(skrillexChange.x, 0.0, config.viewport.width.toDouble), skrillex.location.y))
    this.copy(skrillex = newSkrillex)
  }

  // Only update new shots with location
  def updateShot(): Model = {
    val newShots = (List(Shot.newShot(skrillex.location)) ++ this.shots).take(300)
    this.copy(shots = newShots)
  }

  // Update everything else, in particular constant motion an locations
  def updateTick(config: GameConfig, delta: Seconds): Model = {
    val newLights = lights.map(_.moveBy(distanceFromDelta(10, delta), distanceFromDelta(10, delta), config))
    val hitsChecked = checkHits(shots, grandmas)
    val newShots = hitsChecked.shots.map(_.moveBy(distanceFromDelta(shotSpeed, delta)))
    val (grandmasMoved, newStrikes) = moveAndCheckStrikes(hitsChecked.remainingGrandmas, delta, config)
    val newGrandmas = hitsChecked.resetGrandmas.map(_.reset) ++  grandmasMoved
    val newSplats = hitsChecked.resetGrandmas.map(_.location).map(Splatter.fromLocation)
    this.copy(skrillex, newShots, shotSpeed, newLights, newGrandmas, grandmaSpeed, points + hitsChecked.points, strikes + newStrikes, (newSplats ++ splats).take(200).map(_.explode))
  }
}
object Model {
  def initial(config: GameConfig, shotSpeed: Double, grandmaSpeed: Double): Model =
    Model(
      Skrillex.initial(Location.fromPoint(config.viewport.center), config.viewport.height-50),
      List.empty,
      shotSpeed,
      List(LightWithLocation(RGB.Magenta, Location(0, 0)), LightWithLocation(RGB.Cyan, Location(300, 700)), LightWithLocation(RGB.Yellow, Location(800, 100))),
      0.to(9).map(_ => Grandma.initial(config)).toList,
      grandmaSpeed,
      0, 
      0,
      List.empty
    )
}