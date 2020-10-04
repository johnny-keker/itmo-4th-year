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
    (mockLog.log _).expects(*, *, *).never()

    val f = new FuncEvaluator(mockTrig, mockLog)
    assert(f.func(-0.313) === -0.706703, "sample negative area test with trig evaluator mocking")
  }

  test("positive area basic test") {
    val mockTrig = mock[TTrigEvaluator]
    (mockTrig.sin _).expects(*, *).never()
    (mockTrig.cos _).expects(*, *).never()
    (mockTrig.csc _).expects(*, *).never()

    val mockLog = mock[TLogEvaluator]
    (mockLog.log _).expects(0.313, 2, 1E-6).returning(Math.log(0.313) / Math.log(2)).once()
    (mockLog.log _).expects(0.313, 10, 1E-6).returning(Math.log(0.313) / Math.log(10)).once()
    (mockLog.log _).expects(0.313, 5, 1E-6).returning(Math.log(0.313) / Math.log(5)).repeated(2).times()
    (mockLog.log _).expects(0.313, 3, 1E-6).returning(Math.log(0.313) / Math.log(3)).repeated(3).times()

    val f = new FuncEvaluator(mockTrig, mockLog)
    assert(f.func(0.313) === 0.403459, "sample positive area test with trig evaluator mocking")
  }

  test("invalid input parameters test") {
    val mockTrig = mock[TTrigEvaluator]
    (mockTrig.sin _).expects(*, *).never()
    (mockTrig.cos _).expects(*, *).never()
    (mockTrig.csc _).expects(*, *).never()

    val mockLog = mock[TLogEvaluator]
    (mockLog.log _).expects(*, *, *).never()

    val f = new FuncEvaluator(mockTrig, mockLog)
    assert(f.func(Double.NaN).isNaN, "func(NaN) should be NaN")
    assert(f.func(Double.PositiveInfinity).isNaN, "func(+inf) should be NaN")
    assert(f.func(Double.NegativeInfinity).isNaN, "func(-inf) should be NaN")
  }
}
