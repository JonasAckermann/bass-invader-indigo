package invaders.scenes

import indigo._
import indigo.scenes._
import invaders.model._
import invaders.{GameAssets, Settings}

object InterstitialScene extends Scene[Unit, Model, Unit] {
  type SceneModel     = Model
  type SceneViewModel = Unit

  val name: SceneName = SceneName("intro")

  val modelLens: Lens[Model, Model] =
    Lens.keepLatest

  val viewModelLens: Lens[Unit, Unit] = Lens.keepLatest

  val eventFilters: EventFilters =
    EventFilters.Default
      .withViewModelFilter(_ => None)

  val subSystems: Set[SubSystem] =
    Set()

  def updateModel(
      context: FrameContext[Unit],
      model: Model
  ): GlobalEvent => Outcome[Model] = {
    case KeyboardEvent.KeyUp(Keys.SPACE) =>
      Outcome(Model.initial(Settings.config, Settings.shotSpeed, Settings.grandmaSpeed))
        .addGlobalEvents(SceneEvent.JumpTo(RoomScene.name))

    case _ =>
      Outcome(model)
  }

  def updateViewModel(
      context: FrameContext[Unit],
      model: Model,
      viewModel: Unit
  ): GlobalEvent => Outcome[Unit] =
    _ => Outcome(())

  def present(
      context: FrameContext[Unit],
      model: Model,
      viewModel: Unit
  ): SceneUpdateFragment = {
    val verticalMiddle: Int   = (Settings.config.viewport.height / Settings.config.magnification) / 2

    SceneUpdateFragment.empty
      .addUiLayerNodes(drawTitleText(20, verticalMiddle))
  }

  def drawTitleText(leftAlign: Int, middle: Int): List[SceneGraphNode] =
    List(
      Graphic(Rectangle(0, 0, 40, 50), 1, Material.Textured(GameAssets.grandmaAsset))
        .withRef(Point(20, 0))
        .withAlpha(0.9)
        .scaleBy(4.0, 4.0)
        .flipHorizontal(true)
        .moveTo(Settings.config.viewport.width - 300, 150),
      Text(
        "Thats a mighty racket coming from your room!",
        leftAlign,
        middle - 320,
        1,
        GameAssets.fontKey
      ).alignLeft
      .withTint(0.9, 0.7, 0.7),
      Text(
        "Is that even music?",
        leftAlign,
        middle - 280,
        1,
        GameAssets.fontKey
      ).alignLeft.withTint(0.9, 0.7, 0.7),
      Text(
        "If you dont shut it off, I will!",
        leftAlign,
        middle - 240,
        1,
        GameAssets.fontKey
      ).alignLeft.withTint(0.9, 0.7, 0.7),
      Text(
        "Protect your right to party during grannys nap time.",
        leftAlign,
        middle + 40,
        1,
        GameAssets.fontKey
      ).alignLeft,
      Text(
        "Move using the mouse or touchpad,",
        leftAlign,
        middle + 160,
        1,
        GameAssets.fontKey
      ).alignLeft,
      Text(
        "and drop heavy bass hits from the Space key.",
        leftAlign,
        middle + 200,
        1,
        GameAssets.fontKey
      ).alignLeft
    )
}
