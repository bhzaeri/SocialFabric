package main.java.com.bahram.ca

import com.bahram.ca.{Cell, StateEnum}
import com.bahram.socialfabric.Individual
import com.bahram.socialfabric.topology.TopologyFactory
import com.bahram.util.RandomUtil

/**
  * Created by zaeri on 20/08/16.
  */
object TopographicUpdate {

  def update1(t: Individual, best: Individual, parent: Individual, c: Cell): Unit = {
    val alpha = RandomUtil.nextDouble()
    if (c.state == StateEnum.H)
      for (j <- t.vector.indices) {
        t.vector(j) = best.vector(j) + alpha * Math.sqrt(parent.fitnessValue) / TopologyFactory.dimension
        if (t.vector(j) < c.l(j))
          t.vector(j) = c.l(j)
        if (t.vector(j) > c.u(j))
          t.vector(j) = c.u(j)
      }
    else for (j <- t.vector.indices)
      t.vector(j) = best.vector(j) + alpha * (c.u(j) - c.l(j)) / 2

  }

  def update2(newI: Individual, best: Individual, parent: Individual, c: Cell): Unit = {
    val alpha = RandomUtil.nextDouble()
    if (c.state == StateEnum.H)
      for (j <- newI.vector.indices) {
        val lambda = 1 / parent.vector(j)
        newI.vector(j) = Math.log(1 - alpha) / -lambda
        if (newI.vector(j) < c.l(j))
          newI.vector(j) = c.l(j)
        if (newI.vector(j) > c.u(j))
          newI.vector(j) = c.u(j)
      }
    else {
      for (j <- newI.vector.indices)
        newI.vector(j) = c.l(j) + alpha * (c.u(j) - c.l(j))
    }
  }

  def update3(newI: Individual, best: Individual, parent: Individual, c: Cell): Unit = {
    val alpha = 0.6
    if (c.state == StateEnum.H)
      for (j <- newI.vector.indices) {
        newI.vector(j) = alpha * best.vector(j) + (1 - alpha) * parent.vector(j)
        if (newI.vector(j) < c.l(j))
          newI.vector(j) = c.l(j)
        if (newI.vector(j) > c.u(j))
          newI.vector(j) = c.u(j)
      }
    else {
      val alpha = RandomUtil.nextDouble()
      for (j <- newI.vector.indices)
        newI.vector(j) = c.l(j) + alpha * (c.u(j) - c.l(j))
    }
  }

}