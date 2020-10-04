package function

trait TLog {
  def compute(x: Double, eps: Double): Double
}

class Log extends TLog {
  def compute(x: Double, eps: Double): Double = if (x.isNaN) Double.NaN else x match {
    case Double.PositiveInfinity => Double.NaN
    case Double.NegativeInfinity => Double.NaN
    case _ => log(x, eps)
  }

  def log(x: Double, eps: Double): Double =
    if (x < 1) {
      val z = x - 1
      lSmThOne(z, eps, z, z)
    } else {
      val z = x / (x - 1.0)
      lBgThOne(z, eps, z, 0)
    }

  // x < 1
  def lSmThOne(z: Double, eps: Double, term: Double, sum: Double, i: Double = 2): Double = {
    if (Math.abs(term) < eps) return sum
    lSmThOne(z, eps, term * -z, sum + term / i, i + 1)
  }

  // x > 1
  def lBgThOne(z: Double, eps: Double, term: Double, sum: Double, i: Double = 1): Double = {
    if (Math.abs(term) < eps) return sum
    lBgThOne(z, eps, 1.0 / (i * Math.pow(z, i)), sum + term, i + 1)
  }
}
