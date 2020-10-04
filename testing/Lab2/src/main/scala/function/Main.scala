package function

import scala.io.StdIn.readLine

object Main {
  def main(args: Array[String]): Unit = {
    val sine = new Sinus
    val log = new Log
    val trigEvaluator = new TrigEvaluator(sine)
    val logEvaluator = new LogEvaluator(log)
    val funcEvaluator = new FuncEvaluator(trigEvaluator, logEvaluator)

    val filepath = readLine("Enter csv file path: ")
    val startX = readLine("Enter start of the interval: ").toDouble
    val endX = readLine("Enter end of the interval: ").toDouble
    val step = readLine("Enter step: ").toDouble

    funcEvaluator.writeCsvFile(filepath, startX, step, endX)

    println(s"The data successfully written to $filepath")
  }
}
