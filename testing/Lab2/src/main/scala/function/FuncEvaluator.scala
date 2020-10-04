package function

class FuncEvaluator(val trigEval: TTrigEvaluator, val logEval: TLogEvaluator) {
  def func(x: Double, eps: Double): Double = if (x.isNaN) Double.NaN else x match {
    case Double.PositiveInfinity => Double.NaN
    case Double.NegativeInfinity => Double.NaN
    case _ => {
      if (x > 0)
        posFunc(x, eps)
      else
        negFunc(x, eps)
    }
  }

  def negFunc(x: Double, eps: Double): Double =
    (((trigEval.sin(x, eps) * trigEval.cos(x, eps)) - (trigEval.cos(x, eps) * trigEval.cos(x, eps))) *
      trigEval.csc(x, eps)) * Math.pow(trigEval.sin(x, eps) / trigEval.csc(x, eps), 2) -
      Math.pow(Math.pow(trigEval.cos(x, eps), 2), 3)

  def posFunc(x: Double, eps: Double): Double =
    ((logEval.log(x, 2, eps) - logEval.log(x, 5, eps)) * logEval.log(x, 5, eps)) +
      (Math.pow(logEval.log(x, 3, eps), 3) + Math.pow(logEval.log(x, 10, eps), 3)) +
      (logEval.log(x, 3, eps) * Math.pow(logEval.log(x, 3, eps), 3))
}
