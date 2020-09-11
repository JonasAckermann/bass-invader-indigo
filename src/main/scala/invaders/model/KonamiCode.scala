package invaders.model

import indigo.shared.constants.{Key, Keys}

case class KonamiCode(successfulRun: Vector[KonamiInput] = Vector.empty){
  def next(input: KonamiInput) = {
    println(s"Current state: $successfulRun, input was $input")
    if(KonamiCode.sequence.length <= successfulRun.length) KonamiCode()
    else if(KonamiCode.sequence(successfulRun.length) == input) KonamiCode(KonamiCode.sequence.take(successfulRun.length + 1))
    else if (input == KonamiInput.Break) KonamiCode()
    else this
  }
}
object KonamiCode {
  import KonamiInput._
  val sequence = Vector(Up, Up, Down, Down, Left, Right, Left, Right, B, A, Start)
  def parseEvent(event: Option[Key]): KonamiInput = event match {
    case Some(Keys.UP_ARROW) => Up
    case Some(Keys.DOWN_ARROW) => Down
    case Some(Keys.LEFT_ARROW) => Left
    case Some(Keys.RIGHT_ARROW) => Right
    case Some(Keys.KEY_A) => A
    case Some(Keys.KEY_B) => B
    case Some(Keys.ENTER) => Start
    case Some(_) => Break
    case None => Pass
  }
}

sealed trait KonamiInput
object KonamiInput {
  case object Up    extends KonamiInput
  case object Down  extends KonamiInput
  case object Left  extends KonamiInput
  case object Right extends KonamiInput
  case object A     extends KonamiInput
  case object B     extends KonamiInput
  case object Start extends KonamiInput
  case object Break extends KonamiInput
  case object Pass  extends KonamiInput
}
