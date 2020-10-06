import function._
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._
import org.mockito.MockitoSugar._
import org.mockito.Mockito.{ never, times, verify }
import org.mockito.ArgumentMatchers._

class TrigEvaluatorTests {
  val epsilon = 1E-6

  @Test def `basic cos test`(): Unit = {
    val sinMock = mock[TSinus]
    when(sinMock.compute(0.313 + Math.PI / 2)).thenReturn(Math.sin(0.313 + Math.PI / 2))

    val trigEval = new TrigEvaluator(sinMock)
    assertEquals(trigEval.cos(0.313), Math.cos(0.313), epsilon,
      "cos should call sin(x + PI / 2)")
  }

  @Test def `basic csc test`(): Unit = {
    val sinMock = mock[TSinus]
    when(sinMock.compute(0.313)).thenReturn(Math.sin(0.313))

    val trigEval = new TrigEvaluator(sinMock)
    assertEquals(trigEval.csc(0.313), 1 / Math.sin(0.313), epsilon,
      "csc should call 1 / sin(x)")
  }

  @Test def `csc validation test`(): Unit = {
    val sinMock = mock[TSinus]
    when(sinMock.compute(0)).thenReturn(0)
    when(sinMock.compute(Double.NaN)).thenReturn(Double.NaN)
    when(sinMock.compute(Double.PositiveInfinity)).thenReturn(Double.NaN)
    when(sinMock.compute(Double.NegativeInfinity)).thenReturn(Double.NaN)

    val trigEval = new TrigEvaluator(sinMock)
    assertTrue(trigEval.csc(0).isNaN, "csc should check if sin(x) is 0 and return NaN if so")
    assertTrue(trigEval.csc(Double.NaN).isNaN, "csc(NaN) is NaN")
    assertTrue(trigEval.csc(Double.PositiveInfinity).isNaN, "csc(+inf) is NaN")
    assertTrue(trigEval.csc(Double.NegativeInfinity).isNaN, "csc(-inf) is NaN")
  }

  @Test def `cos validation test`(): Unit = {
    val sinMock = mock[TSinus]

    val trigEval = new TrigEvaluator(sinMock)
    assertTrue(trigEval.cos(Double.NaN).isNaN, s"cos(NaN) is NaN")
    assertTrue(trigEval.cos(Double.PositiveInfinity).isNaN, "cos(+inf) is NaN")
    assertTrue(trigEval.cos(Double.NegativeInfinity).isNaN, "cos(-inf) is NaN")
  }
  // TODO: cos and csc integration testing
}
