import org.junit.Test
import org.junit.Assert._
import objectmodel._

class ObjectModelTests {
  // large list of habits that we will use in test cases
  val habits = List(
    new Habit("Smoking",              HabitType.Unhealthy),
    new Habit("Drinking",             HabitType.Unhealthy),
    new Habit("Swearing",             HabitType.Unhealthy),
    new Habit("Procrastination",      HabitType.Unhealthy),
    new Habit("Doing sports",         HabitType.Healthy),
    new Habit("8h sleep",             HabitType.Healthy),
    new Habit("Meditating",           HabitType.Healthy),
    new Habit("Planning",             HabitType.Healthy),
    new Habit("Cracking knuckles",    HabitType.Neutral),
    new Habit("Pen spinning",         HabitType.Neutral),
    new Habit("Superstitions",        HabitType.Neutral),
    new Habit("Talking to yourself",  HabitType.Neutral)
  )

  @Test
  def greetTest() = {
    // init character test, simply test the toSting() output
    val c = new Character("Jack", 27, List[Habit]())
    assertEquals("Hello, my name is Jack", c.toString())
  }

  @Test
  def lifestyleTest() = {
    // in this assertion we pass whole habits list to
    // our character, that means that he has 4 healthy,
    // 4 unhealthy and 4 neutral habits. This is considered
    // as unhealthy lifestyle
    var c = new Character("Jack", 27, habits)
    assertEquals(false, c.isLifestyleHealthy())

    // in this case we pass all habits except the first one,
    // that means that Jack now has only 3 unhealthy habits
    // and we can call his lifestyle healthy
    c = new Character("Jack", 27, habits.tail)
    assertEquals(true, c.isLifestyleHealthy())
  }

  @Test
  def habitTest() = {
    // init habit test, simply test the toSting() output
    assertEquals("Smoking is an unhealthy habit", habits.head.toString())
  }
}
