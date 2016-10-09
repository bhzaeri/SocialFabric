package main.scala.com.bahram.ca

import com.bahram.ca.{Config, KSEnum, KnowledgeSource}
import com.bahram.socialfabric.Individual
import com.bahram.util.RandomUtil
import org.apache.commons.math3.distribution.TDistribution

import scala.Double.MaxValue
import scala.collection.mutable.ArrayBuffer

/**
  * Created by zaeri on 04/10/16.
  */
class ConfidenceNormative extends KnowledgeSource {
  var mean = Array.fill[Double](Config.dimension)(-1.0)
  var variance = Array.fill[Double](Config.dimension)(-1.0)
  var best: Individual = _
  var lb = Array.fill[Double](Config.dimension)(MaxValue)
  var ub = Array.fill[Double](Config.dimension)(Double.MinValue)
  var pl = Array.fill[Double](Config.dimension)(MaxValue)
  var pu = Array.fill[Double](Config.dimension)(Double.MinValue)
  var counter = 1

  override def update(population: Array[Individual], mergeOutput: Boolean, fitness: (Array[Double]) => Double): Array[Individual] = {
    scala.util.Sorting.stableSort[Individual](population, Config.compareAsc _)
    val l = population.length
    if (best == null || population(0).fitnessValue < best.fitnessValue) {
      best = population(0)
    }
    val s: Int = Math.ceil(0.2 * l).asInstanceOf[Int]
    for (dim <- 0 until Config.dimension) {
      var sum = 0.0
      population.slice(0, s).foreach(i => {
        sum += i.vector(dim)
      })
      val m = sum / s

      if (mean(dim) == -1.0)
        mean(dim) = m
      else
        mean(dim) = 0.8 * mean(dim) + 0.2 * m

      if (this.variance(dim) == -1.0) {
        sum = 0.0
        population.slice(0, s).foreach(i => {
          sum += Math.pow(i.vector(dim) - m, 2)
        })
        variance(dim) = sum / s
      }
      else
        variance(dim) = 0.8 * variance(dim) + 0.2 * Math.pow(m - mean(dim), 2)
    }

    assert(s > 0)

    val level = 0.95
    val tDist: TDistribution = new TDistribution(counter) //stats.getN - 1)
    // Calculate critical value
    val alphA: Double = 1.0 - (1 - level) / 2
    val critVal: Double = tDist.inverseCumulativeProbability(alphA)

    var index = 0
    val offSprings = Array.fill[Individual](Config.populationSize)(null)
    population.slice(0, s).foreach(i => {
      val child = i.copy
      val parent = i
      val alpha = RandomUtil.nextDouble()
      for (dim <- 0 until Config.dimension) {
        val lambda = 1 / parent.vector(dim)
        val stdDev = Math.sqrt(variance(dim))
        val t = critVal * stdDev / counter
        val lb = mean(dim) - t
        val ub = mean(dim) + t
        if (parent.vector(dim) >= lb && parent.vector(dim) <= ub)
          child.vector(dim) = Math.log(1 - alpha) / -lambda
        else
          child.vector(dim) = lb + alpha * (ub - lb)
      }
      child.fitnessValue = fitness(child.vector)
      TribalRunner.checkCount()
      child.ksType = KSEnum.NORMATIVE
      offSprings(index) = child
      index += 1
    })
    counter += 1
    offSprings
  }
}
