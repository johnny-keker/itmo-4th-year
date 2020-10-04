package function

class FuncEvaluator(val trigEval: TTrigEvaluator, val logEval: TLogEvaluator) {
  def func(x: Double): Double = x match {
    case _ => {
      if (x > 0)
        logEval.log(x, 1)
      else
        trigEval.sin(x, 1)
    }
  }
}
