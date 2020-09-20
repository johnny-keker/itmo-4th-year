package objectmodel

class NonExistentCharacterException(private val message: String = "") extends Exception(message)

class Galaxy(name: String) {
  if (name == null || name == "") throw new IllegalArgumentException("Galaxy must have non-empty name!")

  private var characters = List.empty[Character]

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
}
