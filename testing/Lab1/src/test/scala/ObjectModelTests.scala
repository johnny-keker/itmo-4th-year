import org.junit.Test
import org.junit.Assert._
import objectmodel._

class ObjectModelTests {
  @Test
  def greetTest() = {
    val c = new Character("Jack", 27)
    assertEquals("Hello, my name is Jack", c.greet())
  }
}
