package main.java.com.bahram.ca

import com.bahram.ca.Normative
import com.bahram.socialfabric.Individual
import com.bahram.util.RandomUtil

/**
  * Created by zaeri on 20/08/16.
  */
object NormativeUpdate {
  def update1(newI: Individual, best: Individual, parent: Individual, ks: Normative, sum: Double): Unit = {
    var lambda = 0.0
    val alpha = RandomUtil.nextDouble()
    for (j <- newI.vector.indices) {
      lambda = ks.ub(j) - ks.lb(j)
      if (parent.vector(j) >= ks.lb(j) && parent.vector(j) <= ks.ub(j))
        newI.vector(j) = best.vector(j) + lambda * (newI.fitnessValue / sum)
      else
        newI.vector(j) = ks.lb(j) + lambda * alpha
    }
  }

  def update2(newI: Individual, best: Individual, parent: Individual, ks: Normative, sum: Double): Unit = {
    val alpha = RandomUtil.nextDouble()
    for (j <- newI.vector.indices) {
      val lambda = 1 / parent.vector(j)
      if (parent.vector(j) >= ks.lb(j) && parent.vector(j) <= ks.ub(j))
        newI.vector(j) = Math.log(1 - alpha) / -lambda
      else
        newI.vector(j) = ks.lb(j) + (ks.ub(j) - ks.lb(j)) * alpha
      if (newI.vector(j) < ks.lb(j))
        newI.vector(j) = ks.lb(j)
      if (newI.vector(j) > ks.ub(j))
        newI.vector(j) = ks.ub(j)

    }
  }

  def update3(newI: Individual, best: Individual, parent: Individual, ks: Normative, sum: Double): Unit = {
    val alpha = 0.6
    val alpha2 = RandomUtil.nextDouble()
    for (j <- newI.vector.indices) {
      if (parent.vector(j) >= ks.lb(j) && parent.vector(j) <= ks.ub(j))
        newI.vector(j) = alpha * best.vector(j) + (1 - alpha) * parent.vector(j)
      else
        newI.vector(j) = ks.lb(j) + (ks.ub(j) - ks.lb(j)) * alpha2
      if (newI.vector(j) < ks.lb(j))
        newI.vector(j) = ks.lb(j)
      if (newI.vector(j) > ks.ub(j))
        newI.vector(j) = ks.ub(j)
    }
  }
}
