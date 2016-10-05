package com.bahram.ca

import com.bahram.socialfabric.Individual
import com.bahram.socialfabric.topology.TopologyFactory
import main.scala.com.bahram.ca.TribalRunner


/**
  * Created by zaeri on 02/08/16.
  */
class Normative() extends KnowledgeSource {

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
        if (t.vector(j) <= lb(j) || t.fitnessValue < pu(j)) {
          lb(j) = t.vector(j)
          pl(j) = t.fitnessValue
        }
        if (t.vector(j) >= ub(j) || t.fitnessValue < pu(j)) {
          ub(j) = t.vector(j)
          pu(j) = t.fitnessValue
        }
      }
    }
    var sum = 0.0
    population.foreach(sum += _.fitnessValue)
    var i = 0
    val offSprings = Array.fill[Individual](l) {
      assert(i < l)
      val child = population(i).copy()
      val parent = population(i)
      Config.normativeUpdate(child, best, parent, this, sum)
      child.fitnessValue = fitness(child.vector)
      TribalRunner.checkCount()
      child.ksType = KSEnum.NORMATIVE
      i += 1
      child
    }

    offSprings

    //    val length = population.length
    //    val population2: Array[Individual] = population ++ offSprings
    //
    //    scala.util.Sorting.stableSort[Individual](population2, compareAsc _)
    //    population2.slice(0, length)
  }
}
