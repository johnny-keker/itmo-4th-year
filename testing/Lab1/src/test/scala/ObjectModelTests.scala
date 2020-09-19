import org.junit.Assert._
import objectmodel._
import org.junit.{Rule, Test}
import org.junit.rules.ExpectedException

import scala.annotation.meta.getter

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

  //<editor-fold desc="Character tests">

  @Test
  def greetTest(): Unit = {
    // init character test, simply test the toSting() output
    val c = new Character("Jack", 27, habits)
    assertEquals("Hello, my name is Jack", c.toString())
  }

  @Test
  def lifestyleTest(): Unit = {
    // in this assertion we pass whole habits list to
    // our character, that means that he has 4 healthy,
    // 4 unhealthy and 4 neutral habits. This is considered
    // as unhealthy lifestyle
    var c = new Character("Jack", 27, habits)
    assertFalse(c.isLifestyleHealthy())

    // in this case we pass all habits except the first one,
    // that means that Jack now has only 3 unhealthy habits
    // and we can call his lifestyle healthy
    c = new Character("Jack", 27, habits.tail)
    assertTrue(c.isLifestyleHealthy())
  }

  @(Rule@getter)
  val exceptionRule: ExpectedException = ExpectedException.none()

  @Test
  def characterCreationNameValidationTest(): Unit = {
    // in this assertion we expect character creation
    // to throw an exception because of empty character name
    exceptionRule.expectMessage("Character must have non-empty name!")
    val _ = new Character("", 27, habits)
  }

  @Test
  def characterCreationAgeValidationTest(): Unit = {
    // in this assertion we expect character creation
    // to throw an exception because of negative character age
    exceptionRule.expectMessage("Age must be positive integer!")
    val _ = new Character("Jack", -27, habits)
  }

  @Test
  def characterCreationHabitsValidationTest(): Unit = {
    // in this assertion we expect character creation
    // to throw an exception because of Nil as character habits
    exceptionRule.expectMessage("Habits cannot be Nil!")
    val _ = new Character("Jack", 27, Nil)
  }
  //</editor-fold>

  //<editor-fold desc="Habit tests">

  @Test
  def habitTest(): Unit = {
    // init habit test, simply test the toSting() output
    assertEquals("Smoking is an unhealthy habit", habits.head.toString())
  }
  //</editor-fold>
}
