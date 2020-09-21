package objectmodel

import scala.util.Random

class NonExistentCharacterException(private val message: String) extends Exception(message)
class EmptyGalaxyException(private val message: String) extends Exception(message)
class NoWormholeException(private val message: String) extends Exception(message)
class NoMessageFromAnotherGalaxyException(private val message: String) extends Exception(message)

class Galaxy(var name: String) {
  if (name == null || name == "") throw new IllegalArgumentException("Galaxy must have non-empty name!")

  private var characters = List.empty[Character]
  private var wormhole: Wormhole = _

  override def toString: String = s"This is $name galaxy!"

  def addCharacter(c: Character): Unit = characters = c :: characters

  def removeCharacter(c: Character): Unit = characters.contains(c) match {
    case true => characters = characters.filter(_ != c)
    case _ => throw new NonExistentCharacterException(s"No one in $name ever heard of ${c.name}")
  }

  def getCharacterByName(n: String): Character = characters.find(c => c.name == n) match {
    case Some(x) => x
    case _ => throw new NonExistentCharacterException(s"No one in $name ever heard of $n")
  }

  def getRandomCharacter: Character = characters.length match {
    case 0 => throw new EmptyGalaxyException(s"No one lives in $name")
    case x => characters(Random.nextInt(x))
  }

  def getMessageFromWormhole: String = wormhole match {
    case null => throw new NoWormholeException(s"There is no wormhole in $name")
    case w => w.getMessage match {
      case Some(m) => m
      case _ => throw new NoMessageFromAnotherGalaxyException("Another galaxy is silent")
    }
  }

  def setWormhole(w: Wormhole): Unit = wormhole = w
}
