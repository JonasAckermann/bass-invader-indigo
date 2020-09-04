package invaders.scenes

import indigo._
import indigo.scenes._
import invaders.model._
import invaders.{GameAssets, Settings}

object StartScene extends Scene[Unit, Model, Unit] {
  type SceneModel     = Model
  type SceneViewModel = Unit

  val name: SceneName = SceneName("start")

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
      Outcome(model)
        .addGlobalEvents(SceneEvent.JumpTo(InterstitialScene.name))

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
    val horizontalCenter: Int = (Settings.config.viewport.width / Settings.config.magnification) / 2
    val verticalMiddle: Int   = (Settings.config.viewport.height / Settings.config.magnification) / 2

    SceneUpdateFragment.empty
      .addUiLayerNodes(drawTitleText(horizontalCenter, verticalMiddle))
  }

  def drawTitleText(center: Int, middle: Int): List[SceneGraphNode] =
    List(
      Text("Bass Invaders", center, middle - 50, 1, GameAssets.fontKey).alignCenter,
      Text("Press Space to Start.", center, middle - 10, 1, GameAssets.fontKey).alignCenter,
      Text("Made by Jon using the lovely Indigo Engine.", center, middle + 30, 1, GameAssets.fontKey).alignCenter
    )
}
