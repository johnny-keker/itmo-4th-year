package function

import scala.annotation.tailrec

trait TSinus {
  def compute(x: Double, eps: Double = 1E-6): Double
}

class Sinus extends TSinus {
  def compute(x: Double, eps: Double = 1E-6): Double = if (x.isNaN) Double.NaN else x match {
    case Double.PositiveInfinity => Double.NaN
    case Double.NegativeInfinity => Double.NaN
    case _ => s(x, x, eps)
  }

  @tailrec
  final def s(x: Double, cur: Double, eps: Double, n: Int = 1, res: Double = 0.0): Double = {
    if (Math.abs(cur) < eps) return res
    s(x, cur * (-x * x / (2.0 * n * (2.0 * n + 1.0))), eps, n + 1, res + cur)
  }
}
