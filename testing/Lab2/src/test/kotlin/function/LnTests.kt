package function

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.junit.jupiter.api.Test
import kotlin.math.ln

class LnTests {
    private val epsilon = 1E-5
    val lnEval = Log()

    @ParameterizedTest
    @CsvFileSource(resources = ["/ln_neg_cases.csv"], numLinesToSkip = 1)
    fun `negative sine cases`(x: Double, eps: Double, mes: String) {
        assertTrue(lnEval.compute(x, eps).isNaN(), mes)
    }

    @Test fun `ox intersection test`() {
        assertEquals(0.0, lnEval.compute(1.0), epsilon, "ln(1.0) is 0.0")
    }

    /*
     * lets divide ln function in 3 areas:
     * 1. 0.01 - 0.13
     * 2. 0.13 - 1.0
     * 3. 1.0 - 3.0
     */
    @ParameterizedTest
    @CsvFileSource(resources = ["/ln_equivalence_analysis.csv"], numLinesToSkip = 1)
    fun `sine equivalence analysis`(x: Double, mes: String) {
        assertEquals(ln(x), lnEval.compute(x), epsilon, mes)
    }
}