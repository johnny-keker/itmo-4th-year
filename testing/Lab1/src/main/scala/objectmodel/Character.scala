package objectmodel

import scala.util.Random

class FlatCharacterException(private val message: String = "") extends Exception(message)

object Gender extends Enumeration {
  val Female = Value("her")
  val Male = Value("him")
  val Other = Value("it")
}

class Character(
   var name: String,
   var age: Int,
   var gender: Gender.Value,
   var habits: List[Habit]
) {
  if (name == "") throw new IllegalArgumentException("Character must have non-empty name!")
  if (age < 0) throw new IllegalArgumentException("Age must be positive integer!")
  if (gender == null) throw new IllegalArgumentException("Gender cannot be null!")
  if (habits == null) throw new IllegalArgumentException("Habits cannot be null!")

  private val thoughts = habits.map(h => s"I'm ${h.Name} and it's ${h.Type match {
                                                                      case HabitType.Healthy => "cool!"
                                                                      case HabitType.Unhealthy => "terrible("
                                                                      case _ => "ok."
                                                                    }}")

  override def toString: String = s"Hello, my name is $name"

  private def isLifestyleHealthy: Boolean =
    habits.count(h => h.Type == HabitType.Healthy) > habits.count(h => h.Type == HabitType.Unhealthy)

  def lifestyleThoughts: String = if (isLifestyleHealthy) "I don’t seem to have a lifestyle issues"
                                                       else "I think I have a big lifestyle issues"

  def randomThought: String = thoughts.length match {
    case 0 => throw new FlatCharacterException(s"$name has no thoughts! You should've gave $gender at least one habit!")
    case x => thoughts(Random.nextInt(x))
  }
}
