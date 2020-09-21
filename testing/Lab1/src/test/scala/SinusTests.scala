import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._
import scala.util.Random

class SinusTests {
  /*
  @Test
  def testConst() = {
    assertEquals(0.0, Sinus.compute(0.0), "This is sample test - sin(0.0)")
  }
  */
  @Test
  def testArgsValidation(): Unit = {
    assertEquals(Double.NaN, Sinus.compute(Double.NaN), "sin(NaN) must be NaN")
    assertEquals(Double.NaN, Sinus.compute(Double.PositiveInfinity), "sin(+inf) must be NaN")
    assertEquals(Double.NaN, Sinus.compute(Double.NegativeInfinity), "sin(-inf) must be NaN")
  }

  @Test
  def testRandom() = {
    val x = Random.nextDouble()
    assertEquals(Math.sin(x), Sinus.compute(x), Sinus.PRECISION, s"Sin of random double test, arg = $x")
  }
}
