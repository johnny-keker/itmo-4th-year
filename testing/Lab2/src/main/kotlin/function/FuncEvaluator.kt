package function

import kotlin.math.pow
import java.io.PrintWriter
import java.io.File

class FuncEvaluator(private val trigEval: ITrigEvaluator, private val logEval: ILogEvaluator) {
    fun func(x: Double, eps: Double = 1E-5): Double =
        if (x.isNaN() || x.isInfinite() || eps.isNaN() || eps.isInfinite()) Double.NaN
        else if (x > 0) posFunc(x, eps) else negFunc(x, eps)

    private fun posFunc(x: Double, eps: Double): Double =
        (((logEval.log(x, 2.0, eps) - logEval.log(x, 5.0, eps)) * logEval.log(x, 5.0, eps)) +
                (logEval.log(x, 3.0, eps).pow(3.0) + logEval.log(x, 10.0, eps))).pow(3.0) +
                (logEval.log(x, 3.0, eps) * logEval.log(x, 3.0, eps)).pow(3.0)


    private fun negFunc(x: Double, eps: Double): Double =
        (((trigEval.sin(x, eps) * trigEval.cos(x, eps)) - (trigEval.cos(x, eps) * trigEval.cos(x, eps))) *
                trigEval.csc(x, eps)) * (trigEval.sin(x, eps) / trigEval.csc(x, eps)).pow(2.0) -
                trigEval.cos(x, eps).pow(2.0).pow(3.0)

    fun writeCsvFile(filename: String, startX: Double, step: Double, endX: Double) {
        val pw = PrintWriter(File(filename))
        pw.write("x,res\n")
        var currX = startX
        while (currX < endX) {
            pw.write("$currX,${func(currX,1E-6)}\n")
            currX += step
        }
        pw.close()
    }
}