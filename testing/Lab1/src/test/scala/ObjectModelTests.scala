import org.junit.Test
import org.junit.Assert._
import objectmodel._

class ObjectModelTests {
  val habits = List(
    new Habit("Smoking", HabitType.Unhealthy),
    new Habit("Drinking", HabitType.Unhealthy),
    new Habit("Swearing", HabitType.Unhealthy),
    new Habit("Procrastination", HabitType.Unhealthy),
    new Habit("Doing sports", HabitType.Healthy),
    new Habit("8h sleep", HabitType.Healthy),
    new Habit("Meditating", HabitType.Healthy),
    new Habit("Planning", HabitType.Healthy),
    new Habit("Cracking knuckles", HabitType.Neutral),
    new Habit("Pen spinning", HabitType.Neutral),
    new Habit("Superstitions", HabitType.Neutral),
    new Habit("Talking to yourself", HabitType.Neutral)
  )

  @Test
  def greetTest() = {
    val c = new Character("Jack", 27, List[Habit]())
    assertEquals("Hello, my name is Jack", c.greet())
  }

  @Test
  def lifestyleTest() = {
    val c = new Character("Jack", 27, habits)
    assertEquals(false, c.isLifestyleHealthy())
  }

  @Test
  def habitTest() = {
    val h = habits.head
    assertEquals("Smoking is an unhealthy habit", h.toString())
  }
}
