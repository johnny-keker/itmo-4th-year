package function

import kotlin.math.abs

interface ISinus {
    fun compute(x: Double, eps: Double = 1E-5): Double
}

class Sinus: ISinus {
    override fun compute(x: Double, eps: Double): Double =
        if (x.isNaN() || x.isInfinite() || eps.isNaN() || eps.isInfinite()) Double.NaN
        else s(x, x, eps)

    private tailrec fun s(x: Double, cur: Double, eps: Double, n: Int = 1, res: Double = 0.0): Double =
        if (abs(cur) < eps) res
        else s(x, cur * (-x * x / (2.0 * n * (2.0 * n + 1.0))), eps, n + 1, res + cur)
}