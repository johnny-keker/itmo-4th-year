package function

import org.mockito.Mockito.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import kotlin.math.cos
import kotlin.math.ln

class FuncEvaluatorTests {
    private val epsilon = 1E-4

    @Test fun `negative area basic test`() {
        val mockTrig = mock(ITrigEvaluator::class.java).apply {
            `when`(sin(-0.313)).thenReturn(kotlin.math.sin(-0.313))
            `when`(cos(-0.313)).thenReturn(kotlin.math.cos(-0.313))
            `when`(csc(-0.313)).thenReturn(1 / kotlin.math.sin(-0.313))
        }

        val mockLog = mock(ILogEvaluator::class.java)

        val f = FuncEvaluator(mockTrig, mockLog)
        assertEquals(-0.706703, f.func(-0.313), epsilon,
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
        assertEquals(0.403459, f.func(0.313), epsilon,
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

    @Nested
    inner class TrigIntegrationTests {
        lateinit var f: FuncEvaluator

        @BeforeEach
        fun `init sine instance`() {
            val sine = Sinus()
            val trigEval = TrigEvaluator(sine)
            val ln = Log()
            val logEval = LogEvaluator(ln)

            f = FuncEvaluator(trigEval, logEval)
        }

        @Test
        fun `func ox intersection points test`() {
            for (i in -4..0) {
                assertEquals(0.0, f.func(2 * (-0.326841 + 3.14159 * i)), epsilon, "${2 * (-0.326841 + 3.14159 * i)}")
                assertEquals(0.0, f.func(2 * (0.455204 + 3.14159 * i)), epsilon, "${2 * (-0.326841 + 3.14159 * i)}")
            }
            assertEquals(0.0, f.func(0.25972), epsilon)
            assertEquals(0.0, f.func(0.65237), epsilon)
            assertEquals(0.0, f.func(1.0), epsilon)
        }

        @Test
        fun `func extrema test`() {
            for (i in -4..-1) {
                assertEquals(-1.0, f.func(2 * Math.PI * i), epsilon)
                assertEquals(-1.0, f.func(2 * Math.PI * i + Math.PI), epsilon)
                assertEquals(-0.10577, f.func(6.28319 * i - 2.25378), epsilon)
                assertEquals(0.16981, f.func(6.28319 * i + 1.25433), epsilon)
                assertEquals(-0.47759, f.func(6.28319 * i + 2.29858), epsilon)
                assertEquals(-0.17182, f.func(6.28319 * i -1.89889), epsilon)
                assertEquals(-0.47762, f.func(6.28319 * i + 2.27034), epsilon)
            }

            assertEquals(0.40748, f.func(0.306008), epsilon)
            assertEquals(-0.00078, f.func(0.717201), epsilon)
        }

        @ParameterizedTest
        @CsvFileSource(resources = ["/func_test.csv"], numLinesToSkip = 1)
        fun `func sample data test`(x: Double, res: Double) {
            assertEquals(res, f.func(x), epsilon)
        }
    }
}