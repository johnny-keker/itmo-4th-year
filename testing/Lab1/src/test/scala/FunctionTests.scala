import org.junit.Test
import org.junit.Assert._

class FunctionTests {
  @Test
  def testSample() = {
    assertEquals(0.0, Function.sin(0.0), 0)
  }
}
