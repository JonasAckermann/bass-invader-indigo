import indigo._
import scala.scalajs.js.annotation.JSExportTopLevel
import model._

@JSExportTopLevel("IndigoGame")
object BassInvader extends IndigoSandbox[Unit, Model] {

  val magnification = 1
  val width = 1200
  val height = 800
  val speed = 40
  val shotSpeed = 3
  val grandmaSpeed = 2
  val config: GameConfig =
    GameConfig.default
    .withMagnification(magnification)
    .withViewport(width, height)

  val animations: Set[Animation] =
    Set()

  val skrillexAsset = AssetName("skrillex")
  val shotAsset = AssetName("shot")
  val grandmaAsset = AssetName("grandma")
  val bgAsset = AssetName("bg")
  val shotSoundAsset = AssetName("shotSound")
  val assets: Set[AssetType] =
    Set(
       AssetType.Image(skrillexAsset, AssetPath("assets/skrillex.png")),
       AssetType.Image(shotAsset, AssetPath("assets/Wave2.png")),
       AssetType.Image(grandmaAsset, AssetPath("assets/Granny.png")),
       AssetType.Image(bgAsset, AssetPath("assets/bg.png")),   
       AssetType.Audio(shotSoundAsset, AssetPath("assets/drop.m4a"))   
    )

  val fonts: Set[FontInfo] =
    Set()

  def setup(assetCollection: AssetCollection, dice: Dice): Startup[StartupErrors, Unit] =
    Startup.Success(())

  def initialModel(startupData: Unit): Model =
    Model.initial(config, shotSpeed, grandmaSpeed)

  def updateModel(context: FrameContext[Unit], model: Model): GlobalEvent => Outcome[Model] = {

    case MouseEvent.Move(x, y) => 
      Outcome(model.update(Point(x, y), false, config, context.delta))

    case KeyboardEvent.KeyDown(Keys.SPACE) =>
      Outcome(model.update(model.skrillex.location, true, config, context.delta)).addGlobalEvents(PlaySound(AssetName("shotSound"), Volume.Max))

    case FrameTick =>
      Outcome(model.update(model.skrillex.location, false, config, context.delta))

    case _ =>
      Outcome(model)
  }

  def present(context: FrameContext[Unit], model: Model): SceneUpdateFragment =
    SceneUpdateFragment.empty
    .addGameLayerNodes(
      drawScene(model.skrillex, model.shots, model.grandma)
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
  
  def drawScene(skrillex: Skrillex, shots: List[Shot], grandma: Grandma): List[Graphic] = 
    List(
      bgGraphic
    ) ++ shots.map(shot => 
      shotGraphic
        .withRef(10,0)
        .moveTo(shot.location)
    )  ++ List(
      Graphic(Rectangle(0, 0, 40, 50), 1, Material.Textured(grandmaAsset).lit)
      .withRef(Point(20, 0))
      .moveTo(grandma.location)
    ) ++ List(
      Graphic(Rectangle(0, 0, 120, 50), 1, Material.Textured(skrillexAsset).lit)
        .withRef(60,0)
        .moveTo(skrillex.location)
    ) 
}
