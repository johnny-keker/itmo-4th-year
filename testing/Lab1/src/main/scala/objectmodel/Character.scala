package objectmodel

class Character(
  var Name: String,
  var Age: Int,
  var Habits: List[Habit]
) {
  if (Name == "") throw new IllegalArgumentException("Character must have non-empty name!")
  if (Age < 0) throw new IllegalArgumentException("Age must be positive integer!")
  if (Habits == null) throw new IllegalArgumentException("Habits cannot be null!")

  override def toString: String = s"Hello, my name is $Name"

  def isLifestyleHealthy: Boolean =
    Habits.count(h => h.Type == HabitType.Healthy) > Habits.count(h => h.Type == HabitType.Unhealthy)
}
