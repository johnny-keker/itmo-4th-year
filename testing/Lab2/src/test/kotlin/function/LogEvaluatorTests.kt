package function

import org.mockito.Mockito.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import kotlin.math.ln

class LogEvaluatorTests {
    private val epsilon = 1E-6

    @Test fun `basic log test`() {
        val logMock = mock(ILog::class.java).apply {
            `when`(compute(0.313, 1E-5 / 100)).thenReturn(ln(0.313))
            `when`(compute(5.0, 1E-5 / 100)).thenReturn(ln(5.0))
        }

        val logEval = LogEvaluator(logMock)
        assertEquals(ln(0.313) / ln(5.0), logEval.log(0.313, 5.0), epsilon,
            "log should call ln(x)/ln(base))")
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/log_neg_cases.csv"], numLinesToSkip = 1)
    fun `negative log cases`(x: Double, base: Double, eps: Double, mes: String) {
        val logEval = LogEvaluator(mock(ILog::class.java))
        assertTrue(logEval.log(x, base, eps).isNaN(), mes)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/log_cases.csv"], numLinesToSkip = 1)
    fun `integrated log testing`(x: Double, base: Double) {
        val logMock = mock(ILog::class.java).apply {
            `when`(compute(x, 1E-5/100)).thenReturn(ln(x))
            `when`(compute(base, 1E-5/100)).thenReturn(ln(base))
        }
        val logEval = LogEvaluator(logMock)
        assertEquals(ln(x)/ln(base), logEval.log(x, base), epsilon)
    }
}