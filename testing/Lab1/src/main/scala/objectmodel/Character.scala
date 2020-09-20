package objectmodel

import scala.util.Random
import scala.collection.mutable.ListBuffer

class Character(
  var Name: String,
  var Age: Int,
  var Habits: List[Habit]
) {
  if (Name == "") throw new IllegalArgumentException("Character must have non-empty name!")
  if (Age < 0) throw new IllegalArgumentException("Age must be positive integer!")
  if (Habits == null) throw new IllegalArgumentException("Habits cannot be null!")

  private val thoughts = Habits.map(h => s"I'm ${h.Name} and it's ${h.Type match {
                                                                      case HabitType.Healthy => "cool!"
                                                                      case HabitType.Unhealthy => "terrible("
                                                                      case _ => "ok."
                                                                    }}")

  override def toString: String = s"Hello, my name is $Name"

  private def isLifestyleHealthy: Boolean =
    Habits.count(h => h.Type == HabitType.Healthy) > Habits.count(h => h.Type == HabitType.Unhealthy)

  def lifestyleThoughts: String = if (isLifestyleHealthy) "I don’t seem to have a lifestyle issues"
                                                       else "I think I have a big lifestyle issues"

  def randomThought: String = thoughts(Random.nextInt(thoughts.length))
}
