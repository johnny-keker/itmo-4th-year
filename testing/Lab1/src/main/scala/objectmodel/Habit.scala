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
  override def toString: String = s"$Name is an ${Type.toString()} habit"
}
