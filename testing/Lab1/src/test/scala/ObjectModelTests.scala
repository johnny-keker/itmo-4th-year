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
    val c = new Character("Jill", 27, Gender.Female, Nil)
    assertEquals("Hello, my name is Jill", c.toString(),
      "Init character test, simply test the toSting output")
  }

  @Test
  def lifestyleTest(): Unit = {
    // in this assertion we pass whole habits list to
    // our character, that means that he has 4 healthy,
    // 4 unhealthy and 4 neutral habits. This is considered
    // as unhealthy lifestyle
    var c = new Character("Jill", 27, Gender.Female, habits)
    assertEquals("I think I have a big lifestyle issues", c.lifestyleThoughts,
      "Unhealthy lifestyle test")

    // in this case we pass all habits except the first one,
    // that means that Jill now has only 3 unhealthy habits
    // and we could call her lifestyle healthy if
    // she'd quit smoking
    c = new Character("Jill", 27, Gender.Female, habits.tail)
    assertEquals("I don’t seem to have a lifestyle issues", c.lifestyleThoughts,
      "Healthy lifestyle test")
  }

  @Test
  def characterCreationValidationTest(): Unit = {
    // in this assertion we expect character creation
    // to throw an exception because of empty character name
    var exe: Executable = () => new Character("", 27, Gender.Female, habits)
    var exc = assertThrows(classOf[IllegalArgumentException], exe)
    assertEquals("Character must have non-empty name!", exc.getMessage)

    // in this assertion we expect character creation
    // to throw an exception because of negative character age
    exe = () => new Character("Jill", -27, Gender.Female, habits)
    exc = assertThrows(classOf[IllegalArgumentException], exe)
    assertEquals("Age must be positive integer!", exc.getMessage)

    // in this assertion we expect character creation
    // to throw an exception because of null as character habits
    exe = () => new Character("Jill", 27, null, habits)
    exc = assertThrows(classOf[IllegalArgumentException], exe)
    assertEquals("Gender cannot be null!", exc.getMessage)

    // in this assertion we expect character creation
    // to throw an exception because of null as character habits
    exe = () => new Character("Jill", 27, Gender.Female, null)
    exc = assertThrows(classOf[IllegalArgumentException], exe)
    assertEquals("Habits cannot be null!", exc.getMessage)
  }

  @Test
  def randomThoughtTest(): Unit = {
    val c = new Character("Jill", 27, Gender.Female, habits)
    assertTrue(thoughts.contains(c.randomThought))
  }

  @Test
  def throwsOnRandomThoughtIfNoHabits(): Unit = {
    val c = new Character("Jill", 27, Gender.Female, Nil)
    val exe: Executable = () => c.randomThought
    val exc = assertThrows(classOf[FlatCharacterException], exe)
    assertEquals("Jill has no thoughts! You should've gave her at least one habit!", exc.getMessage)
  }
  //</editor-fold>

  //<editor-fold desc="Habit tests">

  @Test
  def habitTest(): Unit = {
    // init habit test, simply test the toSting() output
    assertEquals("smoking is an unhealthy habit", habits.head.toString())
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

  //<editor-fold desc="Galaxy tests">

  @Test
  def initGalaxyTest(): Unit = {
    val g = new Galaxy("Eizouken")
    assertEquals("This is Eizouken galaxy!", g.toString)
  }

  @Test
  def galaxyCreationValidationTest(): Unit = {
    val exe: Executable = () => new Galaxy(null)
    val exc = assertThrows(classOf[IllegalArgumentException], exe)
    assertEquals("Galaxy must have non-empty name!", exc.getMessage)
  }

  @Test
  def galaxyNonExistentCharacterTest(): Unit = {
    val g = new Galaxy("Eizouken")
    val c = new Character("Jill", 27, Gender.Female, habits)

    var exe: Executable = () => g.getCharacterByName("Jill")
    var exc = assertThrows(classOf[NonExistentCharacterException], exe)
    assertEquals("No one in Eizouken ever heard of Jill", exc.getMessage)

    exe = () => g.removeCharacter(c)
    exc = assertThrows(classOf[NonExistentCharacterException], exe)
    assertEquals("No one in Eizouken ever heard of Jill", exc.getMessage)
  }

  @Test
  def galaxyAddCharacterTest(): Unit = {
    val g = new Galaxy("Eizouken")
    val m = new Character("Midori Asakusa", 15, Gender.Female, List(new Habit("curious", HabitType.Healthy)))
    val s = new Character("Sayaka Kanamori", 15, Gender.Female, List(new Habit("resourcefulness", HabitType.Healthy)))
    val t = new Character("Tsubame Mizusaki", 15, Gender.Female, List(new Habit("hard-working", HabitType.Healthy)))

    g.addCharacter(m)
    g.addCharacter(s)
    g.addCharacter(t)

    assertEquals(m, g.getCharacterByName("Midori Asakusa"))
    assertEquals(s, g.getCharacterByName("Sayaka Kanamori"))
    assertEquals(t, g.getCharacterByName("Tsubame Mizusaki"))
  }
  //</editor-fold>
}
