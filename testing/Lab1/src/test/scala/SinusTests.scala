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

  /*
  * lets divide sin function in 7 areas:
  * 1. 0.0 - 0.8
  * 2. 0.8 - PI/2
  * 3. PI/2 - 2.3
  * 4. 2.3 - 3.9
  * 5. 3.9 - PI
  * 6. PI - 5.5
  * 7. 5.5 - 3PI/2
  */
  @Test
  def sinWorksInTheEdgesOfAreas(): Unit = {
    assertEquals(Math.sin(0.0), Sinus.compute(0.0), Sinus.PRECISION, "1 interval left edge")
    assertEquals(Math.sin(0.8), Sinus.compute(0.8), Sinus.PRECISION, "1/2 interval edge")
    assertEquals(Math.sin(Math.PI/2), Sinus.compute(Math.PI/2), Sinus.PRECISION, "2/3 interval edge")
    assertEquals(Math.sin(2.3), Sinus.compute(2.3), Sinus.PRECISION, "3/4 interval edge")
    assertEquals(Math.sin(3.9), Sinus.compute(3.9), Sinus.PRECISION, "4/5 interval edge")
    assertEquals(Math.sin(Math.PI), Sinus.compute(Math.PI), Sinus.PRECISION, "5/6 interval edge")
    assertEquals(Math.sin(5.5), Sinus.compute(5.5), Sinus.PRECISION, "6/7 interval edge")
    assertEquals(Math.sin(3*Math.PI/2), Sinus.compute(3*Math.PI/2), Sinus.PRECISION, "7 interval right edge")
  }

  @Test
  def sinWorksInsideAllAreas(): Unit = {
    var exp = 0.0 + ((0.8 - 0.0) / 2)
    assertEquals(Math.sin(exp), Sinus.compute(exp), Sinus.PRECISION, "1 interval center")
    exp = 0.8 + ((Math.PI/2 - 0.8) / 2)
    assertEquals(Math.sin(exp), Sinus.compute(exp), Sinus.PRECISION, "2 interval center")
    exp = Math.PI/2 + ((2.3 - Math.PI/2) / 2)
    assertEquals(Math.sin(exp), Sinus.compute(exp), Sinus.PRECISION, "3 interval center")
    exp = 2.3 + ((3.9 - 2.3) / 2)
    assertEquals(Math.sin(exp), Sinus.compute(exp), Sinus.PRECISION, "4 interval center")
    exp = 3.9 + ((Math.PI - 3.9) / 2)
    assertEquals(Math.sin(exp), Sinus.compute(exp), Sinus.PRECISION, "5 interval center")
    exp = Math.PI + ((5.5 - Math.PI) / 2)
    assertEquals(Math.sin(exp), Sinus.compute(exp), Sinus.PRECISION, "6 interval center")
    exp = 5.5 + ((3*Math.PI/2 - 5.5) / 2)
    assertEquals(Math.sin(exp), Sinus.compute(exp), Sinus.PRECISION, "3 interval center")
  }

  @Test
  def testRandom() = {
    val x = Random.nextDouble()
    assertEquals(Math.sin(x), Sinus.compute(x), Sinus.PRECISION, s"Sin of random double test, arg = $x")
  }
}
