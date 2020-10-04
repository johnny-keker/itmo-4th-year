package Lab2

import function._
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite

class FuncEvaluatorTests extends AnyFunSuite with MockFactory {
  test("basic test") {
    val mockTrig = mock[TTrigEvaluator]
    (mockTrig.sin _).expects(-1, 1).returning(313).anyNumberOfTimes()

    val mockLog = mock[TLogEvaluator]
    (mockLog.log _).expects(1, 1).returning(-313).anyNumberOfTimes()

    val f = new FuncEvaluator(mockTrig, mockLog)
    assert(f.func(-1, 0) == 313)
    assert(f.func(1, 0) == -313)
  }
}
