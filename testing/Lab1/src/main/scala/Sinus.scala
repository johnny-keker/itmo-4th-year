
object Sinus {
  val PRECISION = 1E-8

  def compute(x: Double): Double = x match {
    case 0.0 => 0.0
    case _ => s(x, x)
  }

  def s(x: Double, cur: Double, n: Int = 1, res: Double = 0.0): Double = {
    if (Math.abs(cur) < PRECISION / 10) return res
    s(x, Math.pow(-1, n) * (Math.pow(x, 2 * n + 1) / factorial(2 * n + 1)), n + 1, res + cur)
  }

  def factorial(x: Int, res: Int = 1): Double = x match {
    case 1 => res
    case _ => factorial(x - 1, res * x)
  }
}
