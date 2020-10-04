package function

object Main {
  def main(args: Array[String]): Unit = {
    val sine = new Sinus
    val log = new Log
    val trigEvaluator = new TrigEvaluator(sine)
    val logEvaluator = new LogEvaluator(log)
    val funcEvaluator = new FuncEvaluator(trigEvaluator, logEvaluator)
    funcEvaluator.writeCsvFile("utils/res.csv", -Math.PI * 3, 0.1, 0)
  }
}
