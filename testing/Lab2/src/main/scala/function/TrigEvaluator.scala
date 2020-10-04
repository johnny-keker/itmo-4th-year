package function

trait TTrigEvaluator {
  def sin(x: Double, eps: Double): Double
  def cos(x: Double, eps: Double): Double
  def csc(x: Double, eps: Double): Double
}

class TrigEvaluator extends TTrigEvaluator {
  override def sin(x: Double, eps: Double): Double = ???

  override def cos(x: Double, eps: Double): Double = ???

  override def csc(x: Double, eps: Double): Double = ???
}
