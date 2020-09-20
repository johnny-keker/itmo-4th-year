package objectmodel

object HabitType extends Enumeration {
  val Healthy = Value("healthy")
  val Unhealthy = Value("unhealthy")
  val Neutral = Value("neutral")
}

class Habit(
  var Name: String,
  var Type: HabitType.Value
) {
  if (Name == "") throw new IllegalArgumentException("Habit must have non-empty name!")
  if (Type == null) throw new IllegalArgumentException("You must specify valid habit type!")

  override def toString: String = s"$Name is an ${Type.toString} habit"
}
