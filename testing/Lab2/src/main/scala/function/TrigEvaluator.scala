package function

trait TTrigEvaluator {
  def sin(x: Double, eps: Double = 1E-6): Double
  def cos(x: Double, eps: Double = 1E-6): Double
  def csc(x: Double, eps: Double = 1E-6): Double
}

class TrigEvaluator(val sin: TSinus) extends TTrigEvaluator {
  def sin(x: Double, eps: Double = 1E-6): Double = sin.compute(x, eps)

  override def cos(x: Double, eps: Double = 1E-6): Double = sin.compute(x + Math.PI / 2, eps)

  override def csc(x: Double, eps: Double = 1E-6): Double = sin.compute(x, eps) match {
    case 0 => Double.NaN
    case a => 1 / a
  }
}
