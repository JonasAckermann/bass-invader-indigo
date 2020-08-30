import indigo._
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("IndigoGame")
object MyGameSandbox extends IndigoSandbox[Unit, Model] {

  val magnification = 1
  val width = 1200
  val height = 800
  val speed = 40
  val shotSpeed = 1
  val config: GameConfig =
    GameConfig.default
    .withMagnification(magnification)
    .withViewport(width, height)

  val animations: Set[Animation] =
    Set()

  val skrillexAsset = AssetName("skrillex")
  val shotAsset = AssetName("shot")
  val bgAsset = AssetName("bg")
  val shotSoundAsset = AssetName("shotSound")
  val assets: Set[AssetType] =
    Set(
       AssetType.Image(skrillexAsset, AssetPath("assets/skrillex.png")),
       AssetType.Image(shotAsset, AssetPath("assets/Wave2.png")),
       AssetType.Image(bgAsset, AssetPath("assets/bg.png")),   
       AssetType.Audio(shotSoundAsset, AssetPath("assets/grannyHit_sample_V2.wav"))   
    )

  val fonts: Set[FontInfo] =
    Set()

  def setup(assetCollection: AssetCollection, dice: Dice): Startup[StartupErrors, Unit] =
    Startup.Success(())

  def initialModel(startupData: Unit): Model =
    Model.initial(config, shotSpeed)

  def updateModel(context: FrameContext[Unit], model: Model): GlobalEvent => Outcome[Model] = {
    case KeyboardEvent.KeyDown(Keys.LEFT_ARROW) =>
      Outcome(model.update(-speed, false, config))

    case KeyboardEvent.KeyDown(Keys.RIGHT_ARROW) =>
      Outcome(model.update(speed, false, config))

    case KeyboardEvent.KeyDown(Keys.SPACE) =>
      Outcome(model.update(0, true, config)).addGlobalEvents(PlaySound(AssetName("shotSound"), Volume.Max))

    case FrameTick =>
      Outcome(model.update(0, false, config))

    case _ =>
      Outcome(model)
  }

  def present(context: FrameContext[Unit], model: Model): SceneUpdateFragment =
    SceneUpdateFragment.empty
    .addGameLayerNodes(
      drawScene(model.skrillex, model.shots)
    )
    .withLights(drawLights(model.lights))

  def drawLights(lights: List[LightWithLocation]): List[PointLight] =
   lights.map(l => PointLight.default
  .moveTo(Point(l.location.x, l.location.y))
  .withAttenuation(500) // How far the light fades out to
  .withColor(l.color)
  .withHeight(100)
  .withPower(20)) 
    
  val shotGraphic = Graphic(Rectangle(0, 0, 20, 30), 1, Material.Textured(shotAsset).lit)
  val bgGraphic = Graphic(Rectangle(0, 0, config.viewport.width, config.viewport.height), 1, Material.Textured(bgAsset).lit)
  
  def drawScene(skrillex: Skrillex, shots: List[Shot]): List[Graphic] = 
    List(
      bgGraphic
    ) ++ shots.map(shot => shotGraphic
        .moveTo(shot.location)) ++ List(
      Graphic(Rectangle(0, 0, 120, 50), 1, Material.Textured(skrillexAsset).lit)
        .moveTo(skrillex.location)
    )
    
}

case class Model(skrillex: Skrillex, shots: List[Shot], shotSpeed: Int, lights: List[LightWithLocation]) {
  def update(skrillexChange: Int, newShot: Boolean, config: GameConfig): Model = {
    val newSkrillex = Skrillex(Point(this.skrillex.location.x + skrillexChange, this.skrillex.location.y))
    val oldShots = shots.map(oldShot => Shot(Point(oldShot.location.x, oldShot.location.y - shotSpeed)))
    val newShots = if (newShot) oldShots ++ List(Shot(skrillex.location)) else oldShots
    val newLights = lights.map(_.moveBy(10, 10, config))
    this.copy(newSkrillex, newShots, shotSpeed, newLights)
  }
}
object Model {
  def initial(config: GameConfig, shotSpeed: Int): Model =
    Model(
      Skrillex.initial(config.viewport.center, config.viewport.height-50),
      List.empty,
      shotSpeed,
      List(LightWithLocation(RGB.Magenta, Point(0, 0)), LightWithLocation(RGB.Cyan, Point(300, 700)), LightWithLocation(RGB.Yellow, Point(800, 100)))
    )
}

case class Skrillex(location: Point)
object Skrillex {
  def initial(center: Point, height: Int) = Skrillex(Point(center.x, height))
}

case class Shot(location: Point)

case class LightWithLocation(color: RGB, location: Point, direction: Direction = Direction(Vertical.Down, Horizontal.Right)){
  def moveBy(x: Int, y: Int, config: GameConfig): LightWithLocation = {
    val newDirection = direction.fromLocation(location, config)
    // need to use new direction, otherwise stuck in cancelling directions
    val newLocation = Point(location.x + newDirection.horizontal.inDirection(x), location.y + newDirection.vertical.inDirection(y))
    LightWithLocation(color, newLocation, newDirection)
  }
}

sealed trait Vertical {
  val opposite: Vertical
  def inDirection(by: Int): Int
}
object Vertical{
  case object Up extends Vertical {
    val opposite: Vertical = Down
    def inDirection(by: Int): Int = -by
  }
  case object Down extends Vertical {
    val opposite: Vertical = Up
    def inDirection(by: Int): Int = by
  }
}
sealed trait Horizontal {
  val opposite: Horizontal
  def inDirection(by: Int): Int
}
object Horizontal{
  case object Right extends Horizontal {
    val opposite = Left
    def inDirection(by: Int): Int = by
  }
  case object Left extends Horizontal {
    val opposite = Right
    def inDirection(by: Int): Int = -by
  }
}
case class Direction(vertical: Vertical, horizontal: Horizontal){
  def fromLocation(location: Point, config: GameConfig): Direction = {
    val newVertical = if(location.y > config.viewport.height || location.y < 0) vertical.opposite else vertical
    val newHorizontal = if(location.x > config.viewport.width || location.x < 0) horizontal.opposite else horizontal
    Direction(newVertical, newHorizontal)
  }
}