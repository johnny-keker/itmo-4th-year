package Lab1

import org.junit.Test
import org.junit.Assert._

class test {
  @Test
  def firstTaskTests(): Unit = {
    val x: FirstTask = new FirstTask()
    assertEquals(x.sin(0), 0, 0)
  }
}
