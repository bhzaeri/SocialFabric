package main.scala.com.bahram.ca

import com.bahram.ca.{Config, KnowledgeSource}
import com.bahram.socialfabric.Individual

/**
  * Created by zaeri on 04/10/16.
  */
class ConfidenceNormative extends KnowledgeSource {
  var mean = Array.fill[Double](Config.dimension)(Double.MaxValue)
  var stdDev = Array.fill[Double](Config.dimension)(0.0)
  var best: Individual = _
  var lb = Array.fill[Double](Config.dimension)(Double.MaxValue)
  var ub = Array.fill[Double](Config.dimension)(Double.MinValue)
  var pl = Array.fill[Double](Config.dimension)(Double.MaxValue)
  var pu = Array.fill[Double](Config.dimension)(Double.MinValue)

  override def update(population: Array[Individual], fitness: (Array[Double]) => Double): Array[Individual] = {
    scala.util.Sorting.stableSort[Individual](population, Config.compareAsc _)
    val l = population.length
    if (best == null || population(0).fitnessValue < best.fitnessValue) {
      best = population(0)
    }
    val s: Int = Math.ceil(0.2 * l).asInstanceOf[Int]
    assert(s > 0)
    for (i <- 0 until s) {
      val t = population(i)
      for (j <- t.vector.indices) {
        if (mean(j) == Double.MaxValue) {
          mean(j) = t.vector(j)
        } else {
          val m: Double = mean(j) + 0.2 * (t.vector(j) - mean(j))
          val std: Double = stdDev(j) + 0.2 * (Math.pow(t.vector(j) - mean(j), 2) - stdDev(j))
          mean(j) = m
          stdDev(j) = std
        }
      }
    }

    var i = 0
    val offSprings = Array.fill[Individual](l) {
      assert(i < l)
      val child = population(i).copy()
      val parent = population(i)
      for (j <- child.vector.indices) {
//        child.vector(j)=
      }
      null
    }
    null
  }
}
