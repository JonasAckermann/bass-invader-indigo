package invaders.scenes

import indigo._
import indigo.scenes._
import invaders.model._
import invaders.{GameAssets, Settings}

object RoomScene extends Scene[Unit, Model, Unit] {
  type SceneModel     = Model
  type SceneViewModel = Unit

  val name: SceneName = SceneName("room")

  val modelLens: Lens[Model, Model] =
    Lens.keepLatest

  val viewModelLens: Lens[Unit, Unit] = Lens.keepLatest

  val eventFilters: EventFilters =
    EventFilters.Default
      .withViewModelFilter(_ => None)

  val subSystems: Set[SubSystem] =
    Set()

  def updateViewModel(
                       context: FrameContext[Unit],
                       model: Model,
                       viewModel: Unit
                     ): GlobalEvent => Outcome[Unit] =
    _ => Outcome(())

  def updateModel(context: FrameContext[Unit], model: Model): GlobalEvent => Outcome[Model] = {

    case MouseEvent.Move(x, y) => 
      Outcome(model.updateSkrillex(Location(x.toDouble, y.toDouble), Settings.config))

    case MouseEvent.Click(_, _) =>
      Outcome(model.updateShot()).addGlobalEvents(PlaySound(AssetName("shotSound"), Volume.Max))

    case KeyboardEvent.KeyDown(Keys.SPACE) =>
      Outcome(model.updateShot()).addGlobalEvents(PlaySound(AssetName("shotSound"), Volume.Max))

    case FrameTick if model.lives <= 0 =>
      Outcome(model)
        .addGlobalEvents(SceneEvent.JumpTo(GameOverScene.name))

    case FrameTick =>
      Outcome(model.updateTick(Settings.config, context.delta))

    case _ =>
      Outcome(model)
  }

  def present(context: FrameContext[Unit], model: Model, viewModel: SceneViewModel): SceneUpdateFragment =
    SceneUpdateFragment.empty
    .addGameLayerNodes(
      drawScene(model.skrillex, model.shots, model.grandmas, model.splats)
    )
    .withLights(drawLights(model.lights))
    .addGameLayerNodes(drawText(model.points, model.lives))

  def drawText(score: Int, lives: Int) = List(Text(s"Score $score", 10, 20, 1, GameAssets.fontKey).alignLeft) ++ List(Text(s"Lives $lives", Settings.config.viewport.width - 10, 20, 1, GameAssets.fontKey).alignRight)

  def drawLights(lights: List[LightWithLocation]): List[PointLight] =
   lights.map(l => PointLight.default
  .moveTo(l.location.toPoint)
  .withAttenuation(500) // How far the light fades out to
  .withColor(l.color)
  .withHeight(100)
  .withPower(20)) 
    
  val shotGraphic = Graphic(Rectangle(0, 0, 20, 30), 1, Material.Textured(GameAssets.shotAsset).lit)
  val bgGraphic = Graphic(Rectangle(0, 0, Settings.config.viewport.width, Settings.config.viewport.height), 1, Material.Textured(GameAssets.bgAsset).lit)
  
  def drawScene(skrillex: Skrillex, shots: List[Shot], grandmas: List[Grandma], splats: List[Splatter]): List[Graphic] = 
    List(
      bgGraphic
    ) ++ splats.map(splat =>
      Graphic(Rectangle(0, 0, 40, 40), 1, Material.Textured(GameAssets.splatAsset).lit)
        .withRef(20,20)
        .rotate(splat.rotation)
        .scaleBy(splat.scale, splat.scale)
        .moveTo(splat.location.toPoint)
    ) ++ shots.map(shot => 
      shotGraphic
        .withRef(10,0)
        .moveTo(shot.location.toPoint)
    )  ++ grandmas.map( grandma => 
      Graphic(Rectangle(0, 0, 40, 50), 1, Material.Textured(GameAssets.grandmaAsset).lit)
      .withRef(Point(20, 0))
      .withAlpha(grandma.fade)
      .moveTo(grandma.location.toPoint)
    ) ++ List(
      Graphic(Rectangle(0, 0, 120, 50), 1, Material.Textured(GameAssets.skrillexAsset).lit)
        .withRef(60,0)
        .moveTo(skrillex.location.toPoint)
    )
}