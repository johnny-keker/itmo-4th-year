import objectmodel._
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.function.Executable

class ObjectModelTests {
  // large list of habits that we will use in test cases
  val habits = List(
    new Habit("smoking",              HabitType.Unhealthy),
    new Habit("drinking",             HabitType.Unhealthy),
    new Habit("swearing",             HabitType.Unhealthy),
    new Habit("procrastinating",      HabitType.Unhealthy),
    new Habit("doing sports",         HabitType.Healthy),
    new Habit("sleeping 8h",          HabitType.Healthy),
    new Habit("meditating",           HabitType.Healthy),
    new Habit("planning",             HabitType.Healthy),
    new Habit("cracking knuckles",    HabitType.Neutral),
    new Habit("spinning pen",         HabitType.Neutral),
    new Habit("superstitious",        HabitType.Neutral),
    new Habit("talking to myself",    HabitType.Neutral)
  )

  val thoughts: List[String] = habits.map(h => s"I'm ${h.Name} and it's ${h.Type match {
                                                                            case HabitType.Healthy => "cool!"
                                                                            case HabitType.Unhealthy => "terrible("
                                                                            case _ => "ok."
                                                                          }}")

  //<editor-fold desc="Character tests">

  @Test
  def greetTest(): Unit = {
    val c = new Character("Jill", 27, Nil)
    assertEquals("Hello, my name is Jill", c.toString(),
      "Init character test, simply test the toSting output")
  }

  @Test
  def lifestyleTest(): Unit = {
    // in this assertion we pass whole habits list to
    // our character, that means that he has 4 healthy,
    // 4 unhealthy and 4 neutral habits. This is considered
    // as unhealthy lifestyle
    var c = new Character("Jill", 27, habits)
    assertEquals("I think I have a big lifestyle issues", c.lifestyleThoughts,
      "Unhealthy lifestyle test")

    // in this case we pass all habits except the first one,
    // that means that Jill now has only 3 unhealthy habits
    // and we could call her lifestyle healthy if
    // she'd quit smoking
    c = new Character("Jill", 27, habits.tail)
    assertEquals("I don’t seem to have a lifestyle issues", c.lifestyleThoughts,
      "Healthy lifestyle test")
  }

  @Test
  def characterCreationValidationTest(): Unit = {
    // in this assertion we expect character creation
    // to throw an exception because of empty character name
    var exe: Executable = () => new Character("", 27, habits)
    var exc = assertThrows(classOf[IllegalArgumentException], exe)
    assertEquals("Character must have non-empty name!", exc.getMessage)

    // in this assertion we expect character creation
    // to throw an exception because of negative character age
    exe = () => new Character("Jill", -27, habits)
    exc = assertThrows(classOf[IllegalArgumentException], exe)
    assertEquals("Age must be positive integer!", exc.getMessage)

    // in this assertion we expect character creation
    // to throw an exception because of Nil as character habits
    exe = () => new Character("Jill", 27, null)
    exc = assertThrows(classOf[IllegalArgumentException], exe)
    assertEquals("Habits cannot be null!", exc.getMessage)
  }

  @Test
  def randomThoughtTest(): Unit = {
    val c = new Character("Jill", 27, habits)
    assertTrue(thoughts.contains(c.randomThought))
  }
  //</editor-fold>

  //<editor-fold desc="Habit tests">

  @Test
  def habitTest(): Unit = {
    // init habit test, simply test the toSting() output
    assertEquals("Smoking is an unhealthy habit", habits.head.toString())
  }

  @Test
  def habitCreationValidationTest(): Unit = {
    var exe: Executable = () => new Habit("", HabitType.Neutral)
    var exc = assertThrows(classOf[IllegalArgumentException], exe)
    assertEquals("Habit must have non-empty name!", exc.getMessage)

    exe = () => new Habit("Hmm", null)
    exc = assertThrows(classOf[IllegalArgumentException], exe)
    assertEquals("You must specify valid habit type!", exc.getMessage)
  }
  //</editor-fold>
}
