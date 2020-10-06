package function

trait TLogEvaluator {
  def log(x: Double, base: Double, eps: Double = 1E-6): Double
}

class LogEvaluator(logImpl: TLog) extends TLogEvaluator {
  def log(x: Double, base: Double, eps: Double = 1E-6): Double = logImpl.compute(x, eps / 100) / logImpl.compute(base, eps / 100)
}
