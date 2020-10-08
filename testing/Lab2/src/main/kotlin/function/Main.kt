package function

fun main() {
    val sine = Sinus()
    val log = Log()
    val trigEvaluator = TrigEvaluator(sine)
    val logEvaluator = LogEvaluator(log)
    val funcEvaluator = FuncEvaluator(trigEvaluator, logEvaluator)

    print("Enter csv file path: ")
    val filepath = readLine()!!
    print("Enter start of the interval: ")
    val startX = readLine()!!.toDouble()
    print("Enter end of the interval: ")
    val endX = readLine()!!.toDouble()
    print("Enter step: ")
    val step = readLine()!!.toDouble()

    funcEvaluator.writeCsvFile(filepath, startX, step, endX)

    println("The data successfully written to $filepath")
}