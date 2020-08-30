package model

import indigo._

case class Model(skrillex: Skrillex, shots: List[Shot], shotSpeed: Int, lights: List[LightWithLocation], grandma: Grandma, grandmaSpeed: Int) {

  private def boundedX(x: Int, minX: Int, maxX: Int): Int = 
    if(x > maxX) maxX
    else if(x < minX) minX
    else x

  def update(skrillexChange: Point, newShot: Boolean, config: GameConfig): Model = {
    val newSkrillex = Skrillex(Point(boundedX(skrillexChange.x, 0, config.viewport.width), skrillex.location.y))
    val oldShots = shots.map(oldShot => Shot(Point(oldShot.location.x, oldShot.location.y - shotSpeed)))
    val newShots = if (newShot) oldShots ++ List(Shot(skrillex.location)) else oldShots
    val newLights = lights.map(_.moveBy(10, 10, config))
    val newGrandma = grandma.moveBy(grandmaSpeed, grandmaSpeed, config)
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