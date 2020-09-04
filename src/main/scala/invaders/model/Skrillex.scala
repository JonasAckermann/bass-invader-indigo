package invaders.model

case class Skrillex(location: Location)
object Skrillex {
  def initial(center: Location, height: Int) = Skrillex(Location(center.x, height.toDouble))
}