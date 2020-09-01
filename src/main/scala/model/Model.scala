package model

import indigo._

case class Model(skrillex: Skrillex, shots: List[Shot], shotSpeed: Int, lights: List[LightWithLocation], grandma: Grandma, grandmaSpeed: Int) {

  private def boundedX(x: Int, minX: Int, maxX: Int): Int = 
    if(x > maxX) maxX
    else if(x < minX) minX
    else x

  private def checkHits(shotsToCheck: List[Shot]): (List[Shot], Boolean) = {
    val filteredShots = shotsToCheck.filterNot(_.hitBox.isHit(grandma.hitBox))
    (filteredShots, filteredShots.length < shotsToCheck.length)
  }

  def update(skrillexChange: Point, newShot: Boolean, config: GameConfig): Model = {
    val newSkrillex = Skrillex(Point(boundedX(skrillexChange.x, 0, config.viewport.width), skrillex.location.y))
    val newLights = lights.map(_.moveBy(10, 10, config))
    
    val (filteredShots, grandmaIsHit) = checkHits(shots)
    val oldShots = filteredShots.map(_.moveBy(shotSpeed))
    val newShots = if (newShot) oldShots ++ List(Shot.newShot(skrillex.location)) else oldShots
    val newGrandma = if(grandmaIsHit) grandma.reset else grandma.moveBy(grandmaSpeed, grandmaSpeed, config)
    this.copy(newSkrillex, newShots, shotSpeed, newLights, newGrandma, grandmaSpeed)
  }
}
object Model {
  def initial(config: GameConfig, shotSpeed: Int, grandmaSpeed: Int): Model =
    Model(
      Skrillex.initial(config.viewport.center, config.viewport.height-50),
      List.empty,
      shotSpeed,
      List(LightWithLocation(RGB.Magenta, Point(0, 0)), LightWithLocation(RGB.Cyan, Point(300, 700)), LightWithLocation(RGB.Yellow, Point(800, 100))),
      Grandma.initial(config),
      grandmaSpeed
    )
}