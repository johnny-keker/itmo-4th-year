import function._
import org.scalactic.{Equality, TolerantNumerics}
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite

class TrigEvaluatorTests extends AnyFunSuite with MockFactory {
  val epsilon = 1E-6

  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(epsilon)

  test("basic cos test") {
    val sinMock = mock[TSinus]
    (sinMock.compute _).expects(0.313 + Math.PI / 2, 1E-6).returning(Math.sin(0.313 + Math.PI / 2)).once()

    val trigEval = new TrigEvaluator(sinMock)
    assert(trigEval.cos(0.313) === Math.cos(0.313), "cos should call sin(x + PI / 2)")
  }

  test ("basic csc test") {
    val sinMock = mock[TSinus]
    (sinMock.compute _).expects(0.313, 1E-6).returning(Math.sin(0.313)).once()

    val trigEval = new TrigEvaluator(sinMock)
    assert(trigEval.csc(0.313) === 1 / Math.sin(0.313), "csc should call 1 / sin(x)")
  }

  test ("csc validation test") {
    val sinMock = mock[TSinus]
    (sinMock.compute _).expects(0, 1E-6).returning(0).once()
    (sinMock.compute _).expects(Double.NaN, 1E-6).returning(Double.NaN).once()
    (sinMock.compute _).expects(Double.PositiveInfinity, 1E-6).returning(Double.NaN).once()
    (sinMock.compute _).expects(Double.NegativeInfinity, 1E-6).returning(Double.NaN).once()

    val trigEval = new TrigEvaluator(sinMock)
    assert(trigEval.csc(0).isNaN, "csc should check if sin(x) is 0 and return NaN if so")
    assert(trigEval.csc(Double.NaN).isNaN, "csc(NaN) is NaN")
    assert(trigEval.csc(Double.PositiveInfinity).isNaN, "csc(+inf) is NaN")
    assert(trigEval.csc(Double.NegativeInfinity).isNaN, "csc(-inf) is NaN")
  }

  test ("cos validation test") {
    val sinMock = mock[TSinus]
    (sinMock.compute _).expects(Double.NaN, 1E-6).returning(Double.NaN).once()
    (sinMock.compute _).expects(Double.PositiveInfinity, 1E-6).returning(Double.NaN).once()
    (sinMock.compute _).expects(Double.NegativeInfinity, 1E-6).returning(Double.NaN).once()

    val trigEval = new TrigEvaluator(sinMock)
    assert(trigEval.cos(Double.NaN).isNaN, "cos(NaN) is NaN")
    assert(trigEval.cos(Double.PositiveInfinity).isNaN, "cos(+inf) is NaN")
    assert(trigEval.cos(Double.NegativeInfinity).isNaN, "cos(-inf) is NaN")
  }

  // TODO: cos and csc integration testing
}
