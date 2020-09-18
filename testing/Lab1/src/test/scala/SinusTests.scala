import org.junit.Test
import org.junit.Assert._
import scala.util.Random

class SinusTests {
  @Test
  def testConst() = {
    assertEquals(0.0, Sinus.compute(0.0), 0)
  }

  @Test
  def testRandom() = {
    val x = Random.nextDouble()
    assertEquals(Math.sin(x), Sinus.compute(x), Sinus.PRECISION)
  }
}
