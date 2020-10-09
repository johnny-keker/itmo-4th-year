package function

interface ILogEvaluator {
    fun log(x: Double, base: Double, eps: Double = 1E-5): Double
}

class LogEvaluator(private val ln: ILog): ILogEvaluator {
    override fun log(x: Double, base: Double, eps: Double) =
        if (x.isNaN() || x.isInfinite()
            || eps.isNaN() || eps.isInfinite()
            || base.isNaN() || base.isInfinite()) Double.NaN
        else ln.compute(x, eps / 100) / ln.compute(base, eps / 100)
}