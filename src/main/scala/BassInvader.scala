import indigo._
import scala.scalajs.js.annotation.JSExportTopLevel
import model._

@JSExportTopLevel("IndigoGame")
object BassInvader extends IndigoSandbox[Unit, Model] {

  val magnification = 1
  val width = 1200
  val height = 800
  val speed = 40.0
  val shotSpeed = 3.0
  val grandmaSpeed = 2.0
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
  val fontAssetName = AssetName("font")
  val assets: Set[AssetType] =
    Set(
       AssetType.Image(skrillexAsset, AssetPath("assets/skrillex.png")),
       AssetType.Image(shotAsset, AssetPath("assets/Wave2.png")),
       AssetType.Image(grandmaAsset, AssetPath("assets/Granny.png")),
       AssetType.Image(bgAsset, AssetPath("assets/bg.png")),   
       AssetType.Audio(shotSoundAsset, AssetPath("assets/drop.m4a")),
       AssetType.Image(fontAssetName, AssetPath("assets/boxy_font.png"))   
    )

  val fonts: Set[FontInfo] =
    Set(Font.fontInfo(fontAssetName))

  def setup(assetCollection: AssetCollection, dice: Dice): Startup[StartupErrors, Unit] =
    Startup.Success(())

  def initialModel(startupData: Unit): Model =
    Model.initial(config, shotSpeed, grandmaSpeed)

  def updateModel(context: FrameContext[Unit], model: Model): GlobalEvent => Outcome[Model] = {

    case MouseEvent.Move(x, y) => 
      Outcome(model.updateSkrillex(Location(x.toDouble, y.toDouble), config))

    case KeyboardEvent.KeyDown(Keys.SPACE) =>
      Outcome(model.updateShot()).addGlobalEvents(PlaySound(AssetName("shotSound"), Volume.Max))

    case FrameTick =>
      Outcome(model.updateTick(config, context.delta))

    case _ =>
      Outcome(model)
  }

  def present(context: FrameContext[Unit], model: Model): SceneUpdateFragment =
    SceneUpdateFragment.empty
    .addGameLayerNodes(
      drawScene(model.skrillex, model.shots, model.grandmas)
    )
    .withLights(drawLights(model.lights))
    .addGameLayerNodes(drawText(model.points, model.strikes))

  def drawText(score: Int, strikes: Int) = List(Text(s"Score $score", 10, 20, 1, Font.fontKey).alignLeft) ++ List(Text(s"Strikes $strikes", config.viewport.width - 10, 20, 1, Font.fontKey).alignRight)

  def drawLights(lights: List[LightWithLocation]): List[PointLight] =
   lights.map(l => PointLight.default
  .moveTo(l.location.toPoint)
  .withAttenuation(500) // How far the light fades out to
  .withColor(l.color)
  .withHeight(100)
  .withPower(20)) 
    
  val shotGraphic = Graphic(Rectangle(0, 0, 20, 30), 1, Material.Textured(shotAsset).lit)
  val bgGraphic = Graphic(Rectangle(0, 0, config.viewport.width, config.viewport.height), 1, Material.Textured(bgAsset).lit)
  
  def drawScene(skrillex: Skrillex, shots: List[Shot], grandmas: List[Grandma]): List[Graphic] = 
    List(
      bgGraphic
    ) ++ shots.map(shot => 
      shotGraphic
        .withRef(10,0)
        .moveTo(shot.location.toPoint)
    )  ++ grandmas.map( grandma => 
      Graphic(Rectangle(0, 0, 40, 50), 1, Material.Textured(grandmaAsset).lit)
      .withRef(Point(20, 0))
      .moveTo(grandma.location.toPoint)
    ) ++ List(
      Graphic(Rectangle(0, 0, 120, 50), 1, Material.Textured(skrillexAsset).lit)
        .withRef(60,0)
        .moveTo(skrillex.location.toPoint)
    ) 
}
