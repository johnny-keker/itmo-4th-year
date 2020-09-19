package objectmodel

class Character(
  var Name: String,
  var Age: Int,
  var Habits: List[Habit]
) {
  if (Name == "") throw new Exception("Character must have non-empty name!")
  if (Age < 0) throw new Exception("Age must be positive integer!")
  if (Habits == Nil) throw new Exception("Habits cannot be Nil!")

  override def toString: String = s"Hello, my name is $Name"

  def isLifestyleHealthy(): Boolean =
    Habits.count(h => h.Type == HabitType.Healthy) > Habits.count(h => h.Type == HabitType.Unhealthy)
}
