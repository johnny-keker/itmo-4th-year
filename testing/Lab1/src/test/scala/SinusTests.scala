import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._

import scala.util.Random

class SinusTests {
  @Test
  def testArgsValidation(): Unit = {
    assertEquals(Double.NaN, Sinus.compute(Double.NaN), "sin(NaN) must be NaN")
    assertEquals(Double.NaN, Sinus.compute(Double.PositiveInfinity), "sin(+inf) must be NaN")
    assertEquals(Double.NaN, Sinus.compute(Double.NegativeInfinity), "sin(-inf) must be NaN")
  }

  @Test
  def oxIntersectionPointsTest(): Unit = {
    for (i <- 0 to 5) {
      assertEquals(0.0, Sinus.compute(Math.PI * i.toDouble), Sinus.PRECISION, s"sin(PI*$i) must be 0.0")
      assertEquals(0.0, Sinus.compute(-Math.PI * i.toDouble), Sinus.PRECISION, s"sin(-PI*$i) must be 0.0")
    }
  }

  @Test
  def extremumPointsTest(): Unit = {
    var expected = 1.0
    for (i <- 1 to 7 by 2) {
      assertEquals(expected, Sinus.compute(Math.PI / 2.0 * i.toDouble), Sinus.PRECISION, s"sin(PI/2*$i) must be $expected")
      expected *= -1
    }
  }

  @Test
  def sinIsPeriodicTest(): Unit = {
    var expected = 1/Math.sqrt(2)
    for (i <- 1 to 12 by 4) {
      assertEquals(expected, Sinus.compute(i * Math.PI / 4), Sinus.PRECISION, s"sin(${i}*Pi/4) must be $expected")
      assertEquals(expected, Sinus.compute((i + 2) * Math.PI / 4), Sinus.PRECISION, s"sin(${i+2}*Pi/4) must be $expected")
      expected *= -1
    }
  }

  @Test
  def testRandom() = {
    val x = Random.nextDouble()
    assertEquals(Math.sin(x), Sinus.compute(x), Sinus.PRECISION, s"Sin of random double test, arg = $x")
  }
}
