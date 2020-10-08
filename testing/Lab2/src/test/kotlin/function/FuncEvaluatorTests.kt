package function

import org.mockito.Mockito.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.ln

class FuncEvaluatorTests {
    private val epsilon = 1E-6

    @Test fun `negative area basic test`() {
        val mockTrig = mock(ITrigEvaluator::class.java).apply {
            `when`(sin(-0.313)).thenReturn(kotlin.math.sin(-0.313))
            `when`(cos(-0.313)).thenReturn(kotlin.math.cos(-0.313))
            `when`(csc(-0.313)).thenReturn(1 / kotlin.math.sin(-0.313))
        }

        val mockLog = mock(ILogEvaluator::class.java)

        val f = FuncEvaluator(mockTrig, mockLog)
        assertEquals(f.func(-0.313), -0.706703, epsilon,
            "sample negative area test with trig evaluator mocking")

        verify(mockTrig, times(2)).sin(-0.313)
        verify(mockTrig, times(4)).cos(-0.313)
        verify(mockTrig, times(2)).csc(-0.313)
        verify(mockLog, never()).log(anyDouble(), anyDouble(), anyDouble())
    }

    @Test fun `positive area basic test`() {
        val mockTrig = mock(ITrigEvaluator::class.java)

        val mockLog = mock(ILogEvaluator::class.java).apply {
            `when`(log(0.313, 2.0)).thenReturn(ln(0.313) / ln(2.0))
            `when`(log(0.313, 5.0)).thenReturn(ln(0.313) / ln(5.0))
            `when`(log(0.313, 3.0)).thenReturn(ln(0.313) / ln(3.0))
            `when`(log(0.313, 10.0)).thenReturn(ln(0.313) / ln(10.0))
        }

        val f = FuncEvaluator(mockTrig, mockLog)
        assertEquals(f.func(0.313), 0.403459, epsilon,
            "sample positive area test with trig evaluator mocking")

        verify(mockTrig, never()).sin(anyDouble(), anyDouble())
        verify(mockTrig, never()).cos(anyDouble(), anyDouble())
        verify(mockTrig, never()).csc(anyDouble(), anyDouble())
        verify(mockLog, times(1)).log(0.313, 2.0)
        verify(mockLog, times(2)).log(0.313, 5.0)
        verify(mockLog, times(3)).log(0.313, 3.0)
        verify(mockLog, times(1)).log(0.313, 10.0)
    }

    @Test fun `invalid input parameters test`() {
        val mockTrig = mock(ITrigEvaluator::class.java)
        val mockLog = mock(ILogEvaluator::class.java)

        val f = FuncEvaluator(mockTrig, mockLog)

        assertTrue(f.func(Double.NaN).isNaN(), "func(NaN) should be NaN")
        assertTrue(f.func(Double.POSITIVE_INFINITY).isNaN(), "func(+inf) should be NaN")
        assertTrue(f.func(Double.NEGATIVE_INFINITY).isNaN(), "func(-inf) should be NaN")

        verify(mockTrig, never()).sin(anyDouble(), anyDouble())
        verify(mockTrig, never()).cos(anyDouble(), anyDouble())
        verify(mockTrig, never()).csc(anyDouble(), anyDouble())
        verify(mockLog, never()).log(anyDouble(), anyDouble(), anyDouble())
    }
}