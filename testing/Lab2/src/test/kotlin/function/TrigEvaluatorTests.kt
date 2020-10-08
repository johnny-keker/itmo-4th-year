package function

import org.mockito.Mockito.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.cos
import kotlin.math.sin

class TrigEvaluatorTests {
    private val epsilon = 1E-6

    @Test fun `basic cos test`() {
        val sinMock = mock(ISinus::class.java).apply {
            `when`(compute(0.313 + Math.PI / 2)).thenReturn(sin(0.313 + Math.PI / 2))
        }

        val trigEval = TrigEvaluator(sinMock)
        assertEquals(trigEval.cos(0.313), cos(0.313), epsilon,
            "cos should call sin(x + PI / 2)")
    }

    @Test fun `basic csc test`() {
        val sinMock = mock(ISinus::class.java).apply {
            `when`(compute(0.313)).thenReturn(sin(0.313))
        }

        val trigEval = TrigEvaluator(sinMock)
        assertEquals(trigEval.csc(0.313), 1 / sin(0.313), epsilon,
            "csc should call 1 / sin(x)")
    }

    @Test fun `csc validation test`() {
        val sinMock = mock(ISinus::class.java).apply {
            `when`(compute(0.0)).thenReturn(0.0)
            `when`(compute(Double.NaN)).thenReturn(Double.NaN)
            `when`(compute(Double.POSITIVE_INFINITY)).thenReturn(Double.NaN)
            `when`(compute(Double.NEGATIVE_INFINITY)).thenReturn(Double.NaN)
        }

        val trigEval = TrigEvaluator(sinMock)
        assertTrue(trigEval.csc(0.0).isNaN(), "csc should check if sin(x) is 0 and return NaN if so")
        assertTrue(trigEval.csc(Double.NaN).isNaN(), "csc(NaN) is NaN")
        assertTrue(trigEval.csc(Double.POSITIVE_INFINITY).isNaN(), "csc(+inf) is NaN")
        assertTrue(trigEval.csc(Double.NEGATIVE_INFINITY).isNaN(), "csc(-inf) is NaN")
    }

    @Test fun `cos validation test`() {
        val sinMock = mock(ISinus::class.java)

        val trigEval = TrigEvaluator(sinMock)
        assertTrue(trigEval.cos(Double.NaN).isNaN(), "cos(NaN) is NaN")
        assertTrue(trigEval.cos(Double.POSITIVE_INFINITY).isNaN(), "cos(+inf) is NaN")
        assertTrue(trigEval.cos(Double.NEGATIVE_INFINITY).isNaN(), "cos(-inf) is NaN")
    }
    // TODO: cos and csc integration testing
}