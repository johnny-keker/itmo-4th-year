package function

import kotlin.math.abs

interface ILog {
    fun compute(x: Double, eps: Double = 1E-7): Double
}

class Log: ILog {
    override fun compute(x: Double, eps: Double): Double =
        if (x.isNaN() || x.isInfinite() || x <= 0 || eps.isNaN() || eps.isInfinite()) Double.NaN
        else log(x, eps)

    private fun log(x: Double, eps: Double): Double {
        val num = (x - 1) / (x + 1)
        return l(num, eps, num, 3.0, 0.0)
    }

    private tailrec fun l(x1: Double, eps: Double, cur: Double, n: Double, res: Double): Double =
        if (abs(cur) < eps) res
        else l(x1, eps, cur * x1 * x1 / n * (n - 2), n + 2, res + 2 * cur)
}