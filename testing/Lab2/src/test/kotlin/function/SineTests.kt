package function

import org.mockito.Mockito.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeAll
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SineTests {
    private val epsilon = 1E-6

    companion object {
        lateinit var sine: Sinus
        @BeforeAll
        @JvmStatic
        fun `init sine instance`() {
            sine = Sinus()
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/sine_neg_cases.csv"], numLinesToSkip = 1)
    fun `negative sine cases`(x: Double, eps: Double, mes: String) {
        assertTrue(sine.compute(x, eps).isNaN(), mes)
    }

    @Test fun `ox intersection points test`() {
        for (i in 0..5) {
            assertEquals(0.0, sine.compute(Math.PI * i.toDouble()), epsilon, "sin(PI*$i) must be 0.0")
            assertEquals(0.0, sine.compute(-Math.PI * i.toDouble()), epsilon, "sin(-PI*$i) must be 0.0")
        }
    }

    @Test fun `extremum points test`() {
        var expected = 1.0
        for (i in 1..7 step 2) {
            assertEquals(expected, sine.compute(Math.PI / 2.0 * i.toDouble()), epsilon, "sin(PI/2*$i) must be $expected")
            expected *= -1
        }
    }

    @Test fun `sin is periodic test`() {
        var expected = 1/ sqrt(2.0)
        for (i in 1..12 step 4) {
            assertEquals(expected, sine.compute(i * Math.PI / 4), epsilon, "sin(${i}*Pi/4) must be $expected")
            assertEquals(expected, sine.compute((i + 2) * Math.PI / 4), epsilon, "sin(${i+2}*Pi/4) must be $expected")
            expected *= -1
        }
    }

    /*
     * lets divide sin function in 7 areas:
     * 1. 0.0 - 0.8
     * 2. 0.8 - PI/2
     * 3. PI/2 - 2.3
     * 4. 2.3 - PI
     * 5. PI - 3.9
     * 6. 3.9 - 5.5
     * 7. 5.5 - 3PI/2
     */
    @ParameterizedTest
    @CsvFileSource(resources = ["/sine_equivalence_analysis.csv"], numLinesToSkip = 1)
    fun `sine equivalence analysis`(x: Double, mes: String) {
        assertEquals(sin(x), sine.compute(x), epsilon, mes)
    }
}