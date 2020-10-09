package function

import org.mockito.Mockito.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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

    @Nested
    inner class TrigIntegrationTests {
        lateinit var sine: Sinus
        lateinit var trigEval: TrigEvaluator

        @BeforeEach
        fun `init sine instance`() {
            sine = Sinus()
            trigEval = TrigEvaluator(sine)
        }

        @Test fun `cos ox intersection points test`() {
            for (i in 1..7 step 2) {
                assertEquals(0.0, trigEval.cos(Math.PI / 2 * i.toDouble()), epsilon, "cos(PI/2*$i) must be 0.0")
                assertEquals(0.0, trigEval.cos(-Math.PI / 2 * i.toDouble()), epsilon, "cos(-PI/2*$i) must be 0.0")
            }
        }

        @Test fun `cos extremum points test`() {
            var expected = 1.0
            for (i in 0..7) {
                assertEquals(expected, trigEval.cos(Math.PI * i.toDouble()), epsilon, "cos(PI*$i) must be $expected")
                expected *= -1
            }
        }

        @Test fun `cos is periodic test`() {
            var expected = 0.877583
            for (i in 0..5) {
                assertEquals(expected, trigEval.cos(0.5 + Math.PI * i), epsilon, "sin(0.5 + PI*${i}) must be $expected")
                expected *= -1
            }
        }

        /*
         * lets divide sin function in 8 areas:
         * 1. 0.0 - PI/4
         * 2. PI/4 - PI/2
         * 3. PI/2 - 3PI/4
         * 4. 3PI/4 - PI
         * 6. PI - 5PI/4
         * 7. 5PI/4 - 3PI/2
         * 8. 3PI/2 - 7PI/4
         * 9. 4PI/4 - 2PI
         */
        @ParameterizedTest
        @CsvFileSource(resources = ["/cos_equivalence_analysis.csv"], numLinesToSkip = 1)
        fun `sine equivalence analysis`(x: Double, mes: String) {
            assertEquals(cos(x), trigEval.cos(x), epsilon, mes)
        }

        @Test fun `csc extremum points test`() {
            var expected = 1.0
            for (i in 1..7 step 2) {
                assertEquals(expected, trigEval.csc(Math.PI / 2 * i.toDouble()), epsilon, "csc(PI/2*$i) must be $expected")
                expected *= -1
            }
        }

        @Test fun `csc is periodic test`() {
            var expected = 2.085829
            for (i in 0..5) {
                assertEquals(expected, trigEval.csc(0.5 + Math.PI * i), epsilon, "csc(0.5 + PI*${i}) must be $expected")
                expected *= -1
            }
        }

        /*
         * lets divide csc function in 4 areas:
         * 1. 0.1 - PI/2
         * 2. PI/2 - PI-0.1
         * 3. PI+0.1 - 3PI/2
         * 4. 3PI/2 - 2PI-0.1
         */
        @ParameterizedTest
        @CsvFileSource(resources = ["/csc_equivalence_analysis.csv"], numLinesToSkip = 1)
        fun `csc equivalence analysis`(x: Double, mes: String) {
            assertEquals(1/sin(x), trigEval.csc(x), epsilon, mes)
        }
    }
}