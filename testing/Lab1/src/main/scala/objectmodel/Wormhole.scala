package objectmodel

class Wormhole(firstGalaxy: Galaxy, secondGalaxy: Galaxy) {
  if (firstGalaxy == null || secondGalaxy == null) throw new IllegalArgumentException("Wormhole should be initiated with two valid galaxies!")

  override def toString: String = s"The wormhole between ${firstGalaxy.name} and ${secondGalaxy.name}"

  private var channel: Option[String] = None

  firstGalaxy.setWormhole(this)
  secondGalaxy.setWormhole(this)

  def getRandomThoughtFromGalaxy(first: Boolean): Boolean = {
    try {
      channel = Some((if (first) firstGalaxy else secondGalaxy).getRandomCharacter.randomThought)
      true
    } catch {
      case _: FlatCharacterException => false
      case _: EmptyGalaxyException => false
    }
  }

  def getMessage: Option[String] = channel
}
