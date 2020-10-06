package function

import scala.annotation.tailrec

trait TLog {
  def compute(x: Double, eps: Double = 1E-6): Double
}

class Log extends TLog {
  def compute(x: Double, eps: Double = 1E-6): Double = if (x.isNaN) Double.NaN else x match {
    case Double.PositiveInfinity => Double.NaN
    case Double.NegativeInfinity => Double.NaN
    case _ => log(x, eps)
  }

  def log(x: Double, eps: Double): Double = {
    val num = (x - 1) / (x + 1)
    l(num, eps, num, n = 3, 0)
  }

  @tailrec
  final def l(x1: Double, eps: Double, cur: Double, n: Double, res: Double): Double = {
    if (Math.abs(cur) < eps) return res
    l(x1, eps, cur * x1 * x1 / n * (n - 2), n + 2, res + 2 * cur)
  }
}
