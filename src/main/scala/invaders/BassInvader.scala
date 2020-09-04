package invaders

import indigo._
import indigo.scenes._
import invaders.model.Model
import invaders.scenes.{GameOverScene, InterstitialScene, RoomScene, StartScene}
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("IndigoGame")
object BassInvader extends IndigoGame[GameViewport, Unit, Model, Unit] {


  def boot(flags: Map[String, String]): BootResult[GameViewport] = {

    BootResult(Settings.config, Settings.config.viewport)
      .withAssets(GameAssets.assets)
      .withFonts(GameAssets.fontInfo(GameAssets.fontAssetName))
  }

  def initialScene(bootData: GameViewport): Option[SceneName] =
    Option(StartScene.name)

  def scenes(bootData: GameViewport): NonEmptyList[Scene[Unit, Model, Unit]] =
    NonEmptyList(StartScene, InterstitialScene, RoomScene, GameOverScene)

  def initialModel(startupData: Unit): Model =
    Model.initial(Settings.config, Settings.shotSpeed, Settings.grandmaSpeed)

  def initialViewModel(startupData: Unit, model: Model): Unit = ()

  def setup(viewport: GameViewport, assetCollection: AssetCollection, dice: Dice): Startup[StartupErrors, Unit] = Startup.Success(())

}
