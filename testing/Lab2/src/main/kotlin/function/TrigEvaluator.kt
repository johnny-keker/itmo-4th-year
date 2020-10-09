package function

interface ITrigEvaluator {
    fun sin(x: Double, eps: Double = 1E-5): Double
    fun cos(x: Double, eps: Double = 1E-5): Double
    fun csc(x: Double, eps: Double = 1E-5): Double
}

class TrigEvaluator(private val sine: ISinus): ITrigEvaluator {
    override fun sin(x: Double, eps: Double): Double = sine.compute(x, eps)

    override fun cos(x: Double, eps: Double): Double =
        if (x.isNaN() || x.isInfinite() || eps.isNaN() || eps.isInfinite()) Double.NaN
        else sine.compute(x + Math.PI / 2, eps)

    override fun csc(x: Double, eps: Double): Double {
        val s = sine.compute(x, eps / 100)
        if (s == 0.0 || s.isNaN() || s.isInfinite())
            return Double.NaN
        return 1 / s
    }
}