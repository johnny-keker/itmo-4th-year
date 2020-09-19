package objectmodel

class Character(
  var Name: String,
  var Age: Int,
  var Habits: List[Habit]
) {
  def greet(): String = s"Hello, my name is $Name"

  def isLifestyleHealthy(): Boolean =
    Habits.count(h => h.Type == HabitType.Healthy) > Habits.count(h => h.Type == HabitType.Unhealthy)
}
