import scala.annotation.tailrec

object Sinus {
  val PRECISION = 1E-6

  def compute(x: Double): Double = if (x.isNaN) Double.NaN else x match {
    case Double.PositiveInfinity => Double.NaN
    case Double.NegativeInfinity => Double.NaN
    case _ => s(x, x)
  }

  @tailrec
  def s(x: Double, cur: Double, n: Int = 1, res: Double = 0.0): Double = {
    if (Math.abs(cur) < PRECISION) return res
    s(x, cur * (-x * x / (2.0 * n * (2.0 * n + 1.0))), n + 1, res + cur)
  }
}
