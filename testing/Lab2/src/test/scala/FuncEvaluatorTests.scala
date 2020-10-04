package Lab2

import function._
import org.scalactic.{Equality, TolerantNumerics}
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite

class FuncEvaluatorTests extends AnyFunSuite with MockFactory {
  val epsilon = 1E-6

  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(epsilon)
  
  test("negative area basic test") {
    val mockTrig = mock[TTrigEvaluator]
    (mockTrig.sin _).expects(-0.313, 1E-6).returning(Math.sin(-0.313)).repeated(2).times()
    (mockTrig.cos _).expects(-0.313, 1E-6).returning(Math.cos(-0.313)).repeated(4).times()
    (mockTrig.csc _).expects(-0.313, 1E-6).returning(1 / Math.sin(-0.313)).repeated(2).times()

    val mockLog = mock[TLogEvaluator]

    val f = new FuncEvaluator(mockTrig, mockLog)
    assert(f.func(-0.313) === -0.706703, "sample negative area test with trig evaluator mocking")
  }
}
