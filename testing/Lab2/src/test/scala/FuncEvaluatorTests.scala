import function._
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions._
import org.mockito.MockitoSugar._
import org.mockito.Mockito.{ never, times, verify }
import org.mockito.ArgumentMatchers._

class FuncEvaluatorTests {
  val epsilon = 1E-6

  @Test def `negative area basic test`(): Unit = {
    val mockTrig = mock[TTrigEvaluator]
    when(mockTrig.sin(-0.313)).thenReturn(Math.sin(-0.313))
    when(mockTrig.cos(-0.313)).thenReturn(Math.cos(-0.313))
    when(mockTrig.csc(-0.313)).thenReturn(1 / Math.sin(-0.313))

    val mockLog = mock[TLogEvaluator]

    val f = new FuncEvaluator(mockTrig, mockLog)
    assertEquals(f.func(-0.313), -0.706703, epsilon,
      "sample negative area test with trig evaluator mocking")

    verify(mockTrig, times(2)).sin(-0.313)
    verify(mockTrig, times(4)).cos(-0.313)
    verify(mockTrig, times(2)).csc(-0.313)
    verify(mockLog, never()).log(anyDouble(), anyDouble(), anyDouble())
  }

  @Test def `positive area basic test`(): Unit = {
    val mockTrig = mock[TTrigEvaluator]

    val mockLog = mock[TLogEvaluator]
    when(mockLog.log(0.313, 2)).thenReturn(Math.log(0.313) / Math.log(2))
    when(mockLog.log(0.313, 5)).thenReturn(Math.log(0.313) / Math.log(5))
    when(mockLog.log(0.313, 3)).thenReturn(Math.log(0.313) / Math.log(3))
    when(mockLog.log(0.313, 10)).thenReturn(Math.log(0.313) / Math.log(10))

    val f = new FuncEvaluator(mockTrig, mockLog)
    assertEquals(f.func(0.313), 0.403459, epsilon,
      "sample positive area test with trig evaluator mocking")

    verify(mockTrig, never()).sin(anyDouble(), anyDouble())
    verify(mockTrig, never()).cos(anyDouble(), anyDouble())
    verify(mockTrig, never()).csc(anyDouble(), anyDouble())
    verify(mockLog, times(1)).log(0.313, 2)
    verify(mockLog, times(2)).log(0.313, 5)
    verify(mockLog, times(3)).log(0.313, 3)
    verify(mockLog, times(1)).log(0.313, 10)
  }

  @Test def `invalid input parameters test`(): Unit = {
    val mockTrig = mock[TTrigEvaluator]
    val mockLog = mock[TLogEvaluator]

    val f = new FuncEvaluator(mockTrig, mockLog)

    assertTrue(f.func(Double.NaN).isNaN, "func(NaN) should be NaN")
    assertTrue(f.func(Double.PositiveInfinity).isNaN, "func(+inf) should be NaN")
    assertTrue(f.func(Double.NegativeInfinity).isNaN, "func(-inf) should be NaN")

    verify(mockTrig, never()).sin(anyDouble(), anyDouble())
    verify(mockTrig, never()).cos(anyDouble(), anyDouble())
    verify(mockTrig, never()).csc(anyDouble(), anyDouble())
    verify(mockLog, never()).log(anyDouble(), anyDouble(), anyDouble())
  }
}
