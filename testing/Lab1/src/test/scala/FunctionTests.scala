import org.junit.Test
import org.junit.Assert._
import scala.util.Random

class FunctionTests {
  @Test
  def testConst() = {
    assertEquals(0.0, Function.sin(0.0), 0)
  }

  @Test
  def testRandom() = {
    val x = Random.nextDouble()
    assertEquals(Math.sin(x), Function.sin(x), Function.PRECISION)
  }
}
